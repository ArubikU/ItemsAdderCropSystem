package dev.arubik.iacs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class listenerProtocol {
	
	static iacs plugin = iacs.getPlugin();
	

	public static PacketAdapter RECIPE_UPDATE = new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.RECIPE_UPDATE) {
	    
		@Override
		public void onPacketSending(PacketEvent event) {
			if(iacs.getCfg("config.debug", false).toString().equalsIgnoreCase("TRUE")) {
				iacs.log("RECIPE_UPDATE : CANCELLED");
			}
			
			
	    	event.setCancelled(true);
	    }
		
		@Override
	    public void onPacketReceiving(PacketEvent event) {
			if(iacs.getCfg("config.debug", false).toString().equalsIgnoreCase("TRUE")) {
				iacs.log("RECIPE_UPDATE : CANCELLED");
			}
	    	event.setCancelled(true);
	    }
	};
	
	public static PacketAdapter AUTO_RECIPE = new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.AUTO_RECIPE) {
	    

		@Override
		public void onPacketSending(PacketEvent event) {
			if(iacs.getCfg("config.debug", false).toString().equalsIgnoreCase("TRUE")) {
				iacs.log("AUTO_RECIPE : CANCELLED");
			}
			
	    	event.setCancelled(true);
	    }
		
		@Override
	    public void onPacketReceiving(PacketEvent event) {
	    	event.setCancelled(true);
	    }
	};
	public static PacketAdapter RECIPES = new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.RECIPES) {
	    

		@Override
		public void onPacketSending(PacketEvent event) {
			if(iacs.getCfg("config.debug", false).toString().equalsIgnoreCase("TRUE")) {
				iacs.log("RECIPES : CANCELLED");
			}
	    	event.setCancelled(true);
	    }
		
		@Override
	    public void onPacketReceiving(PacketEvent event) {
	    	event.setCancelled(true);
	    }
	};
	
	public static void onEnable() {


		if(iacs.getCfg("config.disable-recipes", false).toString().equalsIgnoreCase("TRUE")) {
			iacs.MiniMessage("<rainbow>[IACROPER] Paquete de Recetas Deshabilirado</rainbow>", Bukkit.getConsoleSender(), 0);
			
			ProtocolManager manager = ProtocolLibrary.getProtocolManager();
			if(iacs.getCfg("config.disable-recipes-1", false).toString().equalsIgnoreCase("TRUE")) {
				manager.addPacketListener(RECIPE_UPDATE);
			}
			if(iacs.getCfg("config.disable-recipes-2", false).toString().equalsIgnoreCase("TRUE")) {
			manager.addPacketListener(AUTO_RECIPE);
			}
			if(iacs.getCfg("config.disable-recipes-3", false).toString().equalsIgnoreCase("TRUE")) {
			manager.addPacketListener(RECIPES);
			}
		}
	}
	
	public static void onDisable() {


		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		
		if(manager.getPacketListeners().contains(RECIPE_UPDATE)) {
			manager.removePacketListener(RECIPE_UPDATE);
		}
		if(manager.getPacketListeners().contains(AUTO_RECIPE)) {
			manager.removePacketListener(AUTO_RECIPE);
		}
		if(manager.getPacketListeners().contains(RECIPES)) {
			manager.removePacketListener(RECIPES);
		}
	}
}
