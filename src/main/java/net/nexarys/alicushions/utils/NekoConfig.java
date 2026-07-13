package net.nexarys.alicushions.utils;

import net.nexarys.alicushions.AliCushions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class NekoConfig {
	private final File configFile;
	private FileConfiguration config;
	private boolean needUpdate = false;
	private boolean hasDefault = false;

	public NekoConfig(String path, AliCushions plugin) {
		this(plugin.getDataFolder() + File.separator+ path, path, plugin);
	}

	public NekoConfig(AliCushions plugin, String path) {
		this(path, path, plugin);
	}

	public NekoConfig(String path, String savePath, AliCushions plugin){
		configFile = new File(path);
		config = YamlConfiguration.loadConfiguration(configFile);

		if(!configFile.exists()){
			try{
				plugin.saveResource(savePath, false);
				config = YamlConfiguration.loadConfiguration(configFile);
				hasDefault = true;
			}catch(Exception ignore){}
		}
	}

	public Object get(String path, Object def) {
		if(!config.contains(path) && def != null) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.get(path, def);
	}
	
	public String getString(String path, String def) {
		if(!config.contains(path) && def != null) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getString(path, def);
	}

	public int getInt(String path, int def) {
		if(!config.contains(path)) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getInt(path, def);
	}
	
	public double getDouble(String path, double def) {
		if(!config.contains(path)) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getDouble(path, def);
	}
	
	public List<String> getStringList(String path, List<String> def){
		if(!config.contains(path) && def != null) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getStringList(path);
	}
	
	public ConfigurationSection getConfigurationSection(String path) {
		return config.getConfigurationSection(path);
	}
	
	public Boolean getBoolean(String path, Boolean def) {
		if(!config.contains(path) && def != null) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getBoolean(path, def);
	}
	
	public Object getObject(String path, Class<?> clazz) {
		return config.getObject(path, clazz);
	}
	
	public String getString(String path) {
		return config.getString(path);
	}
		
	public int getInt(String path) {
		return config.getInt(path);
	}
	
	public double getDouble(String path) {
		return config.getDouble(path);
	}
	
	public Boolean getBoolean(String path) {
		return config.getBoolean(path);
	}
	
	public List<String> getStringList(String path){
		return config.getStringList(path);
	}
	
	public void set(String path, Object object) {
		config.set(path, object);
	}
	
	public Boolean contains(String path) {
		return config.contains(path);
	}
	
	public void update() {
		if(needUpdate) {
			saveConfig();
			this.needUpdate = false;
		}
	}
	
	public void forceUpdate() {
		saveConfig();
		this.needUpdate = false;
	}
	
	public Boolean hasDefault() {
		return this.hasDefault;
	}
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public boolean exists() {
		return this.configFile.exists();
	}
	
	public void saveConfig() {
        try {
            this.config.save(this.configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void delete() {
		this.configFile.delete();
	}

	public String getPath() {
		return configFile.getPath();
	}

	public void setInlineComments(String path, List<String> comments){
		config.setInlineComments(path, comments);
		needUpdate = true;
	}

	public void setComments(String path, List<String> comments){
		config.setComments(path, comments);
		needUpdate = true;
	}

	public static File convertInputStreamToFile(InputStream inputStream, String fileName) throws IOException {
		File tempFile = File.createTempFile(fileName, null);

		try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
		}

		return tempFile;
	}
}
