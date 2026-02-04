package tinesone.monstermaze.lobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import tinesone.monstermaze.lobby.handlers.JoinGameHandler;
import tinesone.monstermaze.lobby.handlers.ParkourManager;
import tinesone.monstermaze.util.ConfigLocationReader;

public final class LobbyManager implements Listener
{
    Plugin plugin;
    ConfigLocationReader configLocationReader;
    JoinGameHandler joinGameHandler;
    ParkourManager parkourManager;

    public LobbyManager(Plugin plugin)
    {
        this.plugin = plugin;
        this.configLocationReader = new ConfigLocationReader(plugin);

        makeJoinGameHandler(plugin);
        makeParkourManager(plugin);
    }

    private void makeJoinGameHandler(Plugin plugin)
    {
        Location spawnLocation = configLocationReader.getLocation(plugin.getServer().getWorlds().getFirst(), "lobby-spawn-location");
        this.joinGameHandler = new JoinGameHandler(plugin, spawnLocation);
    }

    private void makeParkourManager(Plugin plugin)
    {
        Location parkourStart = configLocationReader.getLocation(plugin.getServer().getWorlds().getFirst(), "lobby-parkour-location");
        this.parkourManager = new ParkourManager(plugin, parkourStart);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        if(event.getPlayer().getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        joinGameHandler.SetupLobbyPlayer(event);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if(event.getEntity().getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        if(event.getEntity() instanceof Player player && event.getCause() == EntityDamageEvent.DamageCause.FALL)
        {
            parkourManager.resetParkour(event, player);
        }
    }
}
