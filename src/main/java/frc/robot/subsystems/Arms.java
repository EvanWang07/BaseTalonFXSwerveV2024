package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.VoltageOut;

public class Arms extends SubsystemBase {
    private final VoltageOut Arm_request = new VoltageOut(0);    
    
    public TalonFX leftArm = new TalonFX(Constants.Arms.leftArmMotorID);
    public TalonFX rightArm = new TalonFX(Constants.Arms.rightArmMotorID);

    public Arms() {
        
    }

    public void setSpeeds(double speed) {
        leftArm.setControl(Arm_request.withOutput(Constants.Arms.armMaxVoltage * Constants.Drive.basePercentOutput));
        rightArm.setControl(Arm_request.withOutput(Constants.Arms.armMaxVoltage * Constants.Drive.basePercentOutput));
    }  

    public void brakeMotors() {
        leftArm.setControl(Arm_request.withOutput(0));
        rightArm.setControl(Arm_request.withOutput(0));
    }

    public void brakeLeftMotor() { // For manual arm calibration
        leftArm.setControl(Arm_request.withOutput(0));
    }

    public void brakeRightMotor() { // For manual arm calibration
        rightArm.setControl(Arm_request.withOutput(0));
    }

    public double getLeftArmPosition() {
        var leftPositionSignal = leftArm.getPosition();
        double leftAnglePosition = leftPositionSignal.getValueAsDouble();
        return Units.rotationsToDegrees(leftAnglePosition);
    }

    public double getRightArmPosition() {
        var rightPositionSignal = rightArm.getPosition();
        double rightAnglePosition = rightPositionSignal.getValueAsDouble();
        return Units.rotationsToDegrees(rightAnglePosition);
    }

    @Override
    public void periodic() {
        if (Constants.Display.showTheta) {
            System.out.println("Left Arm (ID: 9): " + getLeftArmPosition());
            System.out.println("Right Arm (ID: 10): " + getRightArmPosition());
        }
    }
}
