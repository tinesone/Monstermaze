package tinesone.monstermaze.disguise;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import tinesone.monstermaze.util.NmsHelper;

import java.util.List;
import java.util.UUID;

public class MobDisguise //implements Listener
{
    private static int nextId = 250_000;
    private final int id;
    private final Player player;
    private final EntityType disguiseType;
    private final Plugin plugin;
    private int taskId;
    private boolean enabled = false;


    public MobDisguise(Plugin plugin, Player target, EntityType disguiseType)
    {
        this.id = nextId++;
        this.player = target;
        this.disguiseType = disguiseType;
        this.plugin = plugin;
    }

    public MobDisguise(Plugin plugin, Player target, EntityType disguiseType, boolean enable)
    {
        this(plugin, target, disguiseType);
        if (!enable)
            return;
        enableDisguise();
    }



    public void enableDisguise()
    {
        if (enabled)
            return;
        enabled = true;
        var others = plugin.getServer().getOnlinePlayers().stream()
                .filter(v -> v != player)
                .toList();

        others.forEach(this::disguisePlayer);

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> updatePlayerDisguisePosition(others), 2L, 1L);
    }

    public void disableDisguise()
    {
        if (!enabled)
            return;
        enabled = false;
        Bukkit.getScheduler().cancelTask(taskId);
        Packet<?> removePacket = new ClientboundRemoveEntitiesPacket(id);
        for (Player viewer : plugin.getServer().getOnlinePlayers())
        {
            if (viewer == player) continue;
            viewer.showPlayer(plugin, player);
            NmsHelper.sendPacketToBukkitPlayer(viewer, removePacket);
        }
    }

    private void disguisePlayer(Player v)
    {
        Packet<?> spawnPacket = new ClientboundAddEntityPacket(
                this.id,
                UUID.randomUUID(),
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getYaw(),
                player.getPitch(),
                CraftEntityType.bukkitToMinecraft(disguiseType),
                0,
                new Vec3(0, 0,0),
                0
        );
        NmsHelper.sendPacketToBukkitPlayer(v, spawnPacket);
        v.hidePlayer(plugin, player);
    }

    private void updatePlayerDisguisePosition(List<? extends Player> others)
    {
        Packet<?> movePacket = new ClientboundEntityPositionSyncPacket(
                id,
                new PositionMoveRotation(new Vec3(player.getX(), player.getY(), player.getZ()), new Vec3(0, 0, 0), player.getYaw(), player.getPitch()),
                true
        );
        others.forEach(v -> {NmsHelper.sendPacketToBukkitPlayer(v, movePacket);});
    }


    public EntityType getDisguiseType()
    {
        return disguiseType;
    }

    public int getId()
    {
        return id;
    }

    public Player getPlayer()
    {
        return player;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

//    @EventHandler
//    public void syncPunch(PlayerInteractEvent event)
//    {
//
//    }
}
