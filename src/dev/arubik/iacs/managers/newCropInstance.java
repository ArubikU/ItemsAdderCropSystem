package dev.arubik.iacs.managers;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;
import javax.swing.text.html.parser.Entity;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import dev.arubik.iacs.furnitureGet;
import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.events.ModifyMB;
import dev.arubik.iacs.events.ModifyMB.Operation;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;

public class newCropInstance {
	public static NamespacedKey grow_time =  NamespacedKey.fromString("iacroper:grow_time");
	
	public Location loc;
	public String name;
	public furnitureGet fg = new furnitureGet();
	public @Nullable CustomStack block;
	public @Nullable CustomFurniture furniture;

	public newCropInstance (Location loc,String block) {
		this.loc = loc;
		this.name = block;
		this.block = CustomFurniture.getInstance(block);
	}
	public newCropInstance (org.bukkit.entity.Entity e) {
		if(CustomFurniture.byAlreadySpawned(e) != null) {
			this.loc = CustomFurniture.byAlreadySpawned(e).getArmorstand().getLocation();
			this.name = CustomFurniture.byAlreadySpawned(e).getNamespacedID();
			this.block = CustomStack.getInstance(name);
		}
	}
	
	public void setFurniture() {
		this.furniture = fg.placeFurniture(name, loc, null);
	}
	
	public void takeTime(int delay) {
		FileConfiguration blockConfig = block.getConfig();
		
		if(furniture.getArmorstand().getPersistentDataContainer().get(grow_time,  PersistentDataType.INTEGER) == null) {
			furniture.getArmorstand().getPersistentDataContainer().set(grow_time,  PersistentDataType.INTEGER, blockConfig.getInt("items."+block.getId()+".plant.grow-time"));
		}else {

			furniture.getArmorstand().getPersistentDataContainer().set(grow_time,  PersistentDataType.INTEGER,
					furniture.getArmorstand().getPersistentDataContainer().get(grow_time,  PersistentDataType.INTEGER) - delay
					);
					
					Block farmblock = furniture.getArmorstand().getLocation().getBlock().getRelative(BlockFace.DOWN);
					
					if(CustomBlock.byAlreadyPlaced(farmblock) != null) {
							if(CropManager.contains(farmblock.getLocation())) {
							CropInstance ci = CropManager.getInstance(farmblock.getLocation());
							
							int takedmb = iacs.getPlugin().getConfig().getInt("config.water-take");
							
							if(blockConfig.getString("items."+block.getId()+".plant.water-take") != null) {
								takedmb = Integer.valueOf(blockConfig.getString("items."+block.getId()+".plant.water-take"));
							}
							
							ci.takeMB(takedmb, block);
						}
					}
					else if(farmblock.getBlockData() instanceof Farmland) {
						Farmland data = (Farmland) farmblock.getBlockData(); 
						
						if(data.getMoisture() >= 1) {
							
						}else { return;}
						
					}else { return;}
					
			if(furniture.getArmorstand().getPersistentDataContainer().get(grow_time,  PersistentDataType.INTEGER) - delay <= 0) {
				if(getNextStage() != null) {
					furniture.remove(false);
					furnitureGet.placeFurniture(getNextStage(), furniture.getArmorstand().getLocation(), null);
				}
			}
		
		}
	}
	
	@Nullable
	public String getNextStage() {
		String getNamespacedID = block.getNamespacedID();

		String base = getNamespacedID.replaceAll("_stage_[0-9]+", "");

		if(getNamespacedID.endsWith("_stage_1")) {
			if(CustomBlock.getInstance(base+"_stage_2") != null) {
                return base+"_stage_2";
            }
		}
        if(getNamespacedID.endsWith("_stage_2")) {
            if(CustomBlock.getInstance(base+"_stage_3") != null) {
                return base+"_stage_3";
            }
        }
        if(getNamespacedID.endsWith("_stage_3")) {
            if(CustomBlock.getInstance(base+"_stage_4") != null) {
                return base+"_stage_4";
            }
        }
        if(getNamespacedID.endsWith("_stage_4")) {
            if(CustomBlock.getInstance(base+"_stage_5") != null) {
                return base+"_stage_5";}
        }
        if(getNamespacedID.endsWith("_stage_5")) {
            if(CustomBlock.getInstance(base+"_stage_6") != null) {
                return base+"_stage_6";}
        }
        if(getNamespacedID.endsWith("_stage_6")){
            if(CustomBlock.getInstance(base+"_stage_7") != null) {
                return base+"_stage_7";}
        }
        if(getNamespacedID.endsWith("_stage_7")){
            if(CustomBlock.getInstance(base+"_stage_8") != null) {
                return base+"_stage_8";}
        }
        if(getNamespacedID.endsWith("_stage_8")){
            if(CustomBlock.getInstance(base+"_stage_9") != null) {
                return base+"_stage_9";}
        }
        if(getNamespacedID.endsWith("_stage_9")){
            if(CustomBlock.getInstance(base+"_stage_10") != null) {
                return base+"_stage_10";}
        }
        if(getNamespacedID.endsWith("_stage_10")){
            if(CustomBlock.getInstance(base+"_stage_11") != null) {
                return base+"_stage_11";}
        }
        if(getNamespacedID.endsWith("_stage_11")){
            if(CustomBlock.getInstance(base+"_stage_12") != null) {
                return base+"_stage_12";}
        }
        if(getNamespacedID.endsWith("_stage_12")){
            if(CustomBlock.getInstance(base+"_stage_13") != null) {
                return base+"_stage_13";}
        }
        if(getNamespacedID.endsWith("_stage_13")){
            if(CustomBlock.getInstance(base+"_stage_14") != null) {
                return base+"_stage_14";}
        }
		return null;
	}
	
}
