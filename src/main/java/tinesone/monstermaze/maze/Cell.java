package tinesone.monstermaze.maze;

public class Cell {

    private boolean[] openWalls = {false, false, false, false};

    public void setWalls(boolean[] openWalls)
    {
        this.openWalls = openWalls;
    }

    public boolean[] getWalls()
    {
        return this.openWalls;
    }
}
