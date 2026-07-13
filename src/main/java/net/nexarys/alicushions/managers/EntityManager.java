package net.nexarys.alicushions.managers;

import lombok.Getter;
import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.objects.Cushion;
import net.nexarys.alicushions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class EntityManager {
    private final AliCushions plugin = AliCushions.getInstance();
    private final Map<UUID, Cushion> cushions = new HashMap<>();
    public static final String CUSHION_KEY = "Cushion";

    public EntityManager() {
    }

    public Cushion getCushion(UUID uuid) {
        return cushions.get(uuid);
    }

    public void loadAllInChunks() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                loadInChunk(chunk);
            }
        }
    }

    public void loadInChunk(Chunk chunk) {
        Arrays.stream(chunk.getEntities()).forEach(this::checkAndLoadEntity);
    }

    public void checkAndLoadEntity(Entity entity) {

        if (!Utils.containsData(entity, CUSHION_KEY, PersistentDataType.STRING)) return;
        String key = Utils.getDataString(entity, CUSHION_KEY);

        UUID uuid = UUID.fromString(key);
        Cushion cushion = getCushion(uuid);
        if (cushion == null) {
            entity.remove();
            return;
        }

        if (entity instanceof Interaction interaction) {
            cushion.setInteraction(interaction);
        }

        if (entity instanceof ItemDisplay display) {
            Integer id = Utils.getDataInt(display, "CUSHION_ID");
            if (id != null) {
                cushion.getDisplays().put(id, display);
            } else {
                cushion.setSitDisplay(display);
            }
        }
    }
}
