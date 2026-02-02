package tinesone.monstermaze.levelbuilder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.structure.Structure;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.StructureManager;
import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.Maze;
import tinesone.monstermaze.maze.generators.Prims;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
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

        if (!this.sanityCheck(mazeFolder)) { return false; } //Structures are square


        int cellLength = (int) Objects.requireNonNull(getStructure(CellType.WALL, mazeFolder)).getSize().getX(); //Assume all cells are equal length

        EnumMap<CellType, MazePiece> cellRegistry = makeCellRegistry(mazeFolder, initLocation);
        if (cellRegistry == null)
        {
            return false;
        }


        for(int x=0; x<width;x++)
        {
            for(int y=0; y<height;y++)
            {
                Cell cell = maze.grid()[x + y*width];

                Location cellOrigin = initLocation.clone().add(x*cellLength, 0, y*cellLength);

                MazePlacer.placePiece(cellRegistry.get(cell.getCellType()), cellOrigin, cell.getRotation(), rn);

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


    private boolean sanityCheck(String mazeFolder) //Returns false is something is wrong with any of the structures in the selected folder.
    {
        for(CellType cellType : CellType.values())
        {
            if (plugin.getResource(cellType.getStructureLocation(mazeFolder)) == null)
            {
                plugin.getComponentLogger().warn("Not all cells have a .nbt file! Missing: {}", mazeFolder + "/" + cellType);
                return false;
            }
            Structure structure = getStructure(cellType, mazeFolder);
            assert structure != null;
            if ((structure.getSize().getZ()) != structure.getSize().getX())
            {
                plugin.getComponentLogger().warn("Not all cells are square! Wrong size structure: {}, X: {}, Z: {}", mazeFolder + "/" + cellType, structure.getSize().getZ(), structure.getSize().getX());
                return false;
            }
        }
        return true;
    }

    private EnumMap<CellType, MazePiece> makeCellRegistry(String mazeFolder, Location location)
    {
        EnumMap<CellType, MazePiece> registry = new EnumMap<>(CellType.class);

        StructureAnchorExtractor anchorExtractor = new StructureAnchorExtractor();

        int counter = 0;


        for (CellType cellType : CellType.values())
        {
            Structure structure = getStructure(cellType, mazeFolder);
            assert structure != null;

            int cellLength = (int) Objects.requireNonNull(getStructure(cellType, mazeFolder)).getSize().getX();

            Location testLocation = location.clone().add(counter*cellLength+5, 0, 0);
            StructureAnchorPoint anchorPoint;
            try
            {
                anchorPoint = anchorExtractor.getAnchorPoint(structure, testLocation);
            } catch (IllegalArgumentException e)
            {
                plugin.getComponentLogger().warn("Cannot find anchorPoint for structure: {}", mazeFolder + "/" + cellType);
                return null;
            }

            registry.put(cellType, new MazePiece(structure, anchorPoint));
            counter++;
        }
        return registry;
    }
}
