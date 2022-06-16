package dev.arubik.iacs.skills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev.arubik.iacs.iacs;
import dev.arubik.iacs.Crops.CropInstance;
import dev.arubik.iacs.managers.CropManager;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.utils.config.LineConfig;
import io.lumine.mythic.utils.particles.ParticleBuilder;
import io.lumine.mythic.utils.sound.Sound;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.ContainerManager;

public class Skills implements Listener {
	public Skills() {
	}

	@EventHandler
	public void onSkill(SkillEvent e) {

		if (e.isCancelled() == true) {
			return;
		}

		LineConfig line = e.getLc();
		Location loc = e.getLoc();
		Player p = e.getPlayer();

		PlayerInventory pi = p.getInventory();
		if (line.getKey().equalsIgnoreCase("toItem:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			if (line.getString("take-water") != null) {
				if (loc.getBlock() != null) {
					if (loc.getBlock().getBlockData() instanceof org.bukkit.block.data.Levelled) {
						org.bukkit.block.data.Levelled data = (org.bukkit.block.data.Levelled) loc.getBlock()
								.getBlockData();

						if (data.getLevel() - line.getInteger("take-water") <= 0) {
							Bukkit.getScheduler().runTask(iacs.getPlugin(), () -> {
								loc.getBlock().setType(Material.CAULDRON);
								Block b = loc.getBlock();
								b.setType(Material.CAULDRON);
								BlockData bd = b.getBlockData();
								p.sendBlockChange(loc, bd);
							});
						} else {
							data.setLevel(data.getLevel() - line.getInteger("take-water"));
							Bukkit.getScheduler().runTask(iacs.getPlugin(), () -> {
								loc.getBlock().setBlockData(data);
								p.sendBlockChange(loc, data);
							});
						}
					}
				}
			}

			if (CustomStack.getInstance(line.getString("toItem", "null")) != null) {
				if (pi.getItemInMainHand().getAmount() == 1) {
					pi.setItemInMainHand(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
				} else {
					pi.addItem(CustomStack.getInstance(line.getString("toItem", "null")).getItemStack());
					pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
				}
			}
		}
		if (line.getKey().equalsIgnoreCase("giveItem:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			if (Material.valueOf(line.getString("item")) != null) {
				ItemStack item = new ItemStack(Material.valueOf(line.getString("item")));
				item.setAmount(line.getInteger("amount", 0));
				p.getInventory().addItem(item);
			} else {

				ItemStack item = CustomStack.getInstance(line.getString("item")).getItemStack();
				item.setAmount(line.getInteger("amount", 0));
				p.getInventory().addItem(item);
			}

		}
		if (line.getKey().equalsIgnoreCase("drop:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			
			if (CustomStack.getInstance(line.getString("item")) != null) {
				ItemStack item = CustomStack.getInstance(line.getString("item")).getItemStack();
				item.setAmount(line.getInteger("amount", 0));
				Bukkit.getScheduler().runTask(iacs.getPlugin(), () -> {
					loc.getWorld().dropItem(loc, item);
				});
			} else {
				ItemStack item = new ItemStack(Material.valueOf(line.getString("item"))); 
				item.setAmount(line.getInteger("amount", 0));
				Bukkit.getScheduler().runTask(iacs.getPlugin(), () -> {
					loc.getWorld().dropItem(loc, item);
				});
			}

		}

		if (line.getKey().equalsIgnoreCase("reduceAmount:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			if (pi.getItemInMainHand().getAmount() == 1) {
				if (line.getString("return", "null") != "null") {
					if (line.getString("return", "null").startsWith("MC:")) {
						pi.setItemInMainHand(new ItemStack(
								Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
					} else {
						CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
						pi.setItemInMainHand(css.getItemStack());
					}
				} else {
					pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
				}
			} else {
				pi.getItemInMainHand().setAmount(pi.getItemInMainHand().getAmount() - 1);
				if (line.getString("return", "null") != "null") {
					if (line.getString("return", "null").startsWith("MC:")) {
						pi.addItem(new ItemStack(
								Material.valueOf(line.getString("return", "null").replaceFirst("MC:", ""))));
					} else {
						CustomStack css = CustomStack.getInstance(line.getString("return", "null"));
						pi.addItem(css.getItemStack());
					}
				}
			}
		}
		if (line.getKey().equalsIgnoreCase("reduceUsages:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			int usages = line.getInteger("usages", 1);
			CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
			css.reduceUsages(usages);
			if (line.getBoolean("remove", false) == true) {
				if (css.getUsages() < 1) {
					pi.getItemInMainHand().setAmount(0);
				}
			} else {
				ItemStack used = css.getItemStack();
				used.addEnchantments(pi.getItemInMainHand().getEnchantments());
				used.setAmount(pi.getItemInMainHand().getAmount());
				used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
				pi.setItemInMainHand(used);
			}
		}
		if (line.getKey().equalsIgnoreCase("reduceDurability:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			int usages = line.getInteger("durability", 1);
			CustomStack css = CustomStack.byItemStack(pi.getItemInMainHand());
			css.setDurability(css.getDurability() - usages);
			if (line.getBoolean("remove", false) == true) {
				if (css.getDurability() < 1) {
					pi.getItemInMainHand().setAmount(0);
				}
			} else {
				ItemStack used = css.getItemStack();
				used.addEnchantments(pi.getItemInMainHand().getEnchantments());
				used.setAmount(pi.getItemInMainHand().getAmount());
				used.getItemMeta().setDisplayName(pi.getItemInMainHand().getItemMeta().getDisplayName());
				pi.setItemInMainHand(used);
			}
		}

		if (line.getKey().equalsIgnoreCase("particle:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), new Runnable() {
				@Override
				public void run() {
					ParticleBuilder pb = ParticleBuilder.of(io.lumine.mythic.utils.particles.Particle
							.valueOf(line.getString("particle", "WATER_SPLASH")));
					loc.setX(loc.getX() + line.getFloat("offset_x", 0));
					loc.setY(loc.getY() + line.getFloat("offset_y", 0));
					loc.setZ(loc.getZ() + line.getFloat("offset_z", 0));
					pb = pb.amount(line.getInteger("amount", 5));
					pb = pb.speed(line.getFloat("speed", 1));
					pb = pb.useExactOffsets(line.getBoolean("exactOffset", false));
					pb = pb.at(loc);
					pb = pb.offset(line.getFloat("offest-nsf", 0));
					pb.send(loc);
				}
			}, line.getLong("delay", 0));
		}

		if (line.getKey().equalsIgnoreCase("instance:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if (CropManager.getInstance(loc) != null) {
						CropInstance ci = CropManager.getInstance(loc);

						if (line.getInteger("add-take-water", 0) != 0) {
							ci.modMB(line.getInteger("add-take-water", 0));
						}
					}
				}
			}, line.getLong("delay", 0));
		}

		String user = "%%__USER__%%";
		if (line.getKey().equalsIgnoreCase("sound:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), new Runnable() {
				@Override
				public void run() {
					try {

						if (iacs.mythiclib()) {
							Sound.play(loc, line.getString("sound", "ambient.underwater.enter"),
									line.getFloat("volume", 2), line.getFloat("pitch", 1));

						} else {

							loc.getWorld().playSound(loc, line.getString("sound", "ambient.underwater.enter"),
									line.getFloat("volume", 2), line.getFloat("pitch", 1));

						}
					} catch (Exception | Error e) {
						iacs.log("[IACroper] try with ambient.underwater.enter ");
					}
				}
			}, line.getLong("delay", 0));
		}

		if (line.getKey().equalsIgnoreCase("actionbar:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			String message = line.getString("message", "<red>deluxe message");
			iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 1);

		}
		if (line.getKey().equalsIgnoreCase("msg:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			String message = line.getString("message", "<red>deluxe message");
			iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 0);

		}
		if (line.getKey().equalsIgnoreCase("system:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			String message = line.getString("message", "<red>deluxe message");
			iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 5);

		}
		if (line.getKey().equalsIgnoreCase("tittle:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			String message = line.getString("message", "<red>deluxe message");
			iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 2);

		}
		if (line.getKey().equalsIgnoreCase("subtittle:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			String message = line.getString("message", "<red>deluxe message");
			iacs.MiniMessage(iacs.parsePlaceholder(p, loc, message), p, 3);

		}
		if (line.getKey().equalsIgnoreCase("command:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			Bukkit.getScheduler().runTask(iacs.getPlugin(), () -> {
				String message = line.getString("command", "say yeah");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), iacs.parsePlaceholder(p, loc, message));
			});

		}
		if (line.getKey().equalsIgnoreCase("placeCustomBlock:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			Bukkit.getScheduler().runTask(iacs.getPlugin(), () -> {

				if (CustomBlock.getInstance(line.getString("block")) != null) {

					if (CustomBlock.byAlreadyPlaced(loc.getBlock()) != null) {
						CustomBlock.remove(loc);
					}

					CustomBlock.getInstance(line.getString("block")).place(loc);
				}

			});

		}
		if (line.getKey().equalsIgnoreCase("placeVanillaBlock:")) {

			e.setCancelled(line.getBoolean("cancel-event", false));
			Bukkit.getScheduler().runTask(iacs.getPlugin(), () -> {
				String message = line.getString("command", "say yeah");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), iacs.parsePlaceholder(p, loc, message));

				if (Material.valueOf(line.getString("block")) != null) {

					if (CustomBlock.byAlreadyPlaced(loc.getBlock()) != null) {
						CustomBlock.remove(loc);
					}

					loc.getBlock().setType(Material.valueOf(line.getString("block")));
				}

			});

		}

		if (Bukkit.getPluginManager().getPlugin("PandeLoot") != null) {
			if (Bukkit.getPluginManager().isPluginEnabled("PandeLoot")) {
				if (line.getKey().equalsIgnoreCase("pandeloot:")) {

					e.setCancelled(line.getBoolean("cancel-event", false));
					Bukkit.getScheduler().runTaskLater(iacs.getPlugin(), new Runnable() {
						@Override
						public void run() {
							Location locp = e.getPlayer().getLocation();

							if (line.getBoolean("block-location", false)) {
								locp = loc;
							}
							LootDrop ld = new LootDrop(ContainerManager.get(line.getString("lootbag", "null")),
									e.getPlayer(), locp);
							ld.build().drop();
						}
					}, line.getLong("delay", 0));

				}
			}
		}
		


	}
}
