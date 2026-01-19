package tinesone.monstermaze.maze;

import tinesone.monstermaze.maze.generators.Prims;

import java.util.Arrays;

public class Maze
{
    private final int width;
    private final int height;
    private Cell[] grid;

    public Maze(MazeGenerator generator)
    {
        this.width = generator.getWidth();
        this.height = generator.getHeight();
        this.grid = new Cell[this.width * this.height];
        this.grid = generator.generateMaze();
    }

    public int getWidth() { return this.width; }

    public int getHeight() { return this.height; }

    public int getSize() { return this.width * this.height; }

    public Cell[] grid()
    {
        return grid;
    }

    public static void main(String[] args)
    {
        int width = 25;
        int height = 25;
        Maze maze = new Maze(new Prims(width, height));
        for(int i=0;i<width;i++)
        {
            System.out.println(Arrays.toString(Arrays.copyOfRange(maze.grid(), i * width, (i + 1) * width)));
        }
    }
}
