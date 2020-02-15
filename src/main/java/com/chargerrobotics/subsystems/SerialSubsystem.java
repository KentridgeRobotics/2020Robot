/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fazecast.jSerialComm.SerialPort;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SerialSubsystem extends SubsystemBase {
  private final List<SerialPort> ports;
  private final Map<String,StringBuilder> stringBuilders;
  private final Deque<String> queue;
  private static final int baudRate = 9600;

  private static SerialSubsystem instance;
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
    this.queue = new LinkedList<String>();
    this.ports = new ArrayList<>();
    this.stringBuilders = new HashMap<String,StringBuilder>();
  }

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

  public void close() {
    for (var port : ports) {
      port.closePort();
    }
    stringBuilders.clear();
    ports.clear();
  }

  byte[] buf = new byte[256];

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
          queue.addFirst(s);
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
    String line = queue.pollLast();
    if (line != null) {
      System.err.println("Queue: "+line);
    }
  }
}

