package ai.batiinvestasi.app;

public class Settings {

    public static String BOOTSTRAP_SERVERS = System.getenv("BOOSTRAP_SERVERS");
    public static int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8085"));

}
