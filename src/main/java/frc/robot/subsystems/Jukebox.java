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
    public Timer jukeboxTimer;
    String sensorStatusMessage;

    public Jukebox() {
        DJMotor = new CANSparkMax(Constants.Jukebox.DJMotorID, MotorType.kBrushless);
        leftShooterMotor = new CANSparkMax(Constants.Jukebox.leftShooterMotorID, MotorType.kBrushless);
        rightShooterMotor = new CANSparkMax(Constants.Jukebox.rightShooterMotorID, MotorType.kBrushless);
        DJMotor.setInverted(Constants.Jukebox.DJMotorInverted);
        leftShooterMotor.setInverted(Constants.Jukebox.leftShooterMotorInverted);
        rightShooterMotor.setInverted(Constants.Jukebox.rightShooterMotorInverted);

        intakeSensor = new DigitalInput(Constants.Jukebox.intakeSensorPort);
        jukeboxTimer = new Timer();
        jukeboxTimer.start();
    }

    public void setIntakeMotorSpeeds(double speed, boolean ignoreIntakeSensor) {
        if ((!(checkSensorBreakage())) || ignoreIntakeSensor) {
            DJMotor.set(speed * Constants.Drive.basePercentDJMotorOutput);
        } else {
            brakeShooterMotors();
        }
    }

    public void brakeIntakeMotors() {
        DJMotor.setIdleMode(IdleMode.kBrake);
    }

    public void coastIntakeMotors() {
        DJMotor.setIdleMode(IdleMode.kCoast);
    }

    public void setShooterMotorSpeeds(double speed) {
        leftShooterMotor.set(speed * Constants.Drive.basePercentShooterMotorOutput);
        rightShooterMotor.set(speed * Constants.Drive.basePercentShooterMotorOutput);
    }

    public void brakeShooterMotors() {
        leftShooterMotor.setIdleMode(IdleMode.kBrake);
        rightShooterMotor.setIdleMode(IdleMode.kBrake);
    }

    public void coastShooterMotors() {
        leftShooterMotor.setIdleMode(IdleMode.kCoast);
        rightShooterMotor.setIdleMode(IdleMode.kCoast);
    }

    public boolean checkSensorBreakage() { // Returns true if the beam is broken and false otherwise
        if (!(intakeSensor.get())) {
            return true;
        } else {
            return false;
        }
    }

    public void runJukebox(double setSpeed) {
        jukeboxTimer.reset();
        while (jukeboxTimer.get() < 2.5) {
            setShooterMotorSpeeds(setSpeed);
            if (jukeboxTimer.get() > 1.0) {
                setIntakeMotorSpeeds(1.0, true);
            }
            jukeboxTimer.advanceIfElapsed(0.25);
        }
        System.out.println("Brake!!!");
        brakeIntakeMotors();
        brakeShooterMotors();
    }

    public void displayJukeboxInfoSnapshot() {
        if (checkSensorBreakage()) {
            sensorStatusMessage = "Object Detected!";
        } else {
            sensorStatusMessage = "Nothing Detected";
        }
        if (Constants.Display.showJukeboxInfo) {
            SmartDashboard.putNumber("Shooting Debug Timer", jukeboxTimer.get());
            SmartDashboard.putString("Sensor Status", sensorStatusMessage);
        }
    }

    // @Override
    // public void periodic() {
        
    // }
}
