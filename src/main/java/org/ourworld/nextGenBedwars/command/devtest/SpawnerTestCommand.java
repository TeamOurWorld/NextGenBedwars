package org.ourworld.nextGenBedwars.command.devtest;

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
        itemSpawnerManager.addSpawner(new ItemSpawner(actor.asPlayer().getLocation(), Material.COPPER_INGOT, 20));
    }
}
