package org.ourworld.nextGenBedwars.inventory;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InvHolder implements InventoryHolder {
    private final InvConfig config;
    private Inventory inventory;

    /**
     * 直接进行初始化的构造器
     * <p>
     *     当子类有需要存储的成员就不能用这个构造器而是使用无初始化的构造器
     *     <pre>
     *         private final OfflinePlayer player;
     *
     *         public SubHolder(InvConfig config, OfflinePlayer player) {
     *             super(config);
     *             this.player = player;
     *             ...
     *             this.init(player);
     *         }
     *     </pre>
     * </p>
     * @param config 配置
     * @param player 参与初始化的玩家对象
     */
    public InvHolder(InvConfig config, @Nullable OfflinePlayer player) {
        this.config = config;
        this.init(player);
    }

    /**
     * 没有初始化的构造
     */
    public InvHolder(InvConfig config) {
        this.config = config;
    }

    public InvConfig getConfig() {
        return config;
    }

    /**
     * 该方法一定是在打开前就执行了的
     * @param player 用于参与处理的玩家对选（papi变量等）
     */
    public void init(@Nullable OfflinePlayer player) {
        this.inventory = config.createInv(this, player);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return Objects.requireNonNull(this.inventory, "Inventory is not initialized yet!");
    }

    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!this.getInventory().equals(e.getClickedInventory()) || e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
            return;
        InvButton button = this.getConfig().getButton(e.getSlot());
        if (button == null) return;
        button.onClick(e, this.config.getTransformer());
    }
}
