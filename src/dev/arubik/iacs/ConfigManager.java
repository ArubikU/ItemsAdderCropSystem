package dev.arubik.iacs;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager extends JavaPlugin{
	public static FileConfiguration config = null;
	public static FileConfiguration plants = null;
	public static FileConfiguration fertilizer = null;
	public static File f_config = null;
	public static File f_plants = null;
	public static File f_fertilizer = null;
	
	public FileConfiguration getConfig() {
		if(this.config == null)this.reloadConfig();
		return this.config;
	}
	public void registrarConfig() {
		f_config = new File(this.getDataFolder(), "config.yml");
		if (!f_config.exists()) {this.getConfig().options().copyDefaults(true);saveConfig();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f_config);
		FileConfiguration data = (FileConfiguration) s;
		this.config = data;
	}
    public void reloadConfig() {
        if (this.config == null)
            this.f_config = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(this.f_config);
        InputStream rosource = getResource("config.yml");
        if(rosource == null) return;
        Reader defConfigStream = new InputStreamReader(rosource, StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        this.config.setDefaults(defConfig);
    }
	public FileConfiguration getPlants() {
		if(this.config == null)this.reloadConfig();
		return this.config;
	}
    public void registrarPlants() {
		File config = new File(this.getDataFolder(), "plants.yml");
		if (!config.exists()) {this.getConfig().options().copyDefaults(true);saveConfig();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(config);
		FileConfiguration data = (FileConfiguration) s;
		this.plants = data;
	}
    public void reloadPlants() {
        if (this.plants == null)
            this.f_plants = new File(getDataFolder(), "plants.yml");
        this.plants = YamlConfiguration.loadConfiguration(this.f_plants);
        InputStream rosource = getResource("plants.yml");
        if(rosource == null) return;
        Reader defplantsStream = new InputStreamReader(rosource, StandardCharsets.UTF_8);
        YamlConfiguration defplants = YamlConfiguration.loadConfiguration(defplantsStream);
        this.plants.setDefaults(defplants);
    }
	public FileConfiguration getFertilizer() {
		if(this.config == null)this.reloadConfig();
		return this.config;
	}
	public void registrarFertilizer() {
		File config = new File(this.getDataFolder(), "fertilizer.yml");
		if (!config.exists()) {this.getConfig().options().copyDefaults(true);saveConfig();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(config);
		FileConfiguration data = (FileConfiguration) s;
		this.fertilizer = data;
	}
    public void reloadFertilizer() {
        if (this.fertilizer == null)
            this.f_fertilizer = new File(getDataFolder(), "fertilizer.yml");
        this.fertilizer = YamlConfiguration.loadConfiguration(this.f_fertilizer);
        InputStream rosource = getResource("fertilizer.yml");
        if(rosource == null) return;
        Reader defplantsStream = new InputStreamReader(rosource, StandardCharsets.UTF_8);
        YamlConfiguration defplants = YamlConfiguration.loadConfiguration(defplantsStream);
        this.fertilizer.setDefaults(defplants);
    }
}
