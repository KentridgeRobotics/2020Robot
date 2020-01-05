package org.usfirst.frc.team3786.robot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.usfirst.frc.team3786.robot.Mappings;
import org.usfirst.frc.team3786.robot.utils.BNO055.CalData;
import org.usfirst.frc.team3786.robot.utils.BNO055.opmode_t;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gyroscope implements Runnable {

	private static Gyroscope instance;

	private static final File configFile = new File(Mappings.dataStoragePath, Mappings.gyroCalibrationFileName);

	public static Gyroscope getInstance() {
		if (instance == null)
			instance = new Gyroscope();
		return instance;
	}

	private static BNO055 imu;
	private double accelX, accelY;
	private double robotAccelX, robotAccelY, robotHead;
	private double velX, velY, dispX, dispY;
	private double last, now;
	private double dT;
	private boolean hasStorage = false;

	public Gyroscope() {
		if (imu == null)
			imu = BNO055.getInstance(opmode_t.OPERATION_MODE_NDOF);

		if (new File(Mappings.dataStoragePath).exists()) {
			hasStorage = true;
			SmartDashboard.putBoolean("Save Gyro Calibration", false);
			if (configFile.isFile()) {
				try (FileInputStream fis = new FileInputStream(configFile)) {
					byte[] calibData = fis.readAllBytes();
					if (calibData.length >= 22) {
						imu.setSensorOffsets(calibData);
						FileTime ft = Files.getLastModifiedTime(configFile.toPath());
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yy HH:mm:ss")
								.withZone(ZoneId.systemDefault());
						String formattedTime = formatter.format(ft.toInstant());
						System.out.println("Loaded Gyro Calibration from: " + formattedTime);
					} else {
						System.out.println("Gyro calibration data is corrupt!");
					}
				} catch (IOException e) {
					System.out.println("Failed to read gyro calibration data!");
					e.printStackTrace();
				}
			}
		}

		velX = 0.0;
		velY = 0.0;

		dispX = 0.0;
		dispY = 0.0;

		last = System.nanoTime() / 1000000000.0;
	}

	public double getHeadingContinuous() {
		return imu.getHeadingEuler();
	}

	public double getHeading() {
		return imu.getVectorEuler()[0];
	}

	public double[] getVector() {
		return imu.getVectorEuler();
	}

	public double[] getAccel() {
		double[] result = imu.getVectorLinAccel();
		return result;
	}

	public double[] getGravity() {
		return imu.getVectorGrav();
	}

	public CalData getCalibration() {
		return imu.getCalibration();
	}

	public void run() {
		now = System.nanoTime() / 1000000000.0;
		dT = now - last;
		double velXNext;
		double velYNext;
		double dispXNext;
		double dispYNext = 0.0;
		double[] accel = getAccel();
		robotAccelX = accel[0];
		robotAccelY = accel[1];

		robotHead = getHeading();
		SmartDashboard.putNumberArray("Gyroscope", getVector());
		SmartDashboard.putNumberArray("Gravity", getGravity());
		SmartDashboard.putNumber("Heading", robotHead);

		accelX = Math.cos(robotHead) * robotAccelX + Math.sin(robotHead) * robotAccelY;
		accelY = -Math.sin(robotHead) * robotAccelX + Math.cos(robotHead) * robotAccelY;

		velXNext = velX + accelX * dT;
		velYNext = velY + accelY * dT;

		velX = velXNext;
		velY = velYNext;

		SmartDashboard.putNumber("AccelX", accelX);
		SmartDashboard.putNumber("AccelY", accelY);
		SmartDashboard.putNumber("Time", dT);

		SmartDashboard.putNumber("VelX", velX);
		SmartDashboard.putNumber("VelY", velY);

		dispXNext = dispX + velX * dT;

		dispX = dispXNext;
		dispYNext = dispY + velY * dT;
		dispY = dispYNext;

		last = now;
		SmartDashboard.putNumber("GyroCalibration.Accel", imu.getCalibration().accel);
		SmartDashboard.putNumber("GyroCalibration.Gyro", imu.getCalibration().gyro);
		SmartDashboard.putNumber("GyroCalibration.Mag", imu.getCalibration().mag);
		SmartDashboard.putNumber("GyroCalibration.Sys", imu.getCalibration().sys);

		if (hasStorage) {
			if (SmartDashboard.getBoolean("Save Gyro Calibration", false)) {
				byte[] calibData = imu.getSensorOffsets();
				CalData calibration = imu.getCalibration();
				if (calibration.accel >= 1 && calibration.gyro >= 3 && calibration.mag >= 3) {
					try {
						File directory = configFile.getParentFile();
						String oldFile = "";
						if (new File(directory, Mappings.gyroCalibrationFileName + ".old").isFile()) {
							for (int i = 1; i > 0; i++) {
								if (!(new File(directory, Mappings.gyroCalibrationFileName + ".old" + i).isFile())) {
									configFile.renameTo(
											new File(directory, Mappings.gyroCalibrationFileName + ".old" + i));
									oldFile = Mappings.gyroCalibrationFileName + ".old" + i;
									break;
								}
							}
						} else {
							configFile.renameTo(new File(directory, Mappings.gyroCalibrationFileName + ".old"));
							oldFile = Mappings.gyroCalibrationFileName + ".old";
						}
						if (configFile.createNewFile()) {
							try (FileOutputStream fos = new FileOutputStream(configFile)) {
								fos.write(calibData);
								System.out.println("Saved Gyro Calibration Data");
								if (!oldFile.equals(""))
									System.out.println("Old Calibration Renamed to: " + oldFile);
							} catch (IOException e) {
								System.out.println("Failed to save gyro calibration data!");
								e.printStackTrace();
							}
						}
					} catch (IOException e) {
						System.out.println("Failed to save gyro calibration data!");
						e.printStackTrace();
					}
				} else {
					System.out.println("Gyroscope is not calibrated!");
				}
				SmartDashboard.putBoolean("Save Gyro Calibration", false);
			}
		}
	}

	public double getVelX() {
		return velX;
	}

	public double getVelY() {
		return velY;
	}

	public double getDispX() {
		return dispX;
	}

	public double getDispY() {
		return dispY;
	}

}