package dev.arubik.iacs.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import net.md_5.bungee.api.ChatColor;

public class LineConfig {
	public static LineConfig of(String line) {
		return new LineConfig(line);
	}

	private String fileName = "Unknown";

	private String key;

	private String line;

	public String getFileName() {
		return this.fileName;
	}

	public String getKey() {
		return this.key;
	}

	private HashMap<String, String> config = new HashMap<>();

	public LineConfig(String fileName, String line) {
		this(line);
		this.fileName = fileName;
	}

	public LineConfig(File file, String line) {
		this(line);
		this.fileName = file.getName();
	}

	public LineConfig(String s) {
		this.line = unparseBlock(s);
		s = parseBlock(s);
		if (s.contains("{") && s.contains("}")) {
			this.key = s.substring(0, s.indexOf("{"));
			int startPos = s.indexOf('{') + 1;
			int lastPos = s.lastIndexOf('}');
			int count = 0;
			s = s.substring(startPos, lastPos);
			for (char c : s.toCharArray()) {
				if (c == '{')
					count++;
				if (c == '}')
					count--;
			}
			if (count != 0) {
				// Log.error("" + ConsoleColor.RED + "Error loading LineConfig: Unbalanced
				// Braces" + ConsoleColor.RED);
				// Log.error("" + ConsoleColor.RED + "[Line]: " + ConsoleColor.RED +
				// ConsoleColor.WHITE);
				return;
			}
			if (s.length() == 0)
				return;
			int start = 0;
			int pos = 0;
			int depth = 0;
			String lastKey = "";
			String lastVal = "";
			boolean inb = false;
			s = s + "}";
			for (char c : s.toCharArray()) {
				if (c == '{' || c == '[')
					depth++;
				if (c == '}' || c == ']')
					depth--;
				if ((c == ';' && depth == 0) || (c == '}' && depth < 0))
					try {
						String element = s.substring(start, pos);
						if (pos - start > 0 && element.length() > 0) {
							String key = element.substring(0, element.indexOf('=')).trim().toLowerCase();
							String val = element.substring(element.indexOf('=') + 1).trim();
							this.config.put(key, val);
						}
					} catch (Exception exception) {

					} finally {
						start = pos + 1;
					}
				pos++;
			}
		} else if (s.contains("[") && s.contains("]")) {
			try {
				String[] split = s.split("\\[");
				s = split[1];
				String[] split2 = s.split("\\]");
				s = split2[0];
			} catch (ArrayIndexOutOfBoundsException ex) {
				// Log.error("" + ConsoleColor.RED + "Error loading LineConfig: Invalid Syntax"
				// + ConsoleColor.RED);
				// Log.error("" + ConsoleColor.RED + "[Line]: " + ConsoleColor.RED +
				// ConsoleColor.WHITE);
				return;
			}
		} else {
			this.key = s;
		}
	}

	public String getLine() {
		return this.line;
	}

	public int size() {
		return this.config.size();
	}

	public Set<Map.Entry<String, String>> entrySet() {
		return this.config.entrySet();
	}

