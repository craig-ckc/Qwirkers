package Server.messages.client;

import Game.Models.Position;
import Game.Models.Tile;
import Server.messages.Message;


public class Move extends Message {
    private static final long serialVersionUID = 1L;

    private final Tile tile;
    private final Position position;

    public Move(Tile tile, Position position){
        this.tile = tile;
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("%s placed on posistion %s", tile, position);
    }
}
