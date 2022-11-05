package dev.arubik.iacs.utils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class ParticleBuilder {
  protected final Particle particle;
  
  public static ParticleBuilder of(Particle particle) {
    return new ParticleBuilder(particle);
  }
  
  protected Location location = null;
  
  protected Object particleData = null;
  
  protected int amount = 1;
  
  protected float speed = 0.0F;
  
  protected float offsetX = 0.0F;
  
  protected float offsetY = 0.0F;
  
  protected float offsetZ = 0.0F;
  
  protected boolean useExactOffsets;
  
  protected Vector towards = null;
  
  public ParticleBuilder(Particle particle2) {
    this.particle = particle2;
  }
  
  public ParticleBuilder withData(Object data) {
    if (data.getClass().isAssignableFrom(this.particle.toBukkitParticle().getDataType())) {
      this.particleData = data;
      return this;
    } 
    if (data instanceof Material) {
      Material material = (Material)data;
      if (this.particle.toBukkitParticle().getDataType().equals(ItemStack.class)) {
        try {
          this.particleData = new ItemStack(material);
        } catch (Exception ex) {
          this.particleData = new ItemStack(Material.STONE);
        } 
      } else if (this.particle.toBukkitParticle().getDataType().equals(BlockData.class)) {
        try {
          this.particleData = Bukkit.getServer().createBlockData(material);
        } catch (Exception ex) {
          this.particleData = Bukkit.getServer().createBlockData(Material.STONE);
        } 
      } else if (this.particle.toBukkitParticle().getDataType().equals(MaterialData.class)) {
        try {
          this.particleData = material.getData();
        } catch (Exception ex) {
          this.particleData = Material.STONE.getData();
        } 
      } 
    } 
    if (this.particle.toBukkitParticle().getDataType().equals(org.bukkit.Particle.DustOptions.class));
    return this;
  }
  
  public ParticleBuilder at(Location location) {
    this.location = location;
    return this;
  }
  
  public ParticleBuilder amount(int i) {
    this.amount = i;
    return this;
  }
  
  public ParticleBuilder speed(float i) {
    this.speed = i;
    return this;
  }
  
  public ParticleBuilder offset(float i) {
    this.offsetX = i;
    this.offsetY = i;
    this.offsetZ = i;
    return this;
  }
  
  public ParticleBuilder offset(float x, float y, float z) {
    this.offsetX = x;
    this.offsetY = y;
    this.offsetZ = z;
    return this;
  }
  
  public ParticleBuilder offsetX(float i) {
    this.offsetX = i;
    return this;
  }
  
  public ParticleBuilder offsetY(float i) {
    this.offsetY = i;
    return this;
  }
  
  public ParticleBuilder offsetZ(float i) {
    this.offsetZ = i;
    return this;
  }
  
  public ParticleBuilder towards(Vector v) {
    this.towards = v;
    return this;
  }
  
  public ParticleBuilder useExactOffsets(boolean b) {
    this.useExactOffsets = b;
    return this;
  }
  
  public ParticleBuilder withColor(String strColor) {
    return withColorAndSize(strColor, 1.0F);
  }

	public static Color HexToColor(String hex) {
		hex = hex.replace("#", "");
		switch (hex.length()) {
			case 6:
				return Color.fromRGB(
						Integer.valueOf(hex.substring(0, 2), 16),
						Integer.valueOf(hex.substring(2, 4), 16),
						Integer.valueOf(hex.substring(4, 6), 16));
		}
		return null;
	}

	public Color getColor(String c) {
		if (c.startsWith("#") && c.length() == 7)

			if (c.startsWith("#") && c.length() == 7) {
				return HexToColor(c);
			} else {
				String[] n = c.split(" "); //
				return Color.fromRGB(Integer.valueOf(n[0]), Integer.valueOf(n[1]), Integer.valueOf(n[2]));
			}

		return Color.RED;
	}
  
  public ParticleBuilder withColorAndSize(String strColor, float size) {
    Color c = getColor(strColor);
    this.particleData = new org.bukkit.Particle.DustOptions(c, size);
    return this;
  }
  
  public void send() {
    send(this.location, Players.all());
  }
  
  public void send(Player to) {
	  Collection<Player> a = new ArrayList<Player>();
	  a.add(to);
    send(this.location, a);
  }
  
  public void send(Player to, Location location) {
	  Collection<Player> a = new ArrayList<Player>();
	  a.add(to);
    send(location, a);
  }
  
  public void send(Location location) {
    send(location, Players.all());
  }
  
  public void send(Collection<Player> to) {
    send(this.location, to);
  }
  
  public void send(Collection<Player> to, Location location) {
    send(location, to);
  }
  
  private void send(Location at, Collection<Player> to) {
    if (Bukkit.getServer().isPrimaryThread()) {
      Schedulers.run(() -> sendPackets(at, to));
    } else {
      sendPackets(at, to);
    } 
  }
  
  private void sendPackets(Location loc, Collection<Player> to) {
    if (this.particleData != null && !this.particle.validateData(this.particleData)) {
      //Log.error("Could not send particle: invalid particle data supplied.");
      return;
    } 
    org.bukkit.Particle particle = this.particle.toBukkitParticle();
    if (this.towards != null) {
      for (int i = 0; i < this.amount; i++) {
        Location ln = loc.clone().add((0.0F - this.offsetX) + Numbers.randomDouble() * this.offsetX * 2.0D, (this.offsetY - this.offsetY) + Numbers.randomDouble() * this.offsetY * 2.0D, (0.0F - this.offsetZ) + Numbers.randomDouble() * this.offsetZ * 2.0D);
        for (Player player : to) {
          if (player.getWorld().equals(loc.getWorld()))
            player.spawnParticle(particle, ln, 0, (float)this.towards.getX(), (float)this.towards.getY(), (float)this.towards.getZ(), this.speed); 
        } 
      } 
    } else if (this.useExactOffsets) {
      for (int i = 0; i < this.amount; i++) {
        Location ln = loc.clone().add((0.0F - this.offsetX) + Numbers.randomDouble() * this.offsetX * 2.0D, (this.offsetY - this.offsetY) + Numbers.randomDouble() * this.offsetY * 2.0D, (0.0F - this.offsetZ) + Numbers.randomDouble() * this.offsetZ * 2.0D);
        for (Player player : to) {
          if (player.getWorld().equals(loc.getWorld()))
            player.spawnParticle(particle, ln, 1, 0.0D, 0.0D, 0.0D, this.speed); 
        } 
      } 
    } else if (this.particleData != null) {
      for (Player player : to) {
        if (player.getWorld().equals(loc.getWorld()))
          player.spawnParticle(particle, loc, this.amount, this.offsetX, this.offsetY, this.offsetZ, this.speed, this.particleData); 
      } 
    } else {
      for (Player player : to) {
        if (player.getWorld().equals(loc.getWorld()))
          player.spawnParticle(particle, loc, this.amount, this.offsetX, this.offsetY, this.offsetZ, this.speed); 
      } 
    } 
  }
}
