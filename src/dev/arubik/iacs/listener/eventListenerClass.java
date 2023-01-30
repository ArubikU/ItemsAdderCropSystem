package dev.arubik.iacs.listener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.events.ModifyMB;
import dev.arubik.iacs.skills.SkillEvent;
import dev.arubik.iacs.utils.LineConfig;

public class eventListenerClass implements Listener{

	public eventListenerClass() {
		
	}
	
	@EventHandler
	public void onModifyMB(ModifyMB e) {
		switch(e.getOp()) {
			case ADDITION:{
				tag("~OnAddWater").forEach(event ->{
							SkillEvent skill = new SkillEvent(e.getB().getLocation(), LineConfig.of(event), e.getPlayer());
							if(e.isCancelled() != false) {
								if(skill.getLc().getInteger("AddWater", 0) != 0) {
									e.setAmount(e.getAmount() + skill.getLc().getInteger("AddWater", 0));
								}
								if(skill.getLc().getInteger("TakeWater", 0) != 0) {
									e.setAmount(e.getAmount() - skill.getLc().getInteger("TakeWater", 0));
								}
								if(skill.getLc().getInteger("SetWater", 0) != 0) {
									e.setAmount(skill.getLc().getInteger("SetWater", 0));
								}
							}
							

							if(skill.isCancelled() == true) {
								e.setCancelled(true);
							}
							
							
						});
						
			}
			case REST:{
				tag("~OnTakeWater").forEach(event ->{
					SkillEvent skill  = new SkillEvent(e.getB().getLocation(), LineConfig.of(event), e.getPlayer());
							if(e.isCancelled() != false) {
								if(skill.getLc().getInteger("AddWater", 0) != 0) {
									e.setAmount(e.getAmount() + skill.getLc().getInteger("AddWater", 0));
								}
								if(skill.getLc().getInteger("TakeWater", 0) != 0) {
									e.setAmount(e.getAmount() - skill.getLc().getInteger("TakeWater", 0));
								}
								if(skill.getLc().getInteger("SetWater", 0) != 0) {
									e.setAmount(skill.getLc().getInteger("SetWater", 0));
								}
							}
							

							if(skill.isCancelled() == true) {
								e.setCancelled(true);
							}
							
						
				});
			}
			case REST_SPRINKLER:{
				tag("~OnSprinklerFill").forEach(event ->{
					SkillEvent skill = new SkillEvent(e.getB().getLocation(), LineConfig.of(event), e.getPlayer());
					
							if(e.isCancelled() != false) {
								if(skill.getLc().getInteger("AddWater", 0) != 0) {
									e.setAmount(e.getAmount() + skill.getLc().getInteger("AddWater", 0));
								}
								if(skill.getLc().getInteger("TakeWater", 0) != 0) {
									e.setAmount(e.getAmount() - skill.getLc().getInteger("TakeWater", 0));
								}
								if(skill.getLc().getInteger("SetWater", 0) != 0) {
									e.setAmount(skill.getLc().getInteger("SetWater", 0));
								}
							}
							

							if(skill.isCancelled() == true) {
								e.setCancelled(true);
							}
							
							
					
				});
			}
		}
	}


	@EventHandler
	public void onInteractVanilla(org.bukkit.event.player.PlayerInteractEvent e) {
		
		switch(e.getAction()) {
		case RIGHT_CLICK_BLOCK:{
			tag("~RightClickVanilla").forEach(event ->{
				SkillEvent skill = new SkillEvent(e.getClickedBlock().getLocation(), LineConfig.of(event), e.getPlayer());
				if(skill.isCancelled() == true) {
					e.setCancelled(true);
				}
				});
			
		}
	case LEFT_CLICK_AIR:{
		tag("~LeftClickAirVanilla").forEach(event ->{
			SkillEvent skill = new SkillEvent(e.getClickedBlock().getLocation(), LineConfig.of(event), e.getPlayer());

			if(skill.isCancelled() == true) {
				e.setCancelled(true);
			}
			});
	}
	case LEFT_CLICK_BLOCK:{
		tag("~LeftClickVanilla").forEach(event ->{
			SkillEvent skill = new SkillEvent(e.getClickedBlock().getLocation(), LineConfig.of(event), e.getPlayer());

			if(skill.isCancelled() == true) {
				e.setCancelled(true);
			}
			});
	}
	case RIGHT_CLICK_AIR:{
		tag("~RightClickAirVanilla").forEach(event ->{
			SkillEvent skill = new SkillEvent(e.getClickedBlock().getLocation(), LineConfig.of(event), e.getPlayer());

			if(skill.isCancelled() == true) {
				e.setCancelled(true);
			}
			});
	}
	default:
		break;
	}
}
	
	@EventHandler
	public void onEat(org.bukkit.event.player.PlayerItemConsumeEvent e) {
		tag("~ConsumeItem").forEach(event ->{
			SkillEvent skill = new SkillEvent(e.getPlayer().getLocation(), LineConfig.of(event), e.getPlayer(),e.getItem());

			if(skill.isCancelled() == true) {
				e.setCancelled(true);
			}
			});
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
