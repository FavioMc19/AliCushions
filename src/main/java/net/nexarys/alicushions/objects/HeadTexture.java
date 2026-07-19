package net.nexarys.alicushions.objects;

import lombok.Getter;
import lombok.Setter;
import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.managers.TextureGenerator;
import net.nexarys.alicushions.utils.ImageUtils;
import net.nexarys.alicushions.utils.NekoConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.awt.image.BufferedImage;

@Getter @Setter
public class HeadTexture {
    private final AliCushions plugin = AliCushions.getInstance();
    private BufferedImage baseTexture;
    private String key;
    private int id;
    private BufferedImage head;
    private String textureId;
    private int generationTries = 0;

    public HeadTexture(ConfigurationSection section) {
        this.textureId = section.getString("texture");
        this.id = section.getInt("id");
    }

    public HeadTexture() {

    }

    public boolean generated() {
        return textureId != null;
    }

    public void createBuffered() {
        head = ImageUtils.getPart(id, baseTexture);
    }

    public void save() {
        TextureGenerator textureGenerator = plugin.getTextureGenerator();
        NekoConfig config = textureGenerator.getConfig();
        config.set("textures.heads."+key +"." + id + ".texture", textureId);
        config.set("textures.heads."+key +"." + id + ".id", id);
        config.saveConfig();
    }
}
