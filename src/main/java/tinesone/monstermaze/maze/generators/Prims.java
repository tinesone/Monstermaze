package tinesone.monstermaze.maze.generators;

import tinesone.monstermaze.maze.*;

import java.util.ArrayList;
import java.util.Optional;


public final class Prims extends MazeGeneratorBase
{
    public void generate(Cell[] cells, int width, int height)
    {

        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                cells[x + y*width] = new Cell();
            }
        }


        int x = (int) (Math.random()*width);
        int y = (int) (Math.random()*height);
        ArrayList<Integer> visitedCellsIndices = new ArrayList<>();
        visitedCellsIndices.add(x + y*width);

        ArrayList<Wall> walls = new ArrayList<>();
        walls.add(new Wall(x + y*width, CardinalDirection.NORTH));
        walls.add(new Wall(x + y*width, CardinalDirection.SOUTH));
        walls.add(new Wall(x + y*width, CardinalDirection.EAST));
        walls.add(new Wall(x + y*width, CardinalDirection.WEST));

        while (!walls.isEmpty())
        {
            Wall wall = getRandomWall(walls);
            walls.remove(wall);

            //Optional<Cell> optionalCell = getNeighbor(cells, width, height);
            int cellIndex = wall.cellIndex();
            Optional<Integer> optionalNeighborCellIndex = getNeighborIndex(cellIndex, width, height, wall.direction());
            int neighborCellIndex;
            if (optionalNeighborCellIndex.isPresent())
            {
                neighborCellIndex =  optionalNeighborCellIndex.get();
            } else
            {
                continue;
            }

            if (visitedCellsIndices.contains(neighborCellIndex)) { continue; }
            visitedCellsIndices.add(neighborCellIndex);


            Cell neighborCell = cells[neighborCellIndex];
            neighborCell.openWall(wall.direction().oppositeDirection());
            cells[cellIndex].openWall(wall.direction());


            walls.add(new Wall(neighborCellIndex, CardinalDirection.NORTH));
            walls.add(new Wall(neighborCellIndex, CardinalDirection.SOUTH));
            walls.add(new Wall(neighborCellIndex, CardinalDirection.EAST));
            walls.add(new Wall(neighborCellIndex, CardinalDirection.WEST));
        }
    }

    private Wall getRandomWall(ArrayList<Wall> walls)
    {
        int index = (int)(Math.random()*walls.size());
        return walls.get(index);
    }

    private Optional<Integer> getNeighborIndex(int index, int width, int height, CardinalDirection direction)
    {
        int x = (index % width) + direction.getDx();
        int y = (index / width) + direction.getDy();

        if (x < 0 || x >= width || y < 0 || y >= height)
        {
            return Optional.empty();
        }

        return Optional.of(x + width * y);
    }
}
