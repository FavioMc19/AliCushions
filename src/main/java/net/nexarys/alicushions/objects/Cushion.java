package net.nexarys.alicushions.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.nexarys.alicushions.enums.CushionColor;
import net.nexarys.alicushions.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import java.util.*;

@Getter @Setter
public class Cushion {
    private UUID uuid;
    private Location baseLocation;
    private Location location;
    private CushionColor color;
    private UUID owner;
    private UUID interactionUUID;
    private List<UUID> displaysUUID = new ArrayList<>();
    private Map<Integer, ItemDisplay> displays = new HashMap<>();
    private Interaction interaction;

    public Cushion(UUID uuid, Location baseLocation, Location location, CushionColor color, UUID owner) {
        this.uuid = uuid;
        this.baseLocation = baseLocation;
        this.location = location;
        this.color = color;
        this.owner = owner;
    }

    public void spawn() {
        double[][] offsets = {
                { 0.25, 0.25 },
                { 0.25, -0.25 },
                { -0.25, -0.25 },
                { -0.25, 0.25 }
        };

        for (int i = 0; i < 4; i++) {
            double offsetX = offsets[i][0];
            double offsetZ = offsets[i][1];

            Location spawnLocation = location.clone().add(offsetX, 0, offsetZ).add(.5, 0.25, .5);
            spawnLocation.setYaw(0);
            spawnLocation.setPitch(0);

            int finalI = i;
            ItemDisplay display = location.getWorld().spawn(spawnLocation, ItemDisplay.class, entity -> {
                entity.setItemStack(Utils.getHeadFromURLDirect(color.getHeadById(finalI)));
                Transformation transformation = entity.getTransformation();
                transformation.getScale().set(1, 0.5, 1);
                entity.setTransformation(transformation);
            });

            displays.put(i, display);
            displaysUUID.add(display.getUniqueId());
        }

        this.interaction = location.getWorld().spawn(location.clone().add(0.5, 0, 0.5), Interaction.class, entity -> {
           entity.setInteractionHeight(0.25f);
           entity.setInteractionWidth(1);
        });
    }

    public void remove() {

    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("uuid", uuid.toString());
        json.add("baseLocation", Utils.locationToJson(baseLocation));
        json.add("location", Utils.locationToJson(location));
        json.addProperty("color", color.name());
        json.addProperty("owner", owner.toString());
        json.addProperty("interactionUUID", interactionUUID.toString());

        JsonArray displays = new JsonArray();
        for (UUID uuid : displaysUUID) {
            displays.add(uuid.toString());
        }
        json.add("displays", displays);
        return json;
    }

    public static Cushion fromJson(JsonObject json) {
        Location baseLocation = Utils.jsonToLocation(json.getAsJsonObject("baseLocation"));
        Location location = Utils.jsonToLocation(json.getAsJsonObject("location"));
        CushionColor color = CushionColor.valueOf(json.get("color").getAsString());
        UUID owner = UUID.fromString(json.get("owner").getAsString());
        UUID uuid = UUID.fromString(json.get("uuid").getAsString());

        Cushion cushion = new Cushion(uuid, baseLocation, location, color, owner);

        if (json.has("interactionUUID") && !json.get("interactionUUID").isJsonNull()) {
            cushion.setInteractionUUID(UUID.fromString(json.get("interactionUUID").getAsString()));
        }

        List<UUID> displaysUUID = new ArrayList<>();
        JsonArray displays = json.getAsJsonArray("displays");
        for (int i = 0; i < displays.size(); i++) {
            displaysUUID.add(UUID.fromString(displays.get(i).getAsString()));
        }
        cushion.setDisplaysUUID(displaysUUID);

        return cushion;
    }
}
