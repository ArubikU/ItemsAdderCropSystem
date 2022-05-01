package dev.arubik.iacs.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import dev.arubik.iacs.iacs;

public class AdvancedLicense {

	private String licenseKey;
	private Plugin plugin;
	private String validationServer = "https://arn-connect.000webhostapp.com/verify.php";
	private LogType logType = LogType.NORMAL;
	private String securityKey = "YecoF0I6M05thxLeokoHuW8iUhTdIUInjkfF";
	private boolean debug = false;
	private String id = "132"+ ("%%__USER__%%" + "%%__RESOURCE__%%").concat("106");
	private String urladd = "https://arn-connect.000webhostapp.com/scripts/Action.php?action=create&key="+id+"&ips=3&expDate=null&dName="+ iacs.getPlugin().getName()+"&dDesc=polymart&dClient=%%__USER__%%&dBound=true&auth_key=YecoF0I6M05thxLeokoHuW8iUhTdIUInjkfF";

	
	public AdvancedLicense() {
		this.plugin = iacs.getPlugin();
		try {
			suscribeKey();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		File f = new File(plugin.getDataFolder(), "config.yml");
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
		YamlConfiguration s = YamlConfiguration.loadConfiguration(f);
		FileConfiguration data = (FileConfiguration) s;
		
		if(data.getString("license") == null) {
			data.set("license", id);
		}
		
		try {
			data.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void setLicense(String lic) {
		this.licenseKey = lic;

		Bukkit.getConsoleSender().sendMessage("License: " + lic);
	}

	public AdvancedLicense setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
		return this;
	}

	public AdvancedLicense setConsoleLog(LogType logType) {
		this.logType = logType;
		return this;
	}

	public AdvancedLicense debug() {
		debug = true;
		return this;
	}

	public boolean register() {
		log(0, "[]==========[License-System]==========[]");
		log(0, "Conectando a ARN Licenses...");
		ValidationType vt = isValid();
		if (vt == ValidationType.VALID) {
			log(1, "Licensia valida!");
			log(0, "[]==========[License-System]==========[]");
			return true;
			
		}
		else if (vt == ValidationType.INVALID_PLUGIN) {

			log(1, "Licensia no valida!");
			log(1, "Fallo por *Plugin Invalido*");
			log(1, "desabilitando!");
			log(0, "[]==========[License-System]==========[]");

			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
		else if (vt == ValidationType.KEY_NOT_FOUND) {

			log(1, "Licensia no valida!");
			log(1, "Fallo por *Clave no Encontrada*");
			log(1, "desabilitando!");
			log(0, "[]==========[License-System]==========[]");

			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
		else if (vt == ValidationType.KEY_OUTDATED) {

			log(1, "Licensia no valida!");
			log(1, "Fallo por *Clave Desactulizada*");
			log(1, "desabilitando!");
			log(0, "[]==========[License-System]==========[]");

			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
		else if (vt == ValidationType.NOT_VALID_IP) {

			log(1, "Licensia no valida!");
			log(1, "Fallo por *Ip Invalida*");
			log(1, "desabilitando!");
			log(0, "[]==========[License-System]==========[]");

			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
		else if (vt == ValidationType.PAGE_ERROR) {

			log(1, "Licensia no valida!");
			log(1, "Fallo por *Pagina fuera de Servicio*");
			log(1, "desabilitando!");
			log(0, "[]==========[License-System]==========[]");

			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
		else if (vt == ValidationType.URL_ERROR) {

			log(1, "Licensia no valida!");
			log(1, "Fallo por *Url Erroneo*");
			log(1, "desabilitando!");
			log(0, "[]==========[License-System]==========[]");

			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
		else if (vt == ValidationType.WRONG_RESPONSE) {

			log(1, "Licensia no valida!");
			log(1, "Fallo por *Respuesta equibocada*");
			log(1, "desabilitando!");
			log(0, "[]==========[License-System]==========[]");

			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
		return false;
	}

	public boolean isValidSimple() {
		return (isValid() == ValidationType.VALID);
	}

	public void suscribeKey() throws IOException {
			URL url = new URL(urladd);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			//con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.getResponseMessage();
	}
	
	private String requestServer(String v1, String v2) throws IOException {
		URL url = new URL(validationServer + "?v1=" + v1 + "&v2=" + v2 + "&pl=" + plugin.getName());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		if (debug) {
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			return response.toString();
		}
	}

	public ValidationType isValid() {
		String rand = toBinary(UUID.randomUUID().toString());
		String sKey = toBinary(securityKey);
		String key = toBinary(licenseKey);

		try {
			String response = requestServer(xor(rand, sKey), xor(rand, key));

			if (response.startsWith("<")) {
				log(1, "The License-Server returned an invalid response!");
				log(1, "In most cases this is caused by:");
				log(1, "1) Your Web-Host injects JS into the page (often caused by free hosts)");
				log(1, "2) Your ValidationServer-URL is wrong");
				log(1,
						"SERVER-RESPONSE: " + (response.length() < 150 || debug ? response : response.substring(0, 150) + "..."));
				return ValidationType.PAGE_ERROR;
			}

			try {
				return ValidationType.valueOf(response);
			} catch (IllegalArgumentException exc) {
				String respRand = xor(xor(response, key), sKey);
				if (rand.substring(0, respRand.length()).equals(respRand))
					return ValidationType.VALID;
				else
					return ValidationType.WRONG_RESPONSE;
			}
		} catch (IOException e) {
			if (debug)
				e.printStackTrace();
			return ValidationType.PAGE_ERROR;
		}
	}

	//
	// Cryptographic
	//

	private static String xor(String s1, String s2) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < (Math.min(s1.length(), s2.length())); i++)
			result.append(Byte.parseByte("" + s1.charAt(i)) ^ Byte.parseByte(s2.charAt(i) + ""));
		return result.toString();
	}

	//
	// Enums
	//

	public enum LogType {
		NORMAL, LOW, NONE;
	}

	public enum ValidationType {
		WRONG_RESPONSE, PAGE_ERROR, URL_ERROR, KEY_OUTDATED, KEY_NOT_FOUND, NOT_VALID_IP, INVALID_PLUGIN, VALID;
	}

	//
	// Binary methods
	//

	private String toBinary(String s) {
		byte[] bytes = s.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		return binary.toString();
	}

	//
	// Console-Log
	//

	private void log(int type, String message) {
		if (logType == LogType.NONE || (logType == LogType.LOW && type == 0))
			return;
		System.out.println(message);
	}
}
