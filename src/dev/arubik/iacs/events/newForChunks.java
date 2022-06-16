package dev.arubik.iacs.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.managers.newCropInstance;

public class newForChunks extends BukkitRunnable {

	@Override
	public void run() {

		iacs.getPlugin().getConfig().getStringList("config.worlds").forEach(world -> {

			Bukkit.getWorld(world).getEntitiesByClass(ArmorStand.class).forEach(armostand -> {

				if (armostand.getPersistentDataContainer().has(newCropInstance.grow_time,
						PersistentDataType.INTEGER)) {

					newCropInstance instance = new newCropInstance(armostand);

					instance.takeTime((int) iacs.getCfg("config.time-grow", 650));

				}
			});
		});

	}

}