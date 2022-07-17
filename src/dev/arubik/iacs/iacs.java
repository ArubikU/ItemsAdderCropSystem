package dev.arubik.iacs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.Crops.CropTimer;
import dev.arubik.iacs.cmds.TabExecutor;
import dev.arubik.iacs.events.ModifyMB;
import dev.arubik.iacs.events.forChunks;
import dev.arubik.iacs.events.newForChunks;
import dev.arubik.iacs.events.ModifyMB.Operation;
import dev.arubik.iacs.listener.RightClickListener;
import dev.arubik.iacs.listener.eventListenerClass;
import dev.arubik.iacs.listener.onBreakListener;
import dev.arubik.iacs.listener.rightClickWater;
import dev.arubik.iacs.managers.CropManager;
import dev.arubik.iacs.managers.Metrics;
import dev.arubik.iacs.managers.newManagerIacrops;
import dev.arubik.iacs.skills.Skills;
import dev.lone.itemsadder.api.CustomBlock;
import io.lumine.mythic.utils.adventure.audience.Audience;
import io.lumine.mythic.utils.adventure.platform.bukkit.BukkitAudiences;
import io.lumine.mythic.utils.adventure.text.Component;
import io.lumine.mythic.utils.adventure.text.minimessage.MiniMessage;
import io.lumine.mythic.utils.adventure.title.TitlePart;
import me.clip.placeholderapi.PlaceholderAPI;


public class iacs extends JavaPlugin{

	PluginDescriptionFile pdffile = getDescription();
	public String rutaconf;
	public String version = pdffile.getVersion();
	public static FileConfiguration config;
	public static CropTimer timer;
	
	public static iacs plugin;
	
	public static CropManager cm;
	
	public static ModifyMB castModifyMB(Block b, @Nullable Player who, Operation op, int amount, CropInstance ci) {
		return new ModifyMB(b, who, op, amount, ci);
	}
	
	public PluginDescriptionFile getPdffile() {
		return pdffile;
	}

	public void setPdffile(PluginDescriptionFile pdffile) {
		this.pdffile = pdffile;
	}

	public String getRutaconf() {
		return rutaconf;
	}

