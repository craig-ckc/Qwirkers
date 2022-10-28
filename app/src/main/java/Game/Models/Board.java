package Game.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Game.Enums.Dimension;

public class Board {
    private final Map<Position, Tile> board;
    private final List<Position> filledBlocks;
    private boolean empty;
    private int count;

    private int top;
    private int bottom;
    private int left;
    private int right;

    private int height;
    private int width;

    public Board() {
        board = new TreeMap<>();
        filledBlocks = new ArrayList<>();
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

        height = Dimension.DIMY.getDim();
        width = Dimension.DIMX.getDim();

        top = 0;
        bottom = height - 1;
        left = 0;
        right = width - 1;

        this.empty = true;
    }

    public int getWidth() {
        return width;
    }

    public void setBlock(Position pos, Tile tile) {
        board.put(pos, tile);
        filledBlocks.add(pos);

//        if (pos.getX() == top) {
//            top--;
//
//            for (int y = left; y <= right; y++) {
//                board.put(new Position(top, y), null);
//                count++;
//            }
//
//            height ++;
//        }
//        else if (pos.getX() == bottom) {
//            bottom++;
//
//            for (int y = left; y <= right; y++) {
//                board.put(new Position(bottom, y), null);
//                count++;
//            }
//
//            height ++;
//        }
//        else if (pos.getY() == left) {
//            left--;
//
//            for (int x = top; x <= bottom; x++) {
//                board.put(new Position(x, left), null);
//                count++;
//            }
//
//            width++;
//        }
//        else if (pos.getY() == right) {
//            right++;
//
//            for (int x = top; x <= bottom; x++) {
//                board.put(new Position(x, right), null);
//                count++;
//            }
//
//            width++;
//        }

        count++;
    }

    public Tile getBlock(Position pos) {
        return board.get(pos);
    }

    public Tile removeTile(Position pos) {
        count--;
        filledBlocks.remove(pos);
        return board.put(pos, null);
    }

    public boolean isEmpty() {
        return count <= 0;
    }

    public ArrayList<Tile> getBlocks() {
        return new ArrayList<>(board.values());
    }

    public Map<Position, Tile> getBoard() {
        return board;
    }

    public ArrayList<Position> getBlocks(int i) {
        return new ArrayList<>(board.keySet());
    }

    public List<Position> getFilledBlocks() {
        return filledBlocks;
    }
}
