package org.ourworld.nextGenBedwars;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.ourworld.nextGenBedwars.command.CommandInitialize;
import org.ourworld.nextGenBedwars.gameplay.core.spawner.ItemSpawnerManager;
import org.ourworld.nextGenBedwars.i18n.I18n;
import org.ourworld.nextGenBedwars.listener.inventory.InventoryListener;
import org.ourworld.nextGenBedwars.listener.spawner.SpawnerSpawnListener;
import org.ourworld.nextGenBedwars.manager.CommandManager;
import org.ourworld.nextGenBedwars.util.GlobalTicker;

public final class NextGenBedwars extends JavaPlugin {

    public static JavaPlugin plugin;
    public static ItemSpawnerManager itemSpawnerManager; // 仅供测试，后续需要移除

    public NextGenBedwars() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        reloadConfig();

        GlobalTicker.initialize();
        CommandInitialize.initialize();

        registerManager();
        registerListener();
        getComponentLogger().info(Component.text("[Main] Loaded!").color(NamedTextColor.GREEN));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getComponentLogger().info(Component.text("[Main] Goodbye!").color(NamedTextColor.GREEN));
        itemSpawnerManager.shutdown();
        GlobalTicker.shutdown();
    }

    private void registerListener() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new SpawnerSpawnListener(), this);
        manager.registerEvents(InventoryListener.INSTANCE, this);
    }

    private void registerManager(){
        CommandManager.getInstance().register(this.getLifecycleManager());
    }

    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        super.reloadConfig();

        I18n.init();
    }
}
