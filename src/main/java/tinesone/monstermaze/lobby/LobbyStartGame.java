package tinesone.monstermaze.lobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tinesone.monstermaze.MonstermazePlugin;
import tinesone.monstermaze.game.Game;
import tinesone.monstermaze.util.ConfigHelper;

import java.time.Duration;
import java.util.HashSet;

public final class LobbyStartGame
{
    public static boolean startingGame = false;
    private static boolean countdownStarted = false;
    private static final HashSet<Integer> taskIds = new HashSet<>();


    public static void startTimerIfReady(Plugin plugin)
    {
        if (startingGame) return;
        else if(LobbyReadyGame.readyPercent() >= 1)
        {
            startGame(plugin);
            return;
        }
        else if (countdownStarted) return;
        else if(LobbyReadyGame.getReadyPlayers().length < ConfigHelper.getInt("min-num-of-players")) return;
        else if(LobbyReadyGame.readyPercent() < ConfigHelper.getLong("start-percentage")) return;
        int countdownTimer = ConfigHelper.getInt("start-timer-duration");
        countdownStarted = true;
        timer(plugin, countdownTimer);
    }

    private static void timer(Plugin plugin, int durationInSeconds)
    {
        for(int i = 0; i < durationInSeconds; i++)
        {
            int finalI = i;
            int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                sendTimerTitleToAllPlayers(durationInSeconds - finalI);
            }, i* 20L);
            taskIds.add(taskId);
        }
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            startGame(plugin);
        }, (durationInSeconds + 1L)*20L);
        taskIds.add(taskId);
    }

    private static void cancelTimers()
    {
        taskIds.forEach(Bukkit.getScheduler()::cancelTask);
    }

    private static void startGame(Plugin plugin)
    {
        cancelTimers();
        countdownStarted = false;
        startingGame = true;
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);
        });
        LobbyReadyGame.removeBossBars();
        MonstermazePlugin.setGame(new Game(plugin, "example"));
    }


    private static void sendTimerTitle(Player player, int num)
    {
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds((long) 1.5), Duration.ofSeconds(0)));
        player.sendTitlePart(TitlePart.TITLE, Component.text(String.valueOf(num)).color(NamedTextColor.DARK_PURPLE));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 100, 1);
    }

    private static void sendTimerTitleToAllPlayers(int num)
    {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendTimerTitle(player, num);
        });
    }
}
