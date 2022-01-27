package main.Exceptions;

import lib.Protocol;
import main.Common.Tile;

import java.util.List;

public abstract class GameException extends Throwable{
    private Protocol.Error error;
    private boolean returnTiles = false;

    public GameException(Protocol.Error error)
    {
        super(error.getDescription());
        this.error = error;
    }

    public GameException(Protocol.Error error, boolean returnTiles)
    {
        super(error.getDescription());
        this.error = error;
    }

    public Protocol.Error getError() {
        return error;
    }

    public boolean isReturnTiles()
    {
        return returnTiles;
    }
}
