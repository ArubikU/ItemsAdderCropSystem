package dev.arubik.iacs.listener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.skills.SkillEvent;
import dev.arubik.iacs.utils.LineConfig;

public class IAListeners implements Listener{

	@EventHandler
	public void onBreak(dev.lone.itemsadder.api.Events.CustomBlockBreakEvent e) {
		tag("~BlockBreack").forEach(event ->{
			SkillEvent skill = new SkillEvent(e.getBlock().getLocation(), LineConfig.of(event), e.getPlayer());
			
			if(skill.isCancelled() == true) {
				e.setCancelled(true);
			}
			
		});
		
		tag("~BlockBreak").forEach(event ->{
			SkillEvent skill = new SkillEvent(e.getBlock().getLocation(), LineConfig.of(event), e.getPlayer());

			if(skill.isCancelled() == true) {
				e.setCancelled(true);
			}
			
		});
	}
	
	@EventHandler
	public void onInteract(dev.lone.itemsadder.api.Events.CustomBlockInteractEvent e) {
		switch(e.getAction()) {
			case RIGHT_CLICK_BLOCK:{
				tag("~RightClick").forEach(event ->{
					SkillEvent skill = new SkillEvent(e.getBlockClicked().getLocation(), LineConfig.of(event), e.getPlayer());

					if(skill.isCancelled() == true) {
						e.setCancelled(true);
					}
					});
			}
		case LEFT_CLICK_AIR:{
			tag("~LeftClickAir").forEach(event ->{
				SkillEvent skill = new SkillEvent(e.getBlockClicked().getLocation(), LineConfig.of(event), e.getPlayer());

				if(skill.isCancelled() == true) {
					e.setCancelled(true);
				}
				});
		}
		case LEFT_CLICK_BLOCK:{
			tag("~LeftClick").forEach(event ->{
				SkillEvent skill = new SkillEvent(e.getBlockClicked().getLocation(), LineConfig.of(event), e.getPlayer());

				if(skill.isCancelled() == true) {
					e.setCancelled(true);
				}
				});
		}
		case RIGHT_CLICK_AIR:{
			tag("~RightClickAir").forEach(event ->{
				SkillEvent skill = new SkillEvent(e.getBlockClicked().getLocation(), LineConfig.of(event), e.getPlayer());

				if(skill.isCancelled() == true) {
					e.setCancelled(true);
				}
				});
		}
		default:
			break;
		}
	}
	
	@Nullable
	public static List<String> tag(String s) {
		List<String> returned = new ArrayList<String>();
		List<String> events = iacs.getPlugin().getConfig().getStringList("config.events");
		if(events == null) {return returned;}
		
		for(String event : events) {
			if(event.endsWith(s)) {
				returned.add(event.replace(s, ""));
			}
		}
		
		return returned;
	}
}