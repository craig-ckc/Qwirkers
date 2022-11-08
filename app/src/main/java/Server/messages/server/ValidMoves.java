package Server.messages.server;

import java.util.List;

import Game.Models.Position;
import Server.messages.Message;
 
public class ValidMoves  extends Message{
    private static final long serialVersionUID = 112L;

    public List<Position> positions;

    public ValidMoves(List<Position> positions) {
        this.positions = positions;
    }
}
