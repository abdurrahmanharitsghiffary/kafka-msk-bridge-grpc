import grpc
from protos import kafka_pb2, kafka_pb2_grpc

def main():
    channel = grpc.insecure_channel('localhost:8085')
    stub = kafka_pb2_grpc.KafkaServiceStub(channel)

    request = kafka_pb2.ProduceRequest(topic="stream-data", key="lolerss", value="lolerszii")

    response = stub.ProduceMessage.future(request)

    print("Response from server:", response.result())

if __name__ == "__main__":
    main()
