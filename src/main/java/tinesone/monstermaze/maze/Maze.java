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

    /*public static void main(String[] args)
    {
        int width = 25;
        int height = 25;
        Prims p = new Prims();
        Maze maze = p.generate(width, height);
        for(int i=0;i<width;i++)
        {
            System.out.println(Arrays.toString(Arrays.copyOfRange(maze.grid(), i * width, (i + 1) * width)));
        }
    } */
}
