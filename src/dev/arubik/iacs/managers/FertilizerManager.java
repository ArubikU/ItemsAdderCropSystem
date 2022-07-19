package dev.arubik.iacs.managers;

import java.io.File;

import javax.annotation.Nullable;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import dev.arubik.iacs.iacs;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.lib.api.item.NBTItem;

public class FertilizerManager {


	public static int fertilizerTimeout(String name,int timeout) {
		String b = name.split(" ~ ")[0];
		int a = Integer.valueOf(iacs.getCfgFile(b+".discount-time", 0, "fertilizer.yml").toString());
		return Integer.valueOf((timeout+((timeout/100)*a)));
	}

	public static int fertilizerRetain(String name,int retain) {
		String b = name.split(" ~ ")[0];
		int a = Integer.valueOf(iacs.getCfgFile(b+".discount-water", 0, "fertilizer.yml").toString());
		return Integer.valueOf((((retain/100)*a)));
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
		if(CustomStack.getInstance(iacs.getCfgFile(id+".itemsadder", 0, "fertilizer.yml").toString()) != null) {
			return CustomStack.getInstance(iacs.getCfgFile(id+".itemsadder", 0, "fertilizer.yml").toString()).getItemStack();
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
			if(CustomStack.getInstance(iacs.getCfgFile(a+".itemsadder", 0, "fertilizer.yml").toString()) != null) {
				if(CustomStack.getInstance(iacs.getCfgFile(a+".itemsadder", 0, "fertilizer.yml").toString()).getNamespacedID().equalsIgnoreCase(id)) {
					return a;
				}
			}else {
				return null;
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
			if(CustomStack.getInstance(iacs.getCfgFile(a+".itemsadder", 0, "fertilizer.yml").toString()) != null) {
				if(iacs.getCfgFile(a+".itemsadder", 0, "fertilizer.yml").toString().equalsIgnoreCase(CustomStack.byItemStack(id).getNamespacedID())) {
					return a;
				}
			}else {
				return null;
			}
		}
		return null;
	}
	
}
