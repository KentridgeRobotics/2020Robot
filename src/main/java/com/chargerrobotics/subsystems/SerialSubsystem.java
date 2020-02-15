/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.fazecast.jSerialComm.SerialPort;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** A subsystem to read from all USB serial devices and
 * distribute data to a set of listeners.
 */
public class SerialSubsystem extends SubsystemBase {
  private final List<SerialPort> ports;
  private final Map<String,Consumer<String>> listeners;
  private final Map<String,StringBuilder> stringBuilders;
  private static final int baudRate = 9600;

  private static SerialSubsystem instance;

  /**
   * Singleton, since this manages all ports on the robot
   * @return
   */
  public static SerialSubsystem getInstance() {
    if (instance == null) {
      instance = new SerialSubsystem();
    }
    return instance;
  }
  /**
   * Creates a new SerialSubsystem.
   */
  private SerialSubsystem() {
    this.ports = new ArrayList<>();
    this.stringBuilders = new HashMap<>();
    this.listeners = new HashMap<>();
  }

  /**
   * Call this in teleopInit and autonomousInit
   */
  public void init() {
    if (this.ports.isEmpty()) {
      SerialPort[] availablePorts = SerialPort.getCommPorts();
      if (availablePorts == null) {
        return;
      }
      for (var availablePort : availablePorts) {
        if (availablePort.toString().indexOf("USB-to-Serial") >= 0) {
          this.ports.add(availablePort);
          availablePort.setBaudRate(baudRate);
          availablePort.openPort();
				}
      }
    } else {
      System.err.println("FORGOT TO CLOSE SERIAL SUBSYSTEM");
    }
  }

  /**
   * Call this in disableInit. Probably also good to call when the competition is done
   */
  public void close() {
    for (var port : ports) {
      port.closePort();
    }
    stringBuilders.clear();
    ports.clear();
  }

  /**
   * Register a consumer to listen for messages that start with a given prefix.
   * This is how we distinguish different kinds of messages from each other.
   * 
   * @param messagePrefix
   * @param listener
   */
  public void registerListener(String messagePrefix, Consumer<String> listener) {
    this.listeners.put(messagePrefix, listener);
  }

  private final byte[] buf = new byte[256];

  private void processMessage(String message) {
    for (var prefix : listeners.keySet()) {
      if (message.startsWith(prefix)) {
        listeners.get(prefix).accept(message.substring(prefix.length()));
      }
    }
  }

  private void pollPort(SerialPort serial) {
    int avail = serial.bytesAvailable();
    if (avail > 0) {
      String portName = serial.getSystemPortName();
      StringBuilder sb = stringBuilders.get(portName);
      int bytesRead = serial.readBytes(buf, Math.min(256, avail));
      for (int i=0; i<bytesRead; ++i) {
        if (sb == null) {
          sb = new StringBuilder();
          stringBuilders.put(portName, sb);
        }
        char c = (char)buf[i];
        if (c == '\n') {
          String s = sb.toString();
          processMessage(s);
          sb = null;
          stringBuilders.remove(portName);
        } else {
          sb.append(c);
        }
      }
    }
  }

  @Override
  public void periodic() {
    for (var port : ports) {
      pollPort(port);
    }
  }
}

