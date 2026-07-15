package tinesone.monstermaze.lobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tinesone.monstermaze.util.ConfigHelper;

import java.time.Duration;

public final class LobbyStartGame
{
    public static boolean startingGame = false;

    public static void StartGame(Plugin plugin, LobbyDoors lobbyDoors)
    {
        if(!LobbyReadyGame.allReady()) return;
        else if(LobbyReadyGame.getReadyPlayers().length < ConfigHelper.getInt("min-num-of-players")) return;
        startingGame = true;
        lobbyDoors.setDoorsOpenState(true);
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendSound(player);
            sendTitle(player);
            player.getInventory().clear();
        });
        LobbyReadyGame.removeBossBars();
    }

    private static void sendSound(Player player)
    {
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 0.75f);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 0.5f);
    }

    private static void sendTitle(Player player)
    {
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(5), Duration.ofSeconds(3)));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Walk into the tower!")
                .color(NamedTextColor.DARK_PURPLE));
        player.sendTitlePart(TitlePart.TITLE, Component.text("Game starting!")
                .color(NamedTextColor.DARK_PURPLE)
                .decoration(TextDecoration.BOLD, true));
    }
}
