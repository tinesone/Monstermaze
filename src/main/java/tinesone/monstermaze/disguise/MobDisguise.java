package tinesone.monstermaze.disguise;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
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

    private boolean enabled = false;

    public MobDisguise(Player target, EntityType disguiseType, boolean enabled, Plugin plugin)
    {
        this.id = nextId++;
        this.player = target;
        this.disguiseType = disguiseType;
        this.plugin = plugin;
        this.enabled = enabled;
    }

    public MobDisguise(Player target, EntityType disguiseType, Plugin plugin)
    {
        this(target, disguiseType, false, plugin);
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        if (!enabled) return;
        this.disguise();
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
        for(Player viewer : Bukkit.getOnlinePlayers())
        {
            if(viewer == player) continue;
            ServerPlayer nmsPlayerViewer = ((CraftPlayer) viewer).getHandle();
            nmsPlayerViewer.connection.send(spawnPacket);
            viewer.hidePlayer(plugin, player);
            viewers.add(viewer);
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Packet<?> movePacket = new ClientboundEntityPositionSyncPacket(
                    id,
                    new PositionMoveRotation(new Vec3(player.getX(), player.getY(), player.getZ()), new Vec3(0, 0, 0), player.getYaw(), player.getPitch()),
                    true
            );
            //TODO: move entity
        }, 2L, 2L);
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
}
