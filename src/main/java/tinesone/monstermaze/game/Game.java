package tinesone.monstermaze.game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tinesone.monstermaze.levelbuilder.LevelBuilder;
import tinesone.monstermaze.maze.generators.Prims;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class Game
{
    private final Player[] players;
    private final Player monster;
    private final Random random = new Random();
    private final World gameWorld;
    private final Plugin plugin;
    private int timer = 0;

    public Game(Plugin plugin, Player monster, String levelName)
    {
        this.plugin = plugin;
        this.players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        this.monster = monster;
        this.gameWorld = Bukkit.createWorld(buildWorldCreator());

        LevelBuilder levelBuilder = new LevelBuilder(plugin, levelName);
        levelBuilder.place(new Location(gameWorld, 0, 0, 0), new Prims());
        Arrays.stream(players).forEach(player -> {
            player.setGameMode(GameMode.CREATIVE);
            player.teleport(new Location(gameWorld, 0, 5, 0));
        });
    }

    public Game(Plugin plugin, String levelName)
    {
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        Player monster = players[new Random().nextInt(players.length)];
        this(plugin, monster, levelName);
    }

    private WorldCreator buildWorldCreator()
    {
        WorldCreator worldCreator = new WorldCreator("game-world-" + UUID.randomUUID().toString())
                .type(WorldType.FLAT)
                .generatorSettings("{\"version\":3,\"layers\":[{\"block\":\"minecraft:air\",\"height\":1}],\"structures\":{},\"biome\":\"minecraft:plains\"}")
                ;
        return worldCreator;
    }
}
