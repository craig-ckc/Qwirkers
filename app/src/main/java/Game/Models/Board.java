package Game.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Game.Enums.Dimension;

public class Board {
    private final Map<Position, Tile> board;
    private final List<Position> filledBlocks;

    public Board() {
        board = new TreeMap<>();
        filledBlocks = new ArrayList<>();
        this.setupBoard();
    }

    private void setupBoard() {
        for (int x = 0; x < Dimension.DIMX.getDim(); x++)
            for (int y = 0; y < Dimension.DIMY.getDim(); y++) {
                board.put(new Position(x, y), null);
            }

    }

    public void setBlock(Position pos, Tile tile) {
        board.put(pos, tile);
        filledBlocks.add(pos);
    }

    public Tile block(Position pos) {
        return board.get(pos);
    }

    public Tile removeTile(Position pos) {
        filledBlocks.remove(pos);
        return board.put(pos, null);
    }

    public boolean isEmpty() {
        return filledBlocks.size() < 1;
    }

    public Map<Position, Tile> board() {
        return board;
    }

    public ArrayList<Tile> blocks() {
        return new ArrayList<>(board.values());
    }

    public List<Position> filledBlocks() {
        return filledBlocks;
    }
}
