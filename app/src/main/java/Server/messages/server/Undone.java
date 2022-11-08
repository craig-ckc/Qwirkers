package Server.messages.server;

import Game.Models.Position;
import Game.Models.Tile;
import Server.messages.Message;
 
public class Undone extends Message{
    private static final long serialVersionUID = 101L;

    public Position position;
    public Tile tile;

    public Undone(Position position) {
        this.position = position;
        this.tile = null;
    }
}
