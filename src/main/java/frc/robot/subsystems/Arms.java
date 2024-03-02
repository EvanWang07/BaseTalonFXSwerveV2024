package frc.robot.subsystems;

import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
// import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.Robot;

public class Arms extends SubsystemBase {
    public final VoltageOut Arm_request = new VoltageOut(0);
    MotionMagicVoltage Magic_arm_request = new MotionMagicVoltage(0);
    public TalonFX leftArm;
    public TalonFX rightArm;
    public double PIDTargetPosition;

    public Arms() {
        leftArm = new TalonFX(Constants.Arms.leftArmMotorID);
        rightArm = new TalonFX(Constants.Arms.rightArmMotorID);

        leftArm.getConfigurator().apply(Robot.ctreConfigs.armTalonFXConfigs);
        rightArm.getConfigurator().apply(Robot.ctreConfigs.armTalonFXConfigs);

        new Thread(() -> {
        try {
            Thread.sleep(1000);
            leftArm.setInverted(Constants.Arms.leftArmMotorInverted);
            rightArm.setInverted(Constants.Arms.rightArmMotorInverted);
        } catch (Exception e) {
        }
        }).start();
    }

    public double getScaledPercentArmOutput(double oldOutput) {
        double angleBoundsAdapter = 360 / Constants.Arms.armsMaximumRotation;
        double armOutputMultiplier = 0.075 + 0.925 * Math.abs(Math.sin(Math.toRadians(0.5 * (getAverageArmPosition() / Constants.Arms.armMotorGearRatio) * angleBoundsAdapter)));
        double newArmOutput = armOutputMultiplier * oldOutput;
        return newArmOutput;
    }

    public void setArmMotorSpeeds(double speed) {
        leftArm.setControl(Arm_request.withOutput(speed * Constants.Arms.armsMaxVoltage * getScaledPercentArmOutput(Constants.Drive.maxBasePercentArmOutput)));
        rightArm.setControl(Arm_request.withOutput(speed * Constants.Arms.armsMaxVoltage * getScaledPercentArmOutput(Constants.Drive.maxBasePercentArmOutput)));
    }

    public void setLeftArmMotorSpeed(double speed) { // For manual arm calibration
        leftArm.setControl(Arm_request.withOutput(speed * Constants.Arms.armsMaxVoltage * getScaledPercentArmOutput(Constants.Drive.maxBasePercentArmOutput)));
    }

    public void setRightArmMotorSpeed(double speed) { // For manual arm calibration
        rightArm.setControl(Arm_request.withOutput(speed * Constants.Arms.armsMaxVoltage * getScaledPercentArmOutput(Constants.Drive.maxBasePercentArmOutput)));
    }

    public void correctArmMotorPositions() { // Not used as of right now
        if (getAverageArmPosition() > (Constants.Arms.armUpperBoundTheta + 25)) {
            while (getAverageArmPosition() > (Constants.Arms.armUpperBoundTheta + 25)) {
                leftArm.setControl(Arm_request.withOutput(0.05 * Constants.Arms.armsMaxVoltage));
                rightArm.setControl(Arm_request.withOutput(0.05 * Constants.Arms.armsMaxVoltage));
            }
            brakeArmMotors();
        } else if (getAverageArmPosition() < (Constants.Arms.armLowerBoundTheta - 25)) {
            while (getAverageArmPosition() < (Constants.Arms.armLowerBoundTheta - 25)) {
                leftArm.setControl(Arm_request.withOutput(-0.05 * Constants.Arms.armsMaxVoltage));
                rightArm.setControl(Arm_request.withOutput(-0.05 * Constants.Arms.armsMaxVoltage));
            }
            brakeArmMotors();
        }
    }

    public void brakeArmMotors() {
        // leftArm.setControl(Arm_request.withOutput(0));
        // rightArm.setControl(Arm_request.withOutput(0));
        // leftArm.setNeutralMode(NeutralModeValue.Brake);
        // rightArm.setNeutralMode(NeutralModeValue.Brake);
        leftArm.stopMotor();
        rightArm.stopMotor();
    }

    public void brakeLeftArmMotor() { // For manual arm calibration
        // leftArm.setControl(Arm_request.withOutput(0));
        // leftArm.setNeutralMode(NeutralModeValue.Brake);
        leftArm.stopMotor();
    }

