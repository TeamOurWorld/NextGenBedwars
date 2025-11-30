package org.ourworld.nextGenBedwars.event.spawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BedwarsItemSpawnEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled = false;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    private Location spawnLocation;
    private Material itemType;
    private int spawnTime;

    private ItemDisplay itemDisplay;
    private TextDisplay textDisplay;

    public BedwarsItemSpawnEvent(Location spawnLocation, Material itemType, int spawnTime, ItemDisplay itemDisplay, TextDisplay textDisplay) {
        this.spawnLocation = spawnLocation;
        this.itemType = itemType;
        this.spawnTime = spawnTime;
    }

    public void setSpawnLocation(Location spawnLocation) {
        spawnLocation.setYaw(0);
        spawnLocation.setPitch(0);
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setItemType(Material itemType) {
        this.itemType = itemType;
    }

    public Material getItemType() {
        return itemType;
    }

    public void setSpawnTime(int spawnTime) {
        if (spawnTime <= 0) {
            throw new IllegalArgumentException("[Spawner] Spawn time must be > 0. You passed: " + spawnTime);
        }
        this.spawnTime = spawnTime;
    }

    public ItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    public TextDisplay getTextDisplay() {
        return textDisplay;
    }

    public int getSpawnTime() {
        return spawnTime;
    }

}
