package net.nexarys.alicushions.listeners;

import net.nexarys.alicushions.AliCushions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class WorldListeners implements Listener {
    private final AliCushions plugin = AliCushions.getInstance();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        plugin.getEntityManager().loadInChunk(event.getChunk());
    }
}
