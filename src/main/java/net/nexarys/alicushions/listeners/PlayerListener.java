package net.nexarys.alicushions.listeners;

import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.enums.CushionColor;
import net.nexarys.alicushions.managers.EntityManager;
import net.nexarys.alicushions.objects.Cushion;
import net.nexarys.alicushions.objects.NekoItem;
import net.nexarys.alicushions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final AliCushions plugin = AliCushions.getInstance();

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!(entity instanceof Interaction interaction)) return;
        if (!Utils.containsData(interaction, EntityManager.CUSHION_KEY, PersistentDataType.STRING)) return;

        UUID uuid = UUID.fromString(Objects.requireNonNull(Utils.getDataString(interaction, EntityManager.CUSHION_KEY)));

        Cushion cushion = plugin.getEntityManager().getCushion(uuid);
        if (cushion == null) return;

        ItemDisplay display = cushion.getSitDisplay();

        if (!display.getPassengers().isEmpty()) return;

        display.addPassenger(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Interaction interaction)) return;
        if (!Utils.containsData(interaction, EntityManager.CUSHION_KEY, PersistentDataType.STRING)) return;

        UUID uuid = UUID.fromString(Objects.requireNonNull(Utils.getDataString(interaction, EntityManager.CUSHION_KEY)));

        Cushion cushion = plugin.getEntityManager().getCushion(uuid);
        if (cushion == null) return;

        UUID owner = cushion.getOwner();


        if (owner.equals(event.getDamager().getUniqueId()) || event.getDamager().isOp()) {
            Player player = (Player) event.getDamager();
            if (player.getGameMode() == GameMode.SPECTATOR) return;
            Location sitLocation = cushion.getSitDisplay().getLocation().clone();

            plugin.getEntityManager().getCushions().remove(uuid);
            cushion.remove();

            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            String itemName = Utils.getDataString(interaction, "item_name");
            if (itemName == null) itemName = "yellow";

            NekoItem nekoItem = plugin.getItemManager().getItems().get(itemName.toLowerCase());
            ItemStack item = nekoItem.getItem("CushionColor", nekoItem.getCushionColor().name());
            Objects.requireNonNull(sitLocation.getWorld()).dropItem(sitLocation, item);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack itemStack = event.getItem();
        if (player.getGameMode() == GameMode.SPECTATOR || event.getAction() != Action.RIGHT_CLICK_BLOCK || block == null || itemStack == null || itemStack.getType().isAir()) return;

        if (!player.isSneaking() && block.getType().isInteractable()) return;

        String color = Utils.getCushionColor(itemStack);
        if (color == null) return;

        CushionColor cushionColor = CushionColor.valueOf(color.toUpperCase());

        Location spawnLocation = getSpawnLocation(player, block, event.getBlockFace());
        if (spawnLocation == null) return;

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
        }

        Cushion cushion = new Cushion(UUID.randomUUID(), spawnLocation.getBlock().getRelative(BlockFace.DOWN).getLocation(), spawnLocation, cushionColor, player.getUniqueId());
        cushion.spawn();

        String itemName = Utils.getDataString(itemStack, "item_name");
        if (itemName == null) itemName = "yellow";

        Utils.setData(cushion.getInteraction(), "item_name", itemName);
    }

    private Location getSpawnLocation(Player player, Block block, BlockFace face) {
        if (face == BlockFace.DOWN) {
            return null;
        }

        if (face == BlockFace.UP) {
            Location location = block.getLocation();
            return new Location(location.getWorld(), location.getBlockX(), getTopSurfaceY(block), location.getBlockZ());
        }

        BlockData blockData = block.getBlockData();

        if (blockData instanceof Stairs stairs) {
            BlockFace facing = stairs.getFacing();
            Bisected.Half half = stairs.getHalf();
            if (facing.getOppositeFace() == face) {
                if (half != Bisected.Half.TOP) {
                    RayTraceResult result = player.rayTraceBlocks(6, FluidCollisionMode.NEVER);
                    if (result != null) {
                        Vector hitPosition = result.getHitPosition();
                        double hitY = hitPosition.getY() - block.getY();

                        if (hitY >= 0.5) {
                            Location location = block.getLocation();
                            return new Location(location.getWorld(), location.getBlockX(), location.getY() + 0.5, location.getBlockZ());
                        }
                    }
                }
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