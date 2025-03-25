package ai.batiinvestasi.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {

            Server server = ServerBuilder
                    .forPort(Config.getServerPort())
                    .addService(new KafkaService()).build();

            logger.info("🚀 gRPC Server is starting on port " + Config.getServerPort() + "...");
            server.start();

            logger.info("✅ gRPC Server started successfully on port " + Config.getServerPort());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("🛑 Shutting down gRPC server...");
                server.shutdown();
                logger.info("✅ gRPC Server terminated.");
            }));

            server.awaitTermination();

        } catch (IOException | InterruptedException e) {
            logger.error(e.toString());
        }
    }
}
