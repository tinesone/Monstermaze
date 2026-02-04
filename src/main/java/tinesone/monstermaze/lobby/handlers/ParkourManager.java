package tinesone.monstermaze.lobby.handlers;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public final class ParkourManager
{

    Plugin plugin;
    Location parkourStart;

    public ParkourManager(Plugin plugin, Location parkourStart)
    {
        this.plugin = plugin;
        this.parkourStart = parkourStart;
    }

    public void resetParkour(EntityDamageEvent event, Player player)
    {
        event.setCancelled(true);
        player.teleport(parkourStart);
        player.playSound(player, Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
    }
}
