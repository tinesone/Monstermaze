package tinesone.monstermaze.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;

public final class LobbyReadyGame implements Listener
{
    private static final HashMap<Player, Boolean> readyPlayers = new HashMap<>();
    private static final World lobbyWorld = Bukkit.getWorlds().getFirst();
    //The first world (ie, the world in server.properties) should always be the lobby world

    private static BossBar readyBossBar = Bukkit.createBossBar("§40/0", BarColor.BLUE, BarStyle.SOLID);
    private static BossBar notReadyBossBar = Bukkit.createBossBar("§40/0", BarColor.RED, BarStyle.SOLID);

    private final Plugin plugin;

    public LobbyReadyGame(Plugin plugin)
    {
        this.plugin = plugin;
    }

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

        addPlayerToBossBar(player);
        updateBossBar();
    }

    private static void updateBossBar()
    {
        String title = "§6" + getReadyPlayers().length + "/" + Bukkit.getOnlinePlayers().size();

        notReadyBossBar.setTitle(title);
        readyBossBar.setTitle(title);

        notReadyBossBar.setProgress((double) getReadyPlayers().length / Bukkit.getOnlinePlayers().size());
        readyBossBar.setProgress((double) getReadyPlayers().length / Bukkit.getOnlinePlayers().size());
    }

    public static void removeBossBars()
    {
        readyBossBar.removeAll();
        notReadyBossBar.removeAll();
    }

    private static void addPlayerToBossBar(Player player)
    {
        if (isReady(player))
        {
            notReadyBossBar.removePlayer(player);
            readyBossBar.addPlayer(player);
        }
        else
        {
            readyBossBar.removePlayer(player);
            notReadyBossBar.addPlayer(player);
        }
    }


    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        readyPlayers.remove(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, LobbyReadyGame::updateBossBar, 5);
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
        updateBossBar();
        addPlayerToBossBar(player);

        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), LobbyItems.readyItem(isReady(player)));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, getPitch(isReady(player)));
        if(isReady(player)) {
            LobbyStartGame.startTimerIfReady(plugin);
        }
    }

    private float getPitch(boolean isReady)
    {
        if (isReady)
            return 1.5f;
        return 0.5f;
    }
}
