package org.ourworld.nextGenBedwars.gameplay.core.spawner;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * 资源刷新点的参数
 * @param location 资源刷新点的位置
 * @param material 刷新的资源的类型
 * @param spawnTick 刷新时间间隔
 */
public record ItemSpawner(Location location, Material material, int spawnTick) {}
