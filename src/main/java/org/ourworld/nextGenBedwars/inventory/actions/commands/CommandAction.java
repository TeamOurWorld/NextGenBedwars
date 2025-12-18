package org.ourworld.nextGenBedwars.inventory.actions.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.ourworld.nextGenBedwars.inventory.InvButton;
import org.ourworld.nextGenBedwars.inventory.actions.HeadMatcher;
import org.ourworld.nextGenBedwars.inventory.actions.InvAction;
import org.ourworld.nextGenBedwars.inventory.actions.InvActionFactory;
import org.ourworld.nextGenBedwars.inventory.transformers.InvTransformer;

public class CommandAction implements InvAction {
    private final String command;

    public CommandAction(String command){
        this.command = command;
    }

    @Override
    public void run(InventoryClickEvent event, InvButton button, @NotNull InvTransformer transformer) {
        exec(event, event.getWhoClicked(), button, transformer);
    }

    public void exec(InventoryClickEvent event, CommandSender sender, InvButton button, @NotNull InvTransformer transformer) {
        Bukkit.dispatchCommand(sender, ((TextComponent) transformer.action(event, button, this, Component.text(command))).content());
    }

    public static class Factory implements InvActionFactory {
        public static Factory INSTANCE = new Factory();
        public static HeadMatcher MATCHER = new HeadMatcher("command");

        @Override
        public InvAction create(Object obj) {
            if (!(obj instanceof String) || !MATCHER.test(((String) obj)))
                throw new IllegalArgumentException("Invalid object type for CommandActionFactory: " + obj.getClass().getName());
            return new CommandAction(MATCHER.cropped(((String) obj)));
        }
    }
}
