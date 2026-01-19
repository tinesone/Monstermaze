package tinesone.monstermaze.maze;

public enum CardinalDirections
{
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    private final int dx;
    private final int dy;

    CardinalDirections(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx()
    {
        return dx;
    }

    public int getDy()
    {
        return dy;
    }
}
