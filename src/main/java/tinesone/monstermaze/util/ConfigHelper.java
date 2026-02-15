package tinesone.monstermaze.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;

public final class ConfigHelper
{
    Plugin plugin;

    public ConfigHelper(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public Location getLocation(World world, String locationNameInConfig)
    {
        Location location;
        ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection(locationNameInConfig);
        try
        {
            double x = configurationSection.getDouble("x");
            double y = configurationSection.getDouble("y");
            double z = configurationSection.getDouble("z");
            float yaw = (float) configurationSection.getDouble("yaw");
            float pitch = (float) configurationSection.getDouble("pitch");

            location = new Location(world, x, y, z, yaw, pitch);
        } catch (NullPointerException e)
        {
            plugin.getComponentLogger().warn("can't load the location: {}", locationNameInConfig);
            return null;
        }
        return location;
    }

    public Location[] getLocations(World world, String locationNameInConfig)
    {
        HashSet<Location> locations = new HashSet<>();
        ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection(locationNameInConfig);
        for (String configLocation : configurationSection.getKeys(false))
        {
            try
            {
                double x = configurationSection.getDouble(configLocation + ".x");
                double y = configurationSection.getDouble(configLocation + ".y");
                double z = configurationSection.getDouble(configLocation + ".z");
                locations.add(new Location(world, x, y, z));
            } catch (NullPointerException e)
            {
                plugin.getComponentLogger().warn("can't load the sub-location: {} from: {}", configLocation, locationNameInConfig);
                return null;
            }
        }
        return locations.toArray(new Location[0]);
    }
}
