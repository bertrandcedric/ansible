package org.zenika;

import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileStreamSinkConnector extends SinkConnector {

    public static final String FILE_CONFIG = "file";
    private String filename;

    public FileStreamSinkConnector() {
    }

    public String version() {
        return AppInfoParser.getVersion();
    }

    public void start(Map<String, String> props) {
        this.filename = props.get("file");
    }

    public Class<? extends Task> taskClass() {
        return org.apache.kafka.connect.file.FileStreamSinkTask.class;
    }

    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList configs = new ArrayList();

        for (int i = 0; i < maxTasks; ++i) {
            HashMap config = new HashMap();
            if (this.filename != null) {
                config.put("file", this.filename);
            }

            configs.add(config);
        }
        return configs;
    }

    public void stop() {
    }
}
