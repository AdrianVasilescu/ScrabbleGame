package main.Exceptions;

public class InitialWordNotOnCenterException extends Throwable {
    public InitialWordNotOnCenterException()
    {
        super("The first word has to cover the center of the board!");
    }
}
