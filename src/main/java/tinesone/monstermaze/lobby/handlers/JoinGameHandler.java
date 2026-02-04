package tinesone.monstermaze.lobby.handlers;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;


public final class JoinGameHandler
{
    Plugin plugin;

    Location spawnLocation;

    public JoinGameHandler(Plugin plugin, Location spawnLocation)
    {
        this.plugin = plugin;
        this.spawnLocation = spawnLocation;

    }

    public void SetupLobbyPlayer(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if(player.getWorld() != plugin.getServer().getWorlds().getFirst())
            return;
        event.joinMessage(null);
        player.teleport(spawnLocation);
        player.setGameMode(GameMode.ADVENTURE);
    }
}
