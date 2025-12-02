package org.ourworld.nextGenBedwars.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * 容器配置将该配置对象用来构造的 {@link InvHolder} 所得到的 {@link InventoryHolder} 对象
 * 调用 {@link InventoryHolder#getInventory()} 得到的容器给玩家打开，可以正常使用实例化后的配置行为
 */
public record InvConfig(
        Component title,
        // layout[y][x]
        InvButton[][] layout
) {
    public Inventory createInv(InventoryHolder holder, @Nullable OfflinePlayer papiTarget) {
        Inventory inv = Bukkit.createInventory(holder, layout.length * 9, papi(title, papiTarget));
        for (int y = 0; y < layout.length; y++)
            for (int x = 0; x < layout[y].length; x++) inv.setItem(y * 9 + x, layout[y][x].getIcon(papiTarget));
        return inv;
    }

    public InvButton getButton(int slot) {
        return layout[slot / 9][slot % 9];
    }

    public InvButton getButton(int x, int y) {
        return layout[y][x];
    }

    public static Component papi(Component old, @Nullable OfflinePlayer target) {
        return old;//TODO 以后可能用到PlaceholderAPI
    }

    public static InvConfig of(Component text, List<String> layout, Map<Character, InvButton> buttonMap) {
        return new InvConfig(text, combined(layout, buttonMap));
    }

    public static InvConfig of(String title, List<String> layout, Map<Character, InvButton> buttonMap) {
        return of(Component.text(title), layout, buttonMap);
    }

    public static InvButton[][] combined(List<String> layout, Map<Character, InvButton> buttonMap) {
        InvButton[][] buttons = new InvButton[layout.size()][9];
        for (int y = 0; y < Math.min(layout.size(), 6); y++)
            for (int x = 0; x < Math.min(layout.get(y).length(), 9); x++)
                buttons[y][x] = buttonMap.get(layout.get(y).charAt(x));
        return buttons;
    }

    /**
     * 通过解析yaml得InvConfig
     * <p>
     * yaml格式:
     * <pre>
     *         title: "标题"
     *         layout:
     *           - '000000000'
     *           - '0       0'
     *           - '000000000'
     *          buttons:
     *            '0':
     *              material: STONE
     *              name: "§a按钮"
     *              lore:
     *                - '§alore'
     *              actions:
     *                left:
     *                  - 'command: /say Hello world'
     *     </pre>
     * 其中 actions下的动作类型更具 {@link org.bukkit.event.inventory.ClickType} 美剧的名字来定的(可以小写)
     * <p>
     * 可用的动作列表则是更具注册在工厂 {@link org.ourworld.nextGenBedwars.inventory.actions.InvActionFactories} 来获取的
     * </p>
     * </p>
     *
     * @see org.bukkit.event.inventory.ClickType
     * @see org.ourworld.nextGenBedwars.inventory.actions.InvActionFactories
     */
    public static InvConfig parseYaml(@NotNull ConfigurationSection config) {
        ConfigurationSection section = config.getConfigurationSection("buttons");
        HashMap<Character, InvButton> map = new HashMap<>();
        if (section != null) for (String s : section.getKeys(false)) {
            if (s.length() != 1) throw new IllegalArgumentException("Button name must be a single character");
            map.put(s.charAt(0), InvButton.parseYaml(Objects.requireNonNull(section.getConfigurationSection(s))));
        }
        return of(config.getString("title"), config.getStringList("layout"), map);
    }


    public static class Builder {
        private final Component title;
        private final List<String> layout = new ArrayList<>();
        private final Map<Character, InvButton> buttons = new HashMap<>();

        public Builder(Component title) {
            this.title = title;
        }

        public Builder(String title) {
            this(Component.text(title));
        }

        public List<String> layout() {
            return new ArrayList<>(this.layout);
        }

        public Builder layout(List<String> layout) {
            this.layout.clear();
            this.layout.addAll(layout);
            return this;
        }

        public Map<Character, InvButton> buttons() {
            return new HashMap<>(this.buttons);
        }

        public Builder button(Character identity, InvButton button) {
            this.buttons.put(identity, button);
            return this;
        }

        public InvButton button(Character identity) {
            return this.buttons.get(identity);
        }

        public Component title() {
            return title;
        }

        public InvConfig build() {
            return InvConfig.of(this.title, this.layout, this.buttons);
        }
    }
}
