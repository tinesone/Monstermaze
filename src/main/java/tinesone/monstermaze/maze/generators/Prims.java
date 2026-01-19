package tinesone.monstermaze.maze.generators;

import tinesone.monstermaze.maze.CardinalDirections;
import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.MazeGenerator;
import tinesone.monstermaze.maze.Wall;

import java.util.ArrayList;
import java.util.Arrays;


public class Prims extends MazeGenerator
{
    public Prims(int width, int height)
    {
        super(width, height);
    }

    @Override
    public Cell[] generateMaze()
    {
        Cell[] cells = new Cell[this.getWidth()*this.getHeight()];
        for(int x=0;x<this.getWidth();x++)
        {
            for(int y=0;y<this.getHeight();y++)
            {
                cells[x * y] = new Cell(x,y);
            }
        }
        Cell startCell = cells[(int) (Math.random()*cells.length)];
        ArrayList<Wall> walls = new ArrayList<>();
        walls.add(new Wall(startCell, CardinalDirections.NORTH));
        walls.add(new Wall(startCell, CardinalDirections.SOUTH));
        walls.add(new Wall(startCell, CardinalDirections.EAST));
        walls.add(new Wall(startCell, CardinalDirections.WEST));

        /*while (!walls.isEmpty())
        {
            Wall wall = getRandomWall(walls);
        } */
        return cells;
    }

    private Wall getRandomWall(ArrayList<Wall> walls)
    {
        int index = (int)(Math.random()*walls.size());
        return walls.get(index);
    }

    private Cell getNeighbor(ArrayList<Cell> cells, Cell cell, CardinalDirections direction, int width)
    {
        int x = cell.getX() + direction.getDx();
        int y = cell.getY() + direction.getDy();

        return cells.get(x + width * y);
    }

    public static void main(String[] args)
    {
        Prims p = new Prims(10, 10);
        System.out.println(Arrays.toString(p.generateMaze()));
    }
}
