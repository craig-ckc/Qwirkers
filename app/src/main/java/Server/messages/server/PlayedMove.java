package Server.messages.server;

import Game.Models.Player;
import Game.Models.Position;
import Game.Models.Tile;
import Server.messages.Message;

public class PlayedMove extends Message{
    private static final long serialVersionUID = 105L;

    public Player player;
    public Position position;
    public Tile tile;

    public PlayedMove(Player player, Position position, Tile tile){
        this.player = player;
        this.position = position;
        this.tile = tile;
    }
}
