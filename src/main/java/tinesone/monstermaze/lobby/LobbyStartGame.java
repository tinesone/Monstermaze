package tinesone.monstermaze.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.HashMap;

public final class LobbyStartGame implements Listener
{
    private static final HashMap<Player, Boolean> readyPlayers = new HashMap<>();
    private static final World lobbyWorld = Bukkit.getWorlds().getFirst();
    //The first world (ie, the world in server.properties) should always be the lobby world

    public static boolean isReady(Player player)
    {
        return readyPlayers.get(player);
    }

    public static void setReady(Player player, boolean readyStatus)
    {
        readyPlayers.put(player, readyStatus);
    }

    public static void swapReady(Player player)
    {
        setReady(player, !isReady(player));
    }

    public static Player[] getReadyPlayers()
    {
        return Arrays.stream(readyPlayers.keySet().toArray(new Player[0])).filter(player -> readyPlayers.get(player)).toArray(Player[]::new);
    }

    public static Player[] getNotReadyPlayers()
    {
        return Arrays.stream(readyPlayers.keySet().toArray(new Player[0])).filter(player -> !readyPlayers.get(player)).toArray(Player[]::new);
    }

    public static float readyPercent()
    {
        return (float) getReadyPlayers().length / Bukkit.getOnlinePlayers().size();
    }

    public static boolean allReady()
    {
        return readyPercent() >= 1.0;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        readyPlayers.put(player, false);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        readyPlayers.remove(player);
    }

    @EventHandler
    public void OnReadyItemClick(PlayerInteractEvent event)
    {
        if (event.getPlayer().getLocation().getWorld() != lobbyWorld) return;
        Player player = event.getPlayer();
        if (event.getItem() == null) return;
        if (!event.getItem().equals(LobbyItems.readyItem(isReady(player)))) return;

        event.setCancelled(true);
        swapReady(player);

        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), LobbyItems.readyItem(isReady(player)));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, getPitch(isReady(player)));
    }

    private float getPitch(boolean isReady)
    {
        if (isReady)
            return 1.5f;
        return 0.5f;
    }
}
