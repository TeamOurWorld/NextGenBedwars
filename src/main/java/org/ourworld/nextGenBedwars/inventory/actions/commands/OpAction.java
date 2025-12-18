package org.ourworld.nextGenBedwars.inventory.actions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.ourworld.nextGenBedwars.inventory.InvButton;
import org.ourworld.nextGenBedwars.inventory.actions.HeadMatcher;
import org.ourworld.nextGenBedwars.inventory.actions.InvAction;
import org.ourworld.nextGenBedwars.inventory.actions.InvActionFactory;
import org.ourworld.nextGenBedwars.inventory.transformers.InvTransformer;

public class OpAction extends CommandAction{
    public OpAction(String command) {
        super(command);
    }

    @Override
    public void exec(InventoryClickEvent event, CommandSender sender, InvButton button, @NotNull InvTransformer transformer) {
        if (sender.isOp()) {
            super.exec(event, sender, button, transformer);
            return;
        }
        try {
            sender.setOp(true);
            super.exec(event, sender, button, transformer);
            sender.setOp(false);
        } catch (Exception e) {
            sender.setOp(false);
            throw new RuntimeException();
        } finally {
            sender.setOp(false);
        }
    }

    public static class Factory implements InvActionFactory {
        public static Factory INSTANCE = new Factory();
        public static HeadMatcher MATCHER = new HeadMatcher("op");

        @Override
        public InvAction create(Object obj) {
            if (!(obj instanceof String) || !MATCHER.test(((String) obj)))
                throw new IllegalArgumentException("Invalid object type for OpActionFactory: " + obj.getClass().getName());
            return new OpAction(MATCHER.cropped(((String) obj)));
        }
    }
}
