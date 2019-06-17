package com.psygate.minecraft.spigot.sovereignty.manifold;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by psygate on 04.05.2016.
 */
public class Configuration {
    private boolean enabled;
    private Map<UUID, WorldConfiguration> worlds = new HashMap<>();
    private Set<Material> excludedBlocks;

    public Configuration(FileConfiguration conf) {
        enabled = conf.getBoolean("enabled");

        for (String worldkey : conf.getConfigurationSection("worlds").getKeys(false)) {
            WorldConfiguration wconf = new WorldConfiguration(worldkey, conf.getConfigurationSection("worlds." + worldkey));
            worlds.put(wconf.getWorld().getUID(), wconf);
        }

        excludedBlocks = new HashSet<>(
                conf.getStringList("excluded_blocks").stream()
                        .map(name -> Material.valueOf(name.toUpperCase().trim()))
                        .collect(Collectors.toList())
        );
    }

    public boolean isHandledWorld(World world) {
        return worlds.containsKey(world.getUID()) && worlds.get(world.getUID()).isEnabled();
    }

    public WorldConfiguration getWorldConfiguration(World world) {
        return Objects.requireNonNull(worlds.get(world.getUID()), () -> "Unconfigured world " + world);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Map<UUID, WorldConfiguration> getWorlds() {
        return worlds;
    }

    public Set<Material> getExcludedBlocks() {
        return excludedBlocks;
    }
}
