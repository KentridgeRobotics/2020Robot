/*----------------------------------------------------------------------------*/
/* Copyright (c) 2008-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.utils;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpiutil.math.MathUtil;

/**
 * REV Robotics Smart Servo
 *
 * <p>
 * The range parameters default to 0-270 degrees, approximately the max range of
 * a REV Robotics Smart Servo
 */
public class REVSmartServo extends Servo {
	private static double kMaxServoAngle = 270.0;
	private static double kMinServoAngle = 0.0;

	protected static final double kDefaultMaxServoPWM = 2.5;
	protected static final double kDefaultMinServoPWM = 0.5;

	private final boolean continuous;

	/**
	 * Constructor.<br>
	 *
	 * <p>
	 * By default {@value #kDefaultMaxServoPWM} ms is used as the maxPWM
	 * value<br>
	 * By default {@value #kDefaultMinServoPWM} ms is used as the minPWM
	 * value<br>
	 *
	 * @param channel The PWM channel to which the servo is attached. 0-9 are
	 *                    on-board, 10-19 are on the MXP port
	 */
	public REVSmartServo(final int channel) {
		this(channel, false);
	}

	/**
	 * Constructor.<br>
	 *
	 * <p>
	 * By default {@value #kDefaultMaxServoPWM} ms is used as the maxPWM
	 * value<br>
	 * By default {@value #kDefaultMinServoPWM} ms is used as the minPWM
	 * value<br>
	 *
	 * @param channel    The PWM channel to which the servo is attached. 0-9 are
	 *                       on-board, 10-19 are on the MXP port
	 * @param continuous Whether or not the servo is in continuous mode
	 */
	public REVSmartServo(final int channel, final boolean continuous) {
		super(channel);
		this.continuous = continuous;
		setBounds(kDefaultMaxServoPWM, 0, 0, 0, kDefaultMinServoPWM);
		setPeriodMultiplier(PeriodMultiplier.k4X);

		HAL.report(tResourceType.kResourceType_Servo, getChannel() + 1);
		SendableRegistry.setName(this, "REVSmartServo", getChannel());
		if (continuous)
			setSpeed(0);
	}

	/**
	 * Set the servo speed.
	 * 
	 * <p>Only works when in continuous mode
	 *
	 * @param speed The output percentage from -1.0 to 1.0.
	 */
	public void setSpeed(double speed) {
		if (continuous) {
			speed = MathUtil.clamp(speed, -1.0, 1.0);
			setPosition(speed = (speed + 1) / 2);
		}
	}

	/**
	 * Set the servo angle.
	 *
	 * <p>
	 * Assume that the servo angle is linear with respect to the PWM value (big
	 * assumption, need to test).
	 *
	 * <p>
	 * Servo angles that are out of the supported range of the servo simply
	 * "saturate" in that direction In other words, if the servo has a range of
	 * (X degrees to Y degrees) than angles of less than X result in an angle of
	 * X being set and angles of more than Y degrees result in an angle of Y
	 * being set.
	 * 
	 * <p>Only works when not in continuous mode
	 *
	 * @param degrees The angle in degrees to set the servo.
	 */
	public void setAngle(double degrees) {
		if (!continuous) {
			if (degrees < kMinServoAngle) {
				degrees = kMinServoAngle;
			} else if (degrees > kMaxServoAngle) {
				degrees = kMaxServoAngle;
			}

			setPosition(((degrees - kMinServoAngle)) / getServoAngleRange());
		}
	}

	/**
	 * Get the servo angle.
	 *
	 * <p>
	 * Assume that the servo angle is linear with respect to the PWM value (big
	 * assumption, need to test).
	 * 
	 * <p>Only works when not in continuous mode
	 *
	 * @return The angle in degrees to which the servo is set.
	 */
	public double getAngle() {
		if (!continuous)
			return getPosition() * getServoAngleRange() + kMinServoAngle;
		return 0;
	}

	private double getServoAngleRange() {
		if (!continuous)
			return kMaxServoAngle - kMinServoAngle;
		return 0;
	}

	/**
	 * Sets the servo angle range
	 *
	 * <p>
	 * Assume that the servo angle is linear with respect to the PWM value (big
	 * assumption, need to test).
	 * 
	 * <p>Only works when not in continuous mode
	 *
	 * @param minAngle The minimum angle of the servo in degrees.
	 * @param maxAngle The maximum angle of the servo in degrees.
	 */
	public void setServoAngleRange(double minAngle, double maxAngle) {
		if (!continuous) {
			kMaxServoAngle = minAngle;
			kMinServoAngle = maxAngle;
		}
	}
}
