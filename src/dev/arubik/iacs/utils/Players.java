package dev.arubik.iacs.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import dev.arubik.iacs.iacs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class Players {
  @Nullable
  public static Player getNullable(UUID uuid) {
    return Bukkit.getPlayer(uuid);
  }
  
  public static Optional<Player> get(UUID uuid) {
    return Optional.ofNullable(getNullable(uuid));
  }
  
  @Nullable
  public static Player getNullable(String username) {
    return Bukkit.getPlayerExact(username);
  }
  
  public static Optional<Player> get(String username) {
    return Optional.ofNullable(getNullable(username));
  }
  
  public static Collection<Player> all() {
    return (Collection<Player>) Bukkit.getOnlinePlayers();
  }
  
  public static Stream<Player> stream() {
    return all().stream();
  }
  
  public static void forEach(Consumer<Player> consumer) {
    all().forEach(consumer);
  }
  
  public static void forEachIfPlayer(Iterable<Object> objects, Consumer<Player> consumer) {
    for (Object o : objects) {
      if (o instanceof Player)
        consumer.accept((Player)o); 
    } 
  }
  
  public static Stream<Player> streamInRange(Location center, double radius) {
    return center.getWorld().getNearbyEntities(center, radius, radius, radius).stream()
      .filter(e -> e instanceof Player)
      .map(e -> (Player)e);
  }
  
  public static void forEachInRange(Location center, double radius, Consumer<Player> consumer) {
    streamInRange(center, radius).forEach(consumer);
  }
  
  public static void msg(CommandSender sender, String... msgs) {
    for (String s : msgs)
    	iacs.MiniMessage(s, sender, 0);
  }
  
  @Nullable
  public static OfflinePlayer getOfflineNullable(UUID uuid) {
    return Bukkit.getOfflinePlayer(uuid);
  }
  
  public static Optional<OfflinePlayer> getOffline(UUID uuid) {
    return Optional.ofNullable(getOfflineNullable(uuid));
  }
  
  @Nullable
  public static OfflinePlayer getOfflineNullable(String username) {
    return Bukkit.getOfflinePlayer(username);
  }
  
  public static Optional<OfflinePlayer> getOffline(String username) {
    return Optional.ofNullable(getOfflineNullable(username));
  }
  
  public static Collection<OfflinePlayer> allOffline() {
	  Collection<OfflinePlayer> p = new ArrayList<OfflinePlayer>();
	  for(OfflinePlayer pa:Bukkit.getOfflinePlayers()) {
		  p.add(pa);
	  }
    return p;
  }
  
  public static Stream<OfflinePlayer> streamOffline() {
    return Arrays.stream(Bukkit.getOfflinePlayers());
  }
  
  public static void forEachOffline(Consumer<OfflinePlayer> consumer) {
    for (OfflinePlayer player : Bukkit.getOfflinePlayers())
      consumer.accept(player); 
  }
  
  public static void playSound(Player player, Sound sound) {
    player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
  }
  
  public static void playSound(Player player, Location location, Sound sound) {
    player.playSound(location, sound, 1.0F, 1.0F);
  }
  
  public static void playSound(Location location, Sound sound) {
    location.getWorld().playSound(location, sound, 1.0F, 1.0F);
  }
  
  public static void sendBlockChange(Player player, Location loc, Material type, int data) {
    player.sendBlockChange(loc, type, (byte)data);
  }
  
  public static void sendBlockChange(Player player, Block block, Material type, int data) {
    sendBlockChange(player, block.getLocation(), type, data);
  }
  
  public static void sendBlockChange(Player player, Location loc, Material type) {
    sendBlockChange(player, loc, type, 0);
  }
  
  public static void sendBlockChange(Player player, Block block, Material type) {
    sendBlockChange(player, block, type, 0);
  }
  
  public static void spawnParticle(Player player, Location location, Particle particle) {
    player.spawnParticle(particle, location, 1);
  }
  
  public static void spawnParticle(Location location, Particle particle) {
    location.getWorld().spawnParticle(particle, location, 1);
  }
  
  public static void spawnParticle(Player player, Location location, Particle particle, int amount) {
    Preconditions.checkArgument((amount > 0), "amount > 0");
    player.spawnParticle(particle, location, amount);
  }
  
  public static void spawnParticle(Location location, Particle particle, int amount) {
    Preconditions.checkArgument((amount > 0), "amount > 0");
    location.getWorld().spawnParticle(particle, location, amount);
  }
  
  public static void spawnParticleOffset(Player player, Location location, Particle particle, double offset) {
    player.spawnParticle(particle, location, 1, offset, offset, offset);
  }
  
  public static void spawnParticleOffset(Location location, Particle particle, double offset) {
    location.getWorld().spawnParticle(particle, location, 1, offset, offset, offset);
  }
  
  public static void spawnParticleOffset(Player player, Location location, Particle particle, int amount, double offset) {
    Preconditions.checkArgument((amount > 0), "amount > 0");
    player.spawnParticle(particle, location, amount, offset, offset, offset);
  }
  
  public static void spawnParticleOffset(Location location, Particle particle, int amount, double offset) {
    Preconditions.checkArgument((amount > 0), "amount > 0");
    location.getWorld().spawnParticle(particle, location, amount, offset, offset, offset);
  }
  
  public static void spawnEffect(Player player, Location location, Effect effect) {
    player.playEffect(location, effect, null);
  }
  
  public static void spawnEffect(Location location, Effect effect) {
    location.getWorld().playEffect(location, effect, null);
  }
  
  public static void spawnEffect(Player player, Location location, Effect effect, int amount) {
    Preconditions.checkArgument((amount > 0), "amount > 0");
    for (int i = 0; i < amount; i++)
      player.playEffect(location, effect, null); 
  }
  
  public static void spawnEffect(Location location, Effect effect, int amount) {
    Preconditions.checkArgument((amount > 0), "amount > 0");
    for (int i = 0; i < amount; i++)
      location.getWorld().playEffect(location, effect, null); 
  }
  
  public static void resetWalkSpeed(Player player) {
    player.setWalkSpeed(0.2F);
  }
  
  public static void resetFlySpeed(Player player) {
    player.setFlySpeed(0.1F);
  }
  
  private Players() {
    throw new UnsupportedOperationException("This class cannot be instantiated");
  }
}