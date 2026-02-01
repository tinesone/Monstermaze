package tinesone.monstermaze;


import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tinesone.monstermaze.commands.GenerateMazeCommand;
import tinesone.monstermaze.levelbuilder.LevelBuilder;

public class MonstermazePlugin extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(this, this);
        LevelBuilder levelBuilder = new LevelBuilder(this);
        getCommand("generateMaze").setExecutor(new GenerateMazeCommand(this, levelBuilder));
    }
}
