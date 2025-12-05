package org.ourworld.nextGenBedwars.gameplay.core.spawner;

import org.ourworld.nextGenBedwars.util.GlobalTicker;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ItemSpawnerManager {

    private Set<UUID> spawnerSet;

    public ItemSpawnerManager() {
        spawnerSet = new HashSet<>();
    }

    /**
     * 添加一个新的资源刷新点
     * @param spawner ItemSpawner 数据类，用于构造资源生成点
     * @return 返回一个 UUID
     */
    public UUID addSpawner(ItemSpawner spawner) {
        UUID uuid = UUID.randomUUID();
        setSpawner(uuid, spawner);
        spawnerSet.add(uuid);
        return uuid;
    }

    /**
     * 覆盖一个指定的资源刷新点
     * @param uuid 指定的资源刷新点的 UUID
     * @param spawner 新的 ItemSpawner 数据类，用于覆盖资源生成点参数
     */
    public void setSpawner(UUID uuid, ItemSpawner spawner) {
        GlobalTicker.set(uuid, spawner);
    }

    /**
     * 移除一个指定的资源刷新点
     * @param uuid 指定的资源刷新点的 UUID
     */
    public void removeSpawner(UUID uuid) {
        GlobalTicker.remove(uuid);
        spawnerSet.remove(uuid);
    }

    public void shutdown() {
        spawnerSet.forEach(GlobalTicker::remove);
        spawnerSet.clear();
        spawnerSet = null;
    }

}
