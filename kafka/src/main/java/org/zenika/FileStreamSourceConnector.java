package org.zenika;

import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileStreamSourceConnector extends SourceConnector {
    public static final String TOPIC_CONFIG = "topic";
    public static final String FILE_CONFIG = "file";
    private String filename;
    private String topic;

    public FileStreamSourceConnector() {
    }

    public String version() {
        return AppInfoParser.getVersion();
    }

    public void start(Map<String, String> props) {
        this.filename = props.get("file");
        this.topic = props.get("topic");
        if(this.topic != null && !this.topic.isEmpty()) {
            if(this.topic.contains(",")) {
                throw new ConnectException("FileStreamSourceConnector should only have a single topic when used as a source.");
            }
        } else {
            throw new ConnectException("FileStreamSourceConnector configuration must include \'topic\' setting");
        }
    }

    public Class<? extends Task> taskClass() {
        return org.apache.kafka.connect.file.FileStreamSourceTask.class;
    }

    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList configs = new ArrayList();
        HashMap config = new HashMap();
        if(this.filename != null) {
            config.put("file", this.filename);
        }

        config.put("topic", this.topic);
        configs.add(config);
        return configs;
    }

    public void stop() {
    }
}
