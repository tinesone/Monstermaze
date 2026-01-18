package tinesone.monstermaze.maze;

public class Maze
{
    private final int width;
    private final int height;
    private Cell[] cells;

    public Maze(MazeGenerator generator)
    {
        this.width = generator.getWidth();
        this.height = generator.getHeight();
        this.cells = new Cell[this.width * this.height];
        this.cells = generator.generateMaze();
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getSize() { return width * height; }

    public Cell[] mazeArray()
    {
        return cells;
    }
}
