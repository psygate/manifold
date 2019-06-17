package com.psygate.minecraft.spigot.sovereignty.manifold;

import java.io.File;

/**
 * Created by psygate on 04.05.2016.
 */
public class WorldManager {
    private WorldManager instance;

    private WorldManager() {
        File joinLog = new File()
    }

    public WorldManager getInstance() {
        if (instance == null) {
            instance = new WorldManager();
        }

        return instance;
    }
}
