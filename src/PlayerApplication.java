import main.Game.Player;

public class PlayerApplication {
    public static void main(String[] args) {
        Player player = new Player();
        player.openConnection("localhost", 43215);

        Thread gamePlay = new Thread(() -> player.run());
        gamePlay.start();
        try {
            gamePlay.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
