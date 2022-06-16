package dev.arubik.iacs.managers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import dev.arubik.iacs.Crops.CropInstance;
import dev.lone.itemsadder.api.CustomBlock;


public class sprinklerManager {

	
	public static void addWatter(int radiusx ,int radiusy,int addedwater,Block center,Player p) {

		
		BoundingBox area = center.getBoundingBox();
		
		area = area.expand(radiusx, 0, radiusy,radiusx, 0, radiusy);
		
		for(int x = center.getLocation().clone().subtract(radiusx, 0, 0).getBlockX(); x < center.getLocation().clone().add(radiusx, 0, 0).getBlockX(); x++) {

			for(int y = center.getLocation().clone().subtract(0, radiusy, 0).getBlockY(); x < center.getLocation().clone().add(0, radiusy, 0).getBlockY(); x++) {
				
				Location blockrandom = new Location(center.getLocation().getWorld(), x, center.getLocation().getBlockY(), y);
				
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
