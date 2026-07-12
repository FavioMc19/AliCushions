package net.nexarys.alicushions;

import lombok.Getter;
import net.nexarys.alicushions.listeners.PlayerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class AliCushions extends JavaPlugin {

    @Override
    public void onEnable() {
        initClass();
        initListeners();
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initClass() {
    }

    private void initListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
    }

    public static AliCushions getInstance() {
        return JavaPlugin.getPlugin(AliCushions.class);
    }
}
