package org.ourworld.nextGenBedwars.command;

import org.ourworld.nextGenBedwars.command.devtest.SpawnerTestCommand;
import org.ourworld.nextGenBedwars.manager.CommandManager;

public class CommandInitialize {

    public static void initialize(){
        CommandManager.getInstance().addCommand(new SpawnerTestCommand("spawner"),"bw");
    }
}
