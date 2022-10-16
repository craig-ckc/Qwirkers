package Game.Enums;

public enum Dimension {
    DIMX(30), DIMY(30), TILESIZE(100);

    private int dim; // The x position in the direction

    Dimension(int dim) {
        this.dim = dim;
    }

    public int getDim() {
        return dim;
    }
}
