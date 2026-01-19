package tinesone.monstermaze.maze.generators;

import tinesone.monstermaze.maze.CardinalDirection;
import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.MazeGenerator;
import tinesone.monstermaze.maze.Wall;

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

        for(int x=0;x<this.getWidth();x++)
        {
            for(int y=0;y<this.getHeight();y++)
            {
                cells[x + y*this.getWidth()] = new Cell(x,y);
            }
        }


        Cell startCell = cells[(int) (Math.random()*cells.length)];
        ArrayList<Cell> visitedCells = new ArrayList<>();
        visitedCells.add(startCell);

        ArrayList<Wall> walls = new ArrayList<>();
        walls.add(new Wall(startCell, CardinalDirection.NORTH));
        walls.add(new Wall(startCell, CardinalDirection.SOUTH));
        walls.add(new Wall(startCell, CardinalDirection.EAST));
        walls.add(new Wall(startCell, CardinalDirection.WEST));

        while (!walls.isEmpty())
        {
            Wall wall = getRandomWall(walls);
            walls.remove(wall);

            Optional<Cell> optionalCell = getNeighbor(cells, wall.cell(), wall.direction());
            Cell neighborCell;
            if (optionalCell.isPresent())
            {
                neighborCell = optionalCell.get();
            } else
            {
                continue;
            }

            if (visitedCells.contains(neighborCell)) { continue; }
            visitedCells.add(neighborCell);
            wall.cell().openWall(wall.direction());
            neighborCell.openWall(wall.direction().oppositeDirection());
            walls.add(new Wall(neighborCell, CardinalDirection.NORTH));
            walls.add(new Wall(neighborCell, CardinalDirection.SOUTH));
            walls.add(new Wall(neighborCell, CardinalDirection.EAST));
            walls.add(new Wall(neighborCell, CardinalDirection.WEST));
        }

        for(Cell cell : cells)
        {
            for (CardinalDirection direction : CardinalDirection.values())
            {
                Optional<Cell> optionalCell = getNeighbor(cells, cell, direction);
                if (optionalCell.isEmpty())
                {
                    cell.closeWall(direction);
                }
            }
        }

        return cells;
    }

    private Wall getRandomWall(ArrayList<Wall> walls)
    {
        int index = (int)(Math.random()*walls.size());
        return walls.get(index);
    }

    private Optional<Cell> getNeighbor(Cell[] cells, Cell cell, CardinalDirection direction)
    {
        int x = cell.getX() + direction.getDx();
        int y = cell.getY() + direction.getDy();

        if (x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight()) { return Optional.empty(); }

        return Optional.of(cells[x + this.getWidth() * y]);
    }
}
