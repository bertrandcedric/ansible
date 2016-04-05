package org.zenika;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileStreamSourceTask extends SourceTask {
    private static final Logger log = LoggerFactory.getLogger(org.apache.kafka.connect.file.FileStreamSourceTask.class);
    public static final String FILENAME_FIELD = "filename";
    public static final String POSITION_FIELD = "position";
    private static final Schema VALUE_SCHEMA;
    private String filename;
    private InputStream stream;
    private BufferedReader reader = null;
    private char[] buffer = new char[1024];
    private int offset = 0;
    private String topic = null;
    private Long streamOffset;

    public FileStreamSourceTask() {
    }

    public String version() {
        return (new org.apache.kafka.connect.file.FileStreamSourceConnector()).version();
    }

    public void start(Map<String, String> props) {
        this.filename = props.get("file");
        if(this.filename == null || this.filename.isEmpty()) {
            this.stream = System.in;
            this.streamOffset = null;
            this.reader = new BufferedReader(new InputStreamReader(this.stream));
        }

        this.topic = props.get("topic");
        if(this.topic == null) {
            throw new ConnectException("FileStreamSourceTask config missing topic setting");
        }
    }

    public List<SourceRecord> poll() throws InterruptedException {
        if(this.stream == null) {
            try {
                this.stream = new FileInputStream(this.filename);
                Map readerCopy = this.context.offsetStorageReader().offset(Collections.singletonMap("filename", this.filename));
                if(readerCopy != null) {
                    Object records = readerCopy.get("position");
                    if(records != null && !(records instanceof Long)) {
                        throw new ConnectException("Offset position is the incorrect type");
                    }

                    if(records != null) {
                        log.debug("Found previous offset, trying to skip to file offset {}", records);
                        long nread = ((Long)records).longValue();

                        while(nread > 0L) {
                            try {
                                long e = this.stream.skip(nread);
                                nread -= e;
                            } catch (IOException var13) {
                                log.error("Error while trying to seek to previous offset in file: ", var13);
                                throw new ConnectException(var13);
                            }
                        }

                        log.debug("Skipped to offset {}", records);
                    }

                    this.streamOffset = Long.valueOf(records != null?((Long)records).longValue():0L);
                } else {
                    this.streamOffset = Long.valueOf(0L);
                }

                this.reader = new BufferedReader(new InputStreamReader(this.stream));
                log.debug("Opened {} for reading", this.logFilename());
            } catch (FileNotFoundException var15) {
                log.warn("Couldn\'t find file for FileStreamSourceTask, sleeping to wait for it to be created");
                synchronized(this) {
                    this.wait(1000L);
                    return null;
                }
            }
        }

        try {
            BufferedReader readerCopy1;
            synchronized(this) {
                readerCopy1 = this.reader;
            }

            if(readerCopy1 == null) {
                return null;
            } else {
                ArrayList records1 = null;
                int nread1 = 0;

                while(true) {
                    do {
                        if(!readerCopy1.ready()) {
                            if(nread1 <= 0) {
                                synchronized(this) {
                                    this.wait(1000L);
                                }
                            }

                            return records1;
                        }

                        nread1 = readerCopy1.read(this.buffer, this.offset, this.buffer.length - this.offset);
                        log.trace("Read {} bytes from {}", Integer.valueOf(nread1), this.logFilename());
                    } while(nread1 <= 0);

                    this.offset += nread1;
                    if(this.offset == this.buffer.length) {
                        char[] line = new char[this.buffer.length * 2];
                        System.arraycopy(this.buffer, 0, line, 0, this.buffer.length);
                        this.buffer = line;
                    }

                    while(true) {
                        String line1 = this.extractLine();
                        if(line1 != null) {
                            log.trace("Read a line from {}", this.logFilename());
                            if(records1 == null) {
                                records1 = new ArrayList();
                            }

                            records1.add(new SourceRecord(this.offsetKey(this.filename), this.offsetValue(this.streamOffset), this.topic, VALUE_SCHEMA, line1));
                        }

                        new ArrayList();
                        if(line1 == null) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException var14) {
            return null;
        }
    }

    private String extractLine() {
        int until = -1;
        int newStart = -1;

        for(int result = 0; result < this.offset; ++result) {
            if(this.buffer[result] == 10) {
                until = result;
                newStart = result + 1;
                break;
            }

            if(this.buffer[result] == 13) {
                if(result + 1 >= this.offset) {
                    return null;
                }

                until = result;
                newStart = this.buffer[result + 1] == 10?result + 2:result + 1;
                break;
            }
        }

        if(until != -1) {
            String var4 = new String(this.buffer, 0, until);
            System.arraycopy(this.buffer, newStart, this.buffer, 0, this.buffer.length - newStart);
            this.offset -= newStart;
            if(this.streamOffset != null) {
                this.streamOffset = Long.valueOf(this.streamOffset.longValue() + (long)newStart);
            }

            return var4;
        } else {
            return null;
        }
    }

    public void stop() {
        log.trace("Stopping");
        synchronized(this) {
            try {
                if(this.stream != null && this.stream != System.in) {
                    this.stream.close();
                    log.trace("Closed input stream");
                }
            } catch (IOException var4) {
                log.error("Failed to close FileStreamSourceTask stream: ", var4);
            }

            this.notify();
        }
    }

    private Map<String, String> offsetKey(String filename) {
        return Collections.singletonMap("filename", filename);
    }

    private Map<String, Long> offsetValue(Long pos) {
        return Collections.singletonMap("position", pos);
    }

    private String logFilename() {
        return this.filename == null?"stdin":this.filename;
    }

    static {
        VALUE_SCHEMA = Schema.STRING_SCHEMA;
    }
}
