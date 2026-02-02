package tinesone.monstermaze.levelbuilder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.util.Vector;

import java.util.Random;

public class StructureAnchorExtractor
{
    public StructureAnchorPoint getAnchorPoint(Structure structure, Location location) throws IllegalArgumentException
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


    private StructureAnchorPoint scanStructureForAnchorPoint(Structure structure, Location location) throws IllegalArgumentException
    {
        int markerCount = 0;
        StructureAnchorPoint structureAnchorPoint = null;
        Vector size = structure.getSize();
        Location endLocation = location.clone().add(size.getX(), size.getY(), size.getZ());

        Location blockLocation = location.clone();

        for(int x = location.getBlockX(); x < endLocation.getBlockX(); x++)
        {
            for(int y = location.getBlockY(); y < endLocation.getBlockY(); y++)
            {
                for(int z = location.getBlockZ(); z < endLocation.getBlockZ(); z++)
                {
                    blockLocation.set(x, y, z);
                    Block block = blockLocation.getBlock();

                    if (!block.getType().equals(Material.CHAIN_COMMAND_BLOCK))
                    {
                       continue;
                    }
                    CommandBlock commandBlock = (CommandBlock) block.getState();
                    if (!commandBlock.getCommand().equals("marker"))
                    {
                        continue;
                    }
                    markerCount++;
                    structureAnchorPoint = new StructureAnchorPoint(x - location.getBlockX(), z - location.getBlockZ());
                }
            }
        }
        if (markerCount == 0) { throw new IllegalArgumentException("No marker found in structure"); }
        if (markerCount > 1) { throw new IllegalArgumentException("Too many markers found in structure"); }
        return structureAnchorPoint;
    }

    private void cleanUp(Structure structure, Location location)
    {
        Location endLocation = location.clone().add(structure.getSize());
        for(int x = location.getBlockX(); x < endLocation.getBlockX(); x++)
        {
            for(int y = location.getBlockY(); y < endLocation.getBlockY(); y++)
            {
                for(int z = location.getBlockZ(); z < endLocation.getBlockZ(); z++)
                {
                  new Location(location.getWorld(), x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }
    }
}
