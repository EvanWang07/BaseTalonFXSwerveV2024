package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Arms;

import java.util.function.DoubleSupplier;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;


public class TeleopArm extends Command {
    private Arms a_Arms;
    private DoubleSupplier speedSup;
    private BooleanSupplier slowModeSup;

    public TeleopArm(Arms a_Arms, DoubleSupplier speedSup, BooleanSupplier slowModeSup) {
        this.a_Arms = a_Arms;
        addRequirements(a_Arms);

        this.speedSup = speedSup;
        this.slowModeSup = slowModeSup;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double a_speed = speedSup.getAsDouble();
        boolean a_slowMode = slowModeSup.getAsBoolean();

        if (Constants.Arms.callibrationMode) {
            /* Left Arm Motor */
            if (a_Arms.getLeftArmPosition() <= 9000 && a_Arms.getLeftArmPosition() >= 0) {
                if (a_slowMode) {
                    a_Arms.setSpeeds(a_speed * Constants.Drive.percentBasePercentOutput);
                } else {
                    a_Arms.setSpeeds(a_speed);
                }
            } else if (a_Arms.getLeftArmPosition() > 9000) {
                if (a_speed < 0) {
                    if (a_slowMode) {
                        a_Arms.setSpeeds(a_speed * Constants.Drive.percentBasePercentOutput);
                    } else {
                        a_Arms.setSpeeds(a_speed);
                    }
                } else {
                    a_Arms.brakeLeftMotor();
                }
            } else {
                if (a_speed > 0) {
                    if (a_slowMode) {
                        a_Arms.setSpeeds(a_speed * Constants.Drive.percentBasePercentOutput);
                    } else {
                        a_Arms.setSpeeds(a_speed);
                    }
                } else {
                    a_Arms.brakeLeftMotor();
                }
            }

            /* Right Arm Motor */
            if (a_Arms.getRightArmPosition() <= 9000 && a_Arms.getRightArmPosition() >= 0) {
                if (a_slowMode) {
                    a_Arms.setSpeeds(a_speed * Constants.Drive.percentBasePercentOutput);
                } else {
                    a_Arms.setSpeeds(a_speed);
                }
            } else if (a_Arms.getRightArmPosition() > 9000) {
                if (a_speed < 0) {
                    if (a_slowMode) {
                        a_Arms.setSpeeds(a_speed * Constants.Drive.percentBasePercentOutput);
                    } else {
                        a_Arms.setSpeeds(a_speed);
                    }
                } else {
                    a_Arms.brakeRightMotor();
                }
            } else {
                if (a_speed > 0) {
                    if (a_slowMode) {
                        a_Arms.setSpeeds(a_speed * Constants.Drive.percentBasePercentOutput);
                    } else {
                        a_Arms.setSpeeds(a_speed);
                    }
                } else {
                    a_Arms.brakeRightMotor();
                }
            }
        } else {

        }
    }

    @Override
    public void end(boolean interrupted) {
        a_Arms.brakeMotors();
        if (Constants.Display.showTheta) {
            System.out.println("Left Arm (ID: 9): " + a_Arms.getLeftArmPosition());
            System.out.println("Right Arm (ID: 10): " + a_Arms.getRightArmPosition());
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
     
}
