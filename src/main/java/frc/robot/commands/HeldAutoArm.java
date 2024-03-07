package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Arms;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class HeldAutoArm extends Command {
    private Arms a_Arms;
    private DoubleSupplier setPoint;

    public HeldAutoArm(Arms a_Arms, DoubleSupplier setPoint) {
        this.a_Arms = a_Arms;
        addRequirements(a_Arms);

        this.setPoint = setPoint;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double a_setPoint = setPoint.getAsDouble();
        double armPIDError = Math.abs(a_setPoint - a_Arms.getAverageArmPosition());
        double armPIDRotationVelocity = armPIDError * Constants.Arms.armHeldKP;
        double rotationalAdjustment = 0;

        if (armPIDError > Constants.Arms.calculatedMaxHeldPIDArmThetaOffset) {
            if (Math.abs(a_Arms.getLeftArmPosition() - a_Arms.getRightArmPosition()) <= Constants.Arms.armsMaxErrorTolerance) { // Checks if the motors are synchronized
                if (a_setPoint < a_Arms.getAverageArmPosition()) {
                    rotationalAdjustment = -armPIDRotationVelocity - Constants.Arms.armHeldPIDMinimumRotationalMovement;
                } else {
                    rotationalAdjustment = armPIDRotationVelocity + Constants.Arms.armHeldPIDMinimumRotationalMovement;
                }
            } else {
                System.out.println("WARNING: Arms need calibration! [HeldAutoArm]");
            }
            
            a_Arms.setArmMotorSpeeds(rotationalAdjustment);
        }
    }

    @Override
    public void end(boolean interrupted) {
        a_Arms.brakeArmMotors();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
