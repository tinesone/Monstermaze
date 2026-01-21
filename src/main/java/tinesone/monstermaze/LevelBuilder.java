package tinesone.monstermaze;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import tinesone.monstermaze.maze.Maze;
import tinesone.monstermaze.maze.generators.Prims;

public class LevelBuilder
{
    private final NamespacedKey crossKey;
    private final NamespacedKey straightKey;
    private final NamespacedKey tCrossKey;
    private final NamespacedKey wallKey;
    private final NamespacedKey cornerKey;


    public LevelBuilder(Plugin plugin)
    {
        this.cornerKey = new NamespacedKey(plugin, "corner");
        this.crossKey = new NamespacedKey(plugin, "cross");
        this.straightKey = new NamespacedKey(plugin, "straight");
        this.tCrossKey = new NamespacedKey(plugin, "t_cross");
        this.wallKey = new NamespacedKey(plugin, "wall");
        plugin.getLogger().info(crossKey.toString());
    }

    public void place(Location location, int width, int height, Player player)
    {
        Prims prims = new Prims();
        Maze maze = prims.generate(width, height);
        StructureManager structureManager = Bukkit.getStructureManager();
        Structure cell = structureManager.getStructure(crossKey);
        player.sendMessage(String.valueOf(cell.getSize().getX()));
    }
}
