package tinesone.monstermaze.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tinesone.monstermaze.LevelBuilder;


public class GenerateMazeCommand implements CommandExecutor
{
    private final LevelBuilder levelBuilder;

    public GenerateMazeCommand(LevelBuilder levelBuilder)
    {
        this.levelBuilder = levelBuilder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args)
    {
        if (!(commandSender instanceof Player player))
        {
            commandSender.sendMessage("Only players can execute this command.");
            return true;
        }

        Location initialLocation;
        try
        {
            initialLocation = new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[0]), Integer.parseInt(args[0]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
        {
            commandSender.sendMessage( "Please enter a valid location.");
            return true;
        }

        levelBuilder.place(initialLocation, 25, 25, player);
        return true;
    }
}
