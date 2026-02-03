package tinesone.monstermaze.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        World world = plugin.getServer().getWorld(args[3]);
        if(commandSender instanceof Player player && world == null)
        {
            world = player.getWorld();
        }

        if(world == null)
        {
            commandSender.sendMessage(Component.text("Please enter a valid world name!").color(NamedTextColor.RED));
            return true;
        }

        Location initialLocation;
        try
        {
            initialLocation = new Location(world, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
        {
            commandSender.sendMessage(Component.text("Please enter a valid location").color(NamedTextColor.RED));
            return true;
        }


        if (!levelBuilder.place(initialLocation, 25, 35, "test"))
        {
            commandSender.sendMessage(Component.text("An Error when building the maze has occurred. Please check the log/console").color(NamedTextColor.RED));
            return true;
        }
        return true;
    }
}
