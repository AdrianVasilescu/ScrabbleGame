package main.Exceptions;

import lib.Protocol;
import main.Common.Tile;

import java.util.List;

public class InvalidMoveException extends GameException {
    public InvalidMoveException(Protocol.Error error)
    {
        super(error);
    }
    public InvalidMoveException(Protocol.Error error, boolean returnTiles) {
        super(error, returnTiles);
    }
}
