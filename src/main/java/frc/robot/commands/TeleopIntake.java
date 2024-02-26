package frc.robot.commands;

import frc.robot.subsystems.Jukebox;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class TeleopIntake extends Command {
    private Jukebox j_Jukebox;
    private DoubleSupplier speedSup;
    
    public TeleopIntake(Jukebox j_Jukebox, DoubleSupplier speedSup) {
        this.j_Jukebox = j_Jukebox;
        addRequirements(j_Jukebox);

        this.speedSup = speedSup;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double j_speed = speedSup.getAsDouble();
        j_Jukebox.setIntakeMotorSpeeds(j_speed);
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
