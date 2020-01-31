package com.chargerrobotics.utils;

import java.util.function.Consumer;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * Class for updating settings in robot without needing to push new code
 * 
 * @author     Josh Otake
 *
 * @param  <K> Data stored in mapping
 */
public class NetworkMapping<K> {

	private final NetworkTableEntry entry;
	private K defaultValue;
	private K value;
	private final Runnable runnable;
	private final Consumer<K> consumer;

	/**
	 * Initializes new configurable mapping
	 * 
	 * @param identifier   NetworkTables identifier for value
	 * @param defaultValue Default value of mapping
	 */
	public NetworkMapping(String identifier, K defaultValue) {
		this(identifier, defaultValue, (Runnable) null);
	}

	/**
	 * Initializes new configurable mapping
	 * 
	 * Runnable will be run each time the value is updated
	 * 
	 * @param identifier   NetworkTables identifier for value
	 * @param defaultValue Default value of mapping
	 * @param runnable     Runnable to be run when value is updated
	 */
	public NetworkMapping(String identifier, K defaultValue, Runnable runnable) {
		this(identifier, defaultValue, runnable, false);
	}

	/**
	 * Initializes new configurable mapping
	 * 
	 * Runnable will be run each time the value is updated
	 * 
	 * @param identifier   NetworkTables identifier for value
	 * @param defaultValue Default value of mapping
	 * @param runnable     Runnable to be run when value is updated
	 * @param runFirstTime If true, update runnable will also be run at
	 *                         initialization
	 */
	@SuppressWarnings("unchecked")
	public NetworkMapping(String identifier, K defaultValue, Runnable runnable, boolean runFirstTime) {
		entry = Config.table.getEntry(identifier);
		this.defaultValue = defaultValue;
		Object conf = Config.getConfig().get(identifier);
		if (conf != null)
			defaultValue = (K) conf;
		entry.forceSetValue(defaultValue);
		value = defaultValue;
		this.runnable = runnable;
		this.consumer = null;
		if (runnable != null && runFirstTime)
			runnable.run();
		entry.addListener(runnable != null ? e -> {
			value = (K) e.value.getValue();
			Config.getConfig().set(identifier, value);
			Config.save();
			this.runnable.run();
		} : e -> {
			value = (K) e.value.getValue();
			Config.getConfig().set(identifier, value);
			Config.save();
		}, EntryListenerFlags.kUpdate);
	}

	/**
	 * Initializes new configurable mapping
	 * 
	 * Runnable will be run each time the value is updated
	 * 
	 * @param identifier   NetworkTables identifier for value
	 * @param defaultValue Default value of mapping
	 * @param consumer     Consumer to be run when value is updated - New value
	 *                         is passed to consumer
	 */
	public NetworkMapping(String identifier, K defaultValue, Consumer<K> consumer) {
		this(identifier, defaultValue, consumer, false);
	}

	/**
	 * Initializes new configurable mapping
	 * 
	 * Runnable will be run each time the value is updated
	 * 
	 * @param identifier   NetworkTables identifier for value
	 * @param defaultValue Default value of mapping
	 * @param consumer     Consumer to be run when value is updated - New value
	 *                         is passed to consumer
	 * @param runFirstTime If true, update runnable will also be run at
	 *                         initialization
	 */
	@SuppressWarnings("unchecked")
	public NetworkMapping(String identifier, K defaultValue, Consumer<K> consumer, boolean runFirstTime) {
		entry = Config.table.getEntry(identifier);
		this.defaultValue = defaultValue;
		Object conf = Config.getConfig().get(identifier);
		if (conf != null)
			defaultValue = (K) conf;
		entry.forceSetValue(defaultValue);
		value = defaultValue;
		this.consumer = consumer;
		this.runnable = null;
		if (consumer != null && runFirstTime)
			consumer.accept(value);
		entry.addListener(consumer != null ? e -> {
			value = (K) e.value.getValue();
			Config.getConfig().set(identifier, value);
			Config.save();
			this.consumer.accept(value);
		} : e -> {
			value = (K) e.value.getValue();
			Config.getConfig().set(identifier, value);
			Config.save();
		}, EntryListenerFlags.kUpdate);
	}

	/**
	 * Resets the value to default
	 * 
	 * @return Default value of mapping
	 */
	public K reset() {
		setValue(defaultValue);
		return value;
	}

	/**
	 * Gets the current value
	 * 
	 * @return Current value of mapping
	 */
	public K getValue() {
		return value;
	}

	/**
	 * Sets the current value to NetworkTables
	 * 
	 * @param value Value to set
	 */
	public void setValue(K value) {
		setValue(value, false);
	}

	/**
	 * Sets the current value to NetworkTables
	 * 
	 * @param value     Value to set
	 * @param runUpdate If true, runs update runnable if given
	 */
	public void setValue(K value, boolean runUpdate) {
		entry.forceSetValue(value);
		this.value = value;
		if (runUpdate)
			if (consumer != null)
				consumer.accept(value);
			else
				runnable.run();
	}

	/**
	 * Manually run update runnable if given
	 */
	public void runUpdate() {
		if (consumer != null)
			consumer.accept(value);
		else if (runnable != null)
			runnable.run();
	}

}
