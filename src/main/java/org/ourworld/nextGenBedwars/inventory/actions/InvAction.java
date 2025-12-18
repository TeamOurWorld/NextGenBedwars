package org.ourworld.nextGenBedwars.inventory.actions;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ourworld.nextGenBedwars.inventory.InvButton;
import org.ourworld.nextGenBedwars.inventory.transformers.InvTransformer;

import java.util.Iterator;

public interface InvAction {
    default void run(InventoryClickEvent event, InvButton button, @NotNull InvTransformer transformer, @Nullable Iterator<InvAction> nextRunList) {
        run(event, button, transformer);
        if (nextRunList != null && nextRunList.hasNext())
            nextRunList.next().run(event, button, transformer, nextRunList);
    }

    void run(InventoryClickEvent event, InvButton button, @NotNull InvTransformer transformer);
}
