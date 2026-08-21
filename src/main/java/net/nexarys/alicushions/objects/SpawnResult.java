package net.nexarys.alicushions.objects;

import net.nexarys.alicushions.enums.CushionOrientation;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public record SpawnResult(Location location, CushionOrientation orientation, BlockFace facing) {
}