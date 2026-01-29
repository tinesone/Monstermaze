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
    private Plugin plugin;

    public LevelBuilder(@NotNull Plugin plugin)
    {
        this.plugin = plugin;
    }

    public boolean place(Location location, int width, int height, String mazeFolder)
    {
        Prims prims = new Prims();
        Maze maze = prims.generate(width, height);
        for(CellType cellType : CellType.values())
        {
            if (plugin.getResource(cellType.getStructureLocation(mazeFolder)) == null)
            {
                plugin.getComponentLogger().warn("Not all cells have a .nbt file! Missing: {}", cellType);
                return false;
            }
            Structure structure = getStructure(CellType.WALL, mazeFolder);
            assert structure != null;
            if ((structure.getSize().getZ()) != structure.getSize().getX())
            {
                plugin.getComponentLogger().warn("Not all cells are square! Wrong size structure: {} X: {} Z: {}", cellType, structure.getSize().getZ(), structure.getSize().getX());
                return false;
            }
        }


        int step = (int) Objects.requireNonNull(getStructure(CellType.WALL, mazeFolder)).getSize().getX();

        Random rn = new Random();

        for(int x=0; x<width;x++)
        {
            for(int y=0; y<height;y++)
            {
                Cell cell = maze.grid()[x + y*width];
                Structure mazePiece= getStructure(cell.getCellType(), mazeFolder);

                if (mazePiece == null) { return false; }
                StructureRotation rotation = cell.getRotation();

                Location placeLocation;
                placeLocation = location.clone();


                if (rotation.equals(StructureRotation.CLOCKWISE_90)) { placeLocation.add(-1 + step, 0, 0); }
                else if (rotation.equals(StructureRotation.COUNTERCLOCKWISE_90)) { placeLocation.add(0, 0, -1 + step); }
                else if (rotation.equals(StructureRotation.CLOCKWISE_180)) {placeLocation.add(-1 + step, 0, -1 + step); }

                mazePiece.place(placeLocation, false, rotation, Mirror.NONE, 0, 1, rn);
                location.add(step, 0, 0);
            }
            location.add(-height*step, 0, step);
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
}
