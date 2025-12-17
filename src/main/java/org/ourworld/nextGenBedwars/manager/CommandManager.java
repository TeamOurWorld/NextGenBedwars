package org.ourworld.nextGenBedwars.manager;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.ourworld.nextGenBedwars.command.BaseCommand;
import org.ourworld.nextGenBedwars.exception.CommandIsAlreadyExistsException;
import org.ourworld.nextGenBedwars.framework.BaseManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements BaseManager {
    protected static CommandManager instance;

    protected List<LiteralCommandNode<CommandSourceStack>> commandTreeList;

    public static CommandManager getInstance(){
        if(instance == null){
            instance = new CommandManager();
        }

        return instance;
    }

    public CommandManager(){
        commandTreeList = new ArrayList<>();
    }

    private int containsRoot(String name){
        for (int i = 0; i < commandTreeList.size(); i++) {
            if(commandTreeList.get(i).getRedirect().getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    public boolean addCommand(BaseCommand command){
        String commandName = command.getCommandName();
        String[] args = {commandName};
        return addCommand(command.getTarget(),args);
    }
    public boolean addCommand(BaseCommand command, String... commandPath){
        String commandName = command.getCommandName();
        List<String> list = new ArrayList<>(Arrays.asList(commandPath));
        list.add(commandName);
        String[] args = new String[list.size()];
        return addCommand(command.getTarget(),list.toArray(args));
    }

    private boolean addCommand(LiteralArgumentBuilder<CommandSourceStack> commandBuilder, String... commandPath){
        String commandName = commandPath[commandPath.length - 1];

        //先查找头节点是否存在
        int rootIndex = containsRoot(commandPath[0]);
        LiteralCommandNode<CommandSourceStack> root;

        if(rootIndex != -1){
            root = commandTreeList.get(rootIndex);
            if(commandPath.length == 1){
                throw new CommandIsAlreadyExistsException(commandPath);
            }
        }else {
            root = Commands.literal(commandPath[0]).build();
            if(commandPath.length == 1){
                root = commandBuilder.build();
            }
            commandTreeList.add(root);
        }

        if(commandPath.length == 1){
            return true;
        }
        //再找到父节点
        LiteralCommandNode<CommandSourceStack> currentNode = root;
        for (int i = 1; i < commandPath.length - 1; i++) {
            LiteralCommandNode<CommandSourceStack> temp = (LiteralCommandNode<CommandSourceStack>) currentNode.getChild(commandPath[i]);
            if(temp == null){
                temp = Commands.literal(commandPath[i]).build();
                currentNode.addChild(temp);
            }
            currentNode = temp;
        }

        if (currentNode.getChild(commandName) == null){
            currentNode.addChild(commandBuilder.build());
        }else{
            throw new CommandIsAlreadyExistsException(commandPath);
        }

        //添加新节点
        return true;
    }


    @Override
    public void register(LifecycleEventManager<Plugin> PluginManager) {
        PluginManager.registerEventHandler(LifecycleEvents.COMMANDS, commands->{
            for (LiteralCommandNode<CommandSourceStack> node : commandTreeList) {
                commands.registrar().register(node);
            }
        });
    }
}
