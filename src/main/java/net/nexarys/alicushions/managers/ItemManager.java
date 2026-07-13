package net.nexarys.alicushions.managers;

import lombok.Getter;
import lombok.Setter;
import net.nexarys.alicushions.objects.NekoItem;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class ItemManager {
    private final Map<String, NekoItem> items = new HashMap<>();
}
