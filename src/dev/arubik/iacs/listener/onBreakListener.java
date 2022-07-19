package dev.arubik.iacs.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.managers.CropManager;
import dev.lone.itemsadder.api.CustomBlock;

public class onBreakListener implements Listener{
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void OnBlockBreak(dev.lone.itemsadder.api.Events.CustomBlockBreakEvent e) {
		CustomBlock cb = CustomBlock.getInstance(e.getNamespacedID());
		final Block clone = e.getBlock();
		if(CropManager.contains(e.getBlock().getLocation())
				|| e.getNamespacedID().equalsIgnoreCase((String) iacs.getCfg("config.farming_station", "croper:farm"))
				|| e.getNamespacedID().equalsIgnoreCase((String) iacs.getCfg("config.water_farming_station", "croper:watered_farm"))) {
			
			if(CropManager.contains(e.getBlock().getLocation())) {
				if(CropManager.getInstance(e.getBlock().getLocation()).getAllFertilizer().getType() != Material.BARRIER) {
					e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), CropManager.getInstance(e.getBlock().getLocation()).getAllFertilizer());
				}
			}
			
			CropManager.removeInstance(e.getBlock().getLocation());
		}
		
		Location loc = e.getBlock().getLocation();
		
		if(e.getNamespacedID().contains("_stage_") && e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
			
			if(cb.getConfig().getBoolean("items."+cb.getId()+".allow-silk") == true) {
				return;
			}

			if(cb.getConfig().getString("items."+cb.getId()+".allow-silk") == null ||
					cb.getConfig().getBoolean("items."+cb.getId()+".allow-silk") == false) {
				
				try {
					iacs.log(CustomBlock.getLoot(cb.getBlock(), null, false));
					
					CustomBlock.getLoot(clone, null, false).forEach(st ->{
							ItemStack loot = (ItemStack) st;
							iacs.log(loot);
							loc.getWorld().dropItem(loc, loot);
						});
				}catch(NullPointerException ee) {
					iacs.log(ee);
				}
					CustomBlock.remove(e.getBlock().getLocation());
					e.setCancelled(true);
				}
			}
			
		
		
	}

	@EventHandler
	public void OnBreak(BlockBreakEvent e) {
		if(CropManager.contains(e.getBlock().getLocation())) {
		CropManager.removeInstance(e.getBlock().getLocation());
		}
	}
	
	
}
