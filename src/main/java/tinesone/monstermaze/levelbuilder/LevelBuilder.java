package tinesone.monstermaze.levelbuilder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.Maze;
import tinesone.monstermaze.maze.generators.Prims;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;

public class LevelBuilder
{
    private final Plugin plugin;

    public LevelBuilder(@NotNull Plugin plugin)
    {
        this.plugin = plugin;
    }

    public boolean place(Location location, int width, int height, String mazeFolder)
    {
        Prims prims = new Prims();
        Maze maze = prims.generate(width, height);

        if (!this.sanityCheck(mazeFolder)) { return false; }



        Random rn = new Random();

        for(int x=0; x<width;x++)
        {
            for(int y=0; y<height;y++)
            {
               //TODO
            }
        }



        return true;
    }

    private Structure getStructure(CellType cellType, String resourceFolderName)
    {
        String fileLocation = cellType.getStructureLocation(resourceFolderName);


        InputStream stream = plugin.getResource(fileLocation);

        if(stream == null)
        {
            return null;
        }


        StructureManager structureManager = Bukkit.getStructureManager();
        try
        {
            return structureManager.loadStructure(stream);
        } catch (IOException e)
        {
            return null;
        }
    }

    private boolean sanityCheck(String mazeFolder)
    {
        for(CellType cellType : CellType.values())
        {
            if (plugin.getResource(cellType.getStructureLocation(mazeFolder)) == null)
            {
                plugin.getComponentLogger().warn("Not all cells have a .nbt file! Missing: {}", mazeFolder + "/" + cellType);
                return false;
            }
            Structure structure = getStructure(CellType.WALL, mazeFolder);
            assert structure != null;
            if ((structure.getSize().getZ()) != structure.getSize().getX())
            {
                plugin.getComponentLogger().warn("Not all cells are square! Wrong size structure: {}, X: {}, Z: {}", mazeFolder + "/" + cellType, structure.getSize().getZ(), structure.getSize().getX());
                return false;
            }
        }
        return true;
    }
}