    public void brakeRightArmMotor() { // For manual arm calibration
        // rightArm.setControl(Arm_request.withOutput(0));
        // rightArm.setNeutralMode(NeutralModeValue.Brake);
        rightArm.stopMotor();
    }

    public double getLeftArmPosition() {
        var leftPositionSignal = leftArm.getPosition();
        double leftAnglePosition = leftPositionSignal.getValueAsDouble() + Constants.Arms.calculatedLeftArmThetaOffset;
        return Units.rotationsToDegrees(leftAnglePosition);
    }

    public double getRightArmPosition() {
        var rightPositionSignal = rightArm.getPosition();
        double rightAnglePosition = rightPositionSignal.getValueAsDouble() + Constants.Arms.calculatedRightArmThetaOffset;
        return Units.rotationsToDegrees(rightAnglePosition);
    }

    public double getAverageArmPosition() {
        double averageArmPosition = (getLeftArmPosition() + getRightArmPosition()) / 2;
        return averageArmPosition;
    }

    public double returnSpeed(double speed) {
        return speed;
    }

    public void motionMagicAutoSetArmPosition(double setPoint) {
        PIDTargetPosition = setPoint / Constants.Arms.armMotorGearRatio;
        double setPointInRotations = setPoint / 360;
        double setToleranceInRotations = Constants.Arms.calculatedMaxPIDArmThetaOffset / 360;
        System.out.println("[Magic PID] Rotating the arm motors to " + (setPoint / Constants.Arms.armMotorGearRatio) + " degrees!");
        while ((Magic_arm_request.Position < (setPointInRotations - setToleranceInRotations)) || (Magic_arm_request.Position > (setPointInRotations + setToleranceInRotations))) {
            leftArm.setControl(Magic_arm_request.withPosition(setPointInRotations));
            rightArm.setControl(Magic_arm_request.withPosition(setPointInRotations));
            System.out.println("[Magic PID] Running. Current position: " + getAverageArmPosition() / Constants.Arms.armMotorGearRatio);
        }
        brakeArmMotors();
        System.out.println("[Magic PID] Finished rotating the arm motors to " + (setPoint / Constants.Arms.armMotorGearRatio) + " degrees!");
        System.out.println("[Magic PID] An error of " + (Math.abs(getAverageArmPosition() - setPoint) / Constants.Arms.armMotorGearRatio) + " degree(s) was detected!");
    }

    public void autoSetArmPosition(double setPoint) {
        PIDTargetPosition = setPoint / Constants.Arms.armMotorGearRatio;
        PIDController a_PIDController = new PIDController(Constants.Arms.armKP, Constants.Arms.armKI, Constants.Arms.armKD);
        a_PIDController.setSetpoint(setPoint);
        System.out.println("[PID] Rotating the arm motors to " + (setPoint / Constants.Arms.armMotorGearRatio) + " degrees!");
        a_PIDController.setTolerance(Constants.Arms.calculatedMaxPIDArmThetaOffset);
        a_PIDController.setIZone(Constants.Arms.calculatedMaxPIDArmIntegrationZone);
        if (Math.abs(getLeftArmPosition() - getRightArmPosition()) <= Constants.Arms.armsMaxErrorTolerance) { // Checks if the motors are synchronized
            a_PIDController.reset();
            while (!(a_PIDController.atSetpoint())) {
                double newArmSpeed = a_PIDController.calculate(getAverageArmPosition());
                setArmMotorSpeeds(newArmSpeed * Constants.Arms.percentAutomaticArmOutput);
                System.out.println("[PID] Running. Current position: " + getAverageArmPosition() / Constants.Arms.armMotorGearRatio);
            }
            brakeArmMotors();
            a_PIDController.close();
            System.out.println("[PID] Finished rotating the arm motors to " + (setPoint / Constants.Arms.armMotorGearRatio) + " degrees!");
            System.out.println("[PID] An error of " + (Math.abs(getAverageArmPosition() - setPoint) / Constants.Arms.armMotorGearRatio) + " degree(s) was detected!");
        } else {
            System.out.println("WARNING: Arms need calibration! [PID]");
        }
    }

