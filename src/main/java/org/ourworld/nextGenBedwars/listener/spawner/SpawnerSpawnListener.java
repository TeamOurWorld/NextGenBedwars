package org.ourworld.nextGenBedwars.listener.spawner;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.ourworld.nextGenBedwars.event.spawner.BedwarsItemSpawnEvent;

import static org.ourworld.nextGenBedwars.NextGenBedwars.plugin;

public class SpawnerSpawnListener implements Listener {

    NamespacedKey key = new NamespacedKey(plugin, "no_merge");

    @EventHandler
    public void onItemSpawn(BedwarsItemSpawnEvent event) {
        Item item = event.getSpawnLocation().getWorld().dropItem(
                event.getSpawnLocation(),
                new ItemStack(event.getItemType())
        );
        item.setUnlimitedLifetime(true);
        item.setVelocity(new Vector(0, 0, 0));
        // 添加自定义的“禁止合并”标签
        item.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }


    @EventHandler
    public void onItemMerge(ItemMergeEvent event) {
        // 检查"发起合并"的物品是否有标签
        if (event.getEntity().getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            event.setCancelled(true);
            return;
        }

        // 检查"被合并"的目标物品是否有标签
        if (event.getTarget().getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            event.setCancelled(true);
        }
    }

}
