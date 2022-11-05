package Server.messages.client;

import Game.Models.Position;
import Game.Models.Tile;
import Server.messages.Message;

public class PlayMove extends Message {
    private static final long serialVersionUID = 4L;

    public String session;
    public Position position;
    public Tile tile;

    public PlayMove(String session, Position position, Tile tile) {
        this.session = session;
        this.position = position;
        this.tile = tile;
    }
}
