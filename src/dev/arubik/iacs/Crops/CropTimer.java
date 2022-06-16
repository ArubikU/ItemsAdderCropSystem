package dev.arubik.iacs.Crops;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.events.forChunks;
import dev.arubik.iacs.events.newForChunks;

public class CropTimer {
	int taskID;
	int tTaskID;
	   public static void stopTimer(int ID) {
	        Bukkit.getScheduler().cancelTask(ID);
	        Bukkit.getServer().getScheduler().cancelTask(ID);
	    }
	   
	   
	   public CropTimer(int originalTime) {
	        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();


	        if(iacs.getCfg("config.block-mode", "false").toString().equalsIgnoreCase("TRUE")) {

		        forChunks fs =  new forChunks();
		        BukkitTask ts = fs.runTaskLaterAsynchronously(iacs.getPlugin(), originalTime*20);
		        taskID = ts.getTaskId();
		        
	        }
	        if(iacs.getCfg("config.furniture-mode", "false").toString().equalsIgnoreCase("TRUE")) {

		        newForChunks fs =  new newForChunks();
		        BukkitTask ts = fs.runTaskLaterAsynchronously(iacs.getPlugin(), originalTime*20);
		        tTaskID = ts.getTaskId();
		        
	        }
	        

	    }

	   public int getTaskID() {
		   return taskID;
	   }
	   public int gettTaskID() {
		   return tTaskID;
	   }
}
