package Server.messages.client;

import Game.Models.Tile;
import Server.messages.Message;
 
public class GetValidMoves extends Message{
    private static final long serialVersionUID = 3L;
    
    public Tile tile;

    public GetValidMoves(Tile tile){
        this.tile = tile;
    }
}
