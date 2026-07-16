package tinesone.monstermaze.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import tinesone.monstermaze.MonstermazePlugin;
import tinesone.monstermaze.game.Game;

public class StopCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args)
    {
        if (MonstermazePlugin.getGame() == null)
        {
            sender.sendMessage("No active game!");
            return false;
        }
        Game game = MonstermazePlugin.getGame();
        game.stopGame();
        return true;
    }
}
