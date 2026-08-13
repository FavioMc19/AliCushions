package net.nexarys.alicushions.listeners;

import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.managers.EntityManager;
import net.nexarys.alicushions.objects.Cushion;
import net.nexarys.alicushions.objects.CushionTexture;
import net.nexarys.alicushions.objects.NekoItem;
import net.nexarys.alicushions.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.VoxelShape;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final AliCushions plugin = AliCushions.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(!event.getPlayer().isOp() && !event.getPlayer().hasPermission("*")) return;
        plugin.getUpdateChecker().sendMessage(event.getPlayer(), plugin.UPDATED, plugin.VERSION);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return;
        if (!Utils.containsData(interaction, EntityManager.CUSHION_KEY, PersistentDataType.STRING)) return;

        UUID uuid = UUID.fromString(Objects.requireNonNull(Utils.getDataString(interaction, EntityManager.CUSHION_KEY)));

        Cushion cushion = plugin.getEntityManager().getCushion(uuid);
        if (cushion == null) return;

        if (Utils.containsData(interaction, "sit_player", PersistentDataType.STRING)) return;

        Player player = event.getPlayer();
        ItemDisplay sitDisplay = cushion.getLocation().getWorld().spawn(
                cushion.getLocation().clone().add(0.5, 0.25, 0.5), ItemDisplay.class, e ->
                        Utils.setData(e, EntityManager.CUSHION_KEY, uuid.toString())
        );
        sitDisplay.addPassenger(player);
        Utils.setData(interaction, "sit_player", sitDisplay.getUniqueId().toString());
    }

    @EventHandler (ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Interaction interaction)) return;
        if (!(event.getDamager() instanceof Player player)) return ;
        if (!Utils.containsData(interaction, EntityManager.CUSHION_KEY, PersistentDataType.STRING)) return;

        UUID uuid = UUID.fromString(Objects.requireNonNull(Utils.getDataString(interaction, EntityManager.CUSHION_KEY)));

        Cushion cushion = plugin.getEntityManager().getCushion(uuid);
        if (cushion == null) return;

        if (cushion.hasPermissions(player)) {
            if (player.getGameMode() == GameMode.SPECTATOR) return;
            Location sitLocation = cushion.getLocation().clone().add(0.5, 0.25, 0.5);

            String sitUuidStr = Utils.getDataString(interaction, "sit_player");
            if (sitUuidStr != null) {
                Entity sit = Bukkit.getEntity(UUID.fromString(sitUuidStr));
                if (sit != null) sit.remove();
                Utils.removeData(interaction, "sit_player");
            }

            plugin.getEntityManager().getCushions().remove(uuid);
            cushion.remove();
            event.setCancelled(true);

            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            String itemName = Utils.getDataString(interaction, "item_name");
            if (itemName == null) itemName = "yellow";

            NekoItem nekoItem = plugin.getItemManager().getItems().get(itemName.toLowerCase());
            ItemStack item = nekoItem.getItem("CushionColor", nekoItem.getCushionColor());
            Objects.requireNonNull(sitLocation.getWorld()).dropItem(sitLocation, item);
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() != EquipmentSlot.HAND || player.getGameMode() == GameMode.SPECTATOR) return;
        Block block = event.getClickedBlock();
        ItemStack itemStack = event.getItem();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || block == null || itemStack == null || itemStack.getType().isAir()) return;

        if (!player.isSneaking() && block.getType().isInteractable()) return;

        String color = Utils.getCushionColor(itemStack);
        if (color == null) return;

        Location spawnLocation = getSpawnLocation(player, block, event.getBlockFace());
        if (spawnLocation == null) return;
        Cushion cushion = new Cushion(UUID.randomUUID(), spawnLocation.getBlock().getRelative(BlockFace.DOWN).getLocation(), spawnLocation, color.toLowerCase(), player.getUniqueId());
        CushionTexture texture = plugin.getTextureGenerator().getTextures().get(cushion.getColor());

        if (texture == null) {
            player.sendMessage(Utils.color("&7[&cError&7]&c Texture is null."));
            event.setCancelled(true);
            return;
        }

        if (!texture.isGenerated()) {
            player.sendMessage(Utils.color("&7[&cError&7]&e generating textures..."));
            event.setCancelled(true);
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
        }

        cushion.spawn(player);

        event.setCancelled(true);

        String itemName = Utils.getDataString(itemStack, "item_name");
        if (itemName == null) itemName = "yellow";

        Utils.setData(cushion.getInteraction(), "item_name", itemName);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof ItemDisplay sitDisplay)) return;
        if (!Utils.containsData(sitDisplay, EntityManager.CUSHION_KEY, PersistentDataType.STRING)) return;

        String cushionUuidStr = Utils.getDataString(sitDisplay, EntityManager.CUSHION_KEY);
        if (cushionUuidStr == null) return;

        Cushion cushion = plugin.getEntityManager().getCushion(UUID.fromString(cushionUuidStr));
        player.leaveVehicle();
        sitDisplay.remove();
        if (cushion != null) {
            Utils.removeData(cushion.getInteraction(), "sit_player");
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        Entity vehicle = event.getDismounted();
        if (!(vehicle instanceof ItemDisplay sitDisplay)) return;
        if (!Utils.containsData(sitDisplay, EntityManager.CUSHION_KEY, PersistentDataType.STRING)) return;

        String cushionUuidStr = Utils.getDataString(sitDisplay, EntityManager.CUSHION_KEY);
        if (cushionUuidStr == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Cushion cushion = plugin.getEntityManager().getCushion(UUID.fromString(cushionUuidStr));
                sitDisplay.remove();
                if (cushion != null && cushion.getInteraction() != null) {
                    Utils.removeData(cushion.getInteraction(), "sit_player");
                }
            }
        }.runTaskLater(plugin, 1L);
    }

    private Location getSpawnLocation(Player player, Block block, BlockFace face) {
        if (face == BlockFace.DOWN) {
            return null;
        }

        VoxelShape collisionShape = block.getCollisionShape();
        Collection<BoundingBox> boundingBoxes = collisionShape.getBoundingBoxes();

        if (face == BlockFace.UP && boundingBoxes.size() == 1) {
            Location location = block.getLocation();
            return new Location(location.getWorld(), location.getBlockX(), getTopSurfaceY(block), location.getBlockZ());
        }

        if (face == BlockFace.UP) {
            RayTraceResult result = player.rayTraceBlocks(6, FluidCollisionMode.NEVER);
            if (result != null) {
                Vector hitPosition = result.getHitPosition();
                double hitY = hitPosition.getY() - block.getY();

                Location location = block.getLocation();
                return new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + hitY, location.getBlockZ());
            }
        }

        Block sideBlock = block.getRelative(face);
        if (!sideBlock.getType().isAir()) return null;

        Block belowSide = sideBlock.getRelative(BlockFace.DOWN);
        if (belowSide.getType().isAir()) return null;

        Location belowLocation = belowSide.getLocation();
        return new Location(belowLocation.getWorld(), belowLocation.getBlockX(), getTopSurfaceY(belowSide), belowLocation.getBlockZ());
    }

    private double getTopSurfaceY(Block block) {
        BoundingBox box = block.getBoundingBox();
        if (box.getHeight() <= 0) {
            return block.getY() + 1;
        }
        return box.getMaxY();
    }
}