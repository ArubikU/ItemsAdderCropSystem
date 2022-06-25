package dev.arubik.iacs.events;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import dev.arubik.iacs.furnitureGet;
import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.managers.CropManager;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;

public class forChunks extends BukkitRunnable {

	@Override
	public void run() {
		iacs.startTimer();
		forChunks.runWork();}
	
	public static void runWork() {

		File f = new File(iacs.getPlugin().getDataFolder(), "config.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration conf = (FileConfiguration) s;

		iacs.getPlugin().getConfig().getStringList("config.worlds").forEach(world -> {

			CropManager.getInstances(Bukkit.getWorld(world)).forEach(location -> {
				try {
					Location substracted = location.clone().subtract(0, 1, 0);

					Location seed = substracted;

					seed.setY(substracted.getBlockY() + 1);

					if (CropManager.getInstance(substracted) != null) {

						if (CustomBlock.byAlreadyPlaced(substracted.getBlock()) != null) {

							CropInstance ci = CropManager.getInstance(substracted.clone());


							if (CustomBlock.byAlreadyPlaced(seed.getBlock()) != null) {
								if (CustomBlock.byAlreadyPlaced(seed.getBlock()).getNamespacedID().equalsIgnoreCase(iacs
										.getCfg("config.water_farming_station", "croper:watered_farm").toString())) {
									seed.setY(seed.getBlockY() + 1);
								}
							}
							if (CustomBlock.byAlreadyPlaced(seed.getBlock()) != null) {
								if (CustomBlock.byAlreadyPlaced(seed.getBlock()).getNamespacedID().equalsIgnoreCase(iacs
										.getCfg("config.water_farming_station", "croper:watered_farm").toString())) {
									seed.setY(seed.getBlockY() + 1);
								}
							}

							if (CustomBlock.byAlreadyPlaced(seed.getBlock()) != null) {

								if (CustomBlock.byAlreadyPlaced(seed.getBlock()).getNamespacedID()
										.contains("_stage_")) {


									String getNamespacedID = CustomBlock.byAlreadyPlaced(seed.getBlock())
											.getNamespacedID();

									String base = getNamespacedID.replaceAll("_stage_[0-9]+", "");
									

									int takedmb = iacs.getPlugin().getConfig().getInt("config.water-take");
									try {
										if (iacs.getPlugin().getConfig().getString("plants."+base+".water-take")!=null) {
											takedmb = Integer.valueOf(iacs.getCfg("plants."+base+".water-take", 0).toString());
										}
									} catch (NumberFormatException e) {
										e.printStackTrace();
									}
								

									if (getNamespacedID.endsWith("_stage_1")) {
										if (CustomBlock.getInstance(base + "_stage_2") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_2"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_2"));
										}
									}
									if (getNamespacedID.endsWith("_stage_2")) {
										if (CustomBlock.getInstance(base + "_stage_3") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_3"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_3"));
										}
									}
									if (getNamespacedID.endsWith("_stage_3")) {
										if (CustomBlock.getInstance(base + "_stage_4") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_4"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_4"));
										}
									}
									if (getNamespacedID.endsWith("_stage_4")) {
										if (CustomBlock.getInstance(base + "_stage_5") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_5"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_5"));
										}
									}
									if (getNamespacedID.endsWith("_stage_5")) {
										if (CustomBlock.getInstance(base + "_stage_6") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_6"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_6"));
										}
									}
									if (getNamespacedID.endsWith("_stage_6")) {
										if (CustomBlock.getInstance(base + "_stage_7") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_7"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_7"));
										}
									}
									if (getNamespacedID.endsWith("_stage_7")) {
										if (CustomBlock.getInstance(base + "_stage_8") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_8"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_8"));
										}
									}
									if (getNamespacedID.endsWith("_stage_8")) {
										if (CustomBlock.getInstance(base + "_stage_9") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_9"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_9"));
										}
									}
									if (getNamespacedID.endsWith("_stage_9")) {
										if (CustomBlock.getInstance(base + "_stage_10") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_10"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_10"));
										}
									}
									if (getNamespacedID.endsWith("_stage_10")) {
										if (CustomBlock.getInstance(base + "_stage_11") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_11"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_11"));
										}
									}
									if (getNamespacedID.endsWith("_stage_11")) {
										if (CustomBlock.getInstance(base + "_stage_12") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_12"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_12"));
										}
									}
									if (getNamespacedID.endsWith("_stage_12")) {
										if (CustomBlock.getInstance(base + "_stage_13") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_13"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_13"));
										}
									}
									if (getNamespacedID.endsWith("_stage_13")) {
										if (CustomBlock.getInstance(base + "_stage_14") != null) {
											ci.addSeed(CustomBlock.getInstance(base + "_stage_14"), false);
											ci.takeMB(takedmb, CustomBlock.getInstance(base + "_stage_14"));
										}
									}
								}
								// CropManager.putInstance(Bukkit.getWorld(world).getBlockAt(location).getLocation(),
								// ci);
								CropManager.saveData();
							}

						}
					}

					String user = "%%__USER__%%";

				} catch (Error e) {
		        	iacs.log(e.getMessage());
				}
			});
		});

	
	}

}
