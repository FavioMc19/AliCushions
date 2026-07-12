package net.nexarys.alicushions.managers;

import net.nexarys.alicushions.objects.Cushion;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityManager {
    private final Map<UUID, Cushion> cushions = new HashMap<>();

    public Cushion getCushion(UUID uuid) {
        return cushions.get(uuid);
    }

}
