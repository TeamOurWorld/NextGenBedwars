package org.ourworld.nextGenBedwars.exception;

public class CommandIsAlreadyExistsException extends RuntimeException {
    public CommandIsAlreadyExistsException(String... commandPath) {
        super(commandPathToMessage(commandPath));
    }

    public static String commandPathToMessage(String... commandPath){

        StringBuilder message = new StringBuilder();

        for (int i = 0; i < commandPath.length; i++) {
            if(i != 0){
                message.append("->");
            }
            message.append(commandPath[i]);
        }
        
        return message+" 该指令路径已存在";
    }
}
