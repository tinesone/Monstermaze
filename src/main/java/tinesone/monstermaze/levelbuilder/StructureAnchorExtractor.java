package tinesone.monstermaze.levelbuilder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;

import java.util.Random;

public class StructureAnchorExtractor
{
    public StructureAnchorPoint getAnchorPoint(Structure structure, Location location)
    {
        place(structure, location);
        StructureAnchorPoint structureAnchorPoint = scanStructureForAnchorPoint(structure, location);
        cleanUp(structure, location);

        return structureAnchorPoint;

    }


    private void place(Structure structure, Location location)
    {
        structure.place(location, false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
    }


    private StructureAnchorPoint scanStructureForAnchorPoint(Structure structure, Location location)
    {
        int markerCount = 0;
        StructureAnchorPoint structureAnchorPoint = null;
        Location endLocation = location.clone().add(structure.getSize());

        for(int x = location.getBlockX(); x < location.getBlockX() + endLocation.getBlockX(); x++)
        {
            for(int y = location.getBlockZ(); y < location.getBlockY() + endLocation.getBlockY(); y++)
            {
                for(int z = location.getBlockZ(); z < location.getBlockZ() + endLocation.getBlockZ(); z++)
                {
                    Block block = new Location(location.getWorld(), x, y, z).getBlock();
                    if (block.getType().equals(Material.CHAIN_COMMAND_BLOCK))
                    {
                        CommandBlock commandBlock = (CommandBlock) block.getState();
                        if (commandBlock.getCommand().equals("marker"))
                        {
                            markerCount++;
                            structureAnchorPoint = new StructureAnchorPoint(x - location.getBlockX(), z - location.getBlockZ());
                        }
                    }
                }
            }
        }
        if (markerCount != 1) { return null; }
        return structureAnchorPoint;
    }

    private void cleanUp(Structure structure, Location location)
    {
        Location endLocation = location.clone().add(structure.getSize());
        for(int x = location.getBlockX(); x < location.getBlockX() + endLocation.getBlockX(); x++)
        {
            for(int y = location.getBlockZ(); y < location.getBlockY() + endLocation.getBlockY(); y++)
            {
                for(int z = location.getBlockZ(); z < location.getBlockZ() + endLocation.getBlockZ(); z++)
                {
                  new Location(location.getWorld(), x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }
    }
}
