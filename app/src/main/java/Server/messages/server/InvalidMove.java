package Server.messages.server;

import Game.Models.Tile;
import Server.messages.Message;
 
public class InvalidMove  extends Message{
    private static final long serialVersionUID = 103L;

    public Tile tile;

    public InvalidMove(Tile tile) {
        this.tile = tile;
    }
}
