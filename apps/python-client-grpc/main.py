import grpc
import kafka_pb2, kafka_pb2_grpc
from loguru import logger
import time
import statistics

def main():
    channel = grpc.insecure_channel('grpc-server:8085')
    stub = kafka_pb2_grpc.KafkaServiceStub(channel)

    latencies = []

    for i in range(10): 
        request = kafka_pb2.ProduceRequest(topic="steam-data", key=f"user{i}", value=f"Message {i}")

        start_time = time.perf_counter()
        response = stub.ProduceMessage.future(request)
        result = response.result()  
        end_time = time.perf_counter()

        latency_ms = (end_time - start_time) * 1000
        latencies.append(latency_ms)

        logger.info(f"Response {i+1} from server: {result}")
        logger.info(f"Latency {i+1}: {latency_ms:.2f} ms")

    avg_latency = statistics.mean(latencies)
    logger.info(f"Average Latency over 10 messages: {avg_latency:.2f} ms")

if __name__ == "__main__":
    main()
