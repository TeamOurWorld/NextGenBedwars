package org.ourworld.nextGenBedwars.command.devtest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.ourworld.nextGenBedwars.gameplay.core.spawner.ItemSpawner;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

import static org.ourworld.nextGenBedwars.NextGenBedwars.itemSpawnerManager;

public class SpawnerTestCommand {

    @Command("bw spawner")
    public void spawner(BukkitCommandActor actor) {
        if (!actor.isPlayer())
            return;

        actor.reply("A test spawner has been spawned.");
        itemSpawnerManager.addSpawner(new ItemSpawner(actor.asPlayer().getLocation(), Material.COPPER_INGOT, 100, Material.COPPER_BLOCK, "&c%item_name%\n&r%time%" + ChatColor.GREEN + " 秒后刷新\n&r1" + ChatColor.GOLD + " 级"));
    }
}
