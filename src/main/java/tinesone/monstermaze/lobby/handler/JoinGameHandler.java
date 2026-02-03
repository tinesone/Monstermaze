package tinesone.monstermaze.lobby.handler;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;


public final class JoinGameHandler implements Listener
{
    Plugin plugin;

    Location spawnLocation;

    public JoinGameHandler(Plugin plugin)
    {
        this.plugin = plugin;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if(player.getWorld() != plugin.getServer().getWorlds().getFirst())
            return;
        event.joinMessage(null);
        player.teleport(spawnLocation);
        player.setGameMode(GameMode.ADVENTURE);
    }
}
