package com.chargerrobotics.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.simpleyaml.configuration.file.YamlConfiguration;

import com.chargerrobotics.Constants;

public class COMPortsStorage {

	private static final File configFile = new File(Constants.dataStoragePath, Constants.comPortsFileName);
	private static YamlConfiguration config;
	private static final HashMap<String, String> cache = new HashMap<String, String>();
	
	public static void savePort(String listener, String port) {
		if (!port.equals(cache.put(listener, port))) {
			getConfig().set(listener, port);
			save();
		}
	}
	
	public static String getPort(String listener) {
		if (cache.containsKey(listener))
			return cache.get(listener);
		else
			return getConfig().getString(listener);
	}

	public static YamlConfiguration getConfig() {
		if (config == null) {
			reload();
		}
		return config;
	}

	public static void reload() {
		configFile.getParentFile().mkdirs();
		if (!configFile.isFile()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {

			}
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public static void save() {
		if (config == null || configFile == null) {
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (IOException e) {
			System.err.println("Could not save config to: " + configFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
}
