package dev.arubik.iacs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import dev.arubik.iacs.Crops.CropTimer;
import dev.arubik.iacs.cmds.TabExecutor;
import dev.arubik.iacs.listener.RightClickListener;
import dev.arubik.iacs.listener.eventListenerClass;
import dev.arubik.iacs.listener.onBreakListener;
import dev.arubik.iacs.managers.AdvancedLicense;
import dev.arubik.iacs.managers.CropManager;
import dev.arubik.iacs.skills.Skills;
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

	public static void log(String message) {
		iacs.getPlugin().getLogger().log(Level.WARNING, message);
	}
	

   // private CancellationDetector<BlockBreakEvent> detector = new CancellationDetector<BlockBreakEvent>(BlockBreakEvent.class);
	
	@Override
	public void onEnable() {
		plugin = this;
		registrarConfig();
		
//		detector.addListener(new CancelListener<BlockBreakEvent>() {
//			@Override
//			public void onCancelled(Plugin plugin, BlockBreakEvent event) {
//				System.out.println(event + " cancelled by " + plugin);
//			}
//		});
		
		plugin.getCommand("iacrop").setExecutor(new TabExecutor());
		plugin.getCommand("iacrop").setTabCompleter(new TabExecutor());

		String user = "%%__USER__%%";
		if(user.contains("%%_") == false && user.contains("_%%") == false && user.contains("USER") == false) { 
			AdvancedLicense alincese = new AdvancedLicense();
			File ff = new File(plugin.getDataFolder(), "config.yml");
			if (!ff.exists()) {
				ff.getParentFile().mkdirs();
			}
			YamlConfiguration sf = YamlConfiguration.loadConfiguration(ff);
			FileConfiguration dataf = (FileConfiguration) sf;
			alincese.setLicense(dataf.getString("license"));
			if(alincese.register() == false) {
				Bukkit.getPluginManager().disablePlugin(this);
				this.onDisable();
				return;
			}
		}
		
		File f = new File(getDataFolder(), "dont-touch.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		cm = new CropManager(data);
		

		Bukkit.getPluginManager().registerEvents(new RightClickListener(), this);
		Bukkit.getPluginManager().registerEvents(new onBreakListener(), this);
		Bukkit.getPluginManager().registerEvents(new Skills(), this);
		Bukkit.getPluginManager().registerEvents(new eventListenerClass(), this);
		startTimer();
	}
	
	public static String parsePlaceholder(Player p, Location loc, String message) {

		String returned = message;
		
		for(String s : message.split(" ")) {
			if(s.startsWith("cnf:")) {
				returned = returned.replace(s, iacs.getPlugin().getConfig().getString(s.replaceFirst("cnf:", "")));
			}
		}
		
		if(p != null) {
			for(String s : message.split(" ")) {
				if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
					if(s.startsWith("placeholder:")) {
						returned = returned.replace(s, PlaceholderAPI.setBracketPlaceholders(p, s.replaceFirst("placeholder:", "")));
					}
				}
				if(s.startsWith("player:displayname")) {
					returned = returned.replace(s, p.getDisplayName());
				}
				if(s.startsWith("player:name")) {
					returned = returned.replace(s, p.getName());
				}
				if(s.startsWith("player:uuid")) {
					returned = returned.replace(s, p.getUniqueId().toString());
				}
			}
		}
		
		if(CropManager.getInstance(loc) != null) {

			for(String s : message.split(" ")) {
				if(s.startsWith("instance:mb")) {
					returned = returned.replace("instance:mb", CropManager.getInstance(loc).getMb() + "");
				}
				if(s.startsWith("instance:seed")) {
					returned = returned.replace("instance:seed", CropManager.getInstance(loc).getCurrentseed() + "");
				}
				if(s.startsWith("instance:location")) {
					returned = returned.replace("instance:location", CropManager.getInstance(loc).getLoc() + "");
				}
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
		//Bukkit.getConsoleSender().sendMessage(iacs.MiniMessage("&f[IACroper] Timer <RED>RESTARTED"));
		if(plugin.getConfig().getString("config.time-grow") != null) {
			timer =new CropTimer(plugin.getConfig().getInt("config.time-grow"));
		}
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
	public static void MiniMessage(Object s,ConsoleCommandSender player, int id /* 0: message 1: actionbar*/) {
		BukkitAudiences au = BukkitAudiences.create(iacs.getPlugin());
		Audience p= Audience.empty();
		p = au.sender(player);
		MiniMessage mm = MiniMessage.get();
		Component parsed = mm.deserialize(s.toString());
		iacs.sendMessage(p, parsed, id);
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
		
		}
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
	
	
	public static Object getCfg(String rute, Object temp) {
		File f = new File(iacs.getPlugin().getDataFolder(), "dont-touch.yml");
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
}
