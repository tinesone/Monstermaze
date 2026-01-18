package tinesone.monstermaze.maze;

public abstract class MazeGenerator
{
    private int width;
    private int height;

    public MazeGenerator(int width, int height)
    {
        this.width = width;
        this.height = height;
    };

    public abstract Cell[] generateMaze();

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }
}
