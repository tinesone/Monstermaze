package tinesone.monstermaze.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public final class ConfigLocationReader
{
    Plugin plugin;

    public ConfigLocationReader(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public Location getLocation(World world, String locationName)
    {
        Location location;
        ConfigurationSection spawnLocationConfig = plugin.getConfig().getConfigurationSection(locationName);
        try
        {
            double x = spawnLocationConfig.getDouble("x");
            double y = spawnLocationConfig.getDouble("y");
            double z = spawnLocationConfig.getDouble("z");
            float yaw = (float) spawnLocationConfig.getDouble("yaw");
            float pitch = (float) spawnLocationConfig.getDouble("pitch");

            location = new Location(world, x, y, z, yaw, pitch);
        } catch (NullPointerException e)
        {
            plugin.getComponentLogger().warn("can't load the location {}", locationName);
            return null;
        }
        return location;
    }
}
