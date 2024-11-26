public class Cell {
    public Pair parent;

    public double f, g, h;
    Cell()
    {
        parent = new Pair(-1, -1);
        f = -1;
        g = -1;
        h = -1;
    }

    public Cell(Pair parent, double f, double g, double h) {
        this.parent = parent;
        this.f = f;
        this.g = g;
        this.h = h;
    }

}