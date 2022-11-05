package dev.arubik.iacs.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Sound {
  public static void play(Player player, String sound) {
    play(player.getLocation(), sound, 1.0F, 1.0F);
  }
  
  public static void play(Player player, String sound, float volume, float pitch) {
    play(player.getLocation(), sound, volume, pitch);
  }
  
  public static void play(Location location, String sound) {
    play(location, sound, 1.0F, 1.0F);
  }
  
  public static void play(Location location, String sound, float volume, float pitch) {
    for (Player player : location.getWorld().getPlayers())
      player.playSound(location, sound, volume, pitch); 
  }
}
