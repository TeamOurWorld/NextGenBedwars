package org.ourworld.nextGenBedwars.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public abstract class BaseCommand implements Command<CommandSourceStack> {
    protected LiteralArgumentBuilder<CommandSourceStack> target;

    private final String commandName;
    public abstract int run(CommandContext<CommandSourceStack> context);

    public BaseCommand(String commandName){
        this.commandName = commandName;
        this.target = Commands.literal(commandName);
        preHandleCommand(target);
        target.executes(this);
    }

    public String getCommandName() {
        return commandName;
    }

    public LiteralArgumentBuilder<CommandSourceStack> getTarget() {
        return target;
    }

    protected abstract void preHandleCommand(LiteralArgumentBuilder<CommandSourceStack> target);





}
