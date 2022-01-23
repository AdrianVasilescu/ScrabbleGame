package main.Exceptions;

public class InvalidMoveException extends Throwable {
    public InvalidMoveException()
    {
        super("The set of tiles can't be placed on the desired positions!");
    }
}
