package com.psygate.minecraft.spigot.sovereignty.manifold;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by psygate on 25.04.2016.
 */
public class Manifold extends JavaPlugin {
    private static Manifold instance;
    private static final Logger LOG = Logger.getLogger(Manifold.class.getName());
    private Configuration configuration;

    @Override
    public void onEnable() {
        try {
            subEnable();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void subEnable() {
        instance = this;
        saveDefaultConfig();
        configuration = new Configuration(getConfig());
        if (configuration.isEnabled()) {
            getServer().getPluginManager().registerEvents(new WorldListener(), this);
        } else {
            LOG.warning("Manifold disabled.");
        }
    }

    public static Manifold getInstance() {
        if (instance == null) {
            LOG.severe("Instance not found.");
            fail();
        }

        return instance;
    }

    public static Configuration getConfigration() {
        return getInstance().configuration;
    }

    private static void fail() {
        LOG.severe("Manifold misinitialization.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        Bukkit.shutdown();
    }
}
