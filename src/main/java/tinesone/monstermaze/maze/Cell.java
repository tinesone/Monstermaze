package tinesone.monstermaze.maze;

import org.bukkit.block.structure.StructureRotation;
import tinesone.monstermaze.levelbuilder.CellType;

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

    public StructureRotation getRotation() {
        // Structure files should be saved with north direction as up.
        EnumSet<CardinalDirection> directions = this.getOpenWalls();
        return switch (getCellType()) {
            case CellType.STRAIGHT -> {
                if (directions.contains(CardinalDirection.NORTH)) {
                    yield StructureRotation.NONE;
                }
                yield StructureRotation.CLOCKWISE_90;
            }
            case CellType.CORNER -> {
                //North
                if (directions.contains(CardinalDirection.NORTH)) {
                    if (directions.contains(CardinalDirection.EAST)) {
                        yield StructureRotation.NONE;
                    } //North-East
                    yield StructureRotation.COUNTERCLOCKWISE_90;
                }
                //South
                else if (directions.contains(CardinalDirection.EAST)) {
                    yield StructureRotation.CLOCKWISE_90;
                } //South-East
                yield StructureRotation.CLOCKWISE_180; //South-East
            }
            case CellType.T_CROSS -> {
                if (!directions.contains(CardinalDirection.SOUTH)) {
                    yield StructureRotation.NONE;
                } else if (!directions.contains(CardinalDirection.WEST)) {
                    yield StructureRotation.CLOCKWISE_90;
                } else if (!directions.contains(CardinalDirection.NORTH)) {
                    yield StructureRotation.CLOCKWISE_180;
                }
                yield StructureRotation.COUNTERCLOCKWISE_90;
            }
            case CellType.DEADEND -> {
                if (directions.contains(CardinalDirection.NORTH)) {
                    yield StructureRotation.NONE;
                } else if (directions.contains(CardinalDirection.EAST)) {
                    yield StructureRotation.CLOCKWISE_90;
                } else if (directions.contains(CardinalDirection.SOUTH)) {
                    yield StructureRotation.CLOCKWISE_180;
                }
                yield StructureRotation.COUNTERCLOCKWISE_90;
            }
            default -> {
                int randomInt = (int) (Math.random() * 4);
                yield StructureRotation.values()[randomInt];
            }
        };
    }
}
