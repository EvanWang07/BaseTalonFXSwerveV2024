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

        
    }
}
