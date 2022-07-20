package dev.arubik.iacs.managers;

import java.io.File;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import dev.arubik.iacs.iacs;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.lib.api.item.NBTItem;

public class FertilizerManager {


	public static int fertilizerTimeout(String name,int timeout) {
		String b = name.split(" ~ ")[0];
		int a = timeout + Integer.valueOf(iacs.getCfgFile(b+".discount-time", 0, "fertilizer.yml").toString());
		return a;
	}

	public static int fertilizerRetain(String name,int retain) {
		String b = name.split(" ~ ")[0];
		int a = Integer.valueOf(iacs.getCfgFile(b+".discount-water", 0, "fertilizer.yml").toString());
		return  a;
	}

	public static int fertilizerGold(String name,int retain) {
		String b = name.split(" ~ ")[0];
		int a = Integer.valueOf(iacs.getCfgFile(b+".extra-luck", 0, "fertilizer.yml").toString());
		return retain + a;
	}
	
	public static String display(String name) {
		String b = name.split(" ~ ")[0];
		return iacs.getCfgFile(b+".display-name", b, "fertilizer.yml").toString();
	}
	
//NBTItem.get(a).get("FERTILIZER")
	
	@Nullable
	public static ItemStack fertilizer(String id) {
		File f = new File(iacs.getPlugin().getDataFolder(), "fertilizer.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		if(data.getString(id+".itemsadder") == null) return null;
		
		if(CustomStack.getInstance(data.getString(id+".itemsadder")) != null) {
			return CustomStack.getInstance(data.getString(id+".itemsadder")).getItemStack();
		}else {
			return null;
		}
	}
	
	@Nullable
	public static String getIdPerId(String id) {
		File f = new File(iacs.getPlugin().getDataFolder(), "fertilizer.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		for(String a  : data.getKeys(false)) {
			if(data.getString(a+".itemsadder") == null) continue;
			if(CustomStack.getInstance(data.getString(a+".itemsadder")) != null) {
				if(data.getString(a+".itemsadder").equalsIgnoreCase(id)) {
					return a;
				}
			}
		}
		return null;
	}
	
	@Nullable
	public static String getIdPerStack(ItemStack id) {
		File f = new File(iacs.getPlugin().getDataFolder(), "fertilizer.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		for(String a  : data.getKeys(false)) {
			if(data.getString(a+".itemsadder") == null) continue;
			if(CustomStack.byItemStack(id) == null) continue;
			if(CustomStack.getInstance(data.getString(a+".itemsadder")) != null) {
				if(data.getString(a+".itemsadder").equalsIgnoreCase(CustomStack.byItemStack(id).getNamespacedID())) {
					return a;
				}
			}
		}
		return null;
	}
	
}
