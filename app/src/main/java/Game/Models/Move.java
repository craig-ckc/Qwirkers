package Game.Models;

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
}
