package tinesone.monstermaze.maze;

public class Maze
{
    private final int width;
    private final int height;
    private Cell[] cells;

    public Maze(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width * height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[] mazeArray()
    {
        return cells;
    }
}
