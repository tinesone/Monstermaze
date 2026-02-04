package tinesone.monstermaze;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tinesone.monstermaze.commands.GenerateMazeCommand;
import tinesone.monstermaze.levelbuilder.LevelBuilder;
import tinesone.monstermaze.lobby.LobbyManager;

import java.util.Objects;

public class MonstermazePlugin extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        LevelBuilder levelBuilder = new LevelBuilder(this);
        Objects.requireNonNull(getCommand("generateMaze")).setExecutor(new GenerateMazeCommand(this, levelBuilder));

        Bukkit.getPluginManager().registerEvents(new LobbyManager(this), this);

        this.getComponentLogger().info(Component.text("Successfully enabled Monstermaze plugin!!").color(NamedTextColor.GOLD));
    }

    @Override
    public void onDisable()
    {
        this.getComponentLogger().info("Goodbye!");
    }
}
