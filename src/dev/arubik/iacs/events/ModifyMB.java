package dev.arubik.iacs.events;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import dev.arubik.iacs.Crops.CropInstance;

public class ModifyMB extends org.bukkit.event.block.BlockEvent implements Cancellable{

	
	Operation op;
	int amount;
	CropInstance ci;
	Player player;
	
	public Player getPlayer() {
		return player;
	}

	public Operation getOp() {
		return op;
	}

	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CropInstance getCropInstance() {
		return ci;
	}

	public ModifyMB(Block b, @Nullable Player who, Operation op, int amount, CropInstance ci) {
		super(b);
		this.player = who;
		this.op = op;
		this.amount = amount;
		this.ci = ci;

		Bukkit.getServer().getPluginManager().callEvent(this);
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void setCancelled(boolean arg0) {
		
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public enum Operation {
		ADDITION,
		REST,
		REST_SPRINKLER
	}

}
