package dev.arubik.iacs.listener;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;

import dev.arubik.iacs.furnitureGet;
import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.managers.CropManager;
import dev.arubik.iacs.managers.sprinklerManager;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import io.lumine.mythic.utils.config.LineConfig;

public class RightClickListener implements Listener {


	@EventHandler
	public void onFertilize(CustomBlockInteractEvent e) {
		if (e.getPlayer().getInventory().getItemInMainHand() == null || e.getItem() == null
				|| e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		iacs pl = iacs.getPlugin();
		Block b = e.getBlockClicked();
		
		if(CropManager.contains(b.getLocation())) {
			CropInstance ci = CropManager.getInstance(b.getLocation());
			if(ci.addableFertilizer(e.getPlayer().getInventory().getItemInMainHand())) {
				ItemStack di = ci.addFertilizer(e.getPlayer().getInventory().getItemInMainHand());
				if(di != e.getPlayer().getInventory().getItemInMainHand()) {
					e.getPlayer().sendMessage("ADA");
					e.getPlayer().getInventory().setItemInMainHand(di);
				}
			}else {
				e.getPlayer().sendMessage("ASA");
			}
			return;
		
		}
		
	}
	
	
	@SuppressWarnings("static-access")
	@EventHandler
	public void onRightClickWithWater(CustomBlockInteractEvent e) {

		if (e.getPlayer().getInventory().getItemInMainHand() == null || e.getItem() == null
				|| e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		iacs pl = iacs.getPlugin();
		CropManager cm = pl.getCm();
		Block b = e.getBlockClicked();
		CustomBlock cb = CustomBlock.byAlreadyPlaced(e.getBlockClicked());
		String base = cb.getNamespacedID().toLowerCase().replace("_stage_1", "")
				.replace("_stage_2", "")
				.replace("_stage_3", "")
				.replace("_stage_4", "")
				.replace("_stage_5", "")
				.replace("_stage_6", "")
				.replace("_stage_7", "")
				.replace("_stage_8", "")
				.replace("_stage_9", "")
				.replace("_stage_10", "")
				.replace("_stage_11", "")
				.replace("_stage_12", "")
				.replace("_stage_13", "")
				.replace("_stage_14", "");

		if (CustomBlock.byAlreadyPlaced(
				b.getLocation().clone().subtract(0, 1, 0).getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0))) != null) {


			if (CustomBlock
					.byAlreadyPlaced(b.getLocation().clone().subtract(0, 1, 0).getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)))
					.getNamespacedID()
					.equalsIgnoreCase(pl.getConfig().getString("config.farming_station"))) {

				CropInstance ci = cm.getInstance(b.getLocation().clone().subtract(0, 1, 0).getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0))
						.getLocation());
			
				
				if(iacs.getCfg("config.water-default", true).toString().equalsIgnoreCase("true")) {

				if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
					if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
						ci.addMb(Integer.valueOf(iacs.getCfg("config.WATER_BUCKET",1000).toString()), e.getPlayer());
						e.getPlayer().getInventory().getItemInMainHand()
								.setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
						e.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET));
					}

				}

				if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.POTION)) {

					org.bukkit.inventory.meta.PotionMeta pm = (PotionMeta) e.getPlayer().getInventory()
							.getItemInMainHand().getItemMeta();

					if (pm.hasCustomEffects() == false) {
						if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
							e.getPlayer().getInventory().getItemInMainHand().setAmount(
									e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
							e.getPlayer().getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));
							ci.addMb(Integer.valueOf(iacs.getCfg("config.WATER_BOTTLE",250).toString()), e.getPlayer());
						}
					}
				}
				}

				PlayerInventory pi = e.getPlayer().getInventory();
				
				for (String key : pl.getConfig().getConfigurationSection("config.items").getKeys(false)) {
					if(CustomStack.byItemStack(pi.getItemInMainHand()) != null) {
					if(key.equalsIgnoreCase(CustomStack.byItemStack(pi.getItemInMainHand()).getNamespacedID())){
					LineConfig line = new LineConfig((String) iacs.getCfg("config.items."+key, "{}"));
					int water = line.getInteger("water", 0);
					if(water != 0) {
						sprinklerManager.addWatter(line.getInteger("radx", 0), line.getInteger("rady", 0), water, b, e.getPlayer());
						if(line.getKey().equalsIgnoreCase("toItem:")) {
							if(CustomStack.getInstance(line.getString("toItem", "null")) != null) {
								if(pi.getItemInMainHand().getAmount() == 1) {
									pi.setItemInMainHand(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
								}else {
									pi.addItem(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
									pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
								}
							}
						}
						if(line.getKey().equalsIgnoreCase("reduceAmount:")) {
							if(pi.getItemInMainHand().getAmount() == 1) {
								if(line.getString("return", "null") != "null") {
									if(line.getString("return", "null").startsWith("MC:")) {
										pi.setItemInMainHand(new ItemStack(Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
									}else {
										CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
										pi.setItemInMainHand(css.getItemStack());
									}
								}else {
									pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
								}
							}else {
								pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
								if(line.getString("return", "null") != "null") {
									if(line.getString("return", "null").startsWith("MC:")) {
										pi.addItem(new ItemStack(Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
									}else {
										CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
										pi.addItem(css.getItemStack());
									}
								}
							}
						}
						if(line.getKey().equalsIgnoreCase("reduceUsages:")) {
							int usages = line.getInteger("usages", 1);
							CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
							css.reduceUsages(usages);
							if(line.getBoolean("remove", false) == true) {
								if(css.getUsages() < 1) {
									pi.getItemInMainHand().setAmount(0);
								}
							}else {
							ItemStack used = css.getItemStack();
							used.addEnchantments(pi.getItemInMainHand().getEnchantments());
							used.setAmount(pi.getItemInMainHand().getAmount());
							used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
							pi.setItemInMainHand(used);
							}
						}
						if(line.getKey().equalsIgnoreCase("reduceDurability:")) {
							int usages = line.getInteger("durability", 1);
							CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
							css.setDurability(css.getDurability() - usages);
							if(line.getBoolean("remove", false) == true) {
								if(css.getDurability() < 1) {
									pi.getItemInMainHand().setAmount(0);
								}
							}else {
							ItemStack used = css.getItemStack();
							used.addEnchantments(pi.getItemInMainHand().getEnchantments());
							used.setAmount(pi.getItemInMainHand().getAmount());
							used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
							pi.setItemInMainHand(used);
							}
						}
					}
					
				}
				}
			}

				
			}
			
			if (CustomBlock
					.byAlreadyPlaced(b.getLocation().clone().subtract(0, 1, 0).getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)))
					.getNamespacedID()
					.equalsIgnoreCase(pl.getConfig().getString("config.water_farming_station"))) {
				CropInstance ci = CropManager.getInstance(b.getLocation().clone().subtract(0, 1, 0).getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0))
						.getLocation());
				

				if(iacs.getCfg("config.water-default", true).toString().equalsIgnoreCase("true")) {
						if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
							if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
								ci.addMb(Integer.valueOf(iacs.getCfg("config.WATER_BUCKET",1000).toString()), e.getPlayer());
								e.getPlayer().getInventory().getItemInMainHand()
										.setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
								e.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET));
							}

						}

						if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.POTION)) {

							org.bukkit.inventory.meta.PotionMeta pm = (PotionMeta) e.getPlayer().getInventory()
									.getItemInMainHand().getItemMeta();

							if (pm.hasCustomEffects() == false) {
								if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
									e.getPlayer().getInventory().getItemInMainHand().setAmount(
											e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
									e.getPlayer().getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));
									ci.addMb(Integer.valueOf(iacs.getCfg("config.WATER_BOTTLE",250).toString()), e.getPlayer());
								}
							}
						}
				}

						PlayerInventory pi = e.getPlayer().getInventory();
						
						for (String key : pl.getConfig().getConfigurationSection("config.items").getKeys(false)) {
							if(CustomStack.byItemStack(pi.getItemInMainHand()) != null) {
							if(key.equalsIgnoreCase(CustomStack.byItemStack(pi.getItemInMainHand()).getNamespacedID())){
							LineConfig line = new LineConfig((String) iacs.getCfg("config.items."+key, "{}"));
							int water = line.getInteger("water", 0);
							if(water != 0) {
								sprinklerManager.addWatter(line.getInteger("radx", 0), line.getInteger("rady", 0), water, b, e.getPlayer());
								if(line.getKey().equalsIgnoreCase("toItem:")) {
									if(CustomStack.getInstance(line.getString("toItem", "null")) != null) {
										if(pi.getItemInMainHand().getAmount() == 1) {
											pi.setItemInMainHand(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
										}else {
											pi.addItem(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
											pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
										}
									}
								}
								if(line.getKey().equalsIgnoreCase("reduceAmount:")) {
									if(pi.getItemInMainHand().getAmount() == 1) {
										if(line.getString("return", "null") != "null") {
											if(line.getString("return", "null").startsWith("MC:")) {
												pi.setItemInMainHand(new ItemStack(Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
											}else {
												CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
												pi.setItemInMainHand(css.getItemStack());
											}
										}else {
											pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
										}
									}else {
										pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
										if(line.getString("return", "null") != "null") {
											if(line.getString("return", "null").startsWith("MC:")) {
												pi.addItem(new ItemStack(Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
											}else {
												CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
												pi.addItem(css.getItemStack());
											}
										}
									}
								}
								if(line.getKey().equalsIgnoreCase("reduceUsages:")) {
									int usages = line.getInteger("usages", 1);
									CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
									css.reduceUsages(usages);
									if(line.getBoolean("remove", false) == true) {
										if(css.getUsages() < 1) {
											pi.getItemInMainHand().setAmount(0);
										}
									}else {
									ItemStack used = css.getItemStack();
									used.addEnchantments(pi.getItemInMainHand().getEnchantments());
									used.setAmount(pi.getItemInMainHand().getAmount());
									used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
									pi.setItemInMainHand(used);
									}
								}
								if(line.getKey().equalsIgnoreCase("reduceDurability:")) {
									int usages = line.getInteger("durability", 1);
									CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
									css.setDurability(css.getDurability() - usages);
									if(line.getBoolean("remove", false) == true) {
										if(css.getDurability() < 1) {
											pi.getItemInMainHand().setAmount(0);
										}
									}else {
									ItemStack used = css.getItemStack();
									used.addEnchantments(pi.getItemInMainHand().getEnchantments());
									used.setAmount(pi.getItemInMainHand().getAmount());
									used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
									pi.setItemInMainHand(used);
									}
								}
							}
							}
							
						}
						}

				if (e.getItem().getType() == Material.BONE_MEAL) {

					if (iacs.getCfg("config.bone-meal", "false").toString().equalsIgnoreCase("true")) {

						if (cb.getNamespacedID().endsWith("_stage_1")) {
							if (CustomBlock.getInstance(base + "_stage_2") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_2").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_2");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_2")) {
							if (CustomBlock.getInstance(base + "_stage_3") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_3").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_3");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_3")) {
							if (CustomBlock.getInstance(base + "_stage_4") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_4").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_4");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_4")) {
							if (CustomBlock.getInstance(base + "_stage_5") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_5").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_5");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_5")) {
							if (CustomBlock.getInstance(base + "_stage_6") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_6").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_6");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_6")) {
							if (CustomBlock.getInstance(base + "_stage_7") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_7").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_7");
									
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_7")) {
							if (CustomBlock.getInstance(base + "_stage_8") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_8").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_8");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_8")) {
							if (CustomBlock.getInstance(base + "_stage_9") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_9").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_9");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_9")) {
							if (CustomBlock.getInstance(base + "_stage_10") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_10").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_10");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_10")) {
							if (CustomBlock.getInstance(base + "_stage_11") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_11").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_11");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_11")) {
							if (CustomBlock.getInstance(base + "_stage_12") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_12").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_12");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_12")) {
							if (CustomBlock.getInstance(base + "_stage_13") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_13").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_13");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_13")) {
							if (CustomBlock.getInstance(base + "_stage_14") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_14").place(b.getLocation());
									ci.setCurrentseed(base + "_stage_14");
								}
							}
						}
					}
				}
			}

		}

		if (pl.getConfig().getString("config.farming_station") != null) {

			if (cb.getNamespacedID().equalsIgnoreCase(pl.getConfig().getString("config.farming_station"))) {
				CropInstance ci = new CropInstance(b.getLocation(), 0);


				if(iacs.getCfg("config.water-default", true).toString().equalsIgnoreCase("true")) {
				if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
					if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
						ci.addMb(Integer.valueOf(iacs.getCfg("config.WATER_BUCKET",1000).toString()), e.getPlayer());
						e.getPlayer().getInventory().getItemInMainHand()
								.setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
						e.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET));
					}

				}

				if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.POTION)) {

					org.bukkit.inventory.meta.PotionMeta pm = (PotionMeta) e.getPlayer().getInventory()
							.getItemInMainHand().getItemMeta();

					if (pm.hasCustomEffects() == false) {
						if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
							e.getPlayer().getInventory().getItemInMainHand()
									.setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
							e.getPlayer().getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));
							ci.addMb(Integer.valueOf(iacs.getCfg("config.WATER_BOTTLE",250).toString()), e.getPlayer());
						}
					}
				}
				}

				PlayerInventory pi = e.getPlayer().getInventory();
				
				for (String key : pl.getConfig().getConfigurationSection("config.items").getKeys(false)) {
					if(CustomStack.byItemStack(pi.getItemInMainHand()) != null) {
					if(key.equalsIgnoreCase(CustomStack.byItemStack(pi.getItemInMainHand()).getNamespacedID())){
					LineConfig line = new LineConfig((String) iacs.getCfg("config.items."+key, "{}"));
					int water = line.getInteger("water", 0);
					if(water != 0) {
						sprinklerManager.addWatter(line.getInteger("radx", 0), line.getInteger("rady", 0), water, b, e.getPlayer());
						if(line.getKey().equalsIgnoreCase("toItem:")) {
							if(CustomStack.getInstance(line.getString("toItem", "null")) != null) {
								if(pi.getItemInMainHand().getAmount() == 1) {
									pi.setItemInMainHand(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
								}else {
									pi.addItem(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
									pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
								}
							}
						}
						if(line.getKey().equalsIgnoreCase("reduceAmount:")) {
							if(pi.getItemInMainHand().getAmount() == 1) {
								if(line.getString("return", "null") != "null") {
									if(line.getString("return", "null").startsWith("MC:")) {
										pi.setItemInMainHand(new ItemStack(Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
									}else {
										CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
										pi.setItemInMainHand(css.getItemStack());
									}
								}else {
									pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
								}
							}else {
								pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
								if(line.getString("return", "null") != "null") {
									if(line.getString("return", "null").startsWith("MC:")) {
										pi.addItem(new ItemStack(Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
									}else {
										CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
										pi.addItem(css.getItemStack());
									}
								}
							}
						}
						if(line.getKey().equalsIgnoreCase("reduceUsages:")) {
							int usages = line.getInteger("usages", 1);
							CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
							css.reduceUsages(usages);
							if(line.getBoolean("remove", false) == true) {
								if(css.getUsages() < 1) {
									pi.getItemInMainHand().setAmount(0);
								}
							}else {
							ItemStack used = css.getItemStack();
							used.addEnchantments(pi.getItemInMainHand().getEnchantments());
							used.setAmount(pi.getItemInMainHand().getAmount());
							used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
							pi.setItemInMainHand(used);
							}
						}
						if(line.getKey().equalsIgnoreCase("reduceDurability:")) {
							int usages = line.getInteger("durability", 1);
							CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
							css.setDurability(css.getDurability() - usages);
							if(line.getBoolean("remove", false) == true) {
								if(css.getDurability() < 1) {
									pi.getItemInMainHand().setAmount(0);
								}
							}else {
							ItemStack used = css.getItemStack();
							used.addEnchantments(pi.getItemInMainHand().getEnchantments());
							used.setAmount(pi.getItemInMainHand().getAmount());
							used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
							pi.setItemInMainHand(used);
							}
						}
					}
					}
					
				}
			}

			} else if (cb.getNamespacedID()
					.equalsIgnoreCase(pl.getConfig().getString("config.water_farming_station"))) {
				try {
					CropInstance ci = cm.getInstance(b.getLocation());

					if (e.getPlayer().getInventory().getItemInMainHand() != null) {
						ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();

						if(iacs.getCfg("config.water-default", true).toString().equalsIgnoreCase("true")) {
						if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
							ci.addMb(Integer.valueOf(iacs.getCfg("config.WATER_BUCKET",1000).toString()), e.getPlayer());
							if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
								e.getPlayer().getInventory().getItemInMainHand()
										.setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
								e.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET));
							}
						}

						if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.POTION)) {

							org.bukkit.inventory.meta.PotionMeta pm = (PotionMeta) e.getPlayer().getInventory()
									.getItemInMainHand().getItemMeta();

							if (pm.hasCustomEffects() == false) {
								if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
									e.getPlayer().getInventory().getItemInMainHand().setAmount(
											e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
									e.getPlayer().getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));
								}
								ci.addMb(Integer.valueOf(iacs.getCfg("config.WATER_BOTTLE",250).toString()), e.getPlayer());
							}
						}
						}

						if (CustomStack.byItemStack(hand) != null) {

							if (iacs.getPlugin().getConfig().getString("config.water-identifier")
									.equalsIgnoreCase(CustomStack.byItemStack(hand).getNamespacedID())) {
								
								if(iacs.getPlugin().getConfig().getString("config.water-identifier-msg").split("<br>").length >0) {
									for(String d : iacs.getPlugin().getConfig().getString("config.water-identifier-msg").split("<br>")) {

										iacs.MiniMessage(
												iacs.parsePlaceholder(e.getPlayer(), e.getBlockClicked().getLocation(), d)
												, e.getPlayer()
												, 0);
									};
								}else {

									iacs.MiniMessage(
											iacs.parsePlaceholder(e.getPlayer(), e.getBlockClicked().getLocation(), iacs.getPlugin().getConfig().getString("config.water-identifier-msg"))
											, e.getPlayer()
											, 0);
								}
								
							}

							PlayerInventory pi = e.getPlayer().getInventory();
							
							for (String key : pl.getConfig().getConfigurationSection("config.items").getKeys(false)) {
								if(CustomStack.byItemStack(pi.getItemInMainHand()) != null) {
								if(key.equalsIgnoreCase(CustomStack.byItemStack(pi.getItemInMainHand()).getNamespacedID())){
								LineConfig line = new LineConfig((String) iacs.getCfg("config.items."+key, "{}"));
								int water = line.getInteger("water", 0);
								if(water != 0) {
									sprinklerManager.addWatter(line.getInteger("radx", 0), line.getInteger("rady", 0), water, b, e.getPlayer());
									if(line.getKey().equalsIgnoreCase("toItem:")) {
										if(CustomStack.getInstance(line.getString("toItem", "null")) != null) {
											if(pi.getItemInMainHand().getAmount() == 1) {
												pi.setItemInMainHand(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
											}else {
												pi.addItem(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
												pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
											}
										}
									}
									if(line.getKey().equalsIgnoreCase("reduceAmount:")) {
										if(pi.getItemInMainHand().getAmount() == 1) {
											if(line.getString("return", "null") != "null") {
												if(line.getString("return", "null").startsWith("MC:")) {
													pi.setItemInMainHand(new ItemStack(Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
												}else {
													CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
													pi.setItemInMainHand(css.getItemStack());
												}
											}else {
												pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
											}
										}else {
											pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
											if(line.getString("return", "null") != "null") {
												if(line.getString("return", "null").startsWith("MC:")) {
													pi.addItem(new ItemStack(Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
												}else {
													CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
													pi.addItem(css.getItemStack());
												}
											}
										}
									}
									if(line.getKey().equalsIgnoreCase("reduceUsages:")) {
										int usages = line.getInteger("usages", 1);
										CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
										css.reduceUsages(usages);
										if(line.getBoolean("remove", false) == true) {
											if(css.getUsages() < 1) {
												pi.getItemInMainHand().setAmount(0);
											}
										}else {
										ItemStack used = css.getItemStack();
										used.addEnchantments(pi.getItemInMainHand().getEnchantments());
										used.setAmount(pi.getItemInMainHand().getAmount());
										used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
										pi.setItemInMainHand(used);
										}
									}
									if(line.getKey().equalsIgnoreCase("reduceDurability:")) {
										int usages = line.getInteger("durability", 1);
										CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
										css.setDurability(css.getDurability() - usages);
										if(line.getBoolean("remove", false) == true) {
											if(css.getDurability() < 1) {
												pi.getItemInMainHand().setAmount(0);
											}
										}else {
										ItemStack used = css.getItemStack();
										used.addEnchantments(pi.getItemInMainHand().getEnchantments());
										used.setAmount(pi.getItemInMainHand().getAmount());
										used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
										pi.setItemInMainHand(used);
										}
									}
								}
								}
								
							}
							}

							if (CustomStack.byItemStack(hand).getNamespacedID().toLowerCase().endsWith("_seed")) {
								String based = CustomStack.byItemStack(hand).getNamespacedID().toLowerCase()
										.replace("_seed", "");
								if (CustomBlock.getInstance(based + "_stage_1") != null) {

									if (b.getLocation().add(0, 1, 0).getBlock() == null
											|| b.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR) ||
											CustomBlock
													.byAlreadyPlaced(b.getLocation().add(0, 1, 0).getBlock()) == null) {
										CustomBlock.getInstance(based + "_stage_1").place(b.getLocation().add(0, 1, 0));
										hand.setAmount(hand.getAmount() - 1);
										ci.addSeed(CustomBlock.getInstance(based + "_stage_1"), true);
									}

								}
								if (CustomFurniture.getInstance(based + "_stage_1") != null) {
									if (b.getLocation().add(0, 1, 0).getBlock() == null
											|| b.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR) ||
											CustomBlock
													.byAlreadyPlaced(b.getLocation().add(0, 1, 0).getBlock()) == null) {
										
										furnitureGet.placeFurniture(based + "_stage_1", b.getLocation(), BlockFace.UP);
										
										hand.setAmount(hand.getAmount() - 1);
										
									}

								}
							}
						}

					}

				} catch (Error error) {
					error.printStackTrace();
				}
			}
		}
	}

}
