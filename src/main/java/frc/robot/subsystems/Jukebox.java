package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkBase.IdleMode;

public class Jukebox extends SubsystemBase {
    public CANSparkMax DJMotor;
    public CANSparkMax leftShooterMotor;
    public CANSparkMax rightShooterMotor;
    public DigitalInput intakeSensor;
    public Timer jukeboxTimer = new Timer();

    public Jukebox() {
        DJMotor = new CANSparkMax(Constants.Jukebox.DJMotorID, MotorType.kBrushless);
        leftShooterMotor = new CANSparkMax(Constants.Jukebox.leftShooterMotorID, MotorType.kBrushless);
        rightShooterMotor = new CANSparkMax(Constants.Jukebox.rightShooterMotorID, MotorType.kBrushless);
        DJMotor.setInverted(Constants.Jukebox.DJMotorInverted);
        leftShooterMotor.setInverted(Constants.Jukebox.leftShooterMotorInverted);
        rightShooterMotor.setInverted(Constants.Jukebox.rightShooterMotorInverted);

        intakeSensor = new DigitalInput(Constants.Jukebox.intakeSensorPort);
    }

    public void setIntakeMotorSpeeds(double speed) {
        DJMotor.set(speed);
    }

    public void brakeIntakeMotors() {
        DJMotor.setIdleMode(IdleMode.kBrake);
    }

    public void coastIntakeMotors() {
        DJMotor.setIdleMode(IdleMode.kCoast);
    }

    public void setShooterMotorSpeeds(double speed) {
        leftShooterMotor.set(speed);
        rightShooterMotor.set(speed);
    }

    public void brakeShooterMotors() {
        leftShooterMotor.setIdleMode(IdleMode.kBrake);
        rightShooterMotor.setIdleMode(IdleMode.kBrake);
    }

    public void coastShooterMotors(){
        leftShooterMotor.setIdleMode(IdleMode.kCoast);
        rightShooterMotor.setIdleMode(IdleMode.kCoast);
    }

    public void runJukebox(double setSpeed) {
        jukeboxTimer.reset();
        while (jukeboxTimer.get() <= 3) {
            if (jukeboxTimer.get() <= 1.5) {
                setShooterMotorSpeeds(setSpeed);
            } else {
                setShooterMotorSpeeds(setSpeed);
                setIntakeMotorSpeeds(setSpeed);
            }
        }
        brakeIntakeMotors();
        brakeShooterMotors();
    }

    @Override
    public void periodic() {
        String sensorStatusMessage;
        double intakeSpeeds = DJMotor.get();
        if (!(intakeSensor.get())) {
            brakeIntakeMotors();
            sensorStatusMessage = "Object Detected!";
        } else {
            sensorStatusMessage = "Nothing Detected";
        }
        if (Constants.Display.showJukeboxInfo) {
            SmartDashboard.putNumber("Intake Speed", intakeSpeeds);
            SmartDashboard.putString("Sensor Status", sensorStatusMessage);
        }
    }
}
