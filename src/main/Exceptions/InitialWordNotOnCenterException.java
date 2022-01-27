package main.Exceptions;

import lib.Protocol;
import main.Common.Tile;

import java.util.List;

public class InitialWordNotOnCenterException extends GameException {
    public InitialWordNotOnCenterException(boolean returnTiles)
    {
        super(Protocol.Error.E005, returnTiles);
    }
}
