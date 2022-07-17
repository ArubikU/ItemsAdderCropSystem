package dev.arubik.iacs.managers;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import dev.arubik.iacs.furnitureGet;
import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.utils.config.LineConfig;

public class newManagerIacrops {
	
	public newManagerIacrops() {
		Bukkit.getPluginManager().registerEvents(new rightClickArmorStand(), iacs.getPlugin());
	}

	
	
	public static void sendAnimation(Player player, EquipmentSlot hand) {
		final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		final PacketContainer animation = protocolManager.createPacket(PacketType.Play.Server.ANIMATION);
		animation.getIntegers().write(0, player.getEntityId());
		animation.getIntegers().write(1, (hand == EquipmentSlot.HAND) ? 0 : 3);
		protocolManager.sendServerPacket(player, animation);
	}

	public class rightClickArmorStand implements Listener {
	    @EventHandler
	    public void onUseEvent(PlayerInteractEvent e) {
	        Player player = e.getPlayer();
	        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	        if(e.getClickedBlock().getType().equals(Material.FARMLAND) == false && CustomStack.byItemStack(player.getInventory().getItemInMainHand()) == null)return;
	        String based = CustomStack.byItemStack(player.getInventory().getItemInMainHand()).getNamespacedID();
	        Block b = e.getClickedBlock();
			if (CustomFurniture.getInstance(based + "_stage_1") != null) {
				if (b.getLocation().add(0, 1, 0).getBlock() == null
						|| b.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR) ||
						CustomBlock
								.byAlreadyPlaced(b.getLocation().add(0, 1, 0).getBlock()) == null && furnitureGet.fromLocalization(b.getLocation().add(0, 1, 0).getBlock().getLocation()) == null) {
					
					furnitureGet.placeFurniture(based + "_stage_1", b.getLocation(), BlockFace.UP);
					
					player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
					
				}

			}
			
		}
		@EventHandler(ignoreCancelled = true)
		public void onPlayerClickOnFurniture(PlayerInteractEntityEvent event) {

			Entity right = event.getRightClicked();
			
			if(!(right instanceof ArmorStand) || !(right instanceof ItemFrame))return;

			if (CustomFurniture.byAlreadySpawned(right) == null)
				return;
			
			iacs pl = iacs.getPlugin();

			Block under = right.getLocation().getBlock().getRelative(BlockFace.DOWN);
			Boolean station = (CropManager.getInstance(under.getLocation()) != null);
			Block b = right.getLocation().getBlock();

			PlayerInventory pi = event.getPlayer().getInventory();

			CustomFurniture cb = CustomFurniture.byAlreadySpawned(right);

			String base = cb.getNamespacedID().toLowerCase().replace("_stage_1", "").replace("_stage_2", "")
					.replace("_stage_3", "").replace("_stage_4", "").replace("_stage_5", "").replace("_stage_6", "")
					.replace("_stage_7", "").replace("_stage_8", "").replace("_stage_9", "").replace("_stage_10", "")
					.replace("_stage_11", "").replace("_stage_12", "").replace("_stage_13", "")
					.replace("_stage_14", "");

			if (pi.getItemInMainHand().getType() == Material.BONE_MEAL) {

				if (iacs.getCfg("config.bone-meal", "false").toString().equalsIgnoreCase("true")) {
					if (cb.getNamespacedID().endsWith("_stage_1")) {
						if (CustomFurniture.getInstance(base + "_stage_2") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_2", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_2");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_2")) {
						if (CustomFurniture.getInstance(base + "_stage_3") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_3", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_3");

							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_3")) {
						if (CustomFurniture.getInstance(base + "_stage_4") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_4", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_4");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_4")) {
						if (CustomFurniture.getInstance(base + "_stage_5") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_5", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_5");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_5")) {
						if (CustomFurniture.getInstance(base + "_stage_6") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_6", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_6");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_6")) {
						if (CustomFurniture.getInstance(base + "_stage_7") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_7", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_7");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_7")) {
						if (CustomFurniture.getInstance(base + "_stage_8") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_8", under.getLocation(), null);
								// ci.setCurrentseed(base + "_stage_8");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_8")) {
						if (CustomFurniture.getInstance(base + "_stage_9") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_9", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_9");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_9")) {
						if (CustomFurniture.getInstance(base + "_stage_10") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_10", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_10");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_10")) {
						if (CustomFurniture.getInstance(base + "_stage_11") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_11", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_11");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_11")) {
						if (CustomFurniture.getInstance(base + "_stage_12") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_12", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_12");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_12")) {
						if (CustomFurniture.getInstance(base + "_stage_13") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_13", under.getLocation(), BlockFace.UP);
								// ci.setCurrentseed(base + "_stage_13");
							}
						}
					}
					if (cb.getNamespacedID().endsWith("_stage_13")) {
						if (CustomFurniture.getInstance(base + "_stage_14") != null) {
							pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
							under.getLocation().getWorld().playEffect(under.getLocation(), Effect.BONE_MEAL_USE, 3);
							if (iacs.isRandom(Integer.valueOf(iacs.getCfg("config.bone-meal-percent", 1).toString()))) {
								cb.remove(false);
								furnitureGet.placeFurniture(base + "_stage_14", under.getLocation(), BlockFace.UP);

								CropManager.getInstance(under.getLocation()).takeMB(0, cb);
								
								// ci.setCurrentseed(base + "_stage_14");
							}
						}
					}
				}
			}
			

			
			if (pi.getItemInMainHand().getType().equals(Material.POTION)) {
				

				org.bukkit.inventory.meta.PotionMeta pm = (PotionMeta) pi
						.getItemInMainHand().getItemMeta();

				if (pm.hasCustomEffects() == false) {
					if (pi.getItemInMainHand().getAmount() >= 1) {
						pi.getItemInMainHand().setAmount(
								pi.getItemInMainHand().getAmount() - 1);
						pi.addItem(new ItemStack(Material.GLASS_BOTTLE));
					}
					sprinklerManager.addWatter(0, 0, 1000, b, event.getPlayer());
				}
			}

			if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
				
				sprinklerManager.addWatter(0, 0, 1000, b, event.getPlayer());
				
				if (event.getPlayer().getInventory().getItemInMainHand().getAmount() >= 1) {
					event.getPlayer().getInventory().getItemInMainHand()
							.setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
					event.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET));
				}
			}
			
			for (String key : pl.getConfig().getConfigurationSection("config.items").getKeys(false)) {
				if(CustomStack.byItemStack(pi.getItemInMainHand()) != null) {
				if(key.equalsIgnoreCase(CustomStack.byItemStack(pi.getItemInMainHand()).getNamespacedID())){
				LineConfig line = new LineConfig((String) iacs.getCfg("config.items."+key, "{}"));
				int water = line.getInteger("water", 0);
				if(water != 0 && CropManager.contains(under.getLocation())) {
					
					sprinklerManager.addWatter(line.getInteger("radx", 0), line.getInteger("rady", 0), water, b, event.getPlayer());
					
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

	}


}
