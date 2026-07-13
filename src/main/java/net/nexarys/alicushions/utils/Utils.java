package net.nexarys.alicushions.utils;

import com.google.gson.JsonObject;
import lombok.NonNull;
import net.md_5.bungee.api.chat.TextComponent;
import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.enums.CushionColor;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern hex_pattern = Pattern.compile("(&#|#)([A-Fa-f0-9]{6})");


    public static JsonObject locationToJson(Location location) {
        JsonObject json = new JsonObject();
        json.addProperty("x", location.getX());
        json.addProperty("y", location.getY());
        json.addProperty("z", location.getZ());
        json.addProperty("yaw", location.getYaw());
        json.addProperty("pitch", location.getPitch());
        json.addProperty("world", location.getWorld().getName());
        return json;
    }

    public static Location jsonToLocation(JsonObject json) {
        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();
        double yaw = json.get("yaw").getAsDouble();
        double pitch = json.get("pitch").getAsDouble();
        String worldName = json.get("world").getAsString();
        World world = Bukkit.getWorld(worldName);
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public static ItemStack getHeadFromURL(String texture) {
        PlayerProfile profile =  Bukkit.createPlayerProfile(UUID.randomUUID(), "");
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        PlayerTextures textures = profile.getTextures();
        try{
            URL url = new URL(texture);
            textures.setSkin(url);
            profile.setTextures(textures);
        }catch(Exception ignored) {
        }

        assert meta != null;
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getHeadFromURLDirect(String texture){
        return getHeadFromURL("http://textures.minecraft.net/texture/"+texture);
    }

    public static void removeData(Entity entity, String key) {
        if (entity == null) return;
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);
        dataContainer.remove(namespacedKey);
    }

    public static void setData(Entity entity, String key, Object value) {
        if (entity == null) return;

        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);

        if (value instanceof String string) {
            dataContainer.set(namespacedKey, PersistentDataType.STRING, string);
        }

        if (value instanceof Integer integer) {
            dataContainer.set(namespacedKey, PersistentDataType.INTEGER, integer);
        }

        if (value instanceof Boolean booleanValue) {
            dataContainer.set(namespacedKey, PersistentDataType.BOOLEAN, booleanValue);
        }

        if (value instanceof Double doubleValue) {
            dataContainer.set(namespacedKey, PersistentDataType.DOUBLE, doubleValue);
        }

        if (value instanceof Float floatValue) {
            dataContainer.set(namespacedKey, PersistentDataType.FLOAT, floatValue);
        }
    }

    public static boolean containsData(Entity entity, String key, PersistentDataType<?, ?> type) {
        if (entity == null) return false;

        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);
        return dataContainer.has(namespacedKey, type);
    }

    @Nullable
    public static String getDataString(Entity entity, String key) {
        if (entity == null) return null;

        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);

        return dataContainer.has(namespacedKey, PersistentDataType.STRING) ? dataContainer.get(namespacedKey, PersistentDataType.STRING) : null;
    }

    @Nullable
    public static Integer getDataInt(Entity entity, String key) {
        if (entity == null) return null;

        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);

        return dataContainer.has(namespacedKey, PersistentDataType.INTEGER) ? dataContainer.get(namespacedKey, PersistentDataType.INTEGER) : null;
    }

    @NonNull
    public static Integer getDataInt(Entity entity, String key, int def) {
        Integer value = getDataInt(entity, key);
        return value == null ? def : value;
    }

    @Nullable
    public static Boolean getDataBoolean(Entity entity, String key) {
        if (entity == null) return null;

        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);

        return dataContainer.has(namespacedKey, PersistentDataType.BOOLEAN) ? dataContainer.get(namespacedKey, PersistentDataType.BOOLEAN) : null;
    }

    @NonNull
    public static Boolean getDataBoolean(Entity entity, String key, boolean def) {
        if (entity == null) return null;

        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);

        return dataContainer.has(namespacedKey, PersistentDataType.BOOLEAN) ? Objects.requireNonNull(dataContainer.get(namespacedKey, PersistentDataType.BOOLEAN)) : def;
    }

    @Nullable
    public static Double getDataDouble(Entity entity, String key) {
        if (entity == null) return null;

        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);

        return dataContainer.has(namespacedKey, PersistentDataType.DOUBLE) ? dataContainer.get(namespacedKey, PersistentDataType.DOUBLE) : null;
    }

    @NonNull
    public static Double getDataDouble(Entity entity, String key, double def) {
        Double value = getDataDouble(entity, key);
        return value == null ? def : value;
    }

    @Nullable
    public static Float getDataFloat(Entity entity, String key) {
        if (entity == null) return null;

        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);

        return dataContainer.has(namespacedKey, PersistentDataType.FLOAT) ? dataContainer.get(namespacedKey, PersistentDataType.FLOAT) : null;
    }

    public static String color(String text){
        return ChatColor.translateAlternateColorCodes('&', colorHex(text));
    }

    public static List<String> color(List<String> list){
        List<String> parsed = new ArrayList<>();

        for(String line : list)
            parsed.add(color(line));

        return parsed;
    }

    private static String colorHex(String text) {
        StringBuilder message = new StringBuilder();

        Matcher matcher = hex_pattern.matcher(text);

        int index = 0;
        while(matcher.find()) {
            message.append(text, index, matcher.start()).append(net.md_5.bungee.api.ChatColor.of((matcher.group().startsWith("&") ? matcher.group().substring(1) : matcher.group())));
            index = matcher.end();
        }
        return message.append(text.substring(index)).toString();
    }

    public static void sendMessage(CommandSender sender, String message){
        sender.sendMessage(color(message));
    }

    public static TextComponent getTextComponent(String text) {
        TextComponent base = new TextComponent();
        base.setItalic(false);

        String color = null;
        for(String part : hexSeparator(text)) {
            if(isHex(part)) {
                color = part;
            }else {
                TextComponent component = new TextComponent(color(part));
                if(color != null)
                    component.setColor(net.md_5.bungee.api.ChatColor.of(color.startsWith("&") ? color.substring(1) : color));
                base.addExtra(component);
            }
        }
        return base;
    }

    public static Boolean isHex(String text) {
        if(text == null)
            return false;

        if(text.startsWith("&"))
            text = text.substring(1);

        Matcher matcher = hex_pattern.matcher(text);

        return matcher.matches();
    }


    public static List<String> hexSeparator(String text) {
        List<String> texts = new ArrayList<>();

        Matcher matcher = hex_pattern.matcher(text);

        int index = 0;

        while(matcher.find()) {
            texts.add(text.substring(index, matcher.start()));
            texts.add(matcher.group().startsWith("&") ? matcher.group() : "&"+matcher.group());
            index = matcher.end();
        }

        texts.add(text.substring(index));
        return texts;
    }

    public static String getCushionColor(ItemStack itemStack) {
        if (itemStack == null) return null;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), "CushionColor");
        return dataContainer.get(namespacedKey, PersistentDataType.STRING);
    }

    public static String getDataString(ItemStack itemStack, String key) {
        if (itemStack == null) return null;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(AliCushions.getInstance(), key);
        return dataContainer.get(namespacedKey, PersistentDataType.STRING);
    }
}
