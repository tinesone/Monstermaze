package tinesone.monstermaze.maze;

public class Cell {

    private boolean north;
    private boolean south;
    private boolean east;
    private boolean west;

    private final int x;
    private final int y;

    public Cell(int x, int y)
    {
        north = false;
        south = false;
        east = false;
        west = false;

        this.x = x;
        this.y = y;
    }

    public boolean[] getWalls()
    {
        return new boolean[] {this.north, this.south, this.east, this.west};
    }

    public boolean hasOpenWalls()
    {
        for (boolean wall : this.getWalls())
        {
            if (wall)
            {
                return true;
            }
        }
        return false;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    @Override
    public String toString()
    {
        return "Cell X:".concat(String.valueOf(x)).concat(" Y:".concat(String.valueOf(y)));
    }
}
