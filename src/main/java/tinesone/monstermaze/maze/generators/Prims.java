package tinesone.monstermaze.maze.generators;

import tinesone.monstermaze.maze.CardinalDirections;
import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.MazeGenerator;
import tinesone.monstermaze.maze.Wall;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


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
        ArrayList<Cell> visitedCells = new ArrayList<>();
        for(int x=0;x<this.getWidth();x++)
        {
            for(int y=0;y<this.getHeight();y++)
            {
                cells[x + y*this.getWidth()] = new Cell(x,y);
            }
        }
        Cell startCell = cells[(int) (Math.random()*cells.length)];
        visitedCells.add(startCell);

        ArrayList<Wall> walls = new ArrayList<>();
        walls.add(new Wall(startCell, CardinalDirections.NORTH));
        walls.add(new Wall(startCell, CardinalDirections.SOUTH));
        walls.add(new Wall(startCell, CardinalDirections.EAST));
        walls.add(new Wall(startCell, CardinalDirections.WEST));

        while (!walls.isEmpty())
        {
            Wall wall = getRandomWall(walls);
            Optional<Cell> optionalCell = getNeighbor(cells, wall.cell(), wall.direction());
            if (optionalCell.isEmpty()) { continue; }
            Cell neighborCell = optionalCell.get();
            if (visitedCells.contains(neighborCell)) { continue; }
            visitedCells.add(neighborCell);
            walls.add(new Wall(neighborCell, CardinalDirections.NORTH));
            walls.add(new Wall(neighborCell, CardinalDirections.SOUTH));
            walls.add(new Wall(neighborCell, CardinalDirections.EAST));
            walls.add(new Wall(neighborCell, CardinalDirections.WEST));
        }
        return visitedCells.toArray(new Cell[visitedCells.size()]);
    }

    private Wall getRandomWall(ArrayList<Wall> walls)
    {
        int index = (int)(Math.random()*walls.size());
        return walls.get(index);
    }

    private Optional<Cell> getNeighbor(Cell[] cells, Cell cell, CardinalDirections direction)
    {
        int x = cell.getX() + direction.getDx();
        int y = cell.getY() + direction.getDy();

        if (x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight()) { return Optional.empty(); }

        return Optional.of(cells[x + this.getWidth() * y]);
    }

    public static void main(String[] args)
    {
        Prims p = new Prims(10, 10);
        Cell[] cells = p.generateMaze();
        System.out.println(Arrays.toString(cells));
    }
}
