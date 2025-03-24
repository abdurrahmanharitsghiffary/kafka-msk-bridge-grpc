package ai.batiinvestasi.app;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    public static void produce(String topic, String key, String value) {

        Properties props = new KafkaConfig().build();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            RecordMetadata metadata = producer.send(record).get();

            logger.info("Produced message: Topic = %s, Key = %s, Value = %s, Partition = %d, Offset = %d\n",
                    topic, key, value, metadata.partition(), metadata.offset());

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