	public void setRutaconf(String rutaconf) {
		this.rutaconf = rutaconf;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	public static void setConfig(FileConfiguration config) {
		iacs.config = config;
	}

	public static CropManager getCm() {
		return cm;
	}

	public static void setCm(CropManager cm) {
		iacs.cm = cm;
	}

	public static void setPlugin(iacs plugin) {
		iacs.plugin = plugin;
	}

	
	
	public static void log(Object message) {
		iacs.MiniMessage(" <red>ERROR</red> <gradient:red:white>[" + message.toString() + "]</gradient>", Bukkit.getConsoleSender(), 0);
		
	}
	

   // private CancellationDetector<BlockBreakEvent> detector = new CancellationDetector<BlockBreakEvent>(BlockBreakEvent.class);
	public NamespacedKey genNSK(String s) {
		return new NamespacedKey(this, s);
		
	}
	
	public static Metrics me;
	
	@Override
	public void onEnable() {
		plugin = this;
		registrarConfig();

		listenerProtocol.onEnable();
		
		new targetBlock();
		

		if(iacs.getCfg("config.async-not-safe", false).toString().equalsIgnoreCase("TRUE")) {
			iacs.MiniMessage("<rainbow>[IACROPER] AsynMode Enabled</rainbow>", Bukkit.getConsoleSender(), 0);
		}
		
		
		
		plugin.getCommand("iacrop").setExecutor(new TabExecutor());
		plugin.getCommand("iacrop").setTabCompleter(new TabExecutor());
		
		me = new Metrics(this, 15185);

		String user = "%%__USER__%%";
		

		iacs.MiniMessage("<rainbow>[IACROPER] Encendido</rainbow>", Bukkit.getConsoleSender(), 0);
		iacs.MiniMessage("<green>[IACROPER] Version:"+plugin.getDescription().getVersion(), Bukkit.getConsoleSender(), 0);
		iacs.MiniMessage("<green>[IACROPER] Comprador:"+plugin.getDescription().getDescription(), Bukkit.getConsoleSender(), 0);
		
		File f = new File(getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		cm = new CropManager(data);
		



        if(iacs.getCfg("config.block-mode", "true").toString().equalsIgnoreCase("TRUE")) {

    		Bukkit.getPluginManager().registerEvents(new RightClickListener(), this);
    		
    		if(((dev.lone.itemsadder.Main)Bukkit.getPluginManager().getPlugin("ItemsAdder")).getConfig().getString("blocks.disable-REAL_WIRE")!=null) {
    			FileConfiguration conf = ((dev.lone.itemsadder.Main)Bukkit.getPluginManager().getPlugin("ItemsAdder")).getConfig();
    			
    			
    			if(!conf.getString("blocks.disable-REAL_WIRE").equalsIgnoreCase("false")) {
    				conf.set("blocks.disable-REAL_WIRE",true);

        			try {
    					conf.save(iacs.getFile((dev.lone.itemsadder.Main)Bukkit.getPluginManager().getPlugin("ItemsAdder"), "config.yml"));
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
	        
        }
        if(iacs.getCfg("config.furniture-mode", "false").toString().equalsIgnoreCase("TRUE")) {
        	new newManagerIacrops();
	        
        }
		
		Bukkit.getPluginManager().registerEvents(new onBreakListener(), this);
		Bukkit.getPluginManager().registerEvents(new rightClickWater(), this);
		Bukkit.getPluginManager().registerEvents(new Skills(), this);
		Bukkit.getPluginManager().registerEvents(new eventListenerClass(), this);
		startTimer();
	}
	
	public static void sendBlock(CustomBlock cb, Location loc, int distance) {
		if(iacs.getCfg("config.block-packets", "false").toString().equalsIgnoreCase("false")) {
			cb.place(loc);
			return;
			
		}

		
		if(cb.getNamespacedID().equalsIgnoreCase((String) iacs.getCfg("config.farming_station", ""))
	|| cb.getNamespacedID().equalsIgnoreCase((String) iacs.getCfg("config.water_farming_station", ""))) {

			Bukkit.getScheduler().runTask(iacs.getPlugin(), () ->{
			cb.place(loc);
			});
			
		}else {

			if(loc.getBlock().getType() == cb.getBaseBlockData().getMaterial()) {
				Bukkit.getScheduler().runTask(iacs.getPlugin(), () ->{
					loc.getWorld().setBlockData(loc, cb.getBaseBlockData());
					loc.getWorld().getBlockAt(loc).setBlockData(cb.getBaseBlockData());
					dev.lone.itemsadder.api.CustomBlock$Advanced.placeInCustomRegion(cb, loc);
					Bukkit.getOnlinePlayers().forEach(player -> {
						Location newl = player.getLocation().clone();
						newl.setY(loc.getY());
						if(newl.distance(loc) < distance) {
							player.sendBlockChange(loc, cb.getBaseBlockData());
						}
					});
				});
				Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), () ->{
					Bukkit.getOnlinePlayers().forEach(player -> {
						Location newl = player.getLocation().clone();
						newl.setY(loc.getY());
						if(newl.distance(loc) < distance) {
							player.sendBlockChange(loc, cb.getBaseBlockData());
						}
					});
				}, 2);
			}else {
				loc.getBlock().setType(cb.getBaseBlockData().getMaterial());
				Bukkit.getScheduler().runTask(iacs.getPlugin(), () ->{
					loc.getWorld().setBlockData(loc, cb.getBaseBlockData());
					loc.getWorld().getBlockAt(loc).setBlockData(cb.getBaseBlockData());
					dev.lone.itemsadder.api.CustomBlock$Advanced.placeInCustomRegion(cb, loc);
					Bukkit.getOnlinePlayers().forEach(player -> {
						Location newl = player.getLocation().clone();
						newl.setY(loc.getY());
						if(newl.distance(loc) < distance) {
							player.sendBlockChange(loc, cb.getBaseBlockData());
						}
					});
				});
				Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), () ->{
					Bukkit.getOnlinePlayers().forEach(player -> {
						Location newl = player.getLocation().clone();
						newl.setY(loc.getY());
						if(newl.distance(loc) < distance) {
							player.sendBlockChange(loc, cb.getBaseBlockData());
						}
					});
				}, 2);
			}

		}
	}
	
	
	public static Boolean isRandom(int size) {
		switch (size) {
		case 0: return false;
			case 1:{
				return ((new Random()).nextFloat() + "").endsWith("1");
			}
			case 2:{
				return (((new Random()).nextFloat() + "").endsWith("1") || ((new Random()).nextFloat() + "").endsWith("2"));
			}
			case 3:{
				return (((new Random()).nextFloat() + "").endsWith("1") || ((new Random()).nextFloat() + "").endsWith("2")
					|| ((new Random()).nextFloat() + "").endsWith("3"));
			}
			case 4:{
				return (((new Random()).nextFloat() + "").endsWith("1") || ((new Random()).nextFloat() + "").endsWith("2")
						|| ((new Random()).nextFloat() + "").endsWith("3") || ((new Random()).nextFloat() + "").endsWith("4"));
			}
			case 5:{
				return (((new Random()).nextFloat() + "").endsWith("1") || ((new Random()).nextFloat() + "").endsWith("2")
						|| ((new Random()).nextFloat() + "").endsWith("3") || ((new Random()).nextFloat() + "").endsWith("4")
						|| ((new Random()).nextFloat() + "").endsWith("5"));
			}
			case 6:{
				return (((new Random()).nextFloat() + "").endsWith("1") || ((new Random()).nextFloat() + "").endsWith("2")
						|| ((new Random()).nextFloat() + "").endsWith("3") || ((new Random()).nextFloat() + "").endsWith("4")
						|| ((new Random()).nextFloat() + "").endsWith("5") || ((new Random()).nextFloat() + "").endsWith("6"));
			}
			case 7:{
				return (((new Random()).nextFloat() + "").endsWith("1") || ((new Random()).nextFloat() + "").endsWith("2")
						|| ((new Random()).nextFloat() + "").endsWith("3") || ((new Random()).nextFloat() + "").endsWith("4")
						|| ((new Random()).nextFloat() + "").endsWith("5") || ((new Random()).nextFloat() + "").endsWith("6")
						|| ((new Random()).nextFloat() + "").endsWith("7"));
			}
			case 8:{
				return (((new Random()).nextFloat() + "").endsWith("1") || ((new Random()).nextFloat() + "").endsWith("2")
						|| ((new Random()).nextFloat() + "").endsWith("3") || ((new Random()).nextFloat() + "").endsWith("4")
						|| ((new Random()).nextFloat() + "").endsWith("5") || ((new Random()).nextFloat() + "").endsWith("6")
						|| ((new Random()).nextFloat() + "").endsWith("7") || ((new Random()).nextFloat() + "").endsWith("8"));
			}
			case 9:{
				return (((new Random()).nextFloat() + "").endsWith("1") || ((new Random()).nextFloat() + "").endsWith("2")
						|| ((new Random()).nextFloat() + "").endsWith("3") || ((new Random()).nextFloat() + "").endsWith("4")
						|| ((new Random()).nextFloat() + "").endsWith("5") || ((new Random()).nextFloat() + "").endsWith("6")
						|| ((new Random()).nextFloat() + "").endsWith("7") || ((new Random()).nextFloat() + "").endsWith("8")
						|| ((new Random()).nextFloat() + "").endsWith("9"));
			}
			default: return true;
		}
	}
	
	
	public static String parsePlaceholder(Player p, Location loc, String message) {

		String returned = message;
		
		for(String s : message.split(" ")) {
			if(s.startsWith("cnf:")) {
				returned = returned.replace(s, iacs.getPlugin().getConfig().getString(s.replaceFirst("cnf:", "")));
			}
		}
		
		if(loc!=null) {
			if(returned.contains("location:x")) {
				returned = returned.replace("location:x", loc.getBlockX()+"");
			}
			if(returned.contains("location:y")) {
				returned = returned.replace("location:y", loc.getBlockY()+"");
			}
			if(returned.contains("location:z")) {
				returned = returned.replace("location:z", loc.getBlockZ()+"");
			}
			if(returned.contains("location:world")) {
				returned = returned.replace("location:world", loc.getWorld().getName());
			}
			if(returned.contains("location:chunkx")) {
				returned = returned.replace("location:chunkx", loc.getChunk().getX()+"");
			}
			if(returned.contains("location:chunkz")) {
				returned = returned.replace("location:chunkz", loc.getChunk().getZ()+"");
			}
			if(returned.contains("location:chunk_loaded")) {
				returned = returned.replace("location:chunk_loaded", loc.getChunk().isLoaded()+"");
			}
		}
		
		if(loc.getBlock()!=null) {

			if(returned.contains("block:material")) {
				returned = returned.replace("block:material", loc.getBlock().getType().toString());
			}
		}
		
		
		if(CropManager.getInstance(loc) != null) {
				if(returned.contains("instance:mb")) {
					returned = returned.replace("instance:mb", CropManager.getInstance(loc).getMb() + "");
				}
				if(returned.contains("instance:seed")) {
					returned = returned.replace("instance:seed", CropManager.getInstance(loc).getCurrentseed() + "");
				}
				if(returned.contains("instance:location")) {
					returned = returned.replace("instance:location", CropManager.getInstance(loc).getLoc() + "");
				}
		}

		if(p != null) {
				if(returned.contains("player:displayname")) {
					returned = returned.replace("player:displayname", p.getDisplayName());
				}
				if(returned.contains("player:name")) {
					returned = returned.replace("player:name", p.getName());
				}
				if(returned.contains("player:uuid")) {
					returned = returned.replace("player:uuid", p.getUniqueId().toString());
				}
				if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
					returned = PlaceholderAPI.setBracketPlaceholders(p, returned);
			}
		}
		return returned;
	}
	
