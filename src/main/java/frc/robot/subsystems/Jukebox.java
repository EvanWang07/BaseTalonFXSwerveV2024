package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    public String sensorStatusMessage;

    public Jukebox() {
        DJMotor = new CANSparkMax(Constants.Jukebox.DJMotorID, MotorType.kBrushless);
        leftShooterMotor = new CANSparkMax(Constants.Jukebox.leftShooterMotorID, MotorType.kBrushless);
        rightShooterMotor = new CANSparkMax(Constants.Jukebox.rightShooterMotorID, MotorType.kBrushless);
        DJMotor.setInverted(Constants.Jukebox.DJMotorInverted);
        leftShooterMotor.setInverted(Constants.Jukebox.leftShooterMotorInverted);
        rightShooterMotor.setInverted(Constants.Jukebox.rightShooterMotorInverted);

        intakeSensor = new DigitalInput(Constants.Jukebox.intakeSensorPort);
    }

    public void setIntakeMotorSpeeds(double speed, boolean ignoreIntakeSensor) {
        if ((!(checkSensorBreakage())) || ignoreIntakeSensor) {
            DJMotor.set(speed * Constants.Drive.basePercentDJMotorOutput);
        } else {
            brakeIntakeMotors();
        }
    }

    public double getIntakeMotorSpeed() {
        return DJMotor.getEncoder().getVelocity();
    }

    public void brakeIntakeMotors() {
        DJMotor.set(0);
    }

    public void coastIntakeMotors() {
        DJMotor.setIdleMode(IdleMode.kCoast);
    }

    public void setShooterMotorSpeeds(double speed) {
        leftShooterMotor.set(speed * Constants.Drive.basePercentShooterMotorOutput);
        rightShooterMotor.set(speed * Constants.Drive.basePercentShooterMotorOutput);
    }

    public double getAverageShooterSpeeds() {
        double leftShooterSpeed = leftShooterMotor.getEncoder().getVelocity();
        double rightShooterSpeed = rightShooterMotor.getEncoder().getVelocity();
        double averageShooterSpeed = (leftShooterSpeed + rightShooterSpeed) / 2;
        return averageShooterSpeed;
    }

    public void brakeShooterMotors() {
        leftShooterMotor.set(0);
        rightShooterMotor.set(0);
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
        while ((getAverageShooterSpeeds() < Constants.Jukebox.shooterMotorRPMThreshold) || (getIntakeMotorSpeed() < Constants.Jukebox.DJMotorRPMThreshold)) {
            setShooterMotorSpeeds(setSpeed);
            if (getAverageShooterSpeeds() >= Constants.Jukebox.shooterMotorRPMThreshold) {
                setIntakeMotorSpeeds(setSpeed, true);
            }
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
            SmartDashboard.putString("Sensor Status", sensorStatusMessage);
        }
    }

    @Override
    public void periodic() {
        
    }
}
