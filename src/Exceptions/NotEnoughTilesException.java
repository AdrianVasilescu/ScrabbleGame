package Exceptions;

public class NotEnoughTilesException extends Throwable {
    public NotEnoughTilesException()
    {
        super("Not enough tiles for this action!");
    }
}
