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
import tinesone.monstermaze.maze.generators.Prims;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;


public class LevelBuilder
{
    private final Plugin plugin;

    private final Random rn = new Random();

    public LevelBuilder(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public boolean place(Location initLocation, int width, int height, String mazeFolder)
    {
        Prims prims = new Prims();
        Maze maze = prims.generate(width, height);

        if (!this.sanityCheck(mazeFolder)) { return false; } //Structures are square, all rotations are present

        int cellLength = Objects.requireNonNull(getStructure(CellType.WALL, mazeFolder, Rotation.DEGREES_0)).getSize().getBlockX();

       for(int x = 0; x < width; x++)
       {
           for(int y = 0; y < height; y++)
           {
               Cell cell = maze.grid()[x + y*width];
               Structure structure = getStructure(cell.getCellType(), mazeFolder, cell.getRotation());

               Location placeLocation = initLocation.clone().add(x*cellLength, 0, y*cellLength);

               assert structure != null;
               structure.place(placeLocation, false, StructureRotation.NONE, Mirror.NONE, 0, 1, rn);
           }
       }

        return true;
    }

    private Structure getStructure(CellType cellType, String resourceFolderName, Rotation rotation)
    {
        String fileLocation = cellType.getStructureLocation(resourceFolderName, rotation);


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


    private boolean sanityCheck(String mazeFolder) //Returns false is something is wrong with any of the structures in the selected folder.
    {
        for(CellType cellType : CellType.values())
        {
            for(Rotation rotation: Rotation.values())
            {
                if (plugin.getResource(cellType.getStructureLocation(mazeFolder, rotation)) == null)
                {
                    plugin.getComponentLogger().warn("Not all cells have a .nbt file! Missing: {}, Rotation variant: {}", mazeFolder + "/" + cellType, rotation);
                    return false;
                }
                Structure structure = getStructure(cellType, mazeFolder, rotation);
                assert structure != null;
                if ((structure.getSize().getZ()) != structure.getSize().getX())
                {
                    plugin.getComponentLogger().warn("Not all cells are square! Wrong size structure: {}, X: {}, Z: {}, Rotation variant: {}", mazeFolder + "/" + cellType, structure.getSize().getZ(), structure.getSize().getX(), rotation);
                    return false;
                }
            }
        }
        return true;
    }
}
