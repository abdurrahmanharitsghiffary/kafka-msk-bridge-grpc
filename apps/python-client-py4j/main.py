from py4j.java_gateway import JavaGateway, GatewayClient
from loguru import logger
import time

class KafkaClient:
    def __init__(self):
        self.gateway = JavaGateway(GatewayClient(address="0.0.0.0", port=25333))
        self.kafka_service = self.gateway.entry_point

    def produce_message(self, topic, key, value):
        response = self.kafka_service.produceMessage(topic, key, value)
        logger.info(f"Producer Response: {response}")

    def consume_messages(self, topic):
        response = self.kafka_service.consumeMessage(topic)
        logger.info(f"Consumer Response: {response}")

import statistics

if __name__ == "__main__":
    client = KafkaClient()
    
    latencies = []
    
    for i in range(10):  # Send 10 messages
        start_time = time.perf_counter()
        client.produce_message("test-topic", f"user{i}", f"Message {i}")
        client.consume_messages("test-topic")
        end_time = time.perf_counter()
        
        latencies.append((end_time - start_time) * 1000)  # Convert to ms
    
    avg_latency = statistics.mean(latencies)
    logger.info(f"Average End-to-End Latency over 10 messages: {avg_latency:.2f} ms")

