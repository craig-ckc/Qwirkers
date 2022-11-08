package Server.messages.server;

import Game.Models.Position;
import Game.Models.Tile;
import Server.messages.Message;
 
public class Played  extends Message{
    private static final long serialVersionUID = 106L;

    public Position position;
    public Tile tile;

    public Played(Position position, Tile tile) {
        this.position = position;
        this.tile = tile;
    }
}
