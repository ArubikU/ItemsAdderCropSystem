package dev.arubik.iacs.utils;

import org.bukkit.Bukkit;

import dev.arubik.iacs.iacs;

public class Schedulers {

	public static void run(Runnable runa) {
		Bukkit.getScheduler().runTaskAsynchronously(iacs.getPlugin(), runa);
	}
}
