package dev.arubik.iacs.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.lone.itemsadder.api.CustomBlock;

public class CropManager {

	public FileConfiguration conf;
	
	public static HashMap<Location, CropInstance> instances;
	
	public CropManager(@Nullable FileConfiguration conf) {
		
		this.conf = conf;
		
		if(conf != null) {
				if(conf.getString("instances") != null) {
					try {
						for(String world : conf.getConfigurationSection("instances").getKeys(false)) {
							

							instances = new HashMap<Location, CropInstance>();
							
							//Bukkit.getConsoleSender().sendMessage(world);
							
							for(String cord : conf.getConfigurationSection("instances."+world).getKeys(false)) {
								Location templ = new Location(Bukkit.getWorld(world),
										Integer.valueOf(cord.split("~")[0]),
										Integer.valueOf(cord.split("~")[1]),
										Integer.valueOf(cord.split("~")[2]));
								int mb = conf.getInt("instances."+world+"."+cord+".mb");
								
								CropInstance ci = new CropInstance(templ, mb);
								
								if(conf.getString("instances."+world+"."+cord+".seed") != null) {
									ci.setCurrentseed(conf.getString("instances."+world+"."+cord+".seed"));
								}
								
								if(mb <= 0) {
									continue;
								}
								
								instances.put(templ, ci);
							}
						}
						
					}catch(Exception e) {
						instances = new HashMap<Location, CropInstance>();
						e.printStackTrace();
						//Bukkit.getConsoleSender().sendMessage("[CROPIA] a ocurrido un error se a regenerado el archivo");
						
					}
				}else {
					instances = new HashMap<Location, CropInstance>();
				}
			}
		else {
			instances = new HashMap<Location, CropInstance>();
		}
			saveData();
	}
	
	public static void putInstance(Location location, CropInstance cop) {
		if(cop.getMb() <= 0) {
			return;
		}
		
		
		instances.put(location, cop);
		saveData();

	}
	
