package main.Exceptions;

import lib.Protocol;
public class InitialWordNotOnCenterException extends GameException {
    public InitialWordNotOnCenterException()
    {
        super(Protocol.Error.E005);
    }
}
