package QwirkleGame.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import QwirkleGame.Enums.Color;
import QwirkleGame.Enums.Shape;
import QwirkleGame.Enums.Dimension;
import QwirkleGame.Enums.Direction;

public class Board {
    private Map<String, Tile> board;
    private Map<String, Tile> filledBlocks;
    private boolean empty;
    private int count;

    public Board() {
        board = new TreeMap<>();
        filledBlocks = new TreeMap<>();
        this.setupBoard();
        count = 0;
    }

    private void setupBoard() {
        for (int x = 0; x < Dimension.DIMX.getDim(); x++)
            for (int y = 0; y < Dimension.DIMY.getDim(); y++)
                this.setBlock(x, y, null);

        this.empty = true;
    }

    public void setBlock(int x, int y, Tile tile) {
        board.put(x + "," + y, tile);
        count++;
    }

    public Tile getBlock(int x, int y) {
        return board.get(new String(x + "," + y));
    }

    public Tile removeTile(int x, int y){
        count--;
        return board.put(new String(x + "," + y), null);
    }

    public boolean isEmpty() {
        return count <= 0;
    }

    public ArrayList<Tile> getBlocks(){
        return new ArrayList<>(board.values());
    }

    public Map<String, Tile> getBoard() {
        return board;
    }

    public ArrayList<String> getBlocks(int i){
        return new ArrayList<>(board.keySet());
    }

    public void displayBoard() {
        for (int x = 0; x < Dimension.DIMX.getDim(); x++) {

            for (int y = 0; y < Dimension.DIMY.getDim(); y++){
                Tile tile = getBlock(x, y);
                if ( tile == null)
                    System.out.print("|  ");
                else
                    System.out.print("|"+ tile);
            }

            System.out.println("|");
        }

    }
}
