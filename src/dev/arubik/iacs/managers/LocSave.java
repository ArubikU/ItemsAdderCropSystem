package dev.arubik.iacs.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocSave {
	public int x;
	public int y;
	public int z;
	public World world;

	public LocSave(Location loc) {
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.world = loc.getWorld();
	}
	
	public String getAsString() {
		return x + "~" + y + "~" + z + "~" + world.getName();
	}
	
	public static LocSave getFromString(String s) {
		String[] a = s.split("~");
		
		Bukkit.getConsoleSender().sendMessage(a + "");
		return new LocSave(new Location(Bukkit.getWorld(a[3]), Integer.valueOf(a[0]), Integer.valueOf(a[1]), Integer.valueOf(a[2])));
	}
	
	public Location getLoc() {
		return new Location(world,x,y,z);
	}
}
