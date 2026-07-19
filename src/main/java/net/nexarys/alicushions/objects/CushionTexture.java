package net.nexarys.alicushions.objects;

import lombok.Getter;
import lombok.Setter;
import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.managers.TextureGenerator;
import net.nexarys.alicushions.utils.NekoConfig;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class CushionTexture {
    private String hash;
    private final AliCushions plugin = AliCushions.getInstance();
    private final String name;
    private final BufferedImage texture;
    private Map<Integer, HeadTexture> heads = new HashMap<>();

    public CushionTexture(String name, BufferedImage texture) {
        this.name = name;
        this.texture = texture;
    }

    public boolean isGenerated() {
        int count = 0;
        for (HeadTexture headTexture : heads.values()) {
            if (headTexture.generated()) {
                count++;
            }
        }
        return count == 4;
    }

    public void save() {
        TextureGenerator textureGenerator = plugin.getTextureGenerator();
        NekoConfig config = textureGenerator.getConfig();
        config.set("textures.base."+name+".hash", hash);
        config.saveConfig();
    }

    public String getHeadById(int i) {
        return switch (i) {
            case 1 -> heads.get(0).getTextureId();
            case 0 -> heads.get(1).getTextureId();
            case 3 -> heads.get(2).getTextureId();
            case 2 -> heads.get(3).getTextureId();
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }
}
