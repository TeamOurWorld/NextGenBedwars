package org.ourworld.nextGenBedwars.event.spawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BedwarsItemSpawnEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private Location spawnLocation;
    private Material itemType;
    private int spawnTime;

    public BedwarsItemSpawnEvent(Location spawnLocation, Material itemType, int spawnTime) {
        this.spawnLocation = spawnLocation;
        this.itemType = itemType;
        this.spawnTime = spawnTime;
    }

    public void setSpawnLocation(Location spawnLocation) {
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
        this.spawnTime = spawnTime;
    }

    public int getSpawnTime() {
        return spawnTime;
    }

}
