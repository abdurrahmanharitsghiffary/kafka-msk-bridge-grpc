package ai.batiinvestasi.app;

import org.eclipse.microprofile.config.ConfigProvider;

public class Config {

    private static final org.eclipse.microprofile.config.Config config = ConfigProvider.getConfig();

    public static String getBootsrapServers() {
        return config.getValue("kafka.bootstrap.servers", String.class);
    }

}
