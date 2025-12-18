package org.ourworld.nextGenBedwars.inventory.transformers;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import org.ourworld.nextGenBedwars.inventory.InvButton;
import org.ourworld.nextGenBedwars.inventory.InvConfig;
import org.ourworld.nextGenBedwars.inventory.actions.InvAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface InvTransformer {
    InvTransformer NO_OPERATION = new InvTransformer(){};

    /**
     * 需要有字符串提供的动作所调用
     */
    default Component action(InventoryClickEvent event, InvButton button, InvAction action, Component needTransformed) {
        return needTransformed;
    }

    default Component title(@Nullable InvConfig config, @Nullable InventoryHolder invHolder, @Nullable OfflinePlayer papiTarget, Component title) {
        return title;
    }

    default ItemStack button(InvConfig config, InventoryHolder holder, InvButton invButton, OfflinePlayer papiTarget) {
        final ItemStack icon = invButton.getIcon();
        final ItemMeta itemMeta = icon.getItemMeta();
        if (itemMeta.hasDisplayName())
            itemMeta.displayName(buttonStr(config, holder, invButton, papiTarget, itemMeta.displayName()));
        if (itemMeta.hasLore()) {
            final List<Component> lore = new ArrayList<>(Objects.requireNonNull(itemMeta.lore()));
            lore.replaceAll(s -> buttonStr(config, holder, invButton, papiTarget, s));
            itemMeta.lore(lore);
        }
        icon.setItemMeta(itemMeta);
        return icon;
    }

    default Component buttonStr(InvConfig config, InventoryHolder holder, InvButton invButton, OfflinePlayer papiTarget, Component needTransformed) {
        return needTransformed;
    }
}
