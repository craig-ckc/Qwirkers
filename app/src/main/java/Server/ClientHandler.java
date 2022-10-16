package Server;

public class ClientHandler {
    private static GameClient client;

    public static GameClient getClient() {
        return client;
    }

    public static void setClient(GameClient client) {
        ClientHandler.client = client;
    }
}
