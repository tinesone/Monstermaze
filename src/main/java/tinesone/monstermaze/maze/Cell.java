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

    public int[] getCoordinates()
    {
        return new int[] {this.x, this.y};
    }
}
