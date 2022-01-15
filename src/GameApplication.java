import Board.Controller.BoardController;
import Game.Game;
import Player.Controller.PlayerController;

public class GameApplication {
    public static void main(String[] args) {
        BoardController boardController = new BoardController();
        PlayerController playerController = new PlayerController();
        Game game = new Game(boardController, playerController);

        game.run();

        System.out.println("Hello World!");
    }
}
