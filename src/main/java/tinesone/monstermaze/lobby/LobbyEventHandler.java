package tinesone.monstermaze.lobby;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tinesone.monstermaze.util.ConfigHelper;
import tinesone.monstermaze.util.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public final class LobbyEventHandler implements Listener
{
    private final Plugin plugin;

    private final Location spawnLocation;
    private final Location parkourStartLocation;

    private HashSet<Task> taskList = new HashSet<>();

    private Map<Player, Boolean> readyToStart = new HashMap<>();

    public LobbyEventHandler(Plugin plugin)
    {
        this.plugin = plugin;
        World lobby = plugin.getServer().getWorlds().getFirst();

        ConfigHelper configReader = new ConfigHelper(plugin);
        this.spawnLocation = configReader.getLocation(lobby, "lobby-spawn-location");
        this.parkourStartLocation = configReader.getLocation(lobby, "lobby-parkour-location");
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
        //TODO: add sheep disguise
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
        if(!(player.getWorld() == plugin.getServer().getWorlds().getFirst() && event.getItem().getType() == Material.ENDER_EYE)) return;

        event.setCancelled(true);
        player.sendMessage(Component.text().content("Not implemented yet!")
                .color(NamedTextColor.DARK_RED)
                .build());
    }


    private void SetupLobbyPlayer(Player player)
    {
        if (player.getWorld() != plugin.getServer().getWorlds().getFirst()) return;
        player.teleport(spawnLocation);
        player.setGameMode(GameMode.ADVENTURE);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, 0, false, false));

        closeAllLobbyDoorToPlayer(player);

        readyToStart.put(player, false);

        setupLobbyItems(player);

    }

    private void openRandomLobbyDoorToPlayer(Player player)
    {
        Location[] locations = new ConfigHelper(plugin).getLocations(player.getWorld(), "lobby-start-doors");

        Random rnd = new Random();

        assert locations != null;
        int randomIndex = rnd.nextInt(locations.length);


        locations[randomIndex] = null;
        placeDoors(player, locations);
    }

    private void closeAllLobbyDoorToPlayer(Player player)
    {
        Location[] locations = new ConfigHelper(plugin).getLocations(player.getWorld(), "lobby-start-doors");


        assert locations != null;

        placeDoors(player, locations);
    }

    private void placeDoors(Player player, Location[] locations)
    {
        new Task(() -> {

            for (Location location : locations)
            {
                if (location == null) continue;
                player.sendBlockChange(location, Material.STONE_BRICKS.createBlockData());
                player.sendBlockChange(location.clone().add(0, 1, 0), Material.STONE_BRICKS.createBlockData());
            }
        }, 0, 1);
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

        ItemStack eye = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta = eye.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        meta.displayName(Component.text()
                .content("Choose your class. Currently selected: ")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text("None")
                        .decoration(TextDecoration.ITALIC, true)
                        .color(NamedTextColor.DARK_RED))
                .build());

        eye.setItemMeta(meta);

        player.getInventory().setItem(0, readyItem(readyToStart.get(player)));
        player.getInventory().setItem(1, eye);
    }

    private ItemStack readyItem(Boolean isReady)
    {
        Material material;
        TextComponent itemName;
        if (isReady)
        {
            material = Material.EMERALD;
            itemName = Component.text()
                    .content("Ready!")
                    .decoration(TextDecoration.ITALIC, false)
                    .decoration(TextDecoration.BOLD, true)
                    .color(NamedTextColor.GREEN)
                    .build();
        }
        else
        {
            material = Material.BARRIER;
            itemName = Component.text()
                    .content("Ready?")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.DARK_RED)
                    .build();
        }

        ItemStack readyItem = new ItemStack(material);
        ItemMeta meta = readyItem.getItemMeta();
        meta.displayName(itemName);
        readyItem.setItemMeta(meta);

        return readyItem;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        readyToStart.remove(player);
    }
}
