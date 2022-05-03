package dev.arubik.iacs.Crops;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

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
	        forChunks fs =  new forChunks();
	        BukkitTask ts = fs.runTaskLaterAsynchronously(iacs.getPlugin(), originalTime*20);
	        taskID = ts.getTaskId();
	        

	    }

	   public int getTaskID() {
		   return taskID;
	   }
}
