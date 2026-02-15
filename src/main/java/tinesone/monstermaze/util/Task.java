package tinesone.monstermaze.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Task
{
    private static Plugin plugin;
    private int id = 1;

    public Task(Plugin plugin)
    {
        Task.plugin = plugin;
    }

    public Task(Runnable task, long delayInTicks)
    {
        if(!(plugin.isEnabled())) return;
        id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delayInTicks);
    }


    public Task(Runnable task, long delayInTicks, long periodInTicks)
    {
        if (!(plugin.isEnabled())) return;
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayInTicks, periodInTicks);
    }

    public void cancel()
    {
        Bukkit.getScheduler().cancelTask(id);
    }

    public int getId()
    {
        return this.id;
    }
}
