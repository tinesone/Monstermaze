package tinesone.monstermaze.maze;

import java.util.Arrays;
import java.util.EnumSet;

public class Cell {

    private boolean north;
    private boolean south;
    private boolean east;
    private boolean west;


    public Cell()
    {
        north = false;
        south = false;
        east = false;
        west = false;

    }

    public boolean[] getWalls()
    {
        return new boolean[] {this.north, this.south, this.east, this.west};
    }

    public void openWall(CardinalDirection direction)
    {
        switch (direction)
        {
        case NORTH -> this.north = true;
        case SOUTH -> this.south = true;
        case EAST -> this.east = true;
        case WEST -> this.west = true;
        }
    }

    public void closeWall(CardinalDirection direction)
    {
        switch (direction)
        {
            case NORTH -> this.north = false;
            case SOUTH -> this.south = false;
            case EAST -> this.east = false;
            case WEST -> this.west = false;
        }
    }

    public EnumSet<CardinalDirection> getOpenWalls()
    {
        EnumSet<CardinalDirection> open = EnumSet.noneOf(CardinalDirection.class);
        if (this.north) { open.add(CardinalDirection.NORTH); }
        if (this.south) { open.add(CardinalDirection.SOUTH); }
        if (this.east) { open.add(CardinalDirection.EAST); }
        if (this.west) { open.add(CardinalDirection.WEST); }
        return open;
    }


    @Override
    public String toString()
    {
        EnumSet<CardinalDirection> openWalls = getOpenWalls();
        if (openWalls.size() == 1) { return "u"; }
        if (openWalls.size() == 2)
        {
            if (openWalls.equals(EnumSet.of(CardinalDirection.NORTH, CardinalDirection.SOUTH)) || openWalls.equals(EnumSet.of(CardinalDirection.EAST, CardinalDirection.WEST))) { return "-"; }
            return "L";
        }
        if (openWalls.size() == 3) { return "T"; }
        if (openWalls.size() == 4) { return "+"; }
        return "x";
    }
}
