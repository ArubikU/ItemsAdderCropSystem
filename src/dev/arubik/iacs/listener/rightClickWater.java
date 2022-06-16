package dev.arubik.iacs.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.skills.SkillEvent;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.utils.config.LineConfig;

public class rightClickWater implements Listener{
	public rightClickWater() {
		
	}
	
	@EventHandler
	public void onwater(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
		    List<Block> lineOfSight = event.getPlayer().getLineOfSight(null, 5);
		    Boolean truuee = false;
		    for (Block b : lineOfSight) {
		        if (b.getType() == Material.LEGACY_STATIONARY_WATER || b.getType() == Material.WATER) {
		        	truuee = true;
		        }
		    }
		    if(truuee) {
		    	eventListenerClass.tag("~RightClickWater").forEach(data ->{
		    		if(event.getClickedBlock() != null) {
		    			new SkillEvent(event.getClickedBlock().getLocation(), new LineConfig(data), event.getPlayer());
		    		}else {
						new SkillEvent(event.getPlayer().getLocation(), new LineConfig(data), event.getPlayer());
		    		}
				});
		    }
	}
	}
}
