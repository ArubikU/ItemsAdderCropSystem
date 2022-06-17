package dev.arubik.iacs.skills;

import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.managers.CropManager;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.utils.config.LineConfig;
import me.clip.placeholderapi.PlaceholderAPI;

public class SkillEvent extends org.bukkit.event.Event implements Cancellable {

	public Location loc;
	public LineConfig lc;

	String user = "%%__USER__%%";
	public @Nullable Player p;
	public @Nullable ItemStack involucred;
	public boolean canceled = false;
	public boolean upperevent = false;

	public SkillEvent(Location loc, LineConfig lc, @Nullable Player p, @Nullable ItemStack involucred) {
		super(true);
		this.loc = loc;
		this.lc = lc;
		this.p = p;
		this.involucred = involucred;
		
		
		if (p != null) {
			
			if (lc.getString("mainhand", "false") != "false") {
				if (CustomStack.byItemStack(p.getInventory().getItemInMainHand()) != null) {
					if (!CustomStack.byItemStack(p.getInventory().getItemInMainHand()).getNamespacedID()
							.equalsIgnoreCase(lc.getString("mainhand", "false"))) {
						canceled = true;
					}
				} else {
					if (!p.getInventory().getItemInMainHand().getType().toString()
							.equalsIgnoreCase(lc.getString("mainhand", "false"))) {
						canceled = true;
					}
				}
			}

			if (lc.getString("involucred", "false") != "false") {
				if (CustomStack.byItemStack(involucred) != null) {
					if (!CustomStack.byItemStack(involucred).getNamespacedID()
							.equalsIgnoreCase(lc.getString("involucred", "false"))) {
						canceled = true;
					}
				} else {
					if (!involucred.getType().toString().equalsIgnoreCase(lc.getString("involucred", "false"))) {
						canceled = true;
					}
				}
			}

			if (lc.getString("shifting", "false") != "false") {
				if (p.isSneaking() == false) {
					canceled = true;
				}
			}
			if (lc.getString("gamemode", "false") != "false") {
				if (p.getGameMode().toString().equalsIgnoreCase(lc.getString("gamemode", "false")) == false) {
					canceled = true;
				}
			}
			if (lc.getString("VanillaBlock", "false") != "false") {
				if (loc.getWorld().getBlockAt(loc).getType().toString()
						.equalsIgnoreCase(lc.getString("VanillaBlock", "false")) == false) {
					canceled = true;
				}
			}


			if (CustomBlock.byAlreadyPlaced(loc.getWorld().getBlockAt(loc)) != null) {
				if (lc.getString("CustomBlock", "false") != "false") {
					CustomBlock cb = CustomBlock.byAlreadyPlaced(loc.getWorld().getBlockAt(loc));
					if (cb.getNamespacedID().equalsIgnoreCase(lc.getString("CustomBlock", "false")) == false) {

						canceled = true;
					}

					String user = "%%__USER__%%";
					if (CropManager.getInstance(loc) != null) {
						CropInstance ci = CropManager.getInstance(loc);

						if (lc.getInteger("MB>", 0) != 0) {
							if (ci.getMb() < lc.getInteger("MB>", 0)) {
								canceled = true;
							}
						}
						if (lc.getInteger("MB<", 0) != 0) {
							if (ci.getMb() > lc.getInteger("MB<", 0)) {
								canceled = true;
							}
						}
						if (lc.getInteger("MB_EQ", 0) != 0) {
							if (ci.getMb() != lc.getInteger("MB_EQ=", 0)) {
								canceled = true;
							}
						}
					}
				}
			}

			if (lc.getString("placeholder", "false") != "false" && lc.getString("placeholder-eq", "false") != "false") {
				if (p != null) {
					if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
						if (!PlaceholderAPI.setPlaceholders(p, lc.getString("placeholder", "false"))
								.equalsIgnoreCase(lc.getString("placeholder-eq", "false"))) {

							canceled = true;
						}
					}
				}
			}
		}

		// iacs.MiniMessage(canceled, Bukkit.getConsoleSender(), 0);
		if (canceled != true) {
			Bukkit.getScheduler().runTaskAsynchronously(iacs.getPlugin(),
					() -> Bukkit.getServer().getPluginManager().callEvent(this));
		} else {
		}

	}

	public SkillEvent(Location loc, LineConfig lc, @Nullable Player p) {
		super(true);
		this.loc = loc;
		this.lc = lc;
		this.p = p;

		if (p != null) {
			if (lc.getString("mainhand", "false") != "false") {
				if (CustomStack.byItemStack(p.getInventory().getItemInMainHand()) != null) {
					if (!CustomStack.byItemStack(p.getInventory().getItemInMainHand()).getNamespacedID()
							.equalsIgnoreCase(lc.getString("mainhand", "false"))) {
						canceled = true;
					}
				} else {
					if (!p.getInventory().getItemInMainHand().getType().toString()
							.equalsIgnoreCase(lc.getString("mainhand", "false"))) {
						canceled = true;
					}
				}
			}

			if (lc.getString("shifting", "false") != "false") {
				if (p.isSneaking() == false) {
					canceled = true;
				}
			}
			if (lc.getString("gamemode", "false") != "false") {
				if (p.getGameMode().toString().equalsIgnoreCase(lc.getString("gamemode", "false")) == false) {
					canceled = true;
				}
			}
			if (lc.getString("VanillaBlock", "false") != "false") {
				if (loc.getWorld().getBlockAt(loc).getType().toString()
						.equalsIgnoreCase(lc.getString("VanillaBlock", "false")) == false) {
					canceled = true;
				}
			}
			if (CustomBlock.byAlreadyPlaced(loc.getWorld().getBlockAt(loc)) != null) {
				if (lc.getString("CustomBlock", "false") != "false") {
					CustomBlock cb = CustomBlock.byAlreadyPlaced(loc.getWorld().getBlockAt(loc));
					if (cb.getNamespacedID().equalsIgnoreCase(lc.getString("CustomBlock", "false")) == false) {

						canceled = true;
					}

					String user = "%%__USER__%%";
					if (CropManager.getInstance(loc) != null) {
						CropInstance ci = CropManager.getInstance(loc);

						if (lc.getInteger("MB>", 0) != 0) {
							if (ci.getMb() < lc.getInteger("MB>", 0)) {
								canceled = true;
							}
						}
						if (lc.getInteger("MB<", 0) != 0) {
							if (ci.getMb() > lc.getInteger("MB<", 0)) {
								canceled = true;
							}
						}
						if (lc.getInteger("MB_EQ", 0) != 0) {
							if (ci.getMb() != lc.getInteger("MB_EQ=", 0)) {
								canceled = true;
							}
						}
					}
				}
			}

			if (lc.getString("placeholder", "false") != "false" && lc.getString("placeholder-eq", "false") != "false") {
				if (p != null) {
					if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
						if (!PlaceholderAPI.setPlaceholders(p, lc.getString("placeholder", "false"))
								.equalsIgnoreCase(lc.getString("placeholder-eq", "false"))) {

							canceled = true;
						}
					}
				}
			}
		}

		// iacs.MiniMessage(canceled, Bukkit.getConsoleSender(), 0);
		if (canceled != true) {
			Bukkit.getScheduler().runTaskAsynchronously(iacs.getPlugin(),
					() -> Bukkit.getServer().getPluginManager().callEvent(this));
		} else {
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
		return this.upperevent;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.upperevent = true;
	}

}
