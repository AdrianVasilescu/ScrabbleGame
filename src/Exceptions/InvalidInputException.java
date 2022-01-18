package Exceptions;

public class InvalidInputException extends Throwable {
    public InvalidInputException()
    {
        super("The provided input is invalid!");
    }
}
