package QwirkleGame.Game;

import QwirkleGame.Enums.Color;
import QwirkleGame.Enums.Shape;

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

    public Object isSimilar(Tile tile) {
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
