package Exceptions;

public class NotEnoughPlayersException extends Throwable {
    public NotEnoughPlayersException()
    {
        super("Not enough players have joined the game!");
    }
}
