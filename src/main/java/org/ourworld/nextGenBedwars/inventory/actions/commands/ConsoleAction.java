package org.ourworld.nextGenBedwars.inventory.actions.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.ourworld.nextGenBedwars.inventory.InvButton;
import org.ourworld.nextGenBedwars.inventory.actions.HeadMatcher;
import org.ourworld.nextGenBedwars.inventory.actions.InvAction;
import org.ourworld.nextGenBedwars.inventory.actions.InvActionFactory;
import org.ourworld.nextGenBedwars.inventory.transformers.InvTransformer;

public class ConsoleAction extends CommandAction{
    public ConsoleAction(String command) {
        super(command);
    }

    @Override
    public void exec(InventoryClickEvent event, CommandSender sender, InvButton button, @NotNull InvTransformer transformer) {
        super.exec(event, Bukkit.getConsoleSender(), button, transformer);
    }

    public static class Factory implements InvActionFactory {
        public static Factory INSTANCE = new Factory();
        public static HeadMatcher MATCHER = new HeadMatcher("console");

        @Override
        public InvAction create(Object obj) {
            if (!(obj instanceof String) || !MATCHER.test(((String) obj)))
                throw new IllegalArgumentException("Invalid object type for ConsoleActionFactory: " + obj.getClass().getName());
            return new ConsoleAction(MATCHER.cropped(((String) obj)));
        }
    }
}
