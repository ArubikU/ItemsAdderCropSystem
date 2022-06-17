package dev.arubik.iacs.managers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.lone.itemsadder.api.CustomBlock;


public class sprinklerManager {

	
	public static void addWatter(int radiusx ,int radiusz,int addedwater,Block center,Player p) {

		if(radiusx <= 0 && radiusz <= 0) {

			if(CropManager.contains(center.getLocation())) {
				CropManager.getInstance(center.getLocation()).addMb(addedwater, p);
			}else if(CustomBlock.byAlreadyPlaced(center) != null) {
				if(CustomBlock.byAlreadyPlaced(center).getNamespacedID().equalsIgnoreCase(iacs.getPlugin().getConfig().getString("config.farming_station"))) {
					CropInstance ci = new CropInstance(center.getLocation(), 0);
					ci.addMb(addedwater, p);
					CropManager.putInstance(center.getLocation(), ci);
				}
			}
			
			return;
		}
		
		for(int x = center.getLocation().clone().subtract(radiusx, 0, 0).getBlockX(); x < center.getLocation().clone().add(radiusx, 0, 0).getBlockX(); x++) {

			for(int z = center.getLocation().clone().subtract(0, radiusz, 0).getBlockZ(); z < center.getLocation().clone().add(0, radiusz, 0).getBlockZ(); z++) {
				
				Location blockrandom = new Location(center.getLocation().getWorld(), x, center.getLocation().getBlockY(),z);
				
				if(CropManager.contains(blockrandom)) {
					CropManager.getInstance(blockrandom).addMb(addedwater, p);
				}else if(CustomBlock.byAlreadyPlaced(blockrandom.getBlock()) != null) {
					CropInstance ci = new CropInstance(blockrandom.getBlock().getLocation(), 0);
					ci.addMb(addedwater, p);
					CropManager.putInstance(blockrandom.getBlock().getLocation(), ci);
				}
				
			}
		}
		
	}
	
}
