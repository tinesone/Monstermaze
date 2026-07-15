package tinesone.monstermaze.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import tinesone.monstermaze.util.ConfigHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LobbyDoors
{
    private final Location[] doorLocations;

    private static final Map<Player, Integer> closedDoorTaskIds = new HashMap<>();
    private static final Map<Player, Integer> openDoorTaskIds = new HashMap<>();

    public LobbyDoors()
    {
        World lobby = Bukkit.getWorlds().getFirst();
        this.doorLocations = ConfigHelper.getLocations(lobby, "lobby-start-doors");
    }

    public void setDoorsOpenState(boolean open)
    {
        Arrays.stream(doorLocations).forEach(location -> {
            location.getBlock().setBlockData(setDoorOpenState((Openable) location.getBlock().getBlockData(), open));
        });
    }

    private BlockData setDoorOpenState(Openable door, boolean open)
    {
        door.setOpen(open);
        return door;
    }
}
