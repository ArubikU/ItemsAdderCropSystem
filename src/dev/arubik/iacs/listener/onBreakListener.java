package dev.arubik.iacs.listener;

import org.bukkit.event.Listener;

import dev.arubik.iacs.managers.CropManager;
import dev.lone.itemsadder.api.CustomBlock;

public class onBreakListener implements Listener{
	
	public void OnBlockBreak(dev.lone.itemsadder.api.Events.CustomBlockBreakEvent e) {
		if(CropManager.contains(e.getBlock().getLocation())){
				CustomBlock.remove(e.getBlock().getLocation());
				CropManager.removeInstance(e.getBlock().getLocation());
		}
	}
	
	
}
