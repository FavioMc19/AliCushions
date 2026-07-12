package net.nexarys.alicushions.listeners;

import net.nexarys.alicushions.enums.CushionColor;
import net.nexarys.alicushions.objects.Cushion;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.UUID;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) return;
        Location location = event.getPlayer().getLocation().getBlock().getLocation();

        Cushion cushion = new Cushion(UUID.randomUUID(), location, location, CushionColor.YELLOW, event.getPlayer().getUniqueId());
        cushion.spawn();
        event.getPlayer().sendMessage("Spawneado");
    }
}
