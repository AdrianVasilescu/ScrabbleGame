package main.Exceptions;

import lib.Protocol;

public class InvalidMoveException extends GameException {
    public InvalidMoveException(Protocol.Error error)
    {
        super(error);
    }
}
