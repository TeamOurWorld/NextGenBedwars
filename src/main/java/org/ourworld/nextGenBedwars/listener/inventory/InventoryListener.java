package org.ourworld.nextGenBedwars.listener.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.ourworld.nextGenBedwars.inventory.InvHolder;

public class InventoryListener implements Listener {
    public static Listener INSTANCE = new InventoryListener();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof InvHolder) ((InvHolder) holder).onClick(event);
    }
}
