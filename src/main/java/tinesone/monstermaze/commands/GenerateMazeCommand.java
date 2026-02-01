package tinesone.monstermaze.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import tinesone.monstermaze.levelbuilder.LevelBuilder;


public class GenerateMazeCommand implements CommandExecutor
{
    private final LevelBuilder levelBuilder;
    private final Plugin plugin;

    public GenerateMazeCommand(Plugin plugin, LevelBuilder levelBuilder)
    {
        this.plugin = plugin;
        this.levelBuilder = levelBuilder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args)
    {

        Location initialLocation;
        try
        {
            initialLocation = new Location(plugin.getServer().getWorld("world"), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
        {
            commandSender.sendMessage( "Please enter a valid location.");
            return true;
        }


        if (!levelBuilder.place(initialLocation, 25, 35, "test"))
        {
            commandSender.sendMessage("error when loading the maze. Check the log");
            return true;
        }
        return true;
    }
}
