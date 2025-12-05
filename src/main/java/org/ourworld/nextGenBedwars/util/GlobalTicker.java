package org.ourworld.nextGenBedwars.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.ourworld.nextGenBedwars.NextGenBedwars.plugin;


public class GlobalTicker {

    private static boolean isInit = false;
    private static BukkitTask tickerTask;

    private static Map<UUID, TickerTask> taskMap;

    private GlobalTicker() {}

    /**
     * 用于初始化 GlobalTicker，单例工具类
     */
    public static void initialize() {
        if (isInit) {
            plugin.getComponentLogger().error(Component.text("[GlobalTicker] You can't initialize this util twice!").color(NamedTextColor.RED));
            return;
        }

        taskMap = new ConcurrentHashMap<>();
        tickerTask = new BukkitRunnable() {
            @Override
            public void run() {
                taskMap.forEach((uuid, runnable) -> {
                    runnable.run();
                });
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     * 向 GlobalTicker 添加自定义任务，每 Tick 运行一次
     * @param runnable 你的自定义任务
     * @return 返回该任务的 UUID
     */
    public static UUID add(TickerTask runnable) {
        UUID uuid = UUID.randomUUID();
        set(uuid, runnable);
        return uuid;
    }

    /**
     * 让 GlobalTicker 覆盖一个已有的自定义任务
     * @param uuid 要覆盖的任务的 UUID
     * @param runnable 新的自定义任务
     */
    public static void set(UUID uuid, TickerTask runnable) {
        taskMap.put(uuid, runnable);
    }

    /**
     * 向 GlobalTicker 获取一个指定的自定义任务
     * @param uuid 要获取的任务的 UUID
     * @return 指定的自定义任务
     */
    public static TickerTask get(UUID uuid) {
        return taskMap.get(uuid);
    }

    /**
     * 从 GlobalTicker 中移除一个自定义任务
     * @param uuid 要移除的自定义任务的 UUID
     */
    public static void remove(UUID uuid) {
        TickerTask task = taskMap.remove(uuid);
        if (task != null) {
            task.shutdown();
        }
    }

    /**
     * 注销 GlobalTicker，注销后可重新初始化
     */
    public static void shutdown() {
        taskMap.forEach((uuid, task) -> {
            task.shutdown();
        });
        taskMap.clear();
        taskMap = null;

        tickerTask.cancel();
        tickerTask = null;

        isInit = false;
    }

}
