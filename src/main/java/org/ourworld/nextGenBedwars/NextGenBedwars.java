package org.ourworld.nextGenBedwars;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class NextGenBedwars extends JavaPlugin {

    public static JavaPlugin plugin;

    public NextGenBedwars() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerListener();

        getComponentLogger().info(Component.text("[Main] Loaded!").color(NamedTextColor.GREEN));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getComponentLogger().info(Component.text("[Main] Goodbye!").color(NamedTextColor.GREEN));
    }

    private void registerListener() {
        //getServer().getPluginManager().registerEvents(, this);
    }

}