    public void autoSetLeftArmPosition(double setPoint) {
        PIDTargetPosition = setPoint / Constants.Arms.armMotorGearRatio;
        PIDController a_PIDController = new PIDController(Constants.Arms.armKP, Constants.Arms.armKI, Constants.Arms.armKD);
        a_PIDController.setSetpoint(setPoint);
        System.out.println("[PID] Rotating the left arm motor to " + (setPoint / Constants.Arms.armMotorGearRatio) + " degrees!");
        a_PIDController.setTolerance(Constants.Arms.calculatedMaxPIDArmThetaOffset);
        a_PIDController.setIZone(Constants.Arms.calculatedMaxPIDArmIntegrationZone);
        if (Math.abs(getLeftArmPosition() - getRightArmPosition()) <= Constants.Arms.armsMaxErrorTolerance) { // Checks if the motors are synchronized
            a_PIDController.reset();
            while (!(a_PIDController.atSetpoint())) {
                double newLeftArmSpeed = a_PIDController.calculate(getLeftArmPosition());
                setArmMotorSpeeds(newLeftArmSpeed * Constants.Arms.percentAutomaticArmOutput);
                System.out.println("[PID] Running. Current left arm position: " + getLeftArmPosition() / Constants.Arms.armMotorGearRatio);
            }
            brakeLeftArmMotor();
            a_PIDController.close();
            System.out.println("[PID] Finished rotating the left arm motor to " + (setPoint / Constants.Arms.armMotorGearRatio) + " degrees!");
            System.out.println("[PID] An error of " + (Math.abs(getLeftArmPosition() - setPoint) / Constants.Arms.armMotorGearRatio) + " degree(s) was detected!");
        } else {
            System.out.println("WARNING: Arms need calibration! [PID Left]");
        }
    }

    public void autoSetRightArmPosition(double setPoint) {
        PIDTargetPosition = setPoint / Constants.Arms.armMotorGearRatio;
        PIDController a_PIDController = new PIDController(Constants.Arms.armKP, Constants.Arms.armKI, Constants.Arms.armKD);
        a_PIDController.setSetpoint(setPoint);
        System.out.println("[PID] Rotating the right arm motor to " + (setPoint / Constants.Arms.armMotorGearRatio) + " degrees!");
        a_PIDController.setTolerance(Constants.Arms.calculatedMaxPIDArmThetaOffset);
        a_PIDController.setIZone(Constants.Arms.calculatedMaxPIDArmIntegrationZone);
        if (Math.abs(getLeftArmPosition() - getRightArmPosition()) <= Constants.Arms.armsMaxErrorTolerance) { // Checks if the motors are synchronized
            a_PIDController.reset();
            while (!(a_PIDController.atSetpoint())) {
                double newRightArmSpeed = a_PIDController.calculate(getRightArmPosition());
                setArmMotorSpeeds(newRightArmSpeed * Constants.Arms.percentAutomaticArmOutput);
                System.out.println("[PID] Running. Current right arm position: " + getRightArmPosition() / Constants.Arms.armMotorGearRatio);
            }
            brakeRightArmMotor();
            a_PIDController.close();
            System.out.println("[PID] finished rotating the right arm motor to " + (setPoint / Constants.Arms.armMotorGearRatio) + " degrees!");
            System.out.println("[PID] An error of " + (Math.abs(getRightArmPosition() - setPoint) / Constants.Arms.armMotorGearRatio) + " degree(s) was detected!");
        } else {
            System.out.println("WARNING: Arms need calibration! [PID Right]");
        }
    }

    @Override
    public void periodic() {
        if (Constants.Display.showArmTheta) {
            SmartDashboard.putNumber("Left Arm Position", (getLeftArmPosition() / Constants.Arms.armMotorGearRatio));
            SmartDashboard.putNumber("Right Arm Position", (getRightArmPosition() / Constants.Arms.armMotorGearRatio));
            SmartDashboard.putNumber("Average Arm Position", (getAverageArmPosition() / Constants.Arms.armMotorGearRatio));
            SmartDashboard.putNumber("Arm Position Discrepancy", Math.abs((getLeftArmPosition() - getRightArmPosition()) / Constants.Arms.armMotorGearRatio));
            SmartDashboard.putNumber("PID Target Position", PIDTargetPosition);
        }
        System.out.println("LEFT: " + leftArm.getInverted());
        System.out.println("RIGHT: " + rightArm.getInverted());
        leftArm.setInverted(Constants.Arms.leftArmMotorInverted);
        rightArm.setInverted(Constants.Arms.rightArmMotorInverted);
    }
}
