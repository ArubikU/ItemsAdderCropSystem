package dev.arubik.iacs.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryManager {

	
	public void OpenInventory(Inventory inv, Player p,String name,int id) {
		
		InventoryHolder holder = new InvHolder(id);
		
		Inventory invs = Bukkit.createInventory(holder, inv.getSize(), name);
		invs.getHolder().getClass();
	}
	
	public class InvHolder implements InventoryHolder {

		public int id;
		
		public Inventory Inventory;
		
		public InvHolder(int id) {
			this.id = id;
		}

		public void setInventory(Inventory inv) {
			this.Inventory = inv;
		}


		@Override
		public org.bukkit.inventory.Inventory getInventory() {
			// TODO Auto-generated method stub
			return null;
		}

		
		
	}
	
}
