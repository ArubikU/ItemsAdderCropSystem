package dev.arubik.iacs.Crops;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.events.ModifyMB;
import dev.arubik.iacs.events.ModifyMB.Operation;
import dev.arubik.iacs.managers.CropManager;
import dev.arubik.iacs.managers.LocSave;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;

public class CropInstance {
	public Location loc;
	
	public Location seedloc;
	
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public String getCurrentseed() {
		return currentseed;
	}

	public void setCurrentseed(String currentseed) {
		this.currentseed = currentseed;
	}

	public int getMb() {
		return mb;
	}

	public void setMb(int mb) {
		this.mb = mb;
	}

	public int getBonemeal() {
		return bonemeal;
	}

	public void setBonemeal(int bonemeal) {
		this.bonemeal = bonemeal;
	}

	public String currentseed;
	public int mb;
	public int bonemeal;

	public CropInstance(ConfigurationSection conf) {
		this.bonemeal = conf.getInt("bonemeal");
		this.loc = new Location(Bukkit.getWorld(conf.getString("loc.world"))
				, conf.getInt("loc.x")
				, conf.getInt("loc.y")
				, conf.getInt("loc.z"));
		this.mb = conf.getInt("mb");
	}
	
	public CropInstance(String s) {
		String[] splited = s.split("~|~");
		LocSave dasa = LocSave.getFromString(splited[0]);
		this.loc = new Location(dasa.world, dasa.x ,dasa.y,dasa.z);
		this.currentseed = splited[3];
		this.mb = Integer.valueOf(splited[1]);
		this.bonemeal = Integer.valueOf(splited[2]);
	}
	
	public String getAsString() {
		return new LocSave(loc).getAsString() + "~|~" + mb + "~|~" + bonemeal + "~|~" + currentseed;
	}
	
	
	public CropInstance(Location loc, int mb) {
		this.loc = loc;
		this.seedloc = loc.clone().add(0, 1, 0);
		this.mb = mb;
		CropManager.putInstance(loc, this);
	}

	public void addbonemeal(int added) {
		this.bonemeal += added;
	}

	public void takebonemeal(int added) {
		this.bonemeal -= added;
		if(this.bonemeal < 0) {
			this.bonemeal = 0;
		}
	}

	public void addMb(int addedd, Player p) {
		

		CropInstance thiss = this;
		
		//new ModifyMB(loc.getBlock(),p, Operation.ADDITION, added, this)
		Bukkit.getScheduler().runTaskAsynchronously(iacs.getPlugin(), new Runnable() {
			

		@Override
		public void run() {
			int added = addedd;
			ModifyMB mmb = new ModifyMB(loc.getBlock(),p, Operation.ADDITION, added, thiss);
			
			iacs.sendBlock(CustomBlock.getInstance(iacs.getPlugin().getConfig().getString("config.water_farming_station")), loc, 100);
			
			if(mmb.isCancelled()) {
				return;
			}
			added = mmb.getAmount();

			mb += added;
			
		
			
			if(mb >= iacs.getPlugin().getConfig().getInt("config.max-water")) {
				
				mb = iacs.getPlugin().getConfig().getInt("config.max-water");
				
				iacs.MiniMessage(iacs.parsePlaceholder(p, loc, iacs.getPlugin().getConfig().getString("config.max-water-error")), p , 0);
			}
			
			CropManager.putInstance(loc.getWorld().getBlockAt(loc).getLocation(), thiss);
		}
				
		
		});
		
		
	}
	
	public void modMB(int rest) {
		CropInstance thiss = this;
		

		if(mb + rest<=0) {
			CropManager.removeInstance(loc.getWorld().getBlockAt(loc.clone().subtract(0, 0, 0)).getLocation());
			CustomBlock.remove(loc.getWorld().getBlockAt(loc.clone().subtract(0, 0, 0)).getLocation() );
			

			iacs.sendBlock(CustomBlock.getInstance((String) iacs.getCfg("config.farming_station", "croper:farm")), loc, 150);
			
		}else 
		if(mb + rest> iacs.getPlugin().getConfig().getInt("config.max-water")) {
			mb = iacs.getPlugin().getConfig().getInt("config.max-water");
			CropManager.putInstance(loc.getWorld().getBlockAt(loc).getLocation(), this);
		}
		else {
		mb += rest;
		CropManager.putInstance(thiss.getLoc(), thiss);
		}
	}
	
