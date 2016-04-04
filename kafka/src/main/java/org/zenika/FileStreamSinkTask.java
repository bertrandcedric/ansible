package org.zenika;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.file.FileStreamSinkConnector;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileStreamSinkTask extends SinkTask {
    private static final Logger log = LoggerFactory.getLogger(org.apache.kafka.connect.file.FileStreamSinkTask.class);
    private String filename;
    private PrintStream outputStream;

    public FileStreamSinkTask() {
    }

    public FileStreamSinkTask(PrintStream outputStream) {
        this.filename = null;
        this.outputStream = outputStream;
    }

    public String version() {
        return (new FileStreamSinkConnector()).version();
    }

    public void start(Map<String, String> props) {
        this.filename = props.get("file");
        if (this.filename == null) {
            this.outputStream = System.out;
        } else {
            try {
                this.outputStream = new PrintStream(new FileOutputStream(this.filename, true));
            } catch (FileNotFoundException var3) {
                throw new ConnectException("Couldn\'t find or create file for FileStreamSinkTask", var3);
            }
        }
    }

    public void put(Collection<SinkRecord> sinkRecords) {
        Iterator i$ = sinkRecords.iterator();

        while (i$.hasNext()) {
            SinkRecord record = (SinkRecord) i$.next();
            log.trace("Writing line to {}: {}", this.logFilename(), record.value());
            this.outputStream.println(record.value());
        }
    }

    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
        log.trace("Flushing output stream for {}", this.logFilename());
        this.outputStream.flush();
    }

    public void stop() {
        if (this.outputStream != System.out) {
            this.outputStream.close();
        }
    }

    private String logFilename() {
        return this.filename == null ? "stdout" : this.filename;
    }
}
