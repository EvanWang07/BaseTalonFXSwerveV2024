package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoAlign extends Command {
    private Vision v_Vision;
    private Swerve s_Swerve;
    private BooleanSupplier autoAlign;

    public AutoAlign(Vision v_Vision, Swerve s_Swerve, BooleanSupplier autoAlign) {
        this.v_Vision = v_Vision;
        addRequirements(v_Vision);
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);

        this.autoAlign = autoAlign;
    }

    @Override
    public void execute() {
        double headingAlignVelocity = v_Vision.getTX() * Constants.Vision.visionRotationKP;
        double distanceAlignVelocity = v_Vision.getTY() * Constants.Vision.visionTranslationKP;
        boolean m_autoAlign = autoAlign.getAsBoolean();

        if (m_autoAlign) {
            if (v_Vision.hasTarget()) {
                double headingError = v_Vision.getTX();
                double distanceError = v_Vision.getTY();
                double rotationalAdjustment = 0.0;
                double translationalAdjustment = 0.0;
                boolean autoAlignCompleted = false;

                if (Math.abs(headingError) > Constants.Vision.maxHorizontalAngleAlignError) {
                    if (headingError < 0) {
                        rotationalAdjustment = headingAlignVelocity + Constants.Vision.visionMinimumRotationalMovement;
                    } else {
                        rotationalAdjustment = headingAlignVelocity - Constants.Vision.visionMinimumRotationalMovement;
                    }
                    s_Swerve.drive(
                        new Translation2d(0, 0), 
                        rotationalAdjustment * Constants.Swerve.maxAngularVelocity, 
                        false, 
                        false
                    );
                } else if (Math.abs(distanceError) > Constants.Vision.distanceAlignSetpoint) {
                    if (distanceError < 0) {
                        translationalAdjustment = distanceAlignVelocity + Constants.Vision.visionMinimumTranslationalMovement;
                    } else {
                        translationalAdjustment = distanceAlignVelocity - Constants.Vision.visionMinimumTranslationalMovement;
                    }
                    s_Swerve.drive(
                        new Translation2d(0, translationalAdjustment), 
                        0, 
                        false, 
                        false
                    );
                } else {
                    autoAlignCompleted = true;
                    SmartDashboard.putBoolean("Automatic Target Completed", autoAlignCompleted);
                }
            }
        }
    }
}