	public static void dropItems(ItemStack items,Location loc) {
		loc.getWorld().dropItem(loc, items);
	}
	public static void dropItems(List<ItemStack> items,Location loc) {
		items.forEach(item->{
			loc.getWorld().dropItem(loc, item);
		});
	}
	
	public static void startTimer() {
		if(plugin.getConfig().getString("config.time-grow") != null) {
			timer =new CropTimer(plugin.getConfig().getInt("config.time-grow"));
		}
	}

	public static void Message(Player pl,String s) {

		BukkitAudiences au = BukkitAudiences.create(iacs.getPlugin());
		Audience p= Audience.empty();
		p = au.player(pl);
		MiniMessage mm = MiniMessage.get();
		Component parsed = mm.deserialize(s.toString());
		iacs.sendMessage(p, parsed, 0);
	}

	public static void Message(CommandSender pl,String s) {

		BukkitAudiences au = BukkitAudiences.create(iacs.getPlugin());
		Audience p= Audience.empty();
		p = au.sender(pl);
		MiniMessage mm = MiniMessage.get();
		Component parsed = mm.deserialize(s.toString());
		iacs.sendMessage(p, parsed, 0);
	}
	
	public static void MiniMessage(Object s,Player player, int id /* 0: message 1: actionbar*/) {
		BukkitAudiences au = BukkitAudiences.create(iacs.getPlugin());
		Audience p= Audience.empty();
		p = au.player(player);
		MiniMessage mm = MiniMessage.get();
		Component parsed = mm.deserialize(s.toString());
		iacs.sendMessage(p, parsed, id);
	}
	
