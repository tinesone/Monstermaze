package tinesone.monstermaze.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import tinesone.monstermaze.disguise.MobDisguise;

public class DisguiseTestCommand implements CommandExecutor
{
    private final Plugin plugin;
    public DisguiseTestCommand(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }
        EntityType type;
        try
        {
            type = EntityType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e)
        {
            player.sendMessage("That mob doesn't exist!");
            return false;
        }
        MobDisguise disguise = new MobDisguise(plugin, player, type, true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, disguise::disableDisguise, 500L);
        return true;
    }
}
