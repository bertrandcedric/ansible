package org.zenika;

import kafka.producer.KeyedMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

public class KafkaSampleTest {

    private String bootstrap_servers = "192.168.99.100:32790";
    private String topic = "topic2";

    @Test
    public void kafkaProducer() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrap_servers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 1000000; i++) {
            Thread.sleep(Double.valueOf(Math.random() * 400).longValue());
            JSONObject obj = new JSONObject().put("test", i);
            KeyedMessage<String, String> message = new KeyedMessage<>(topic, String.valueOf(i % 10), obj.toString());
            producer.send(new ProducerRecord<>(message.topic(), message.key(), message.message()));
        }

        producer.close();
    }

    @Test
    public void kafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrap_servers);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");

        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        System.out.println("Topic list : ");
        consumer.listTopics().values().stream().flatMap(l -> l.stream()).filter(f -> f.topic().equals(topic)).forEach(System.out::println);
//        System.out.println("Metrics : ");
//        consumer.metrics().values().forEach(x -> System.out.println(x.metricName() + " => " + x.value()));
        consumer.seekToBeginning();
        while (true) {
            consumer.poll(1).forEach(record -> System.out.println(String.format("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value())));
        }
    }
}
