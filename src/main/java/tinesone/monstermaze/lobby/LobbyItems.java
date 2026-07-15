package tinesone.monstermaze.lobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class LobbyItems
{
    public static ItemStack readyItem(Boolean isReady)
    {
        Material material;
        TextComponent itemName;
        if (isReady)
        {
            material = Material.EMERALD;
            itemName = Component.text()
                    .content("Ready!")
                    .decoration(TextDecoration.ITALIC, false)
                    .decoration(TextDecoration.BOLD, true)
                    .color(NamedTextColor.GREEN)
                    .build();
        }
        else
        {
            material = Material.BARRIER;
            itemName = Component.text()
                    .content("Ready?")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.DARK_RED)
                    .build();
        }

        ItemStack readyItem = new ItemStack(material);
        ItemMeta meta = readyItem.getItemMeta();
        meta.displayName(itemName);
        readyItem.setItemMeta(meta);

        return readyItem;
    }
}
