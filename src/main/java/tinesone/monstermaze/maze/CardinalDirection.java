package tinesone.monstermaze.maze;

public enum CardinalDirection
{
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    private final int dx;
    private final int dy;

    CardinalDirection(int dx, int dy)
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
    public CardinalDirection oppositeDirection()
    {
        return switch (this)
        {
            case NORTH -> CardinalDirection.SOUTH;
            case SOUTH -> CardinalDirection.NORTH;
            case EAST -> CardinalDirection.WEST;
            case WEST -> CardinalDirection.EAST;
        };
    }
}
