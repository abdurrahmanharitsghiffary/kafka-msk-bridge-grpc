package ai.batiinvestasi.app;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;
import kafka.Kafka;
import kafka.KafkaServiceGrpc.KafkaServiceImplBase;

public class KafkaService extends KafkaServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private final KafkaProducer<String, String> producer;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public KafkaService() {
        Properties props = new KafkaConfig().build();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void produceMessage(Kafka.ProduceRequest request, StreamObserver<Kafka.ProduceResponse> responseObserver) {
        logger.info("Received request: {}", request.getAllFields());

        ProducerRecord<String, String> record = new ProducerRecord<>(request.getTopic(), request.getKey(), request.getValue());

        producer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception == null) {
                responseObserver.onNext(Kafka.ProduceResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Message sent successfully to topic " + request.getTopic())
                        .build());
            } else {
                responseObserver.onNext(Kafka.ProduceResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Failed to send message: " + exception.getMessage())
                        .build());
            }
            responseObserver.onCompleted();
        });
    }

    @Override
    public void consumeMessages(Kafka.ConsumeRequest request, StreamObserver<Kafka.ConsumeResponse> responseObserver) {
        logger.info("Received request: {}", request.getAllFields());

        Properties props = new KafkaConfig().build();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, request.getConsumerGroup());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(request.getTopic()));
        executorService.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        Kafka.ConsumeResponse response = Kafka.ConsumeResponse.newBuilder()
                                .setKey(record.key())
                                .setValue(record.value())
                                .setOffset(record.offset())
                                .build();
                        responseObserver.onNext(response);
                    }
                }
            } catch (Exception e) {
                logger.error("Error while consuming messages", e);
            } finally {
                consumer.close();
                responseObserver.onCompleted();
            }
        });
    }
}
