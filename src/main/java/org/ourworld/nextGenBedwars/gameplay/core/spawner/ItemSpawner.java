package org.ourworld.nextGenBedwars.gameplay.core.spawner;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.ourworld.nextGenBedwars.event.spawner.BedwarsItemSpawnEvent;
import org.ourworld.nextGenBedwars.util.TickerTask;

import static org.ourworld.nextGenBedwars.NextGenBedwars.plugin;

public class ItemSpawner implements TickerTask {

    private Location location;
    private Material itemType;
    private int spawnTick;
    private String holoTextContent;

    private ItemDisplay holoItem = null;
    private TextDisplay holoText = null;

    private int leftTick;

    public ItemSpawner(Location location, Material itemType, int spawnTick, Material holoItemType, String holoTextContent) {
        location.setYaw(0);
        location.setPitch(0);
        this.location = location;
        this.itemType = itemType;
        this.spawnTick = spawnTick;
        if(holoItemType != null)
            location.getWorld().spawn(location, ItemDisplay.class, itemDisplay -> {
                this.holoItem = itemDisplay;

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
            location.getWorld().spawn(location, TextDisplay.class, textDisplay -> {
                this.holoText = textDisplay;
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
        if (leftTick <= 0) {
            BedwarsItemSpawnEvent event = new BedwarsItemSpawnEvent(location, itemType, spawnTick, holoItem, holoText);
            plugin.getServer().getPluginManager().callEvent(event);

            this.location = event.getSpawnLocation();
            this.itemType = event.getItemType();
            this.spawnTick = event.getSpawnTime();

            this.leftTick = this.spawnTick;
        } else {
            this.leftTick--;
        }

        if (holoItem != null)
            holoItem.setRotation(holoItem.getYaw() - 5.0f, 0); // todo: 插值动画 + 上下浮动

        if (holoText != null)
            holoText.text(replaceHolder(holoTextContent));
    }

    @Override
    public void shutdown() {
        if (holoItem != null) holoItem.remove();
        if (holoText != null) holoText.remove();
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
