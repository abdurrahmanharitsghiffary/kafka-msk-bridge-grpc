package ai.batiinvestasi.app;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py4j.GatewayServer;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private final KafkaProducer<String, String> producer;
    private final KafkaConsumer<String, String> consumer;

    public App() {
        Properties props = new KafkaConfig().build();

        this.producer = new KafkaProducer<>(props);
        this.consumer = new KafkaConsumer<>(props);
    }

    public String produceMessage(String topic, String key, String value) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            Future<RecordMetadata> response = producer.send(record);

            return response.get().toString();
        } catch (Exception e) {
            return "Failed to send message: " + e.getMessage();
        }
    }

    // Method to consume messages from Kafka
    public String consumeMessage(String topic) {
        consumer.subscribe(Collections.singletonList(topic));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

        StringBuilder result = new StringBuilder();
        for (ConsumerRecord<String, String> record : records) {
            result.append(String.format("Key: %s, Value: %s, Offset: %d\n",
                    record.key(), record.value(), record.offset()));
        }
        return result.toString().isEmpty() ? "No messages received." : result.toString();
    }

    public static void main(String[] args) {
        App kafkaService = new App();

        GatewayServer server = new GatewayServer(kafkaService);

        server.start();
        logger.info("Py4J KafkaService is running on localhost:{}...", 25333);
    }
}
