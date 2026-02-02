package tinesone.monstermaze.maze;

import tinesone.monstermaze.levelbuilder.CellType;
import tinesone.monstermaze.levelbuilder.Rotation;

import java.util.EnumSet;
import java.util.Random;

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

    public CellType getCellType()
    {
        EnumSet<CardinalDirection> openWalls = this.getOpenWalls();
        int setSize = openWalls.size();
        if (setSize == 1) { return CellType.DEADEND; }
        else if (setSize == 2)
        {
            if(openWalls.equals(EnumSet.of(CardinalDirection.NORTH, CardinalDirection.SOUTH)) || openWalls.equals(EnumSet.of(CardinalDirection.EAST, CardinalDirection.WEST))) { return CellType.STRAIGHT; }
            else { return CellType.CORNER; }
        }
        else if (setSize == 3) { return CellType.T_CROSS; }
        else if (setSize == 4) { return CellType.CROSS; }
        return CellType.WALL;
    }
    public Rotation getRotation() {
        Random random = new Random();
        // Structure files should be saved with north direction as up.
        EnumSet<CardinalDirection> directions = this.getOpenWalls();
        return switch (getCellType()) {
            case CellType.STRAIGHT -> {
                if (directions.contains(CardinalDirection.NORTH)) {
                    yield randomRotationFromList(EnumSet.of(Rotation.DEGREES_0, Rotation.DEGREES_180), random);
                }
                yield randomRotationFromList(EnumSet.of(Rotation.DEGREES_90, Rotation.DEGREES_270), random);
            }
            case CellType.CORNER -> {
                //North
                if (directions.contains(CardinalDirection.NORTH)) {
                    if (directions.contains(CardinalDirection.WEST)) {
                        yield Rotation.DEGREES_0; //North-West
                    } //North-East
                    yield Rotation.DEGREES_90;
                }
                //South
                else if (directions.contains(CardinalDirection.EAST)) {
                    yield Rotation.DEGREES_180; //South-East
                }
                yield Rotation.DEGREES_270; //South-West
            }
            case CellType.T_CROSS -> {
                if (!directions.contains(CardinalDirection.SOUTH)) {
                    yield Rotation.DEGREES_0;
                } else if (!directions.contains(CardinalDirection.WEST)) {
                    yield Rotation.DEGREES_90;
                } else if (!directions.contains(CardinalDirection.NORTH)) {
                    yield Rotation.DEGREES_180;
                }
                yield Rotation.DEGREES_270;
            }
            case CellType.DEADEND -> {
                if (directions.contains(CardinalDirection.NORTH)) {
                    yield Rotation.DEGREES_0;
                } else if (directions.contains(CardinalDirection.EAST)) {
                    yield Rotation.DEGREES_90;
                } else if (directions.contains(CardinalDirection.SOUTH)) {
                    yield Rotation.DEGREES_180;
                }
                yield Rotation.DEGREES_270;
            }
            default ->
                    randomRotationFromList(EnumSet.of(Rotation.DEGREES_0, Rotation.DEGREES_180, Rotation.DEGREES_90, Rotation.DEGREES_270), random);
        };
    }
    private Rotation randomRotationFromList(EnumSet<Rotation> rotations, Random rn)
    {
        int size = rotations.size();
        int item = rn.nextInt(size);
        int i = 0;
        for (Rotation rotation : rotations)
        {
            if (i == item)
            {
                return rotation;
            }
            i++;
        }
        return null;
    }
}
