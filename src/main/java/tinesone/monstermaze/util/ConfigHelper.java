package tinesone.monstermaze.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public final class ConfigHelper
{

    public static Location getLocation(World world, String locationNameInConfig)
    {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Monstermaze");
        Location location;
        ConfigurationSection configurationSection;
        try
        {
            assert plugin != null;
            configurationSection = plugin.getConfig().getConfigurationSection(locationNameInConfig);
            assert configurationSection != null;
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

    public static Location[] getLocations(World world, String locationNameInConfig)
    {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Monstermaze");
        HashSet<Location> locations = new HashSet<>();
        ConfigurationSection configurationSection;
        try
        {
            assert plugin != null;
            configurationSection = plugin.getConfig().getConfigurationSection(locationNameInConfig);
            assert configurationSection != null;
        } catch (NullPointerException e)
        {
            plugin.getComponentLogger().warn("Something went wrong with reading the config");
            return null;
        }
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

    public static long getLong(String longNameInConfig)
    {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Monstermaze");
        assert plugin != null;
        return plugin.getConfig().getLong(longNameInConfig);
    }

    public static Map<String, Double> getScaleValues(String scalesInConfig)
    {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Monstermaze");
        assert plugin != null;
        ConfigurationSection configurationSection =  plugin.getConfig().getConfigurationSection(scalesInConfig);
        assert configurationSection != null;
        Map<String, Double> scales = new HashMap<>();
        configurationSection.getKeys(false).forEach(key -> {scales.put(key, configurationSection.getDouble(key));});
        return scales;
    }
}
