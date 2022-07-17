package dev.arubik.iacs;


import java.util.Collection;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;

import dev.lone.itemsadder.api.CustomFurniture;

public class furnitureGet {
	
	@Nullable
	public static CustomFurniture placeFurniture(String name, Location location, @Nullable BlockFace face) {
		if(face != null) {
			Location blockc = location.getBlock().getRelative(face).getLocation();
			CustomFurniture cf = CustomFurniture.spawnPreciseNonSolid(name, blockc);
			
			return cf;
		}else {
			Location blockc = location.getBlock().getLocation();
			CustomFurniture cf = CustomFurniture.spawnPreciseNonSolid(name, blockc);
			return cf;
		}
	}

	@Nullable
	public static CustomFurniture fromLocalization(Location location) {
		return CustomFurniture.byAlreadySpawned(furnitureGet.getFromLocation(location, null));
	}
	
	@Nullable
	public static Entity getFromLocation(Location location,@Nullable BlockFace face) {
		Entity ea = null;
		if(face != null) {
			Location blockc = location.getBlock().getRelative(face).getLocation();
			BoundingBox bb = new BoundingBox();
			bb = BoundingBox.of(blockc.getBlock());
			for(Entity instance : furnitureGet.getFromBoundingBox(bb,blockc.getWorld())) {
				if(instance instanceof Entity) {
					if(CustomFurniture.byAlreadySpawned((Entity) instance) != null){
						ea = (Entity) instance;
					}
				}
			}
		}else {
			Location blockc = location.getBlock().getRelative(BlockFace.UP).getLocation();
			BoundingBox bb = new BoundingBox();
			bb = BoundingBox.of(blockc.getBlock());
			for(Entity instance : furnitureGet.getFromBoundingBox(bb,blockc.getWorld())) {
				if(instance instanceof Entity) {
					if(CustomFurniture.byAlreadySpawned((Entity) instance) != null){
						ea = (Entity) instance;
					}
				}
			}
		}
		return ea;
	}
	
	public static Collection<Entity> getFromBoundingBox(BoundingBox bb, World w){
		return w.getNearbyEntities(bb);
	}
	
}
