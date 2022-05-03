package dev.arubik.iacs.skills;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.managers.CropManager;
import dev.lone.itemsadder.api.CustomBlock;
import io.lumine.mythic.utils.config.LineConfig;

public class SkillEvent extends org.bukkit.event.Event implements Cancellable{

	public Location loc;
	public LineConfig lc;
	public Player p;
	
	public SkillEvent(Location loc, LineConfig lc,@Nullable Player p) {
		super(true);
		this.loc = loc;
		this.lc = lc;
		this.p = p;
		

		Bukkit.getScheduler().runTaskAsynchronously(iacs.getPlugin(), () -> Bukkit.getServer().getPluginManager().callEvent(this));
		

		if(p != null) {
			if(lc.getString("shifting", "false") != "false") {
				if(p.isSneaking() == false) {
					this.setCancelled(true);
				}
			}
			if(lc.getString("gamemode", "false") != "false") {
				if(p.getGameMode().toString().equalsIgnoreCase(lc.getString("gamemode", "false")) == false) {
					this.setCancelled(true);
				}
			}
			if(lc.getString("VanillaBlock", "false") != "false") {
				if(loc.getWorld().getBlockAt(loc).getType().toString().equalsIgnoreCase(lc.getString("VanillaBlock", "false")) == false){

					this.setCancelled(true);
				}
			}
			if(CustomBlock.byAlreadyPlaced(loc.getWorld().getBlockAt(loc)) != null) {
				if(lc.getString("CustomBlock", "false") != "false") {
					CustomBlock cb = CustomBlock.byAlreadyPlaced(loc.getWorld().getBlockAt(loc));
					if(cb.getNamespacedID().equalsIgnoreCase(lc.getString("CustomBlock", "false")) == false){

						this.setCancelled(true);
					}
					if(CropManager.getInstance(loc) != null){
						CropInstance ci = CropManager.getInstance(loc);
						
						if(lc.getInteger("MB>",0) != 0){
							if(ci.getMb() < lc.getInteger("MB>",0)) {
								this.setCancelled(true);
							}
						}
						if(lc.getInteger("MB<",0) != 0){
							if(ci.getMb() > lc.getInteger("MB<",0)) {
								this.setCancelled(true);
							}
						}
						if(lc.getInteger("MB_EQ",0) != 0){
							if(ci.getMb() != lc.getInteger("MB_EQ=",0)) {
								this.setCancelled(true);
							}
						}
					}
				}
			}

			if(lc.getString("placeholder", "false") != "false") {
				if(p != null) {
					if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!= null) {
						
					}
				}
			}
		}
		
		
	}
	
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public LineConfig getLc() {
		return lc;
	}

	public void setLc(LineConfig lc) {
		this.lc = lc;
	}

	public Player getPlayer() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}


	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCancelled(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

}
