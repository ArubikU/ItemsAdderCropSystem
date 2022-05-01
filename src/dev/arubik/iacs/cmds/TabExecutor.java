package dev.arubik.iacs.cmds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import dev.arubik.iacs.iacs;

public class TabExecutor implements org.bukkit.command.TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String all, String[] args) {
		return new ArrayList<String>();
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
					iacs.MiniMessage("<rainbow>[IACROP] Recargado!</rainbow>", sender, 0);
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
				}
			
			
		}catch(Error | IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
