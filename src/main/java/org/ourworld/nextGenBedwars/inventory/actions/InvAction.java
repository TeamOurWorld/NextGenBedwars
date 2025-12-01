package org.ourworld.nextGenBedwars.inventory.actions;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;
import org.ourworld.nextGenBedwars.inventory.InvButton;

import java.util.Iterator;

public interface InvAction {
    default void run(InventoryClickEvent event, InvButton button, @Nullable Iterator<InvAction> nextRunList) {
        run(event,button);
        if (nextRunList != null && nextRunList.hasNext()) nextRunList.next().run(event, button, nextRunList);
    }

    void run(InventoryClickEvent event, InvButton button);
}
