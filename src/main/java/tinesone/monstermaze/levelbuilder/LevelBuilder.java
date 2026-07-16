package tinesone.monstermaze.levelbuilder;

import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.plugin.Plugin;
import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.Maze;
import tinesone.monstermaze.maze.MazeGenerator;


import java.util.HashSet;
import java.util.Objects;
import java.util.Random;


public final class LevelBuilder
{

    private final Random rn = new Random();
    private final MazeTiles mazeTiles;
    private Maze maze;
    private Location initLocation;

    public LevelBuilder(Plugin plugin, String mazeFolder)
    {
        this.mazeTiles = new MazeTiles(plugin, mazeFolder);
    }

    public boolean place(Location initLocation, MazeGenerator mazeGenerator)
    {
        maze = mazeGenerator.generate(mazeTiles.getAmountTilesWidth(), mazeTiles.getAmountTilesHeight());
        this.initLocation = initLocation;

        int cellLength = Objects.requireNonNull(mazeTiles.getStructure(CellType.WALL, Rotation.DEGREES_0)).getSize().getBlockX();

       for(int x = 0; x < maze.getWidth(); x++)
       {
           for(int y = 0; y < maze.getHeight(); y++)
           {
               Cell cell = maze.grid()[x + y* mazeTiles.getAmountTilesWidth()];
               Structure structure = mazeTiles.getStructure(cell.getCellType(), cell.getRotation());

               Location placeLocation = initLocation.clone().add(x*cellLength, 0, y*cellLength);

               assert structure != null;
               structure.place(placeLocation, false, StructureRotation.NONE, Mirror.NONE, 0, 1, rn);
           }
       }
        return true;
    }

    public Location[] getSpawnLocations() throws IllegalStateException
    {
        if (maze == null)
            throw new IllegalArgumentException("No maze generated");
        if (initLocation == null)
            throw new IllegalArgumentException("No init location provided");
        HashSet<Location> locations = new HashSet<>();
        Structure structure = mazeTiles.getStructure(CellType.WALL, Rotation.DEGREES_0);
        double tileWidth = structure.getSize().getX();
        double tileHeight = structure.getSize().getZ();
        Location selectedLocation = initLocation.clone();
        for(int x = 0; x < maze.getWidth(); x++)
        {
            for(int y = 0; y < maze.getHeight(); y++)
            {
                if (maze.grid()[x + y*maze.getWidth()].getCellType() == CellType.WALL)
                    continue;
                Location spawnLocation = selectedLocation.clone().add(tileWidth /2, 0, tileHeight /2);
                locations.add(spawnLocation);
                selectedLocation.add(0, 0, tileHeight);
            }
            selectedLocation.add(tileWidth, 0, -tileHeight*maze.getHeight());
        }
        return locations.toArray(new Location[0]);
    }
}
