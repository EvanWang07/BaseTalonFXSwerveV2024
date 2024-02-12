package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.VoltageOut;

public class Arms extends SubsystemBase {
    private final VoltageOut Arm_request = new VoltageOut(0);
    public TalonFX leftArm;
    public TalonFX rightArm;

    public Arms() {
        leftArm = new TalonFX(Constants.Arms.leftArmMotorID);
        rightArm = new TalonFX(Constants.Arms.rightArmMotorID);
    }

    public void setArmMotorSpeeds(double speed) {
        leftArm.setControl(Arm_request.withOutput(Constants.Arms.armsMaxVoltage * Constants.Drive.basePercentArmOutput));
        rightArm.setControl(Arm_request.withOutput(Constants.Arms.armsMaxVoltage * Constants.Drive.basePercentArmOutput));
    }

    public void setLeftArmMotorSpeed(double speed) { // For manual arm calibration
        leftArm.setControl(Arm_request.withOutput(Constants.Arms.armsMaxVoltage * Constants.Drive.basePercentArmOutput));
    }

    public void setRightArmMotorSpeed(double speed) { // For manual arm calibration
        rightArm.setControl(Arm_request.withOutput(Constants.Arms.armsMaxVoltage * Constants.Drive.basePercentArmOutput));
    }

    public void brakeArmMotors() {
        leftArm.setControl(Arm_request.withOutput(0));
        rightArm.setControl(Arm_request.withOutput(0));
    }

    public void brakeLeftArmMotor() { // For manual arm calibration
        leftArm.setControl(Arm_request.withOutput(0));
    }

    public void brakeRightArmMotor() { // For manual arm calibration
        rightArm.setControl(Arm_request.withOutput(0));
    }

    public double getLeftArmPosition() {
        var leftPositionSignal = leftArm.getPosition();
        double leftAnglePosition = leftPositionSignal.getValueAsDouble() + Constants.Arms.leftArmThetaOffset;
        return Units.rotationsToDegrees(leftAnglePosition);
    }

    public double getRightArmPosition() {
        var rightPositionSignal = rightArm.getPosition();
        double rightAnglePosition = rightPositionSignal.getValueAsDouble() + Constants.Arms.rightArmThetaOffset;
        return Units.rotationsToDegrees(rightAnglePosition);
    }

    public double getAverageArmPosition() {
        double averageArmPosition = (getLeftArmPosition() + getRightArmPosition()) / 2;
        return averageArmPosition;
    }

    public void autoSetArmPosition(double setPoint) {
        PIDController a_PIDController = new PIDController(Constants.Arms.armKP, Constants.Arms.armKI, Constants.Arms.armKD);
        a_PIDController.setSetpoint(setPoint);
        if (Math.abs(getLeftArmPosition() - getRightArmPosition()) <= Constants.Arms.armsMaxErrorTolerance) { // Checks if the motors are synchronized
            a_PIDController.reset();
            while ((getAverageArmPosition() > (setPoint + (Constants.Arms.armsMaxErrorTolerance / 2))) || (getAverageArmPosition() < (setPoint - (Constants.Arms.armsMaxErrorTolerance / 2)))) {
                double newLeftArmSpeed = a_PIDController.calculate(getLeftArmPosition());
                setLeftArmMotorSpeed(newLeftArmSpeed * Constants.Arms.percentAutomaticArmOutput);
                double newRightArmSpeed = a_PIDController.calculate(getRightArmPosition());
                setRightArmMotorSpeed(newRightArmSpeed * Constants.Arms.percentAutomaticArmOutput);
            }
            brakeArmMotors();
            a_PIDController.close();
        } else {
            System.out.println("WARNING: Arms need calibration!");
        }
    }

    @Override
    public void periodic() {
        if (Constants.Display.showArmTheta) {
            SmartDashboard.putNumber("Arm Positions", (getLeftArmPosition() / Constants.Arms.armMotorGearRatio));
            SmartDashboard.putNumber("Arm Positions", (getRightArmPosition() / Constants.Arms.armMotorGearRatio));
        }
    }
}
