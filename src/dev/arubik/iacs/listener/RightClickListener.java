package dev.arubik.iacs.listener;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.managers.CropManager;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;

public class RightClickListener implements Listener {

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
				b.getLocation().add(0, -1, 0).getWorld().getBlockAt(b.getLocation().add(0, -1, 0))) != null) {

			if (CustomBlock
					.byAlreadyPlaced(b.getLocation().add(0, -1, 0).getWorld().getBlockAt(b.getLocation().add(0, -1, 0)))
					.getNamespacedID()
					.equalsIgnoreCase(pl.getConfig().getString("config.water_farming_station"))) {

				CropInstance cim = CropManager.getInstance(b.getLocation().add(0, -1, 0));

				Runnable verify = new Runnable() {

					@Override
					public void run() {
						CropInstance ci = CropManager.getInstance(b.getLocation().add(0, -1, 0));
						if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
							if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
								ci.addMb(1000, e.getPlayer());
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
									ci.addMb(250, e.getPlayer());
								}
							}
						}

						for (String key : pl.getConfig().getConfigurationSection("config.items").getKeys(false)) {
							if (CustomStack.byItemStack(e.getPlayer().getInventory().getItemInMainHand()) != null) {
								if (CustomStack.byItemStack(e.getPlayer().getInventory().getItemInMainHand())
										.getNamespacedID().equalsIgnoreCase(key)) {
									CustomStack csi = CustomStack
											.byItemStack(e.getPlayer().getInventory().getItemInMainHand());

									if (pl.getConfig().getString("config.items." + key) != null) {
										if (pl.getConfig().getInt("config.items." + key) >= 1) {
											int added = pl.getConfig().getInt("config.items." + key);
											if (csi.hasUsagesAttribute() != false) {
												csi.setUsages(csi.getUsages() - 1);
												ci.addMb(added, e.getPlayer());
												return;
											} else if (csi.hasCustomDurability() == true) {
												if (csi.getDurability() > 0) {
													csi.setDurability(csi.getDurability() - 1);
													ci.addMb(added, e.getPlayer());
													return;
												}
											} else {
												if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
													e.getPlayer().getInventory().getItemInMainHand().setAmount(
															e.getPlayer().getInventory().getItemInMainHand().getAmount()
																	- 1);
													ci.addMb(added, e.getPlayer());
												}
											}
										}
									}

								}
							}
						}
					}

				};

				verify.run();

				if (e.getItem().getType() == Material.BONE_MEAL) {

					if (iacs.getCfg("config.bone-meal", "false").toString().equalsIgnoreCase("true")) {

						if (cb.getNamespacedID().endsWith("_stage_1")) {
							if (CustomBlock.getInstance(base + "_stage_2") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_2").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_2");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_2")) {
							if (CustomBlock.getInstance(base + "_stage_3") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_3").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_3");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_3")) {
							if (CustomBlock.getInstance(base + "_stage_4") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_4").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_4");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_4")) {
							if (CustomBlock.getInstance(base + "_stage_5") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_5").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_5");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_5")) {
							if (CustomBlock.getInstance(base + "_stage_6") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_6").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_6");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_6")) {
							if (CustomBlock.getInstance(base + "_stage_7") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_7").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_7");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_7")) {
							if (CustomBlock.getInstance(base + "_stage_8") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_8").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_8");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_8")) {
							if (CustomBlock.getInstance(base + "_stage_9") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_9").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_9");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_9")) {
							if (CustomBlock.getInstance(base + "_stage_10") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_10").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_10");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_10")) {
							if (CustomBlock.getInstance(base + "_stage_11") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_11").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_11");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_11")) {
							if (CustomBlock.getInstance(base + "_stage_12") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_12").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_12");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_12")) {
							if (CustomBlock.getInstance(base + "_stage_13") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_13").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_13");
								}
							}
						}
						if (cb.getNamespacedID().endsWith("_stage_13")) {
							if (CustomBlock.getInstance(base + "_stage_14") != null) {
								e.getItem().setAmount(e.getItem().getAmount() - 1);
								b.getLocation().getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 3);
								if ((((new Random()).nextFloat() + "").endsWith("3")
										|| ((new Random()).nextFloat() + "").endsWith("4"))) {
									CustomBlock.remove(b.getLocation());
									CustomBlock.getInstance(base + "_stage_14").place(b.getLocation());
									cim.setCurrentseed(base + "_stage_14");
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

				if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
					if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
						ci.addMb(1000, e.getPlayer());
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
							ci.addMb(250, e.getPlayer());
						}
					}
				}

				for (String key : pl.getConfig().getConfigurationSection("config.items").getKeys(false)) {
					if (CustomStack.byItemStack(e.getPlayer().getInventory().getItemInMainHand()) != null) {
						if (CustomStack.byItemStack(e.getPlayer().getInventory().getItemInMainHand()).getNamespacedID()
								.equalsIgnoreCase(key)) {
							CustomStack csi = CustomStack.byItemStack(e.getPlayer().getInventory().getItemInMainHand());

							if (pl.getConfig().getString("config.items." + key) != null) {
								if (pl.getConfig().getInt("config.items." + key) >= 1) {
									int added = pl.getConfig().getInt("config.items." + key);
									if (csi.hasUsagesAttribute() != false) {
										csi.setUsages(csi.getUsages() - 1);
										ci.addMb(added, e.getPlayer());
										return;
									} else if (csi.hasCustomDurability() == true) {
										if (csi.getDurability() > 0) {
											csi.setDurability(csi.getDurability() - 1);
											ci.addMb(added, e.getPlayer());
											return;
										}
									} else {
										if (e.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
											e.getPlayer().getInventory().getItemInMainHand().setAmount(
													e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
											ci.addMb(added, e.getPlayer());
										}
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

						if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
							ci.addMb(1000, e.getPlayer());
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
								ci.addMb(250, e.getPlayer());
							}
						}

						if (CustomStack.byItemStack(hand) != null) {

							if (iacs.getPlugin().getConfig().getString("config.water-identifier")
									.equalsIgnoreCase(CustomStack.byItemStack(hand).getNamespacedID())) {
								iacs.MiniMessage(
										iacs.parsePlaceholder(e.getPlayer(), e.getBlockClicked().getLocation(), iacs.getPlugin().getConfig().getString("config.water-identifier-msg"))
										, e.getPlayer()
										, 0);
							}

							for (String key : pl.getConfig().getConfigurationSection("config.items").getKeys(false)) {
								if (CustomStack.byItemStack(e.getPlayer().getInventory().getItemInMainHand()) != null) {
									if (CustomStack.byItemStack(e.getPlayer().getInventory().getItemInMainHand())
											.getNamespacedID().equalsIgnoreCase(key)) {
										CustomStack csi = CustomStack
												.byItemStack(e.getPlayer().getInventory().getItemInMainHand());

										if (pl.getConfig().getString("config.items." + key) != null) {
											if (pl.getConfig().getInt("config.items." + key) >= 1) {
												int added = pl.getConfig().getInt("config.items." + key);
												if (csi.hasUsagesAttribute() != false) {
													csi.setUsages(csi.getUsages() - 1);
													ci.addMb(added, e.getPlayer());
													return;
												} else if (csi.hasCustomDurability() == true) {
													if (csi.getDurability() > 0) {
														csi.setDurability(csi.getDurability() - 1);
														ci.addMb(added, e.getPlayer());
														return;
													}
												} else {
													if (e.getPlayer().getInventory().getItemInMainHand()
															.getAmount() >= 1) {
														e.getPlayer().getInventory().getItemInMainHand()
																.setAmount(e.getPlayer().getInventory()
																		.getItemInMainHand().getAmount() - 1);

														ci.addMb(added, e.getPlayer());
													}
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
										ci.addSeed(CustomBlock.getInstance(based + "_stage_1"));
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
