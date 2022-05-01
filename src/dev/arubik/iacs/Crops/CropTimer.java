package dev.arubik.iacs.Crops;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.events.forChunks;

public class CropTimer {
	int taskID;
	   public static void stopTimer(int ID) {
	        Bukkit.getScheduler().cancelTask(ID);
	        Bukkit.getServer().getScheduler().cancelTask(ID);
	    }
	   
	   
	   public CropTimer(int originalTime) {
	        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        taskID = scheduler.scheduleSyncDelayedTask(iacs.getPlugin(), new Runnable() {
	            @Override
	            public void run() {
	            	iacs.startTimer();
	            	try {
	            		
	            		if(iacs.getCfg("config.async-not-safe", false).toString().equalsIgnoreCase("TRUE")) {
	            			scheduler.runTaskAsynchronously(iacs.getPlugin(), new Runnable() {
								@Override
								public void run() {
				            		new forChunks();
								}
	            				
	            			});
	            		}
	            		else {
		            		new forChunks();	
	            		}
	            	}catch (Error e) {}
	            	
	            }
	        }, Long.valueOf(originalTime*20 + ""));
	        

	    }

	   public int getTaskID() {
		   return taskID;
	   }
}
