package tinesone.monstermaze.levelbuilder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class MazeTiles
{
    private final int width;
    private final int height;
    private final Plugin plugin;
    private final String mazeFolder;
    private final String metaDataLocation;

    public MazeTiles(Plugin plugin, String mazeFolder)
    {
        metaDataLocation = "/structures/" + mazeFolder + "/metadata.yml";
        this.plugin = plugin;
        if (!sanityCheck(mazeFolder)) {
            throw new IllegalArgumentException("Sanity check failed for \"" + mazeFolder + "\"");
        }
        Map<String, Integer> metaData = new Yaml().load(getClass().getResourceAsStream(metaDataLocation));
        this.mazeFolder = mazeFolder;
        this.width = metaData.get("width");
        this.height = metaData.get("height");
    }

    public Structure getStructure(CellType cellType, Rotation rotation)
    {
        return loadStructure(cellType, rotation, mazeFolder);
    }

    public int getAmountTilesWidth()
    {
        return width;
    }

    public int getAmountTilesHeight()
    {
        return height;
    }

    private boolean sanityCheck(String mazeFolder) //Returns false is something is wrong with any of the structures in the selected folder.
    {
        for (CellType cellType : CellType.values()) {
            for (Rotation rotation : Rotation.values()) {
                if (plugin.getResource(cellType.getStructureLocation(mazeFolder, rotation)) == null) {
                    plugin.getComponentLogger().warn("Not all cells have a .nbt file! Missing: {}, Rotation variant: {}", mazeFolder + "/" + cellType, rotation);
                    return false;
                }
                Structure structure = loadStructure(cellType, rotation, mazeFolder);
                assert structure != null;
                if ((structure.getSize().getZ()) != structure.getSize().getX()) {
                    plugin.getComponentLogger().warn("Not all cells are square! Wrong size structure: {}, X: {}, Z: {}, Rotation variant: {}", mazeFolder + "/" + cellType, structure.getSize().getZ(), structure.getSize().getX(), rotation);
                    return false;
                }
            }
        }
        try {
            new Yaml().load(getClass().getResourceAsStream(metaDataLocation));
        } catch (Exception e) {
            plugin.getComponentLogger().warn("Failed to load metadata.yml of \"{}\"", mazeFolder);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Structure loadStructure(CellType cellType, Rotation rotation, String resourceFolderName)
    {
        String fileLocation = cellType.getStructureLocation(resourceFolderName, rotation);


        InputStream stream = plugin.getResource(fileLocation);

        if (stream == null) {
            return null;
        }


        StructureManager structureManager = Bukkit.getStructureManager();
        try {
            return structureManager.loadStructure(stream);
        } catch (IOException e) {
            return null;
        }
    }
}
