package tinesone.monstermaze;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tinesone.monstermaze.commands.DisguiseTestCommand;
import tinesone.monstermaze.commands.GenerateMazeCommand;
import tinesone.monstermaze.disguise.MobDisguise;
import tinesone.monstermaze.levelbuilder.LevelBuilder;
import tinesone.monstermaze.lobby.LobbyEventHandler;

import java.util.Objects;

public class MonstermazePlugin extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        LevelBuilder levelBuilder = new LevelBuilder(this);
        Objects.requireNonNull(getCommand("generateMaze")).setExecutor(new GenerateMazeCommand(this, levelBuilder));
        Objects.requireNonNull(getCommand("disguise")).setExecutor(new DisguiseTestCommand(this));

        Bukkit.getPluginManager().registerEvents(new LobbyEventHandler(this), this);

        this.getComponentLogger().info(Component.text("Successfully enabled Monstermaze plugin!! Version: " + this.getPluginMeta().getVersion()).color(NamedTextColor.GOLD));

        //new Task(this);
    }

    @Override
    public void onDisable()
    {
        this.getServer().getOnlinePlayers().stream().filter(MobDisguise::isPlayerDisguised).forEach(player -> {
            Objects.requireNonNull(player.getAttribute(Attribute.SCALE)).setBaseValue(1f);
        }); //Reset scale before shutting down, prevent weird shenanigans.
        this.getComponentLogger().info("Goodbye!");
    }
}
