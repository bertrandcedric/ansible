package org.zenika;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import kafka.producer.KeyedMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterSampleTest {

    private String bootstrap_servers = "192.168.99.100:32790";

    @Test
    public void twitterKafkaProducer() throws InterruptedException {
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

        TwitterClient twitterClient = new TwitterClient(Lists.newArrayList("#News"));
        String topic = "topic3";
        KeyedMessage<String, String> message;
        for (int i = 0; i < 1000; i++) {
            JSONObject obj = new JSONObject(twitterClient.getQueue().take());

            String text = obj.toString();
            Double user_id = obj.getJSONObject("user").getDouble("id");
            message = new KeyedMessage<>(topic, String.valueOf(user_id), text);
            producer.send(new ProducerRecord<>(message.topic(), message.key(), message.message()));
        }

        twitterClient.getClient().stop();

        producer.close();
    }

    @Test
    public void twitterTest() throws InterruptedException {
        TwitterClient twitterClient = new TwitterClient(Lists.newArrayList("#News"));
        for (int i = 0; i < 10; i++) {
            JSONObject obj = new JSONObject(twitterClient.getQueue().take());
            String text = obj.getString("text");
            System.out.println(text);
        }
        twitterClient.getClient().stop();
    }

    private class TwitterClient {
        private BlockingQueue<String> queue;
        private Client client;

        public BlockingQueue<String> getQueue() {
            return queue;
        }

        public Client getClient() {
            return client;
        }

        public TwitterClient(List<String> terms) {
            queue = new LinkedBlockingQueue<>(10);
            StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
            endpoint.trackTerms(terms);

            String consumerKey = "jxHG2aYojjTbL1NUYMSR4pbJE";
            String consumerSecret = "z8ZkYrVyaYJBede3b27k4ZSo1yDTzU7hIAspLv0P5COTUKZogi";
            String token = "113077154-BDZjw4Oy8L3lgOCyFZcf47b71P1E0btTrflPjsi8";
            String secret = "oMHptLr7Lw5lgTNcmYAeP7o5emAadHieqeHfY0RZRHGQ2";
            Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);

            client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                    .endpoint(endpoint).authentication(auth)
                    .processor(new StringDelimitedProcessor(queue)).build();

            client.connect();
        }
    }
}