	public static void saveData() {
		File f = new File(iacs.getPlugin().getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		if(instances != null) {
		
		for(Location loc : instances.keySet()) {
			if(instances.get(loc) != null) {
			data.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".mb", instances.get(loc).getMb());
			data.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".seed", instances.get(loc).getCurrentseed());
			}
			}
		}else {
			instances = new HashMap<Location, CropInstance>();
			Bukkit.getConsoleSender().sendMessage("[CROPIA] a ocurrido un error se a regenerado el archivo");
		}
		try {
		
			data.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Nullable
	public static CropInstance getInstance(Location loc) {
		

		File f = new File(iacs.getPlugin().getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration conf = (FileConfiguration) s;
		
		int mb = conf.getInt("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".mb");
		
		

		CropInstance ci = new CropInstance(loc, mb);
		if(conf.getString("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".mb") == null || mb < 0) {
			ci.setMb(0);
		}
		

		if(conf.getString("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".seed") != null || mb < 0) {
			ci.setCurrentseed(conf.getString("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".seed"));
		}
		
		try {
			conf.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ci;
	}
	
	public static List<Location> getInstances(World world) {
		
		File f = new File(iacs.getPlugin().getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration conf = (FileConfiguration) s;
		
		if(!iacs.getPlugin().getConfig().getStringList("config.worlds").contains(world.getName())) {
			iacs.MiniMessage("<rainbow>[IACroper]</rainbow> <gray>El mundo de la instancias no esta habilitado en config", Bukkit.getConsoleSender(), 0);
		}
		
		List<Location> locs = new ArrayList<Location>();
		
		if(conf.getConfigurationSection("instances."+world.getName()) != null) {

			conf.getConfigurationSection("instances."+world.getName()).getKeys(false).forEach(key ->{
				try {
				String[] splited = key.split("~");
				locs.add(new Location(world, Integer.valueOf(splited[0]), Integer.valueOf(splited[1]), Integer.valueOf(splited[2]) ) );
				}catch(Exception e) {
					iacs.log(e);
				}
			});
			
			
		}
		return locs;
	}
	
	public HashMap<Location, CropInstance> getMap(){
		return instances;
	}
	
	public Object getObject() throws IOException {
		return instances;
	}
	
	@SuppressWarnings("unchecked")
	public static void removeInstance(Location loc) {
		try {
			instances.remove(loc);
			instances.remove(loc.clone().subtract(0, 1, 0));}
		catch (Exception e) {
			
		}

		if(CustomBlock.byAlreadyPlaced(loc.clone().add(0,1,0).getBlock()) != null) {
			if(CustomBlock.byAlreadyPlaced(loc.clone().add(0,1,0).getBlock()).getNamespacedID().contains("_stage_")) {
				CustomBlock.byAlreadyPlaced(loc.clone().add(0,1,0).getBlock()).getLoot(false).forEach(stack ->{
					loc.clone().add(0,1,0).getBlock().getLocation().getWorld().dropItem(loc.clone().add(0,1,0).getBlock().getLocation(),(ItemStack) stack);
					});
				CustomBlock.remove(loc.clone().add(0,1,0).getBlock().getLocation());
			}
			

		}
		
		File f = new File(iacs.getPlugin().getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration conf = (FileConfiguration) s;

		conf.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+ ".mb", null);
		conf.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".seed", null);
		conf.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ(), null);
		conf.set("instances."+loc.clone().subtract(0, 1, 0).getWorld().getName()+"."+loc.clone().subtract(0, 1, 0).getBlockX()+"~"+loc.clone().subtract(0, 1, 0).getBlockY()+"~"+loc.clone().subtract(0, 1, 0).getBlockZ()+ ".mb", null);
		conf.set("instances."+loc.clone().subtract(0, 1, 0).getWorld().getName()+"."+loc.clone().subtract(0, 1, 0).getBlockX()+"~"+loc.clone().subtract(0, 1, 0).getBlockY()+"~"+loc.clone().subtract(0, 1, 0).getBlockZ()+".seed", null);
		conf.set("instances."+loc.clone().subtract(0, 1, 0).getWorld().getName()+"."+loc.clone().subtract(0, 1, 0).getBlockX()+"~"+loc.clone().subtract(0, 1, 0).getBlockY()+"~"+loc.clone().subtract(0, 1, 0).getBlockZ(), null);
		
		
		try {
			conf.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeInstanceW(Location loc) {
			instances.remove(loc);
			instances.remove(loc.clone().subtract(0, 1, 0));
		File f = new File(iacs.getPlugin().getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration conf = (FileConfiguration) s;
		conf.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+ ".mb", null);
		conf.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".seed", null);
		conf.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ(), null);
		conf.set("instances."+loc.clone().subtract(0, 1, 0).getWorld().getName()+"."+loc.clone().subtract(0, 1, 0).getBlockX()+"~"+loc.clone().subtract(0, 1, 0).getBlockY()+"~"+loc.clone().subtract(0, 1, 0).getBlockZ()+ ".mb", null);
		conf.set("instances."+loc.clone().subtract(0, 1, 0).getWorld().getName()+"."+loc.clone().subtract(0, 1, 0).getBlockX()+"~"+loc.clone().subtract(0, 1, 0).getBlockY()+"~"+loc.clone().subtract(0, 1, 0).getBlockZ()+".seed", null);
		conf.set("instances."+loc.clone().subtract(0, 1, 0).getWorld().getName()+"."+loc.clone().subtract(0, 1, 0).getBlockX()+"~"+loc.clone().subtract(0, 1, 0).getBlockY()+"~"+loc.clone().subtract(0, 1, 0).getBlockZ(), null);
		try {
			conf.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public static boolean contains(Location loc) {

		File f = new File(iacs.getPlugin().getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration conf = (FileConfiguration) s;
		
		if(conf.getString("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".mb") != null) {
			return true; //
		}
		return instances.containsKey(loc);
	}
	
}
