package main.Exceptions;

import lib.Protocol;

public class NotEnoughTilesException extends GameException {
    public NotEnoughTilesException()
    {
        super(Protocol.Error.E007);
    }
}
