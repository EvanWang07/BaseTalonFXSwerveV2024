package frc.robot.commands;

import frc.robot.subsystems.Time;
import frc.robot.subsystems.Jukebox;

import edu.wpi.first.wpilibj2.command.Command;

public class AutoIntake extends Command {
    private Time t_Time;
    private Jukebox j_Jukebox;

    public AutoIntake(Time t_Time, Jukebox j_Jukebox) {
        this.t_Time = t_Time;
        addRequirements(t_Time);
        this.j_Jukebox = j_Jukebox;
        addRequirements(j_Jukebox);
    }

    @Override
    public void initialize() {
        t_Time.resetTime();
    }

    @Override
    public void execute() {
        if (t_Time.getTime() <= 1.5) {
            if (!(j_Jukebox.checkSensorBreakage())) {
                j_Jukebox.setIntakeMotorSpeeds(1.0, false);
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        j_Jukebox.setIntakeMotorSpeeds(0, true);
    }

    @Override
    public boolean isFinished() {
        if (t_Time.getTime() > 1.5) {
            return true;
        } else {
            return false;
        }
    }    
}
