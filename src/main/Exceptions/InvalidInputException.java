package main.Exceptions;

import lib.Protocol;

public class InvalidInputException extends GameException {
    public InvalidInputException(Protocol.Error error)
    {
        super(error);
    }
}
