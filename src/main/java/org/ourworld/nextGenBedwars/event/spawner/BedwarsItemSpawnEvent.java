package org.ourworld.nextGenBedwars.event.spawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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

    private UUID itemDisplayId;
    private UUID textDisplayId;

    private String holoTextContent;

    public BedwarsItemSpawnEvent(Location spawnLocation, Material itemType, int spawnTime, UUID itemDisplay, UUID textDisplay, String holoTextContent) {
        this.spawnLocation = spawnLocation;
        this.itemType = itemType;
        this.spawnTime = spawnTime;
        this.itemDisplayId = itemDisplay;
        this.textDisplayId = textDisplay;
        this.holoTextContent = holoTextContent;
    }

    public void setSpawnLocation(Location spawnLocation) {
        Location locCopy = spawnLocation.clone();
        locCopy.setYaw(0);
        locCopy.setPitch(0);
        this.spawnLocation = locCopy;
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

    public void setItemDisplay(ItemDisplay itemDisplay) {
        this.itemDisplayId = (itemDisplay != null) ? itemDisplay.getUniqueId() : null;
    }

    public void setTextDisplay(TextDisplay textDisplay) {
        this.textDisplayId = (textDisplay != null) ? textDisplay.getUniqueId() : null;
    }

    public UUID getItemDisplayId() {
        return itemDisplayId;
    }

    public UUID getTextDisplayId() {
        return textDisplayId;
    }

    public void setHoloTextContent(String holoTextContent) {
        this.holoTextContent = holoTextContent;
    }

    public String getHoloTextContent() {
        return holoTextContent;
    }

    public int getSpawnTime() {
        return spawnTime;
    }

}
