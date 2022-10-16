package Game.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Game.Enums.Color;
import Game.Enums.Shape;

public class Bag {
    private static final int SETS = 3;
    protected List<Tile> bag = new ArrayList<Tile>();

    public Bag() {
        createBag();
    }

    private void createBag() {
        for (int i = 0; i < SETS; i++)
            for (Shape shape : Shape.values())
                for (Color color : Color.values())
                    if (!shape.equals(Shape.EMPTY) && !color.equals(Color.EMPTY))
                        bag.add(new Tile(shape, color));
    }

    public Tile takeTile() {
        Random rand = new Random();
        int value = rand.nextInt(bag.size() - 1);

        return bag.remove(value);
    }

    public void addTile(Tile tile) {
        bag.add(tile);
    }

    public List<Tile> tradeTile(List<Tile> tiles) {
        List<Tile> temp = new ArrayList<>();

        for (int i = 0; i < tiles.size(); i++) {
            temp.add(takeTile());
        }

        bag.addAll(tiles);

        return temp;
    }

    public int getSize() {
        return bag.size();
    }
}
