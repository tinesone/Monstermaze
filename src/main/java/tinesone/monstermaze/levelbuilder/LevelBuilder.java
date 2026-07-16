package tinesone.monstermaze.levelbuilder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.StructureManager;
import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.Maze;
import tinesone.monstermaze.maze.MazeGenerator;
import tinesone.monstermaze.maze.generators.Prims;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;


public final class LevelBuilder
{

    private final Random rn = new Random();
    private final MazeTiles mazeTiles;

    public LevelBuilder(Plugin plugin, String mazeFolder)
    {
        this.mazeTiles = new MazeTiles(plugin, mazeFolder);
    }

    public boolean place(Location initLocation, MazeGenerator mazeGenerator)
    {
        Maze maze = mazeGenerator.generate(mazeTiles.getWidth(), mazeTiles.getHeight());

        int cellLength = Objects.requireNonNull(mazeTiles.getStructure(CellType.WALL, Rotation.DEGREES_0)).getSize().getBlockX();

       for(int x = 0; x < mazeTiles.getWidth(); x++)
       {
           for(int y = 0; y < mazeTiles.getHeight(); y++)
           {
               Cell cell = maze.grid()[x + y* mazeTiles.getWidth()];
               Structure structure = mazeTiles.getStructure(cell.getCellType(), cell.getRotation());

               Location placeLocation = initLocation.clone().add(x*cellLength, 0, y*cellLength);

               assert structure != null;
               structure.place(placeLocation, false, StructureRotation.NONE, Mirror.NONE, 0, 1, rn);
           }
       }
        return true;
    }
}
