package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.chargerrobotics.utils.NetworkMapping;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterHoodSubsystem extends SubsystemBase {
    private static ShooterHoodSubsystem instance;
    private WPI_TalonSRX shooterHood;

    public final NetworkMapping<Double> kP = new NetworkMapping<Double>("hood_p", Constants.hoodP, val -> {setPIDP(val);});
    public final NetworkMapping<Double> kI = new NetworkMapping<Double>("hood_i", Constants.hoodI, val -> {setPIDI(val);});
    public final NetworkMapping<Double> kD = new NetworkMapping<Double>("hood_d", Constants.hoodD, val -> {setPIDD(val);});
    public final NetworkMapping<Double> kSetPoint = new NetworkMapping<Double>("hood_pos_setpoint", 0.0, val -> {setSetPoint(val);});

    private boolean isRunning;

    public static ShooterHoodSubsystem getInstance() {
        if (instance == null) {
            instance = new ShooterHoodSubsystem();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
        return instance;
    }

    public ShooterHoodSubsystem() {
        shooterHood = new WPI_TalonSRX(Constants.shooterHoodID);
        shooterHood.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder,0,0);
        //shooterHood.setSensorPhase(Constants.hoodSensorPhase);
//        shooterHood.configAllowableClosedloopError(Constants.hoodGainSlot, Constants.hoodErrorThreshold);
		setPIDP(kP.getValue());
		setPIDI(kI.getValue());
		setPIDD(kD.getValue());
       // if(Constants.hoodSensorPhase) {absolutePosition *= -1;}
        //if(Constants.hoodMotorInverted) {absolutePosition *= -1;}
        //shooterHood.setSelectedSensorPosition(absolutePosition, Constants.hoodPIDLoopId, Constants.hoodTimeOutMs);
    }

    public void resetShooterEncoder() {
        shooterHood.setSelectedSensorPosition(0, Constants.hoodPIDLoopId, Constants.hoodTimeOutMs);
    }

	private void setPIDP(double P) {
		shooterHood.config_kP(Constants.hoodPIDLoopId, P, Constants.hoodTimeOutMs);
	}

	private void setPIDI(double I) {
		shooterHood.config_kI(Constants.hoodPIDLoopId, I, Constants.hoodTimeOutMs);
	}

	private void setPIDD(double D) {
		shooterHood.config_kD(Constants.hoodPIDLoopId, D, Constants.hoodTimeOutMs);
	}

	private void setPIDTarget(double setPoint) {
		shooterHood.set(ControlMode.Position, setPoint);
	}
	
	private void setSetPoint(double setPoint) {
		if (isRunning)
			setPIDTarget(setPoint);
	}

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
        if (isRunning)
            setPIDTarget(kSetPoint.getValue());
        else
            shooterHood.set(0.0);
    }

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putNumber("hoodCurrPos", shooterHood.getSensorCollection().getQuadraturePosition());
    }

}