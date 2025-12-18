# Command

## BaseCommand类
此类实现了`Command<CommandSourceStack>`接口
<br>
开发者只需要重写`int run(CommandContext<CommandSourceStack> context)`和`preHandleCommand(LiteralArgumentBuilder<CommandSourceStack> target)`

### 示例代码
SpwanerTestCommand.java
```java
package org.ourworld.nextGenBedwars.command.devtest;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.ourworld.nextGenBedwars.command.BaseCommand;
import org.ourworld.nextGenBedwars.inventory.InvHolderUseExample;

public class SpawnerTestCommand extends BaseCommand {

    public SpawnerTestCommand(String commandName) {
        //commandName是指令名称 例如指令bw spawn,那么spawn就是指令名称,bw是指令路径
        super(commandName);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        //指令运行的方法
        InvHolderUseExample.customInv();
        return 0;
    }

    @Override
    protected void preHandleCommand(LiteralArgumentBuilder<CommandSourceStack> target) {
        //这里提供了预处方法
        //例如给指令添加参数
        target.then(Commands.argument("allow", BoolArgumentType.bool())
                .executes(ctx -> {
                    boolean allowed = ctx.getArgument("allow", boolean.class);
            })
        );
    }
}
```


## 注册
 我将注册交给Manager去统一注册，直接在`org.ourworld.nextGenBedwars.CommandInitialize`里面添加就行了
`CommandManager.getInstance().addCommand(BaseCommand command, String... commandPath)`

> addCommand(BaseCommand command, String... commandPath)
> addCommand(BaseCommand command)

| 参数名称        | 参数类型        | 注释                                 |
|-------------|-------------|------------------------------------|
| command     | BaseCommand | 指令实现类,例如`new CustomCommand("spawn")` |
| commandpath | String...   | 指令的路径,例如你想要指令为`bw spawn`只需要传`bw`即可     |



参数`command`需要传`BaseCommand`子类的实例


### 示例代码
```java
package org.ourworld.nextGenBedwars.command;

import org.ourworld.nextGenBedwars.command.devtest.SpawnerTestCommand;
import org.ourworld.nextGenBedwars.manager.CommandManager;

public class CommandInitialize {

    public static void initialize(){
        //注册bw spawner指令
        CommandManager.getInstance().addCommand(new SpawnerTestCommand("spawner"),"bw");
    }
}

```
