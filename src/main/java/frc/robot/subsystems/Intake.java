package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

public class Intake extends SubsystemBase {
    // private final CANSparkMax intakeMotor;

    public Intake() {
        // intakeMotor = new CANSparkMax(0, MotorType.kBrushless);
    }

    @Override
    public void periodic() {

    }
}
