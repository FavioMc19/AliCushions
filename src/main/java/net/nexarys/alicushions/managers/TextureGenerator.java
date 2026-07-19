package net.nexarys.alicushions.managers;

import lombok.Getter;
import lombok.Setter;
import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.objects.CushionTexture;
import net.nexarys.alicushions.objects.HeadTexture;
import net.nexarys.alicushions.utils.ImageUtils;
import net.nexarys.alicushions.utils.NekoConfig;
import net.nexarys.alicushions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.mineskin.MineskinClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class TextureGenerator {
    private final AliCushions plugin = AliCushions.getInstance();
    private Map<String, CushionTexture> textures = new HashMap<>();
    private List<HeadTexture> generatingList = new ArrayList<>();
    private final NekoConfig config;
    private boolean generating = false;

    public TextureGenerator() {
        config = new NekoConfig("data/textures_data.alicushions", plugin);
        config.update();
    }

    private void debug(String message) {
        Bukkit.broadcastMessage(message);
        plugin.getLogger().info(message);
    }

    public void checkAndGenerateCushions() {
        File folder =  new File(plugin.getDataFolder() + "/cushions/");
        if (!folder.exists()) folder.mkdir();

        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isFile()) continue;

                String fileName = file.getName();
                String name = fileName.substring(0, fileName.lastIndexOf('.'));
                if (!fileName.endsWith(".png")) continue;

                try {
                    String hash = ImageUtils.getImageHash(file);
                    BufferedImage image = ImageUtils.adjustSize(ImageIO.read(file));
                    boolean exist = config.contains("textures.base." + name);
                    boolean needRegenerate = !config.getString("textures.base."+name+".hash", "").equals(hash);
                    CushionTexture cushionTexture = new CushionTexture(name, image);
                    cushionTexture.setHash(hash);
                    textures.put(name, cushionTexture);

                    if (!exist || needRegenerate) {
                        for (int i = 0; i < 4; i++) {
                            HeadTexture headTexture = new HeadTexture();
                            headTexture.setKey(name);
                            headTexture.setId(i);
                            headTexture.setBaseTexture(image);
                            headTexture.createBuffered();
                            cushionTexture.getHeads().put(i, headTexture);
                            generatingList.add(headTexture);
                        }
                        generatorNext();
                        continue;
                    }

                    boolean tryRegeneration = false;

                    for (int i = 0; i < 4; i++) {
                        ConfigurationSection section = config.getConfigurationSection("textures.heads." + name + "." + i);
                        HeadTexture headTexture = new HeadTexture(section);
                        headTexture.setKey(name);
                        headTexture.setBaseTexture(image);
                        cushionTexture.getHeads().put(i, headTexture);

                        if (!headTexture.generated()) {
                            headTexture.createBuffered();
                            tryRegeneration = true;
                            generatingList.add(headTexture);
                        }
                    }

                    if (tryRegeneration) generatorNext();

                } catch (Exception e) {
                    e.printStackTrace();
                    plugin.getLogger().warning("Error loading texture file: " + file.getName());
                }
            }
        }
    }

    private void generatorNext() {
        if (generating || generatingList.isEmpty()) return;

        HeadTexture headTexture = generatingList.get(0);
        generating = true;

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            BufferedImage image = headTexture.getHead();
            MineskinClient client = new MineskinClient("MyUserAgent");
            try {
                client.generateUpload(image).thenAccept(uploaded_skin -> {
                    String texture = Utils.getTextureFromURL(uploaded_skin.data.texture.url);
                    headTexture.setTextureId(texture);
                    headTexture.save();
                    CushionTexture cushionTexture = textures.get(headTexture.getKey());

                    generating = false;
                    generatingList.remove(0);

                    if (cushionTexture.isGenerated()) {
                        cushionTexture.save();
                    }

                    generatorNext();
                });
            } catch(Exception exception) {
                exception.printStackTrace();
                headTexture.setGenerationTries(headTexture.getGenerationTries() + 1);

                if (headTexture.getGenerationTries() >= 3) {
                    generatingList.remove(0);
                }

                generatorNext();
            }
        }, 20 * 5);
    }

}
