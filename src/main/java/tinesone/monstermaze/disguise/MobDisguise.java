package tinesone.monstermaze.disguise;

import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import tinesone.monstermaze.util.NmsHelper;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class MobDisguise implements Listener
{
    private static int nextId = 250_000;
    private final int id;
    private final Player player;
    private final EntityType disguiseType;
    private final Plugin plugin;
    private int taskId;
    private boolean enabled = false;

    private final static HashSet<Player> currentDisguised = new HashSet<>();


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
        if (isPlayerDisguised(player))
            return;
        if (enabled)
            return;
        enabled = true;
        currentDisguised.add(player);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        var others = getViewers();

        others.forEach(this::disguisePlayerToViewer);

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> updatePlayerDisguisePosition(others), 2L, 1L);
    }

    public void disableDisguise()
    {
        if (!enabled)
            return;
        enabled = false;
        currentDisguised.remove(player);
        Bukkit.getScheduler().cancelTask(taskId);
        getViewers().forEach(this::revealPlayerToViewer);
        HandlerList.unregisterAll(this);
    }

    private void revealPlayerToViewer(Player v)
    {
        Packet<?> removePacket = new ClientboundRemoveEntitiesPacket(id);
        v.showPlayer(plugin, player);
        NmsHelper.sendPacketToBukkitPlayer(v, removePacket);
    }

    private void disguisePlayerToViewer(Player v)
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
        Packet<?> headRotationPacket = buildHeadRotatePacket();
        others.forEach(v -> {
            NmsHelper.sendPacketToBukkitPlayer(v, movePacket);
            NmsHelper.sendPacketToBukkitPlayer(v, headRotationPacket);
        });
    }

    @EventHandler
    public void syncPunch(PlayerInteractEvent event)
    {
        if (!enabled) return;
        if (event.getPlayer() != player || (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)) return;
        updatePlayerDisguiseAnimation(getViewers(), 0);
    }

    @EventHandler
    public void syncHeldItem(PlayerItemHeldEvent event)
    {
        if (!enabled) return;
        if (event.getPlayer() != player) return;
        ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());
        if (heldItem == null)
        {
            updatePlayerDisguiseHeldItem(getViewers(), ItemStack.of(Material.AIR));
            return;
        }
        updatePlayerDisguiseHeldItem(getViewers(), heldItem);
    }

    private void updatePlayerDisguiseAnimation(List<? extends Player> others, int animationNum)
    {
        Packet<?> punchPacket = buildAnimatePacket(animationNum);
        others.forEach(v -> NmsHelper.sendPacketToBukkitPlayer(v, punchPacket));
    }

    private void updatePlayerDisguiseHeldItem(List<? extends Player> others, ItemStack itemStack)
    {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack); //Same name, very nice :/
        Packet<?> equipmentPacket = new ClientboundSetEquipmentPacket(id, List.of(new Pair<>(EquipmentSlot.MAINHAND, nmsItemStack)));
        others.forEach(v -> NmsHelper.sendPacketToBukkitPlayer(v, equipmentPacket));
    }

    private ClientboundAnimatePacket buildAnimatePacket(int animationNum)
    {
        StreamCodec<FriendlyByteBuf, ClientboundAnimatePacket> codec = ClientboundAnimatePacket.STREAM_CODEC;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(this.id);
        buf.writeVarInt(animationNum);
        return codec.decode(buf);
    }

    private ClientboundRotateHeadPacket buildHeadRotatePacket()
    {
        StreamCodec<FriendlyByteBuf, ClientboundRotateHeadPacket> codec = ClientboundRotateHeadPacket.STREAM_CODEC;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(this.id);
        buf.writeByte(Mth.packDegrees(player.getYaw()));
        return codec.decode(buf);
    }

    private List<? extends Player> getViewers()
    {
        return plugin.getServer().getOnlinePlayers().stream()
                .filter(v -> v != player)
                .toList();
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
    public static boolean isPlayerDisguised(Player player)
    {
        return currentDisguised.contains(player);
    }
}