	public static String getKey(String s) {
		String key = null;
		if (s.contains("{")) {
			key = s.substring(0, s.indexOf("{"));
		} else if (s.contains("[")) {
			key = s.substring(0, s.indexOf("["));
		} else {
			key = s;
		}
		return key;
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean def) {
		key = key.toLowerCase();
		String s = this.config.get(key);
		if (s == null)
			return def;
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public boolean getBoolean(String[] key, boolean def) {
		String s = null;
		for (String k : key) {
			s = this.config.get(k.toLowerCase());
			if (s != null)
				break;
		}
		if (s == null)
			return def;
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String[] key) {
		return getString(key, null, new String[0]);
	}

	public String getString(String key, String def) {
		String s = this.config.get(key.toLowerCase());
		if (s == null)
			return def;
		return s;
	}

	public String getString(String[] key, String def, String... args) {
		String s = null;
		for (String k : key) {
			s = this.config.get(k.toLowerCase());
			if (s != null)
				return s;
		}
		for (String a : args) {
			if (a != null)
				return a;
		}
		return def;
	}

	public int getInteger(String key) {
		return getInteger(key, 0);
	}

	public int getInteger(String[] key) {
		return getInteger(key, 0);
	}

	public int getInt(String key) {
		return getInteger(key, 0);
	}

	public int getInt(String[] key) {
		return getInteger(key, 0);
	}

	public int getInteger(String key, int def) {
		String s = this.config.get(key.toLowerCase());
		if (s == null)
			return def;
		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public int getInteger(String[] key, int def) {
		String s = null;
		for (String k : key) {
			s = this.config.get(k.toLowerCase());
			if (s != null)
				break;
		}
		if (s == null)
			return def;
		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public double getDouble(String key) {
		return getDouble(key, 0.0D);
	}

	public double getDouble(String[] key) {
		return getDouble(key, 0.0D);
	}

	public double getDouble(String key, double def) {
		String s = this.config.get(key.toLowerCase());
		if (s == null)
			return def;
		try {
			return Double.parseDouble(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public double getDouble(String[] key, double def) {
		String s = null;
		for (String k : key) {
			s = this.config.get(k.toLowerCase());
			if (s != null)
				break;
		}
		if (s == null)
			return def;
		try {
			return Double.parseDouble(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public float getFloat(String key) {
		return getFloat(key, 0.0F);
	}

	public float getFloat(String[] key) {
		return getFloat(key, 0.0F);
	}

	public float getFloat(String key, float def) {
		String s = this.config.get(key.toLowerCase());
		if (s == null)
			return def;
		try {
			return Float.parseFloat(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public float getFloat(String[] key, float def) {
		String s = null;
		for (String k : key) {
			s = this.config.get(k.toLowerCase());
			if (s != null)
				break;
		}
		if (s == null)
			return def;
		try {
			return Float.parseFloat(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public long getLong(String key) {
		return getLong(key, 0L);
	}

	public long getLong(String[] key) {
		return getLong(key, 0L);
	}

	public long getLong(String key, long def) {
		String s = this.config.get(key.toLowerCase());
		if (s == null)
			return def;
		try {
			return Long.parseLong(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public long getLong(String[] key, long def) {
		String s = null;
		for (String k : key) {
			s = this.config.get(k.toLowerCase());
			if (s != null)
				break;
		}
		if (s == null)
			return def;
		try {
			return Long.parseLong(s);
		} catch (Exception ex) {
			return def;
		}
	}

	public Color getColor(String[] key, String def) {
		String s = null;
		for (String k : key) {
			s = this.config.get(k.toLowerCase());
			if (s != null)
				break;
		}
		return getColor(s, def);
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

	public Color getColor(String key, String def) {
		String c = (key == null) ? def : getString(key, def);
		if (c == null)
			return null;
		if (c.startsWith("#") && c.length() == 7)

			if (c.startsWith("#") && c.length() == 7) {
				return HexToColor(c);
			} else {
				String[] n = c.split(" "); //
				return Color.fromRGB(Integer.valueOf(n[0]), Integer.valueOf(n[1]), Integer.valueOf(n[2]));
			}

		return Color.RED;
	}

	public static String unparseBlock(String s) {
		if (s.contains("\"")) {
			String[] split = s.split("\"");
			int i = 0;
			String ns = "";
			for (String str : split) {
				if (i % 2 == 1) {
					ns = ns.concat("\"" + unparseSpecialChars(str) + "\"");
				} else {
					ns = ns.concat(str);
				}
				i++;
			}
			s = ns;
		}
		if (s.contains("'")) {
			String[] split = s.split("'");
			int i = 0;
			String ns = "";
			for (String str : split) {
				if (i % 2 == 1) {
					ns = ns.concat("'" + unparseSpecialChars(str) + "'");
				} else {
					ns = ns.concat(str);
				}
				i++;
			}
			s = ns;
		}
		int pos = 0;
		int count = 0;
		int ss = 0;
		int sc = 0;
		int ec = 0;
		String parsed = "";
		for (char c : s.toCharArray()) {
			if (c == '{') {
				if (count == 0)
					sc = pos;
				count++;
			}
			count--;
			if (c == '}' && count == 0) {
				ec = pos;
				String f = s.substring(ss, sc);
				String m = s.substring(sc, ec).replace(" ", "<&csp>").replace("-", "<&da>");
				String e = s.substring(ec);
				parsed = parsed + parsed + f;
				ss = pos;
			}
			pos++;
		}
		parsed = parsed + parsed;
		return parsed;
	}

	public static String parseBlock(String s) {
		return s.replace("<&csp>", " ").replace("<&da>", "-").trim();
	}

	public static String parseSpecialChars(String s) {
		if (s == null)
			return null;
		s = s.replace("<&co>", ":");
		s = s.replace("<&sq>", "'");
		s = s.replace("<&da>", "-");
		s = s.replace("<&bs>", "\\");
		s = s.replace("<&fs>", "/");
		s = s.replace("<&sp>", " ");
		s = s.replace("<&cm>", ",");
		s = s.replace("<&sc>", ";");
		s = s.replace("<&eq>", "=");
		s = s.replace("<&dq>", "\"");
		s = s.replace("<&rb>", "]");
		s = s.replace("<&lb>", "[");
		s = s.replace("<&rc>", "}");
		s = s.replace("<&lc>", "{");
		s = s.replace("<&nl>", "\n");
		s = s.replace("<&nm>", "#");
		s = s.replace("<&skull>", "☠️");
		s = s.replace("<&heart>", "❤️");
		s = ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}

	public static String unparseSpecialChars(String s) {
		if (s == null)
			return null;
		s = s.replace("-", "<&da>");
		s = s.replace("\\", "<&bs>");
		s = s.replace("/", "<&fs>");
		s = s.replace(" ", "<&sp>");
		s = s.replace(",", "<&cm>");
		s = s.replace(";", "<&sc>");
		s = s.replace("=", "<&eq>");
		s = s.replace("{", "<&lc>");
		s = s.replace("}", "<&rc>");
		s = s.replace("[", "<&lb>");
		s = s.replace("]", "<&rb>");
		s = s.replace("'", "<&sq>");
		return s;
	}

	public boolean contains(String... paths) {
		Boolean contain = true;
		for (String path : paths)
			contain = this.config.containsKey(path);
		return contain;

	}

	public void validate(String... paths) {
		for (String path : paths)
			Validate.isTrue(this.config.containsKey(path), "Config is missing parameter '" + path + "'");
	}

}