	//ConsoleCommandSender 
	public static void MiniMessage(Object s,@Nullable ConsoleCommandSender player, int id /* 0: message 1: actionbar*/) {
		if(player == null) {
			BukkitAudiences au = BukkitAudiences.create(iacs.getPlugin());
			Audience p= Audience.empty();
			p = au.sender(Bukkit.getConsoleSender());
			MiniMessage mm = MiniMessage.get();
			Component parsed = mm.deserialize(s.toString());
			iacs.sendMessage(p, parsed, id);
		}else {
		BukkitAudiences au = BukkitAudiences.create(iacs.getPlugin());
		Audience p= Audience.empty();
		p = au.sender(player);
		MiniMessage mm = MiniMessage.get();
		Component parsed = mm.deserialize(s.toString());
		iacs.sendMessage(p, parsed, id);
		}
	}
	//CommandSender 
	public static void MiniMessage(Object s,CommandSender player, int id /* 0: message 1: actionbar*/) {
		BukkitAudiences au = BukkitAudiences.create(iacs.getPlugin());
		Audience p= Audience.empty();
		p = au.sender(player);
		MiniMessage mm = MiniMessage.get();
		Component parsed = mm.deserialize(s.toString());
		iacs.sendMessage(p, parsed, id);
	}
	
	
	public static void sendMessage(Audience p, Component parsed, int id) {

		if(id == 0){
				p.sendMessage(parsed);
			}
		else if(id == 1){
				p.sendActionBar(parsed);
			}
		else if(id == 2){
				p.sendTitlePart(TitlePart.TITLE, parsed);
			}
		else if(id == 3){
				p.sendTitlePart(TitlePart.SUBTITLE, parsed);
			}
		else if(id == 5){
				p.sendMessage(parsed, io.lumine.mythic.utils.adventure.audience.MessageType.SYSTEM);
			}
		else {
			p.sendMessage(parsed);
		}
	}
	
	
	@Override
	public void onDisable() {
		
		if(timer != null) {
			
		CropTimer.stopTimer(timer.getTaskID());
		CropTimer.stopTimer(timer.gettTaskID());
		
		}
		listenerProtocol.onDisable();
		if(cm != null) {

		File f = new File(getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		try {
			for(Location loc : cm.getMap().keySet()) {
				data.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".mb", cm.getMap().get(loc).mb);
			}
			data.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		

		File ff = new File(getDataFolder(), "backups/"+ format.format(now) +".yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration sf = YamlConfiguration.loadConfiguration(ff);
		FileConfiguration dataf = (FileConfiguration) sf;

		for(Location loc : cm.getMap().keySet()) {
			dataf.set("instances."+loc.getWorld().getName()+"."+loc.getBlockX()+"~"+loc.getBlockY()+"~"+loc.getBlockZ()+".mb", cm.getMap().get(loc).mb);
		}
		try {
			dataf.save(ff);
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		
	}
	
	public static iacs getPlugin() {
		return plugin;
	}

	public void registrarConfig() {

		File config = new File(this.getDataFolder(), "config.yml");
		rutaconf = config.getPath();
		if (!config.exists()) {this.getConfig().options().copyDefaults(true);saveConfig();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(config);
		FileConfiguration data = (FileConfiguration) s;
		
		iacs.config = data;

	}

	public static Boolean mythiclib() {

		File f = new File(iacs.getPlugin().getDataFolder(), "config.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		return s.getBoolean("config.mythic-lib");
	}
	
	public static Object getCfg(String rute, Object temp) {
		File f = new File(iacs.getPlugin().getDataFolder(), "config.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		if(data.get(rute) == null) {
			return temp;
		}
		
		if(temp instanceof Boolean) {
			return data.getBoolean(rute);
		}
        if(temp instanceof String) {
            return data.getString(rute);
        }
        if(temp instanceof Integer){
            return data.getInt(rute);
        }
        if(temp instanceof List<?>){
            return data.getList(rute);
        }
        if(temp instanceof Double) {
        	return data.getDouble(rute);
        }
		if(data.get(rute) != null) {
			return data.get(rute);
		}
		return temp;
	}
	
	public static File getFile(Plugin p,String rute) {

		File f = new File(p.getDataFolder(), rute);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		
		return f;
	}

	public static Object getCfgFile(String rute, Object temp,String rote) {
		File f = new File(iacs.getPlugin().getDataFolder(), rote);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		if(data.get(rute) == null) {
			return temp;
		}
		
		if(temp instanceof Boolean) {
			return data.getBoolean(rute);
		}
        if(temp instanceof String) {
            return data.getString(rute);
        }
        if(temp instanceof Integer){
            return data.getInt(rute);
        }
        if(temp instanceof List<?>){
            return data.getList(rute);
        }
        if(temp instanceof Double) {
        	return data.getDouble(rute);
        }
		if(data.get(rute) != null) {
			return data.get(rute);
		}
		return temp;
	}
	public static FileConfiguration getConfig(String rote) {
		File f = new File(iacs.getPlugin().getDataFolder(), rote);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		return s;
	}
}
