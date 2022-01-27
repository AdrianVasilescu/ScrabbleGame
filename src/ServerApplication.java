import main.Game.Server;

public class ServerApplication {
    public static void main(String[] args) {
        Server server = new Server(43215);
        Thread lobby = new Thread(() -> server.run());

        lobby.start();
        try {
            lobby.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
