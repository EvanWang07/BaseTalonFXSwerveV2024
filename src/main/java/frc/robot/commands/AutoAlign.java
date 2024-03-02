package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoAlign extends Command {
    private Vision v_vision;
    private Swerve s_Swerve;
    private BooleanSupplier autoAlign;

    public AutoAlign(Vision v_vision, Swerve s_Swerve, BooleanSupplier autoAlign) {
        this.v_vision = v_vision;
        addRequirements(v_vision);
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);

        this.autoAlign = autoAlign;
    }

    @Override
    public void execute() {
        double horizontalAlignVelocity = v_vision.getTX() * Constants.Vision.visionKP;
        double min_Movement = Constants.Vision.visionMin_Movement;
        boolean m_autoAlign = autoAlign.getAsBoolean();

        if (m_autoAlign) {
            double headingError = v_vision.getTX();
            double rotationAdjustment = 0.0;

            if (Math.abs(headingError) > Constants.Vision.maxHorizontalAngleAlignError) {
                if (headingError < 0) {
                    rotationAdjustment = horizontalAlignVelocity + min_Movement;
                } else {
                    rotationAdjustment = horizontalAlignVelocity - min_Movement;
                }
            } 
            
            s_Swerve.drive(
                new Translation2d(0, 0), 
                rotationAdjustment * Constants.Swerve.maxAngularVelocity, 
                false, 
                false
            );
        }
    }
}
