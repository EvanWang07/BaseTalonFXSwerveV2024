package frc.robot.commands;

import frc.robot.subsystems.Climbers;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class TeleopClimb extends Command {
    private Climbers c_Climbers;
    private DoubleSupplier speedUpSup;
    private DoubleSupplier speedDownSup;
    
    public TeleopClimb(Climbers c_Climbers, DoubleSupplier speedUpSup, DoubleSupplier speedDownSup) {
        this.c_Climbers = c_Climbers;
        addRequirements(c_Climbers);

        this.speedUpSup = speedUpSup;
        this.speedDownSup = speedDownSup;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double c_upSpeed = speedUpSup.getAsDouble();
        double c_downSpeed = speedDownSup.getAsDouble();
        c_Climbers.setClimberMotorSpeeds(c_upSpeed - c_downSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        c_Climbers.brakeClimberMotors();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
