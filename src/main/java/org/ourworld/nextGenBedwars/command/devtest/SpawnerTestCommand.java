package org.ourworld.nextGenBedwars.command.devtest;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.ourworld.nextGenBedwars.command.BaseCommand;
import org.ourworld.nextGenBedwars.gameplay.core.spawner.ItemSpawner;

import static org.ourworld.nextGenBedwars.NextGenBedwars.itemSpawnerManager;

public class SpawnerTestCommand extends BaseCommand {

    public SpawnerTestCommand(String commandName) {
        super(commandName);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        itemSpawnerManager.addSpawner(new ItemSpawner(context.getSource().getLocation(), Material.COPPER_INGOT, 100, Material.COPPER_BLOCK, "&c%item_name%\n&r%time%" + ChatColor.GREEN + " 秒后刷新\n&r1" + ChatColor.GOLD + " 级"));
        return 0;
    }

    @Override
    protected void preHandleCommand(LiteralArgumentBuilder<CommandSourceStack> target) {
    }
}
