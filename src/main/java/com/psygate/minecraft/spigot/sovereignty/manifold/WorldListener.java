package com.psygate.minecraft.spigot.sovereignty.manifold;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static com.psygate.minecraft.spigot.sovereignty.manifold.SpawnTypes.BEDRESPAWN;
import static com.psygate.minecraft.spigot.sovereignty.manifold.SpawnTypes.FIRSTJOIN;

/**
 * Created by psygate on 04.05.2016.
 */
public class WorldListener implements Listener {
    private final static Logger LOG = Logger.getLogger(WorldListener.class.getName());
    private final static Random rand = new Random();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void playerJoin(PlayerSpawnLocationEvent ev) {
        Location loc = ev.getSpawnLocation();
        if (Manifold.getConfigration().isHandledWorld(loc.getWorld())) {
            WorldConfiguration wconf = Manifold.getConfigration().getWorldConfiguration(loc.getWorld());
            if ((!ev.getPlayer().hasPlayedBefore() && wconf.getSpawnTypes().contains(FIRSTJOIN))
                    ) {
                LOG.info("First join spawning " + ev.getPlayer().getName());
                ev.setSpawnLocation(generateSpawn(ev.getPlayer(), wconf));
            } else if (ev.getPlayer().getBedSpawnLocation() != null && wconf.getSpawnTypes().contains(BEDRESPAWN)) {
                LOG.info("Bed spawning " + ev.getPlayer().getName());
                ev.setSpawnLocation(generateSpawn(ev.getPlayer(), wconf));
            } else if ()

        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void chatEvent(AsyncPlayerChatEvent ev) {
        if (ev.getMessage().equals("spawn")) {
            Bukkit.getScheduler().runTask(Manifold.getInstance(), () ->
                    ev.getPlayer().teleport(generateSpawn(ev.getPlayer(), Manifold.getConfigration().getWorldConfiguration(ev.getPlayer().getLocation().getWorld())))
            );
        }
    }

    private Location generateSpawn(Player player, WorldConfiguration wconf) {
        Location loc = null;

        int tries = 0;
        while ((loc == null || isExcluded(loc) && tries < 10)) {
            tries++;
            double x, z;
            switch (wconf.getShape()) {
                case CIRCLE:
                    double radx = wconf.getRadiusX() * rand.nextDouble();
                    double radz = wconf.getRadiusZ() * rand.nextDouble();
                    double angle = Math.PI * 2 * rand.nextDouble();
                    x = Math.floor(wconf.getX() + Math.cos(angle) * radx) + 0.5;
                    z = Math.floor(wconf.getZ() + Math.sin(angle) * radz) + 0.5;
                    loc = new Location(
                            wconf.getWorld(),
                            x,
                            wconf.getWorld().getHighestBlockYAt((int) x, (int) z) + 1,
                            z
                    );
                    break;
                case SQUARE:
                    //TODO
                    loc = new Location(wconf.getWorld(), 0, 256, 0);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown shape: " + wconf.getShape());
            }
        }
        if (tries == 10) {
            LOG.info("Gave up on " + player.getName() + ", no suitable spawn found.");
        }

        while (loc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            loc.setY(loc.getY() - 1);
        }
        return loc;
    }

    private boolean isExcluded(Location loc) {
        for (int i = 0; i < 10; i++) {
            Material type = loc.getBlock().getRelative(BlockFace.DOWN, i).getType();
            if (type == Material.AIR && i < 3) {
                continue;
            } else if (Manifold.getConfigration().getExcludedBlocks().contains(type)) {
                return true;
            }
        }
        return false;
    }
}
