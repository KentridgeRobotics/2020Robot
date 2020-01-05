package org.usfirst.frc.team3786.robot.subsystems.vision;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoException;
import edu.wpi.first.cameraserver.CameraServer;

public class Cameras {

	private static UsbCamera drive = null;

	public static void setup() {
		initDrive();
	}

	private static void initDrive() {
		drive = CameraServer.getInstance().startAutomaticCapture();
		try {
			drive.setConnectVerbose(0);
			if (drive != null) {
				drive.setResolution(160, 120);
				drive.setFPS(30);
				drive.setWhiteBalanceManual(4500);
				drive.setExposureAuto();
				drive.setBrightness(50);
			}
		} catch (VideoException e) {
			e.printStackTrace();
		}
	}

}