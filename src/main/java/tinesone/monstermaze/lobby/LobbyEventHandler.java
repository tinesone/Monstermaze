package tinesone.monstermaze.lobby;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tinesone.monstermaze.disguise.MobDisguise;
import tinesone.monstermaze.util.ConfigHelper;

import java.time.Duration;
import java.util.*;

public final class LobbyEventHandler implements Listener
{
    private final Plugin plugin;

    private final Location spawnLocation;
    private final Location parkourStartLocation;



    public LobbyEventHandler(Plugin plugin)
    {
        this.plugin = plugin;
        World lobby = plugin.getServer().getWorlds().getFirst();

        this.spawnLocation = ConfigHelper.getLocation(lobby, "lobby-spawn-location");
        this.parkourStartLocation = ConfigHelper.getLocation(lobby, "lobby-parkour-location");
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if(player.getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        event.joinMessage(Component.text()
                .content(player.getName())
                .color(NamedTextColor.YELLOW)
                .decorate(TextDecoration.BOLD)
                .append(Component.text()
                        .content(" Joined the game!")
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.BOLD, false))
                .build());
        SetupLobbyPlayer(player);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event)
    {
        if(event.getEntity().getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        if(!(event.getEntity() instanceof Player player && event.getCause() == EntityDamageEvent.DamageCause.FALL)) return;
        event.setCancelled(true);
        setupLobbyItems(player);
        if(player.getLocation().add(0, -1, 0).getBlock().getType() != Material.BARRIER) return;

       resetParkour(player);
    }

    @EventHandler
    public void onClickChest(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if(player.getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.CHEST) return;

        event.setCancelled(true);

        chestRewards(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if(player.getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event)
    {
        if (event.getEntity().getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        event.getEntity().remove();
        if (event.getHitEntity() == null) return;
        if (!(event.getHitEntity() instanceof Player)) return;
        event.setCancelled(true);
        sheepMorph(event);
    }

    private void sheepMorph(ProjectileHitEvent event)
    {
        MobDisguise disguise = new MobDisguise(plugin, (Player) event.getHitEntity(), EntityType.SHEEP, true);
        long duration = ConfigHelper.getLong("lobby-sheep-morph-duration")*20; //20 ticks in each second
        assert event.getHitEntity() != null;
        ((Player) event.getHitEntity()).playSound(event.getHitEntity().getLocation(), Sound.ENTITY_SHEEP_HURT, 2.0f, 1.0f);
        event.getHitEntity().sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(duration), Duration.ofSeconds(0)));
        event.getHitEntity().sendActionBar(Component.text("You are a sheep!").color(NamedTextColor.YELLOW));
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, disguise::disableDisguise, duration);
    }

    @EventHandler
    public void cancelPVP(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player damager && damager.getWorld() == plugin.getServer().getWorlds().getFirst())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void cancelEye(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if(!(player.getWorld() == plugin.getServer().getWorlds().getFirst() && (event.getItem() != null ? event.getItem().getType() : null) == Material.ENDER_EYE)) return;

        event.setCancelled(true);
        player.sendMessage(Component.text().content("Not implemented yet!")
                .color(NamedTextColor.DARK_RED)
                .build());
    }

    @EventHandler
    public void cancelDrop(PlayerDropItemEvent event)
    {
        if (event.getPlayer().getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        event.setCancelled(true);
    }


    private void SetupLobbyPlayer(Player player)
    {
        if (player.getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        player.teleport(spawnLocation);
        player.setGameMode(GameMode.ADVENTURE);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, 0, false, false));


        setupLobbyItems(player);

    }



    private void resetParkour(Player player)
    {
        player.teleport(parkourStartLocation);
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    private void chestRewards(Player player)
    {
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
        player.getInventory().setItem(4, getBow());
        player.getInventory().setItem(9, new ItemStack(Material.ARROW));
    }


    private ItemStack getBow()
    {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.itemName(Component.text("Sheep-tron 5000").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        bow.setItemMeta(meta);
        return bow;
    }

    private void setupLobbyItems(Player player)
    {
        player.getInventory().clear();


        player.getInventory().setItem(0, LobbyItems.readyItem(LobbyReadyGame.isReady(player)));
        player.getInventory().setItem(1, LobbyItems.getClassSelectItem());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void kickLatePlayer(PlayerJoinEvent event)
    {
        if (LobbyStartGame.startingGame)
        {
            event.getPlayer().kick(Component.text("Game is starting. Please wait 1 minute").color(NamedTextColor.DARK_RED));
        }
    }
}
