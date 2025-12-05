package org.ourworld.nextGenBedwars.gameplay.core.spawner;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.ourworld.nextGenBedwars.event.spawner.BedwarsItemSpawnEvent;
import org.ourworld.nextGenBedwars.util.TickerTask;

import java.util.UUID;

import static org.ourworld.nextGenBedwars.NextGenBedwars.plugin;

public class ItemSpawner implements TickerTask {

    private Location location;
    private Material itemType;
    private int spawnTick;
    private String holoTextContent;

    private UUID holoItemId = null;
    private UUID holoTextId = null;

    private int leftTick;

    public ItemSpawner(Location location, Material itemType, int spawnTick, Material holoItemType, String holoTextContent) {
        Location locCopy = location.clone();
        locCopy.setYaw(0);
        locCopy.setPitch(0);
        this.location = locCopy;
        this.itemType = itemType;
        this.spawnTick = spawnTick;
        if(holoItemType != null)
            location.getWorld().spawn(this.location, ItemDisplay.class, itemDisplay -> {
                this.holoItemId = itemDisplay.getUniqueId();

                itemDisplay.setItemStack(new ItemStack(holoItemType));
                itemDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
                itemDisplay.setTransformation(new Transformation(
                        new Vector3f(0, 2.5f, 0),         // 平移
                        new AxisAngle4f(0, 0, 0, 0),   // 左旋转
                        new Vector3f(0.75f, 0.75f, 0.75f),// 缩放
                        new AxisAngle4f(0, 0, 0, 0)    // 右旋转
                ));
                itemDisplay.setShadowRadius(0);
                itemDisplay.setShadowStrength(0);
                itemDisplay.setPersistent(false);
            });

        if (holoTextContent != null)
            location.getWorld().spawn(this.location, TextDisplay.class, textDisplay -> {
                this.holoTextId = textDisplay.getUniqueId();
                this.holoTextContent = holoTextContent;

                textDisplay.text(replaceHolder(holoTextContent));
                textDisplay.setBillboard(Display.Billboard.VERTICAL);
                textDisplay.setShadowed(true);
                textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                textDisplay.setTransformation(new Transformation(
                        new Vector3f(0, 1.625f, 0),         // 平移
                        new AxisAngle4f(0, 0, 0, 0),   // 左旋转
                        new Vector3f(0.75f, 0.75f, 0.75f),// 缩放
                        new AxisAngle4f(0, 0, 0, 0)    // 右旋转
                ));
                textDisplay.setPersistent(false);
            });

        leftTick = spawnTick;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setItemType(Material itemType) {
        this.itemType = itemType;
    }

    public void setSpawnTick(int spawnTick) {
        this.spawnTick = spawnTick;
    }

    public Material getItemType() {
        return itemType;
    }

    public Location getLocation() {
        return location;
    }

    public int getSpawnTick() {
        return spawnTick;
    }

    @Override
    public void run() {
        ItemDisplay itemDisplay = null;
        TextDisplay textDisplay = null;

        if (holoItemId != null)
            itemDisplay = (ItemDisplay) Bukkit.getEntity(holoItemId);
        if (holoTextId != null)
            textDisplay = (TextDisplay) Bukkit.getEntity(holoTextId);

        if (leftTick <= 0) {
            BedwarsItemSpawnEvent event = new BedwarsItemSpawnEvent(location, itemType, spawnTick, holoItemId, holoTextId, holoTextContent);
            plugin.getServer().getPluginManager().callEvent(event);

            this.location = event.getSpawnLocation();
            this.itemType = event.getItemType();
            this.spawnTick = event.getSpawnTime();
            this.holoTextContent = event.getHoloTextContent();

            if (this.holoItemId != event.getItemDisplayId()) { // todo: 重新生成 display 实体
                // 只要旧的不为空，就先删掉 (涵盖了移除和替换)
                if (this.holoItemId != null && location.isChunkLoaded() && itemDisplay != null)
                    itemDisplay.remove();
                // 更新引用 (涵盖了从无到有、替换、移除变成null)
                this.holoItemId = event.getItemDisplayId();
                itemDisplay = (ItemDisplay) Bukkit.getEntity(holoItemId);
            }

            if (this.holoTextId != event.getTextDisplayId()) {
                if (this.holoTextId != null && location.isChunkLoaded() && textDisplay != null)
                    textDisplay.remove();

                this.holoTextId = event.getTextDisplayId();
                textDisplay = (TextDisplay) Bukkit.getEntity(holoTextId);
            }

            this.leftTick = this.spawnTick;
        } else {
            this.leftTick--;
        }

        if (holoItemId != null && itemDisplay != null && location.isChunkLoaded())
            itemDisplay.setRotation(itemDisplay.getYaw() - 5.0f, 0); // todo: 插值动画 + 上下浮动

        if (holoTextId != null && textDisplay != null && location.isChunkLoaded())
            if (holoTextContent != null)
                textDisplay.text(replaceHolder(holoTextContent)); // todo: 优化刷新逻辑 使其不要每次都更新 而是仅当文字变化时才更新
    }

    @Override
    public void shutdown() {
        if (!location.isChunkLoaded())
            return;

        if (holoItemId != null && Bukkit.getEntity(holoItemId) != null) {
            Bukkit.getEntity(holoItemId).remove();
            holoItemId = null;
        }
        if (holoTextId != null && Bukkit.getEntity(holoTextId) != null) {
            Bukkit.getEntity(holoTextId).remove();
            holoTextId = null;
        }

        location = null;
        itemType = null;
        spawnTick = -1;
        leftTick = -1;
    }

    private Component replaceHolder(String rawText) {
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(this.holoTextContent);
        return component.replaceText(TextReplacementConfig.builder()
                        .matchLiteral("%item_name%")
                        .replacement(Component.translatable(itemType.translationKey())) // 填入翻译组件
                        .build())
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("%time%")
                        .replacement(String.valueOf((this.leftTick - 1) / 20 + 1)) // 填入纯文本
                        .build()
                );
        // todo: 后续需要在这里替换等级和资源的 i18n
    }
}
