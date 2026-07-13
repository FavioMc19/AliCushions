package net.nexarys.alicushions.objects;

import lombok.Getter;
import lombok.Setter;
import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.enums.CushionColor;
import net.nexarys.alicushions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter @Setter
public class NekoItem {
    private final AliCushions plugin;
    private String key;
    private String name;
    private Integer custom_model_data;
    private List<String> lore;
    private Material material = Material.AMETHYST_SHARD;
    private ItemStack itemStack;
    private ItemStack head;
    private boolean unbreakable = false;
    private final Map<String, String> tags = new HashMap<>();
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private CushionColor cushionColor = CushionColor.YELLOW;

    private boolean recipeEnabled = false;
    private String recipeType;
    private Material recipeRowIngredient;
    private List<String> recipeShape;
    private Map<Character, Material> recipeShapedIngredients;
    private List<Material> recipeShapelessIngredients;
    private int recipeAmount = 1;

    public NekoItem(AliCushions plugin, ConfigurationSection config) {
        this.plugin = plugin;
        this.key = config.getName();

        if(config.contains("name"))
            name = Utils.color(config.getString("name"));

        if(config.contains("lore"))
            lore = Utils.color(config.getStringList("lore"));

        if(config.contains("custom_model_data"))
            custom_model_data = config.getInt("custom_model_data");

        if(config.contains("material"))
            material = Material.valueOf(Objects.requireNonNull(config.getString("material")).toUpperCase());

        if(config.contains("texture") && material != null && material.equals(Material.PLAYER_HEAD))
            head = Utils.getHeadFromURL(config.getString("texture"));

        if (config.contains("color")) {
            cushionColor = CushionColor.valueOf(Objects.requireNonNull(config.getString("color")).toUpperCase());
            setTag("item_name", key);
            setTag("CushionColor", cushionColor.name());
        }

        if (config.contains("recipe_enabled")) {
            recipeEnabled = config.getBoolean("recipe_enabled");
        }

        if (config.contains("recipe")) {
            ConfigurationSection recipeSection = config.getConfigurationSection("recipe");
            if (recipeSection != null) {
                parseRecipe(recipeSection);
            }
        }
    }

    private void parseRecipe(ConfigurationSection recipeSection) {
        recipeType = recipeSection.getString("type", "row").toUpperCase();
        recipeAmount = recipeSection.getInt("amount", 1);

        switch (recipeType) {
            case "ROW" -> recipeRowIngredient = Material.valueOf(Objects.requireNonNull(recipeSection.getString("ingredient")).toUpperCase());
            case "SHAPED" -> {
                recipeShape = recipeSection.getStringList("shape");
                recipeShapedIngredients = new HashMap<>();
                ConfigurationSection keysSection = recipeSection.getConfigurationSection("keys");
                if (keysSection != null) {
                    for (String keyChar : keysSection.getKeys(false)) {
                        Material material = Material.valueOf(Objects.requireNonNull(keysSection.getString(keyChar)).toUpperCase());
                        recipeShapedIngredients.put(keyChar.charAt(0), material);
                    }
                }
            }
            case "SHAPELESS" -> {
                recipeShapelessIngredients = new ArrayList<>();
                for (String materialName : recipeSection.getStringList("ingredients")) {
                    recipeShapelessIngredients.add(Material.valueOf(materialName.toUpperCase()));
                }
            }
        }
    }

    public void registerRecipe() {
        if (recipeType == null || !recipeEnabled) return;

        switch (recipeType) {
            case "ROW" -> registerRowRecipe();
            case "SHAPED" -> registerShapedRecipe();
            case "SHAPELESS" -> registerShapelessRecipe();
        }
    }

    private void registerRowRecipe() {
        String[][] rows = {
                { "AAA", "   ", "   " },
                { "   ", "AAA", "   " },
                { "   ", "   ", "AAA" }
        };

        for (int i = 0; i < rows.length; i++) {
            ItemStack result = getItem();
            result.setAmount(recipeAmount);

            NamespacedKey namespacedKey = new NamespacedKey(plugin, key + "_row_" + i);
            if (Bukkit.getRecipe(namespacedKey) != null) continue;
            ShapedRecipe recipe = new ShapedRecipe(namespacedKey, result);
            recipe.shape(rows[i]);
            recipe.setIngredient('A', recipeRowIngredient);

            Bukkit.addRecipe(recipe);
        }
    }

    private void registerShapedRecipe() {
        if (recipeShape == null || recipeShapedIngredients == null) return;

        ItemStack result = getItem();
        result.setAmount(recipeAmount);

        NamespacedKey namespacedKey = new NamespacedKey(plugin, key + "_shaped");
        if (Bukkit.getRecipe(namespacedKey) != null) return;
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, result);
        recipe.shape(recipeShape.toArray(new String[0]));

        for (Map.Entry<Character, Material> entry : recipeShapedIngredients.entrySet()) {
            recipe.setIngredient(entry.getKey(), entry.getValue());
        }

        Bukkit.addRecipe(recipe);
    }

    private void registerShapelessRecipe() {
        if (recipeShapelessIngredients == null) return;

        ItemStack result = getItem();
        result.setAmount(recipeAmount);

        NamespacedKey namespacedKey = new NamespacedKey(plugin, key + "_shapeless");
        if (Bukkit.getRecipe(namespacedKey) != null) return;
        ShapelessRecipe recipe = new ShapelessRecipe(namespacedKey, result);

        for (Material material : recipeShapelessIngredients) {
            recipe.addIngredient(material);
        }

        Bukkit.addRecipe(recipe);
    }

    public void setTag(String key, String value){
        tags.put(key, value);
    }

    public ItemStack getItem(){
        if(itemStack != null) return itemStack.clone();

        ItemStack itemStack = new ItemStack(material);

        if(head != null)
            itemStack = head;

        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        if(name != null)
            meta.setDisplayName(name);

        if(lore != null)
            meta.setLore(lore);

        if(custom_model_data != null)
            meta.setCustomModelData(custom_model_data);

        if (!enchantments.isEmpty()) {
            enchantments.entrySet().forEach(entry -> meta.addEnchant(entry.getKey(), entry.getValue(), true));
        }

        if (unbreakable) {
            meta.setUnbreakable(true);
        }

        if(!tags.isEmpty()){
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            for(Map.Entry<String, String> entry : tags.entrySet()){
                dataContainer.set(new NamespacedKey(plugin, entry.getKey()), PersistentDataType.STRING, entry.getValue());
            }
        }

        itemStack.setItemMeta(meta);
        this.itemStack = itemStack;

        return itemStack;
    }

    public ItemStack getItem(String key, Object value){
        ItemStack item = getItem();
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        if(value instanceof Integer integer){
            container.set(namespacedKey, PersistentDataType.INTEGER, integer);
        }

        if(value instanceof String string){
            container.set(namespacedKey, PersistentDataType.STRING, string);
        }
        item.setItemMeta(meta);
        return item;
    }

    public void setDefaultMaterial(Material material) {
        this.material = material;
    }

    public void addEnchant(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
    }
}