	public void takeMB(int temp,CustomBlock discordo) {
		
		CropInstance thiss = this;

        int datat = temp;
        

		String base = discordo.getNamespacedID().replaceAll("_stage_[0-9]+", "");

        if(CustomStack.getInstance(base) != null) {
            CustomStack cs = CustomStack.getInstance(base);
            FileConfiguration datastorage = cs.getConfig();
            
            if(datastorage.getString("items."+cs.getId() + ".plant.extra-water") != null) {
            	datat += Integer.valueOf(datastorage.getString("items."+cs.getId() + ".plant.extra-water"));
            }
        }
        
        int data = datat;
       
			int rest = data;
			Operation op = Operation.REST;
			for(int d = -2; d <= 2; d++) {
				for(int e = -2; e <= 2; e++) {
					Location sprinkler = loc.clone();
					sprinkler.setX(loc.clone().getBlockX() + d);
					sprinkler.setZ(loc.clone().getBlockZ() + e);
					if(CustomBlock.byAlreadyPlaced(sprinkler.getWorld().getBlockAt(sprinkler)) != null) {
						if(CustomBlock.byAlreadyPlaced(sprinkler.getWorld().getBlockAt(sprinkler)).getNamespacedID().equalsIgnoreCase(
								iacs.getPlugin().getConfig().getString("config.sprinkler"))) {
							if(sprinkler.getWorld().getBlockAt(sprinkler).getBlockData() instanceof Waterlogged ) {
								Waterlogged wl = (Waterlogged) sprinkler.getWorld().getBlockAt(sprinkler).getBlockData();
								if(wl.isWaterlogged()) {
									op = Operation.REST_SPRINKLER;
									rest = 0;
								}
							}else if(sprinkler.getWorld().getBlockAt(sprinkler).getType().equals(Material.WATER)
									|| sprinkler.getWorld().getBlockAt(sprinkler).getType().equals(Material.WATER_CAULDRON)) {
								op = Operation.REST_SPRINKLER;
								rest = 0;
							}
						}
					}
					sprinkler.setY(sprinkler.getBlockY() + 1);
					if(CustomBlock.byAlreadyPlaced(sprinkler.getWorld().getBlockAt(sprinkler)) != null) {
						if(CustomBlock.byAlreadyPlaced(sprinkler.getWorld().getBlockAt(sprinkler)).getNamespacedID().equalsIgnoreCase(
								iacs.getPlugin().getConfig().getString("config.sprinkler"))) {
							if(sprinkler.getWorld().getBlockAt(sprinkler).getBlockData() instanceof Waterlogged ) {
	
								Waterlogged wl = (Waterlogged) sprinkler.getWorld().getBlockAt(sprinkler).getBlockData();
								if(wl.isWaterlogged()) {
									op = Operation.REST_SPRINKLER;
									rest = 0;
								}
							}else if(sprinkler.getWorld().getBlockAt(sprinkler).getType().equals(Material.WATER)
									|| sprinkler.getWorld().getBlockAt(sprinkler).getType().equals(Material.WATER_CAULDRON)) {
								op = Operation.REST_SPRINKLER;
								rest = 0;
							}
						}
					}
					
				}
			}
			
			try {

				ModifyMB mmb = iacs.castModifyMB(loc.getBlock(), null, op, rest, this);
				if(mmb.isCancelled()) {
					return;
				}
		
				rest = mmb.getAmount();
				
	
				mb -= rest;


			}catch(Exception e){
				iacs.MiniMessage(" <red>ERROR</red> <gradient:red:white>[" + e.toString() + "]</gradient>", Bukkit.getConsoleSender(), rest);
			}

			
			if(mb <=0) {
				CropManager.removeInstanceW(loc);
				
				iacs.sendBlock(CustomBlock.getInstance((String) iacs.getCfg("farming_station", "croper:farm")), loc, 100);
	
			}else {
				CropManager.putInstance(this.getLoc(), this);
			}
		}

	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	public void addSeed(CustomBlock cb) {
		String base = cb.getNamespacedID().replaceAll("_stage_[0-9]+", "");
		if(!cb.getNamespacedID().endsWith("_stage_1")) {
			int baselevel = 0;
			if(this.getCurrentseed() != null) {
			if(this.getCurrentseed().endsWith("_stage_2")) baselevel = 2;
            if(this.getCurrentseed().endsWith("_stage_3")) baselevel = 3;
            if(this.getCurrentseed().endsWith("_stage_4")) baselevel = 4;
            if(this.getCurrentseed().endsWith("_stage_5")) baselevel = 5;
            if(this.getCurrentseed().endsWith("_stage_6")) baselevel = 6;
            if(this.getCurrentseed().endsWith("_stage_7")) baselevel = 7;
            if(this.getCurrentseed().endsWith("_stage_8")) baselevel = 8;
            if(this.getCurrentseed().endsWith("_stage_9")) baselevel = 9;
            if(this.getCurrentseed().endsWith("_stage_10")) baselevel = 10;
            if(this.getCurrentseed().endsWith("_stage_11")) baselevel = 11;
            if(this.getCurrentseed().endsWith("_stage_12")) baselevel = 12;
            if(this.getCurrentseed().endsWith("_stage_13")) baselevel = 13;
			}else {baselevel = 0;}
 			
            if(baselevel != 0){
                if(CustomStack.getInstance(base) != null) {
                    CustomStack cs = CustomStack.getInstance(base);
                    FileConfiguration data = cs.getConfig();
                    
                    if(data.getString("items."+cs.getId()+".plant.stages-grow") != null) {
                        String stages = data.getString("items."+cs.getId()+".plant.stages-grow");
                        
                        if(stages.contains("-") && stages.split("-").length == 2) {
                            int inicial = Integer.valueOf(stages.split("-")[0]);
                            int end = Integer.valueOf(stages.split("-")[1]);
                            
                            int rand = getRandomNumber(inicial,end) + baselevel ;
                            
                            if(CustomBlock.getInstance(base+"_stage_"+rand) != null) {
                                this.currentseed = CustomBlock.getInstance(base+"_stage_"+rand).getNamespacedID();
                    			iacs.sendBlock(CustomBlock.getInstance(base+"_stage_"+rand), seedloc, 100);
                            }
                        }else {
                            
                            int rand = Integer.valueOf(data.getString("items."+cs.getId()+".plant.stages-grow")) + baselevel;

                            if(CustomBlock.getInstance(base+"_stage_"+rand) != null) {
                                this.currentseed = CustomBlock.getInstance(base+"_stage_"+rand).getNamespacedID();
                    			iacs.sendBlock(CustomBlock.getInstance(base+"_stage_"+rand), seedloc, 100);
                            }
                        }
                    }
                }
            }
		}

		this.currentseed =  cb.getNamespacedID();
		
		CropInstance inthis = this;
		inthis.currentseed =  cb.getNamespacedID();

		if(iacs.getCfg("config.async-not-safe", false).toString().equalsIgnoreCase("TRUE")) {

	        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.runTaskAsynchronously(iacs.getPlugin(), new Runnable() {
				@Override
				public void run() {

					iacs.sendBlock(cb, seedloc, 150);
				
				loc.getWorld().playEffect(seedloc, Effect.BONE_MEAL_USE, 5);
				CropManager.putInstance(loc.getWorld().getBlockAt(loc).getLocation(), inthis);
				}
			});
		}
		else {
			iacs.sendBlock(cb, seedloc, 150);
		loc.getWorld().playEffect(seedloc, Effect.BONE_MEAL_USE, 5);
		CropManager.putInstance(loc.getWorld().getBlockAt(loc).getLocation(), this);
		}

	}
}
