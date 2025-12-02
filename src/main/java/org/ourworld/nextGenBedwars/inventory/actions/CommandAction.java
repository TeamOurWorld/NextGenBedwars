package org.ourworld.nextGenBedwars.inventory.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.ourworld.nextGenBedwars.NextGenBedwars;
import org.ourworld.nextGenBedwars.inventory.InvButton;

public record CommandAction(String command) implements InvAction {
    @Override
    public void run(InventoryClickEvent event, InvButton button) {
        HumanEntity whoClicked = event.getWhoClicked();
        if (!(whoClicked instanceof Player player)) {
            NextGenBedwars.plugin.getLogger().warning("A non-player entity tried to run a command!");
            return;
        }
        Bukkit.dispatchCommand(player, this.command());
    }

    public static class Factory implements InvActionFactory {
        public static Factory INSTANCE = new Factory();
        public static HeadMatcher MATCHER = new HeadMatcher("command");

        @Override
        public InvAction create(Object obj) {
            if (!(obj instanceof String str) || !MATCHER.test(str))
                throw new IllegalArgumentException("Invalid object type for CommandActionFactory: " + obj.getClass().getName());
            return new CommandAction(MATCHER.cropped(str));
        }
    }
}
