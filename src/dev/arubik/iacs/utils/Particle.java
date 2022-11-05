package dev.arubik.iacs.utils;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public enum Particle {
  EXPLOSION_NORMAL(new String[] { "poof", "explode", "explosion", "explosion_small" }),
  EXPLOSION_LARGE(new String[] { "largeexplode", "largeexplosion" }),
  EXPLOSION_HUGE(new String[] { "explosion_emitter", "hugeexplode", "hugeexplosion" }),
  FIREWORKS_SPARK(new String[] { "firework", "fireworksspark" }),
  WATER_BUBBLE(new String[] { "bubble" }),
  WATER_SPLASH(new String[] { "splash" }),
  WATER_WAKE(new String[] { "fishing", "wake" }),
  SUSPENDED(new String[] { "underwater" }),
  SUSPENDED_DEPTH(new String[] { "underwater", "depthsuspend" }),
  CRIT(new String[] { "crit" }),
  CRIT_MAGIC(new String[] { "enchanted_hit", "magiccrit" }),
  SMOKE_NORMAL(new String[] { "smoke" }),
  SMOKE_LARGE(new String[] { "large_smoke", "largesmoke" }),
  SPELL(new String[] { "effect" }),
  SPELL_INSTANT(new String[] { "instant_effect", "instantSpell" }),
  SPELL_MOB(new String[] { "entity_effect", "mobSpell" }),
  SPELL_MOB_AMBIENT(new String[] { "ambient_entity_effect", "mobSpellAmbient" }),
  SPELL_WITCH(new String[] { "witch", "witchMagic" }),
  DRIP_WATER(new String[] { "dripping_water", "dripWater" }),
  DRIP_LAVA(new String[] { "dripping_lava", "dripLava" }),
  VILLAGER_ANGRY(new String[] { "angry_villager", "angryVillager" }),
  VILLAGER_HAPPY(new String[] { "happy_villager", "happyVillager" }),
  TOWN_AURA(new String[] { "mycelium", "townaura" }),
  NOTE(new String[] { "note" }),
  PORTAL(new String[] { "portal" }),
  ENCHANTMENT_TABLE(new String[] { "enchant", "enchantmenttable", "enchantingtable" }),
  FLAME(new String[] { "flame" }),
  LAVA(new String[] { "lava" }),
  CLOUD(new String[] { "cloud" }),
  REDSTONE(new String[] { "dust", "reddust" }),
  SNOWBALL(new String[] { "item_snowball", "snowballpoof" }),
  SNOW_SHOVEL(new String[] { "item_snowball", "snowshovel" }),
  SLIME(new String[] { "item_slime" }),
  HEART(new String[] { "heart" }),
  BARRIER(new String[] { "barrier" }),
  ITEM_CRACK(new String[] { "item", "iconcrack", "itemcrack" }),
  BLOCK_CRACK(new String[] { "block", "blockcrack" }),
  BLOCK_DUST(new String[] { "dust", "blockdust" }),
  WATER_DROP(new String[] { "rain", "droplet" }),
  MOB_APPEARANCE(new String[] { "elder_guardian", "mobappearance" }),
  DRAGON_BREATH(new String[] { "dragon_breath", "dragonbreath" }),
  END_ROD(new String[] { "end_rod", "endRod" }),
  DAMAGE_INDICATOR(new String[] { "damage_indicator", "damageIndicator" }),
  SWEEP_ATTACK(new String[] { "sweep_attack", "sweepAttack" }),
  FALLING_DUST(new String[] { "falling_dust", "fallingDust" }),
  TOTEM(new String[] { "totem_of_undying" }),
  SPIT(new String[] { "spit" }),
  SQUID_INK(new String[] { "squid_ink", "squidink" }),
  BUBBLE_POP(new String[] { "bubble_pop", "bubblepop" }),
  CURRENT_DOWN(new String[] { "current_down", "currentdown" }),
  BUBBLE_COLUMN_UP(new String[] { "bubble_column_up", "bubblecolumn", "bubble_column" }),
  NAUTILUS(new String[] { "nautilus" }),
  DOLPHIN(new String[] { "dolphin" }),
  COMPOSTER(new String[] { "composter" }),
  FALLING_LAVA(new String[] { "fallinglava", "falling_lava" }),
  FALLING_WATER(new String[] { "fallingwater", "falling_water" }),
  FLASH(new String[] { "flash" }),
  LANDING_LAVA(new String[] { "landinglava", "landing_lava" }),
  SNEEZE(new String[] { "sneeze" }),
  CAMPFIRE_COSY_SMOKE(new String[] { "campfire_cosy_smoke", "campfire_cosy", "campfire_cozy_smoke", "campfire_cozy", "campfire" }),
  CAMPFIRE_SIGNAL_SMOKE(new String[] { "campfire_signal_smoke", "campfire_signal" }),
  DRIPPING_HONEY(new String[] { "drippinghoney" }),
  FALLING_HONEY(new String[] { "fallinghoney" }),
  FALLING_NECTAR(new String[] { "fallingnectar" }),
  LANDING_HONEY(new String[] { "landinghoney" });
  
  private static final Map<String, Particle> PARTICLE_ALIASES;
  
  private final String[] aliases;
  
  static {
    PARTICLE_ALIASES = new HashMap<>();
    for (Particle particle : values()) {
      PARTICLE_ALIASES.put(particle.toString(), particle);
      for (String alias : particle.getAliases())
        PARTICLE_ALIASES.put(alias.toUpperCase(), particle); 
    } 
  }
  
  public static Particle fromString(String key) {
    Particle particle = PARTICLE_ALIASES.getOrDefault(key.toUpperCase(), null);
    if (particle == null) {
      //Log.error("Particle '" + key + "' is not supported by this version.");
      return CLOUD;
    } 
    return particle;
  }
  
  public String[] getAliases() {
    return this.aliases;
  }
  
  Particle(String... aliases) {
    this.aliases = aliases;
  }
  
  public ParticleBuilder create() {
    return ParticleBuilder.of(this);
  }
  
  public org.bukkit.Particle toBukkitParticle() {
    return org.bukkit.Particle.valueOf(toString());
  }
  
  public boolean requiresData() {
    return !toBukkitParticle().getDataType().equals(Void.class);
  }
  
  public boolean isDustParticle() {
    return (toBukkitParticle().getDataType() == org.bukkit.Particle.DustOptions.class);
  }
  
  public boolean validateData(Object obj) {
    org.bukkit.Particle particle = toBukkitParticle();
    if (particle.getDataType().equals(Void.class))
      return false; 
    if (particle.getDataType().equals(ItemStack.class))
      return obj instanceof ItemStack; 
    if (particle.getDataType() == BlockData.class)
      return obj instanceof BlockData; 
    if (particle.getDataType() == MaterialData.class)
      return obj instanceof MaterialData; 
    if (particle.getDataType() == org.bukkit.Particle.DustOptions.class)
      return obj instanceof org.bukkit.Particle.DustOptions; 
    return true;
  }
}
