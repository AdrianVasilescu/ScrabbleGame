import Board.Controller.BoardController;
import Game.Game;
import Player.Controller.PlayerController;

public class GameApplication {
    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
