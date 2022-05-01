package dev.arubik.iacs.skills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.managers.CropManager;
import io.lumine.mythic.utils.config.LineConfig;
import io.lumine.mythic.utils.particles.ParticleBuilder;
import io.lumine.mythic.utils.sound.Sound;

public class Skills implements Listener{
	public Skills() {
	}
	
	@EventHandler
	public void onSkill(SkillEvent e) {
		
		if(e.isCancelled() == true) {
			return;
		}
		

		LineConfig line = e.getLc();
		Location loc = e.getLoc();
		Player p = e.getPlayer();


		if(line.getKey().equalsIgnoreCase("particle:")){
			Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), new Runnable() {
				@Override
				public void run() {
					ParticleBuilder pb =ParticleBuilder.of(io.lumine.mythic.utils.particles.Particle.valueOf(line.getString("particle", "WATER_SPLASH")));
					loc.setX(loc.getX() + line.getFloat("offset_x", 0));
					loc.setY(loc.getY() + line.getFloat("offset_y", 0));
					loc.setZ(loc.getZ() + line.getFloat("offset_z", 0));
					pb = pb.amount(line.getInteger("amount", 5));
					pb = pb.speed(line.getFloat("speed", 1));
					pb = pb.useExactOffsets(line.getBoolean("exactOffset", false));
					pb = pb.at(loc);
					pb = pb.offset(line.getFloat("offest-nsf", 0));
					pb.send(loc);
				}
			}, line.getLong("delay", 0));
		}

		if(line.getKey().equalsIgnoreCase("instance:")){
			Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if(CropManager.getInstance(loc) != null) {
						CropInstance ci = CropManager.getInstance(loc);
						
						if(line.getInteger("add-take-water", 0 ) != 0 ) {
							ci.modMB(line.getInteger("add-take-water", 0 ));
						}
					}
				}
			}, line.getLong("delay", 0));
		}
		if(line.getKey().equalsIgnoreCase("sound:")){
			Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), new Runnable() {
				@Override
				public void run() {
					try {
					Sound.play(loc, line.getString("sound", "ambient.underwater.enter"), line.getFloat("volume", 2), line.getFloat("pitch", 1));
					}catch(Exception | Error e) {
						iacs.log("[IACroper] try with ambient.underwater.enter ");
					}
					}
			}, line.getLong("delay", 0));
		}
		
		if(line.getKey().equalsIgnoreCase("actionbar:")) {
			
					String message = line.getString("message","<red>deluxe message");
			        iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 1);
				
		}
		if(line.getKey().equalsIgnoreCase("msg:")) {
			
			String message = line.getString("msg","<red>deluxe message");
	        iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 0);
		
		}
		if(line.getKey().equalsIgnoreCase("system:")) {
			
			String message = line.getString("message","<red>deluxe message");
	        iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 5);
		
		}
		if(line.getKey().equalsIgnoreCase("tittle:")) {
			
			String message = line.getString("message","<red>deluxe message");
	        iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 2);
		
		}
		if(line.getKey().equalsIgnoreCase("subtittle:")) {
			
			String message = line.getString("message","<red>deluxe message");
	        iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 3);
		
		}
		if(line.getKey().equalsIgnoreCase("command:")) {

					String message = line.getString("command","say yeah");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), iacs.parsePlaceholder(p, loc, message));
				
		}
	}
}
