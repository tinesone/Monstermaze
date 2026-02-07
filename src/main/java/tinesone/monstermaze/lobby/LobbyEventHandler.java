package tinesone.monstermaze.lobby;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.kyori.adventure.text.Component;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import tinesone.monstermaze.util.ConfigHelper;
import tinesone.monstermaze.util.DelayedTask;

public final class LobbyEventHandler implements Listener
{
    private final Plugin plugin;

    private final Location spawnLocation;
    private final Location parkourStartLocation;

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
        player.getInventory().clear();
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
    public void onPVPdamage(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof Player victim && victim.getWorld() == plugin.getServer().getWorlds().getFirst())) return;
        event.setCancelled(true);
        event.getDamager().remove();


        if(!event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) return;

        MobDisguise sheepDisguise = new MobDisguise(DisguiseType.SHEEP);
        victim.getAttribute(Attribute.SCALE).setBaseValue(0.5f);
        victim.playSound(victim.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 2.0F, 1.0F);
        victim.playSound(victim.getLocation(), Sound.ENTITY_SHEEP_AMBIENT, 2.0F, 1.0F);
        sheepDisguise.getWatcher().setScale(1d);
        sheepDisguise.setEntity(victim);
        sheepDisguise.startDisguise();
        new DelayedTask(() ->{
            sheepDisguise.stopDisguise();
            sheepDisguise.removePlayer(victim);
            victim.getAttribute(Attribute.SCALE).setBaseValue(1f);
        }, 5*20);

    }

    private void SetupLobbyPlayer(Player player)
    {
        if (player.getWorld() != plugin.getServer().getWorlds().getFirst())
            return;
        player.getInventory().clear();
        player.teleport(spawnLocation);
        player.setGameMode(GameMode.ADVENTURE);
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
}
