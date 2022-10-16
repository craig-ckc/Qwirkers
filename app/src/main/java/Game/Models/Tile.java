package Game.Models;

import Game.Enums.Color;
import Game.Enums.Shape;

public class Tile {
    private final Shape shape;
    private final Color color;

    public Tile(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public Shape getShape() {
        return this.shape;
    }

    public Color getColor() {
        return this.color;
    }

    public Object getSimilar(Tile tile) {
        if (this.shape == tile.getShape()) {
            return this.shape;
        } else if (this.color == tile.getColor()) {
            return this.color;
        } else {
            return null;
        }
    }


    public boolean equal(Tile tile) {
        return this.shape == tile.getShape() && this.color == tile.getColor();
    }

    @Override
    public String toString() {
        return color + "_" + shape;
    }

}
