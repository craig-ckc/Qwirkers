package Game.Models;

import java.util.Objects;

public class Move {
    private final Tile tile;
    private final Position position;

    public Move(Tile tile, Position position){
        this.tile = tile;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Move))
            return false;

        Move move = (Move) obj;
        return move.position.equals(position) && move.tile.equals(tile);
    }

}
