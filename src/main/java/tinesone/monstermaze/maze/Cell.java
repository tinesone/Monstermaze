package tinesone.monstermaze.maze;

public class Cell {

    private boolean[] walls = {false, false, false, false};

    public void setWalls(boolean[] walls)
    {
        this.walls = walls;
    }

    public boolean[] getWalls()
    {
        return this.walls;
    }
}
