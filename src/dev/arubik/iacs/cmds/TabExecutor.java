package dev.arubik.iacs.cmds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.events.forChunks;

public class TabExecutor implements org.bukkit.command.TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String all, String[] args) {
		
		List<String> result = new ArrayList<String>();
		

		if(!sender.isOp() || !(sender instanceof Player)){
			return result;
		}
		
		
		result.add("reload");
		result.add("contact");
		result.add("grow");
		result.add("remove <world>");
		String user = "%%__USER__%%";
		
		return result;
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String all, String[] args) {
		
		if(!sender.isOp() || !(sender instanceof Player)){
			return false;
		}
		
		try {
			
			if(args[0].toUpperCase().equalsIgnoreCase("reload")){
					iacs.getPlugin().registrarConfig();
					iacs.getPlugin().reloadConfig();

					String user = "%%__USER__%%";
					iacs.MiniMessage("<rainbow>[IACROP] Reloaded!</rainbow>", sender, 0);
				}
			else if(args[0].toUpperCase().equalsIgnoreCase("remove")){

				File ff = new File(iacs.getPlugin().getDataFolder(), "config.yml");
				if (!ff.exists()) {
					ff.getParentFile().mkdirs();
				}
				YamlConfiguration sf = YamlConfiguration.loadConfiguration(ff);
				FileConfiguration dataf = (FileConfiguration) sf;
				
				List<String> list = dataf.getStringList("config.worlds");
				list.remove(args[1]);
				
				dataf.set("config.worlds", list);
				dataf.save(ff);
				iacs.getPlugin().registrarConfig();
				iacs.getPlugin().reloadConfig();
				iacs.MiniMessage("<rainbow>[IACROP] World disabled!</rainbow>", sender, 0);
			}
			else if(args[0].toUpperCase().equalsIgnoreCase("grow")){
				

				Bukkit.getServer().getScheduler().runTaskAsynchronously(iacs.getPlugin(), ()->{
					forChunks.runWork();
				});
				
				iacs.MiniMessage("<rainbow>[IACROP] World has been grow!</rainbow>", sender, 0);
			}
			
			
		}catch(Error | IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
