package dev.arubik.iacs.listener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.events.ModifyMB;
import dev.arubik.iacs.skills.SkillEvent;
import io.lumine.mythic.utils.config.LineConfig;

public class eventListenerClass implements Listener{

	public eventListenerClass() {
		
	}
	
	@EventHandler
	public void onModifyMB(ModifyMB e) {
		switch(e.getOp()) {
			case ADDITION:{
				tag("~OnAddWater").forEach(event ->{
					SkillEvent skill = new SkillEvent(e.getBlock().getLocation(), LineConfig.of(event), e.getPlayer());
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
							
						});
						
			}
			case REST:{
				tag("~OnTakeWater").forEach(event ->{
					SkillEvent skill = new SkillEvent(e.getBlock().getLocation(), LineConfig.of(event), e.getPlayer());
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
						
				});
			}
			case REST_SPRINKLER:{
				tag("~OnSprinklerFill").forEach(event ->{
					SkillEvent skill = new SkillEvent(e.getBlock().getLocation(), LineConfig.of(event), e.getPlayer());
					
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
							
					
				});
			}
		}
	}

	@EventHandler
	public void onBreak(dev.lone.itemsadder.api.Events.CustomBlockBreakEvent e) {

		tag("~BlockBreack").forEach(event ->{
			new SkillEvent(e.getBlock().getLocation(), LineConfig.of(event), e.getPlayer());
		});
		
		tag("~BlockBreak").forEach(event ->{
			new SkillEvent(e.getBlock().getLocation(), LineConfig.of(event), e.getPlayer());
		});
	}
	
	@EventHandler
	public void onInteract(dev.lone.itemsadder.api.Events.CustomBlockInteractEvent e) {
		switch(e.getAction()) {
			case RIGHT_CLICK_BLOCK:{
				tag("~RightClick").forEach(event ->{
					new SkillEvent(e.getBlockClicked().getLocation(), LineConfig.of(event), e.getPlayer());
				});
			}
		case LEFT_CLICK_AIR:{
			tag("~LeftClickAir").forEach(event ->{
				new SkillEvent(e.getBlockClicked().getLocation(), LineConfig.of(event), e.getPlayer());
			});
		}
		case LEFT_CLICK_BLOCK:{
			tag("~LeftClick").forEach(event ->{
				new SkillEvent(e.getBlockClicked().getLocation(), LineConfig.of(event), e.getPlayer());
			});
		}
		case RIGHT_CLICK_AIR:{
			tag("~RightClickAir").forEach(event ->{
				new SkillEvent(e.getBlockClicked().getLocation(), LineConfig.of(event), e.getPlayer());
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
