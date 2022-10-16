package Game.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Game.Enums.Dimension;

public class Board {
    private Map<Position, Tile> board;
    private List<Position> filledBlocks;
    private boolean empty;
    private int count;

    public Board() {
        board = new TreeMap<>();
        filledBlocks = new  ArrayList<>();
        empty = true;
        this.setupBoard();
        count = 0;
    }

    private void setupBoard() {
        for (int x = 0; x < Dimension.DIMX.getDim(); x++)
            for (int y = 0; y < Dimension.DIMY.getDim(); y++) {
                board.put(new Position(x, y), null);
                count++;
            }

        this.empty = true;
    }

    public void setBlock(Position pos, Tile tile) {
        board.put(pos, tile);
        filledBlocks.add(pos);
        count++;
    }

    public Tile getBlock(Position pos) {
        return board.get(pos);
    }

    public Tile removeTile(Position pos){
        count--;
        filledBlocks.remove(pos);
        return board.put(pos, null);
    }

    public boolean isEmpty() {
        return count <= 0;
    }

    public ArrayList<Tile> getBlocks(){
        return new ArrayList<>(board.values());
    }

    public Map<Position, Tile> getBoard() {
        return board;
    }

    public ArrayList<Position> getBlocks(int i){
        return new ArrayList<>(board.keySet());
    }

    public List<Position> getFilledBlocks(){
        return filledBlocks;
    }
}
