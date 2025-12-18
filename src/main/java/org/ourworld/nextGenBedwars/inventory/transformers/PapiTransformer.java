package org.ourworld.nextGenBedwars.inventory.transformers;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;
import org.ourworld.nextGenBedwars.inventory.InvButton;
import org.ourworld.nextGenBedwars.inventory.InvConfig;
import org.ourworld.nextGenBedwars.inventory.actions.InvAction;

/**
 * 需要为容器支持Papi时才用（服务器得先有Papi）
 * <p>
 * 使用方法
 *     <pre>
 *         new InvConfig.Builder(title).transformer(PapiTransformer.INSTANCE)...
 *     </pre>
 * </p>
 */
public class PapiTransformer implements InvTransformer {
    public static PapiTransformer INSTANCE = new PapiTransformer();

    @Override
    public Component action(InventoryClickEvent event, InvButton button, InvAction action, Component needTransformed) {
        if (!(event.getWhoClicked() instanceof OfflinePlayer)) return needTransformed;
        return papi(needTransformed, ((OfflinePlayer) event.getWhoClicked()));
    }

    @Override
    public Component title(@Nullable InvConfig config, @Nullable InventoryHolder invHolder, @Nullable OfflinePlayer papiTarget, Component title) {
        return papi(title, papiTarget);
    }

    @Override
    public Component buttonStr(InvConfig config, InventoryHolder holder, InvButton invButton, OfflinePlayer papiTarget, Component needTransformed) {
        return papi(needTransformed, papiTarget);
    }

    private Component papi(Component str, OfflinePlayer target) {
/* TODO 待引入PlaceholderAPI依赖
        return PlaceholderAPI.setPlaceholders(target, str);
*/
        throw new RuntimeException("No implement yet!");
    }
}
