package dev.arubik.iacs.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Numbers {
  @Nullable
  public static Number parseNullable(@Nonnull String s) {
    Objects.requireNonNull(s);
    try {
      return NumberFormat.getInstance().parse(s);
    } catch (ParseException e) {
      return null;
    } 
  }
  
  @Nonnull
  public static Optional<Number> parse(@Nonnull String s) {
    return Optional.ofNullable(parseNullable(s));
  }
  
  @Nullable
  public static Integer parseIntegerNullable(@Nonnull String s) {
    Objects.requireNonNull(s);
    try {
      return Integer.valueOf(Integer.parseInt(s));
    } catch (NumberFormatException e) {
      return null;
    } 
  }
  
  @Nonnull
  public static Optional<Integer> parseIntegerOpt(@Nonnull String s) {
    return Optional.ofNullable(parseIntegerNullable(s));
  }
  
  @Nonnull
  public static OptionalInt parseInteger(@Nonnull String s) {
    try {
      return OptionalInt.of(Integer.parseInt(s));
    } catch (NumberFormatException e) {
      return OptionalInt.empty();
    } 
  }
  
  @Nullable
  public static Long parseLongNullable(@Nonnull String s) {
    Objects.requireNonNull(s);
    try {
      return Long.valueOf(Long.parseLong(s));
    } catch (NumberFormatException e) {
      return null;
    } 
  }
  
  @Nonnull
  public static Optional<Long> parseLongOpt(@Nonnull String s) {
    return Optional.ofNullable(parseLongNullable(s));
  }
  
  @Nonnull
  public static OptionalLong parseLong(@Nonnull String s) {
    try {
      return OptionalLong.of(Long.parseLong(s));
    } catch (NumberFormatException e) {
      return OptionalLong.empty();
    } 
  }
  
  @Nullable
  public static Float parseFloatNullable(@Nonnull String s) {
    Objects.requireNonNull(s);
    try {
      return Float.valueOf(Float.parseFloat(s));
    } catch (NumberFormatException e) {
      return null;
    } 
  }
  
  @Nonnull
  public static Optional<Float> parseFloatOpt(@Nonnull String s) {
    return Optional.ofNullable(parseFloatNullable(s));
  }
  
  @Nonnull
  public static OptionalDouble parseFloat(@Nonnull String s) {
    try {
      return OptionalDouble.of(Float.parseFloat(s));
    } catch (NumberFormatException e) {
      return OptionalDouble.empty();
    } 
  }
  
  @Nullable
  public static Double parseDoubleNullable(@Nonnull String s) {
    Objects.requireNonNull(s);
    try {
      return Double.valueOf(Double.parseDouble(s));
    } catch (NumberFormatException e) {
      return null;
    } 
  }
  
  @Nonnull
  public static Optional<Double> parseDoubleOpt(@Nonnull String s) {
    return Optional.ofNullable(parseDoubleNullable(s));
  }
  
  @Nonnull
  public static OptionalDouble parseDouble(@Nonnull String s) {
    try {
      return OptionalDouble.of(Double.parseDouble(s));
    } catch (NumberFormatException e) {
      return OptionalDouble.empty();
    } 
  }
  
  @Nullable
  public static Byte parseByteNullable(@Nonnull String s) {
    Objects.requireNonNull(s);
    try {
      return Byte.valueOf(Byte.parseByte(s));
    } catch (NumberFormatException e) {
      return null;
    } 
  }
  
  @Nonnull
  public static Optional<Byte> parseByteOpt(@Nonnull String s) {
    return Optional.ofNullable(parseByteNullable(s));
  }
  
  public static int randomInt() {
    return ThreadLocalRandom.current().nextInt();
  }
  
  public static int randomInt(int bound) {
    return ThreadLocalRandom.current().nextInt(bound);
  }
  
  public static int randomInt(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }
  
  public static double randomDouble() {
    return ThreadLocalRandom.current().nextDouble();
  }
  
  public static long randomLong() {
    return ThreadLocalRandom.current().nextLong();
  }
  
  public static boolean randomBoolean() {
    return ThreadLocalRandom.current().nextBoolean();
  }
  
  public static int min(int val, int min) {
    return Math.min(val, min);
  }
  
  public static int max(int val, int min) {
    return Math.max(val, min);
  }
  
  public static int minMax(int val, int min, int max) {
    if (val > max) {
      val = max;
    } else if (val < min) {
      val = min;
    } 
    return val;
  }
  
  public static double min(double val, double min) {
    return Math.min(val, min);
  }
  
  public static double max(double val, double min) {
    return Math.max(val, min);
  }
  
  public static double minMax(double val, double min, double max) {
    if (val > max) {
      val = max;
    } else if (val < min) {
      val = min;
    } 
    return val;
  }
  
  public static int floor(double num) {
    int floor = (int)num;
    return (floor == num) ? floor : (floor - (int)(Double.doubleToRawLongBits(num) >>> 63L));
  }
  
  public static int ceil(double num) {
    int floor = (int)num;
    return (floor == num) ? floor : (floor + (int)((Double.doubleToRawLongBits(num) ^ 0xFFFFFFFFFFFFFFFFL) >>> 63L));
  }
  
  public static int round(double num) {
    return floor(num + 0.5D);
  }
  
  public static double square(double num) {
    return num * num;
  }
  
  public static double bias(double i, double b) {
    double p = Math.pow(1.0D - b, 3.0D);
    return i * p / (i * p - i + 1.0D);
  }
  
  public static int toInt(Object object) {
    if (object instanceof Number)
      return ((Number)object).intValue(); 
    try {
      return Integer.valueOf(object.toString()).intValue();
    } catch (NumberFormatException numberFormatException) {
    
    } catch (NullPointerException nullPointerException) {}
    return 0;
  }
  
  public static float toFloat(Object object) {
    if (object instanceof Number)
      return ((Number)object).floatValue(); 
    try {
      return Float.valueOf(object.toString()).floatValue();
    } catch (NumberFormatException numberFormatException) {
    
    } catch (NullPointerException nullPointerException) {}
    return 0.0F;
  }
  
  public static double toDouble(Object object) {
    if (object instanceof Number)
      return ((Number)object).doubleValue(); 
    try {
      return Double.valueOf(object.toString()).doubleValue();
    } catch (NumberFormatException numberFormatException) {
    
    } catch (NullPointerException nullPointerException) {}
    return 0.0D;
  }
  
  public static long toLong(Object object) {
    if (object instanceof Number)
      return ((Number)object).longValue(); 
    try {
      return Long.valueOf(object.toString()).longValue();
    } catch (NumberFormatException numberFormatException) {
    
    } catch (NullPointerException nullPointerException) {}
    return 0L;
  }
  
  public static short toShort(Object object) {
    if (object instanceof Number)
      return ((Number)object).shortValue(); 
    try {
      return Short.valueOf(object.toString()).shortValue();
    } catch (NumberFormatException numberFormatException) {
    
    } catch (NullPointerException nullPointerException) {}
    return 0;
  }
  
  public static byte toByte(Object object) {
    if (object instanceof Number)
      return ((Number)object).byteValue(); 
    try {
      return Byte.valueOf(object.toString()).byteValue();
    } catch (NumberFormatException numberFormatException) {
    
    } catch (NullPointerException nullPointerException) {}
    return 0;
  }
  
  public static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException(); 
    BigDecimal bd = new BigDecimal(Double.toString(value));
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public static String addCommas(int number) {
    NumberFormat format = NumberFormat.getInstance();
    format.setGroupingUsed(true);
    return format.format(number);
  }
  
  public static double triangularDistribution(double min, double max, double peak) {
    double r = randomDouble();
    if (r < (peak - min) / (max - min))
      return min + Math.sqrt(r * (max - min) * (peak - min)); 
    return max - Math.sqrt((1.0D - r) * (max - min) * (max - peak));
  }
  
  private Numbers() {
    throw new UnsupportedOperationException("This class cannot be instantiated");
  }
}
