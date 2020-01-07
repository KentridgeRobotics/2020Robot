package org.usfirst.frc.team3786.robot.utils.config;

import java.io.File;
import java.io.IOException;

import org.simpleyaml.configuration.file.YamlConfiguration;
import org.usfirst.frc.team3786.robot.Mappings;
import org.usfirst.frc.team3786.robot.Robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Config {

	public static final NetworkTableInstance inst = NetworkTableInstance.getDefault();
	public static final NetworkTable table = inst.getTable("Config");

	private static final File configFile = new File(Mappings.dataStoragePath, Mappings.configFileName);
	private static YamlConfiguration config;

	public static void setup() {
		inst.startClientTeam(Robot.TEAM);
		getConfig();
	}

	public static YamlConfiguration getConfig() {
		if (config == null) {
			reload();
		}
		return config;
	}

	public static void reload() {
		configFile.mkdirs();
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
