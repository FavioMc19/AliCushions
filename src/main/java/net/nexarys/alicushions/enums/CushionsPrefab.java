package net.nexarys.alicushions.enums;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;

@Getter
public enum CushionsPrefab {
    CAKE(Material.CAKE, "&d");

    private final Material material;
    private final String nameColor;

    CushionsPrefab(Material material, String nameColor) {
        this.material = material;
        this.nameColor = nameColor;
    }

    public static boolean exist(String name) {
        try {
            CushionsPrefab.valueOf(name.toUpperCase());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @NonNull
    public static  CushionsPrefab parse(String name) {
        try {
            return CushionsPrefab.valueOf(name.toUpperCase());
        } catch (Exception ignored) {
            return CAKE;
        }
    }
}
