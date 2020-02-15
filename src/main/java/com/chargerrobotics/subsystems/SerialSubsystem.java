/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.LinkedList;

import com.chargerrobotics.utils.ArduinoSerial;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SerialSubsystem extends SubsystemBase {
  private final ArduinoSerial serial;
  private StringBuilder sb = null;
  private final Deque<String> queue;

  /**
   * Creates a new SerialSubsystem.
   */
  public SerialSubsystem(String serialPort) {
    this.queue = new LinkedList<String>();
    this.serial = new ArduinoSerial(serialPort) {
      @Override
      protected void recData(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
          if (sb == null) {
            sb = new StringBuilder();
          }
          char c = (char)buffer.get();
          if (c == '\n') {
            String s = sb.toString();
            SmartDashboard.putString("SerialData", s);
            queue.addFirst(s);
            sb = null;
          } else {
            sb.append(c);
          }
        }
      }
    };
  }

  public String nextLine() {
      return queue.pollLast();
  }


  @Override
  public void periodic() {
    serial.poll();
  }
}
