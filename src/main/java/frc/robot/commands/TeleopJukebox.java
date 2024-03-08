package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Jukebox;

import java.util.function.DoubleSupplier;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class TeleopJukebox extends Command {
    private Jukebox j_Jukebox;
    private DoubleSupplier speedSup;
    private BooleanSupplier useIntake;
    private BooleanSupplier useShooter;
    
    public TeleopJukebox(Jukebox j_Jukebox, DoubleSupplier speedSup, BooleanSupplier useIntake, BooleanSupplier useShooter) {
        this.j_Jukebox = j_Jukebox;
        addRequirements(j_Jukebox);

        this.speedSup = speedSup;
        this.useIntake = useIntake;
        this.useShooter = useShooter;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double j_speed = speedSup.getAsDouble();
        boolean j_useIntake = useIntake.getAsBoolean();
        boolean j_useShooter = useShooter.getAsBoolean();
        if (j_useIntake && (!(j_Jukebox.checkSensorBreakage()))) {
            j_Jukebox.setIntakeMotorSpeeds(j_speed, false);
            System.out.println("Intake Running");
        } else {
            j_Jukebox.brakeIntakeMotors();
        }
        if (j_useShooter) {
            j_Jukebox.setShooterMotorSpeeds(Constants.Jukebox.shooterSpeed);
        } else {
            j_Jukebox.brakeShooterMotors();
        }
    }

    @Override
    public void end(boolean interrupted) {
        j_Jukebox.brakeIntakeMotors();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
