package tinesone.monstermaze;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import tinesone.monstermaze.maze.CardinalDirection;
import tinesone.monstermaze.maze.Cell;
import tinesone.monstermaze.maze.Maze;
import tinesone.monstermaze.maze.generators.Prims;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Random;

public class LevelBuilder
{
    private Plugin plugin;

    public LevelBuilder(@NotNull Plugin plugin)
    {
        this.plugin = plugin;
    }

    public boolean place(Location location, int width, int height, Player player)
    {
        Prims prims = new Prims();
        Maze maze = prims.generate(width, height);

        Structure mazePiece= getStructure(maze.grid()[4]);

        assert mazePiece != null;
        // player.sendMessage(String.valueOf(mazePiece.getSize().getX()));
        // player.sendMessage(location.toString());
        mazePiece.place(location, false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
        return true;
    }

    private Structure getStructure(Cell cell)
    {

        InputStream stream = null;

        EnumSet<CardinalDirection> openWalls = cell.getOpenWalls();
        if(openWalls.isEmpty())
        {
            stream = plugin.getResource("structures/test/wall.nbt");
        } else if (openWalls.size() == 1)
        {
            stream = plugin.getResource("structures/test/deadend.nbt");
        } else if (openWalls.size() == 2)
        {
            if(openWalls.equals(EnumSet.of(CardinalDirection.NORTH, CardinalDirection.SOUTH)) || openWalls.equals(EnumSet.of(CardinalDirection.EAST, CardinalDirection.WEST)))
            {
                stream = plugin.getResource("structures/test/straigth.nbt");
            } else
            {
                stream = plugin.getResource("structures/test/corner.nbt");
            }
        } else if (openWalls.size() == 3)
        {
            stream = plugin.getResource("structures/test/t_cross.nbt");
        } else
        {
            stream = plugin.getResource("structures/test/cross.nbt");
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
