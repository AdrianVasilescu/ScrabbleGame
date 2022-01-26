package main.Exceptions;

import lib.Protocol;

public abstract class GameException extends Throwable{
    private Protocol.Error error;

    public GameException(Protocol.Error error)
    {
        super(error.getDescription());
        this.error = error;
    }

    public Protocol.Error getError() {
        return error;
    }
}
