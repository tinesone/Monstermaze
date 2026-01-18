package tinesone.monstermaze.maze.generators;

import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.MazeGenerator;

import static java.lang.Math.random;

public class Prims extends MazeGenerator
{
    public Prims(int width, int height)
    {
        super(width, height);
    }

    @Override
    public Cell[] generateMaze()
    {
        Cell[] cells = new Cell[this.getWidth() * this.getHeight()];
        for (int i = 0; i < this.getWidth() * this.getHeight(); i++)
        {
            cells[i] = new Cell(i, i*getWidth());
        }

        Cell randomCell = cells[(int) (Math.random() * this.getWidth() * this.getHeight() + 1)]; //Pick a random cell

        return cells;
    }

    private boolean emptyCellExists(Cell[] cells)
    {
        for (Cell cell : cells)
        {
            if (!cell.hasOpenWalls())
            {
                return true;
            }
        }
        return false;
    }

}
