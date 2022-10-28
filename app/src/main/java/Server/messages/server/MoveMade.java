package Server.messages.server;

import Game.Models.Tile;
import Game.Models.Position;
import Server.messages.Message;

public class MoveMade extends Message {
    private static final long serialVersionUID = 103L;

    public Tile tile;
    public Position position;

    public MoveMade(Tile tile, Position position) {
        this.tile = tile;
        this.position = position;
    }
}
