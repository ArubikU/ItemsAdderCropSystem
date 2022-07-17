package dev.arubik.iacs.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;

public class InventoryManager {
	@SuppressWarnings("unchecked")
	public void openFarmInventory(Player player,Location farm) {
		CropInstance ci = CropManager.getInstance(farm);
		List<String> inv = (List<String>)iacs.getCfgFile("inv", new ArrayList<String>(), "inventory.yml");
		String name = iacs.getCfgFile("name", new ArrayList<String>(), "inventory.yml").toString();
		
		
		
		name = dev.lone.itemsadder.api.FontImages.FontImageWrapper.replaceFontImages(name);
		Inventory in = Bukkit.createInventory(player, inv.size(), name);
		
	}
}
