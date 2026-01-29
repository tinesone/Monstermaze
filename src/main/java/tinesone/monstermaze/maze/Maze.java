package tinesone.monstermaze.maze;

import tinesone.monstermaze.maze.generators.Prims;

import java.util.Arrays;

public class Maze
{
    private final int width;
    private final int height;
    private final Cell[] grid;

    public Maze(Cell[] cells, int width, int height)
    {
        this.width = width;
        this.height = height;
        this.grid = cells;


        if (cells.length != width * height) {
            throw new IllegalArgumentException("Dimension of cells " + cells.length
                    + " does not match specified dimension of maze ("
                    + width + ", " + height + ")");
        }
    }

    public int getWidth() { return this.width; }

    public int getHeight() { return this.height; }

    public int getSize() { return this.width * this.height; }

    public Cell[] grid()
    {
        return grid;
    }

}
