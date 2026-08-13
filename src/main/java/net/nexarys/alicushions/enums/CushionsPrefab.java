package net.nexarys.alicushions.enums;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;

import java.util.Locale;

@Getter
public enum CushionsPrefab {
    CAKE(Material.CAKE, "&d",
            "95e2e4963ee6a9e0576ba7bffe914c3e690b24cb63b68bc82a8c01d4ab533d7c",
            "448c1b7141a1be7027939eff6f0dea28e9aa042ec004fa776bbd5113c926c69a",
            "612239029de6723d57fcb19dac40902017e1302eb4f4d0f65e032b08e019dad4",
            "f2ddd1820bde1021a82996e60c90bdcc0492dd86d6c42a82042fb6d3b01d8c66",
            "85a2e8fcee4ffadb2296360f1d32b2454c2be0798dbc086f3668edc350936898"),

    CREEPER(Material.LIME_CARPET, "&e",
            "4bb400807f5e69d9958ec5c15cc49e6d3a74afd4f8431188080daa71f4b4e268",
            "af9ad0db299f94d690013620533f555f51e141324f78fe260b3651f5612a17e1",
            "3de7f0a1f9cf0b112c01a5767bc3b3c479118ef80c140be468f20902a72f1e46",
            "7efd14865282ae0554b3f28c24c87e73b49fea44e67d3551441b987576e8b078",
            "c00e0747e4c72d31750813aba95b4c1e86ae63402ad1b49800dec83f2f6082cb");

    private final Material material;
    private final String nameColor;
    private final String texture1;
    private final String texture2;
    private final String texture3;
    private final String texture4;
    private final String hash;

    CushionsPrefab(Material material, String nameColor, String texture1, String texture2, String texture3, String texture4,  String hash) {
        this.material = material;
        this.nameColor = nameColor;
        this.texture1 = texture1;
        this.texture2 = texture2;
        this.texture3 = texture3;
        this.texture4 = texture4;
        this.hash = hash;
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

    public static CushionsPrefab getPrefab(String name) {
        try {
            return CushionsPrefab.valueOf(name.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String getPrefabTexture(CushionsPrefab prefab, int id) {
        return switch (id) {
            case 0 -> prefab.getTexture1();
            case 1 -> prefab.getTexture2();
            case 2 -> prefab.getTexture3();
            case 3 -> prefab.getTexture4();
            default -> null;
        };
    }
}
