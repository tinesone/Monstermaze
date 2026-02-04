package tinesone.monstermaze.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;


public class DelayedTask
{
    private static Plugin plugin;

    private int id = -1;

    public DelayedTask(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public DelayedTask(Runnable runnable, long delayInTicks)
    {
        if(plugin.isEnabled())
        {
            id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delayInTicks);
        }
        else
        {
            runnable.run();
        }
    }

    public DelayedTask(Runnable runnable)
    {
        this(runnable, 0);
    }

    public int getId()
    {
        return id;
    }
}
