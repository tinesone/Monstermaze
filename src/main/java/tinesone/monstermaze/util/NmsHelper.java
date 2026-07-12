package tinesone.monstermaze.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class NmsHelper
{
    public static void sendPacketToBukkitPlayer(Player receiver, Packet<?> packet)
    {
        ServerPlayer nmsPlayerReceiver = ((CraftPlayer) receiver).getHandle();
        nmsPlayerReceiver.connection.send(packet);
    }
}
