package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Jukebox;
import frc.robot.subsystems.Time;

import edu.wpi.first.wpilibj2.command.Command;

public class AutoJukebox extends Command {
    private Jukebox j_Jukebox;
    private Time t_Time;

    public AutoJukebox(Jukebox j_Jukebox, Time t_Time) {
        this.j_Jukebox = j_Jukebox;
        addRequirements(j_Jukebox);
        this.t_Time = t_Time;
        addRequirements(t_Time);
    }

    @Override
    public void initialize() {
        t_Time.resetTime();
    }

    @Override
    public void execute() {
        if (t_Time.getTime() < 2.5) {
            j_Jukebox.setShooterMotorSpeeds(Constants.Jukebox.shooterSpeed);
        }
        if (t_Time.getTime() < 1) {
            j_Jukebox.setIntakeMotorSpeeds(1, true);
        }
        if (t_Time.getTime() >= 2.5) {
            end(true);
        }
    }

    @Override
    public void end(boolean interrupted) {
        j_Jukebox.brakeIntakeMotors();
        j_Jukebox.brakeShooterMotors();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
