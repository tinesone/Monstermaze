package tinesone.monstermaze.disguise;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.UUID;

public class MobDisguise
{
    private static int nextId = 250_000;
    private final int id;
    private final Player player;
    private final EntityType disguiseType;
    private final Plugin plugin;
    private int taskId;

    private boolean enabled = false;

    public MobDisguise(Plugin plugin, Player target, EntityType disguiseType, boolean enabled)
    {
        this.id = nextId++;
        this.player = target;
        this.disguiseType = disguiseType;
        this.plugin = plugin;
        this.setEnabled(enabled);
    }

    public MobDisguise(Plugin plugin, Player target, EntityType disguiseType)
    {
        this(plugin, target, disguiseType, false);
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        if (enabled)
            this.disguise();
        else
            this.reveal();
    }

    private void disguise()
    {
        this.enabled = true;
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

        HashSet<Player> viewers = new HashSet<>();
        for(Player viewer : plugin.getServer().getOnlinePlayers())
        {
            if (viewer == player) continue;
            sendPacketToBukkitPlayer(viewer, spawnPacket);
            viewer.hidePlayer(plugin, player);
            viewers.add(viewer);
        }

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Packet<?> movePacket = new ClientboundEntityPositionSyncPacket(
                    id,
                    new PositionMoveRotation(new Vec3(player.getX(), player.getY(), player.getZ()), new Vec3(0, 0, 0), player.getYaw(), player.getPitch()),
                    true
            );
            for (Player viewer : viewers)
            {
                sendPacketToBukkitPlayer(viewer, movePacket);
            }
        }, 2L, 1L);
    }

    private void reveal()
    {
        Bukkit.getScheduler().cancelTask(taskId);
        Packet<?> removePacket = new ClientboundRemoveEntitiesPacket(id);
        for (Player viewer : plugin.getServer().getOnlinePlayers())
        {
            if (viewer == player) continue;
            viewer.showPlayer(plugin, player);
            sendPacketToBukkitPlayer(viewer, removePacket);
        }
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

    private static void sendPacketToBukkitPlayer(Player receiver, Packet<?> packet)
    {
        ServerPlayer nmsPlayerReceiver = ((CraftPlayer) receiver).getHandle();
        nmsPlayerReceiver.connection.send(packet);
    }
}
