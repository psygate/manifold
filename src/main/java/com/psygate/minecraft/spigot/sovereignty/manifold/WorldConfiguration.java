package com.psygate.minecraft.spigot.sovereignty.manifold;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by psygate on 04.05.2016.
 */
public class WorldConfiguration {
    private static final Logger LOG = Logger.getLogger(WorldConfiguration.class.getName());
    private boolean enabled;
    private SpawnShape shape;
    private String worldName;
    private double x, z, radiusX, radiusZ;
    private Set<SpawnTypes> spawnTypes = new HashSet<>();

    public WorldConfiguration(String worldName, ConfigurationSection configurationSection) {
        enabled = configurationSection.getBoolean("enabled");
        shape = SpawnShape.valueOf(configurationSection.getString("shape").trim().toUpperCase());
        radiusX = configurationSection.getDouble("radius.x");
        radiusZ = configurationSection.getDouble("radius.z");

        x = configurationSection.getInt("center.x");
        z = configurationSection.getInt("center.z");
        this.worldName = worldName;
        spawnTypes = new HashSet<>(
                configurationSection.getStringList("handled_types")
                        .stream()
                        .map(name -> SpawnTypes.valueOf(name.trim().toUpperCase()))
                        .collect(Collectors.toList())
        );
        LOG.info("Loaded configuration for " + worldName + "\n" + this);
    }

    public World getWorld() {
        World w = Bukkit.getWorld(worldName);
        if (w == null) {
            throw new IllegalStateException("Illegal world name: " + worldName);
        }

        return w;
    }

    public static Logger getLOG() {
        return LOG;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public SpawnShape getShape() {
        return shape;
    }

    public String getWorldName() {
        return worldName;
    }

    public double getRadiusX() {
        return radiusX;
    }

    public double getRadiusZ() {
        return radiusZ;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public Set<SpawnTypes> getSpawnTypes() {
        return spawnTypes;
    }

    @Override
    public String toString() {
        return "WorldConfiguration{" +
                "enabled=" + enabled +
                ", shape=" + shape +
                ", worldName='" + worldName + '\'' +
                ", x=" + x +
                ", z=" + z +
                ", radiusX=" + radiusX +
                ", radiusZ=" + radiusZ +
                ", spawnTypes=" + spawnTypes +
                '}';
    }
}
