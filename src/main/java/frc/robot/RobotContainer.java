package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

// import frc.robot.autos.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final Joystick driver = new Joystick(Constants.Drive.driveController);
    private final Joystick weapons = new Joystick(Constants.Drive.weaponController);

    /* Drive Controls */
    private final int d_translationAxis = XboxController.Axis.kLeftY.value;
    private final int d_strafeAxis = XboxController.Axis.kLeftX.value;
    private final int d_rotationAxis = XboxController.Axis.kRightX.value;

    /* Weapon Controls */
    private final int w_rotationAxis = XboxController.Axis.kLeftY.value;
    private final int w_climbUpAxis = XboxController.Axis.kLeftTrigger.value;
    private final int w_climbDownAxis = XboxController.Axis.kRightTrigger.value;

    /* Driver Buttons */
    private final JoystickButton d_angleAlign = new JoystickButton(driver, XboxController.Button.kX.value);
    private final JoystickButton d_zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton d_robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    private final JoystickButton d_slowMode = new JoystickButton(driver, XboxController.Button.kRightBumper.value);

    /* Weapons Buttons */
    private final JoystickButton w_resetArm = new JoystickButton(weapons, XboxController.Button.kY.value);
    private final JoystickButton w_trapAutoArm = new JoystickButton(weapons, XboxController.Button.kA.value);
    private final JoystickButton w_ampAutoArm = new JoystickButton(weapons, XboxController.Button.kB.value);
    private final JoystickButton w_speakerAutoArm = new JoystickButton(weapons, XboxController.Button.kX.value);
    private final JoystickButton w_shootNote = new JoystickButton(weapons, XboxController.Button.kLeftBumper.value);
    private final JoystickButton w_intakeNote = new JoystickButton(weapons, XboxController.Button.kRightBumper.value);
    private final JoystickButton w_slowMode = new JoystickButton(weapons, XboxController.Button.kLeftStick.value);

    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    private final Arms a_Arms = new Arms();
    private final Jukebox j_Jukebox = new Jukebox();
    private final Climbers c_Climbers = new Climbers();
    private final Vision v_Vision = new Vision();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        
        /* PathPlanner Registered Commands */
        NamedCommands.registerCommand("Shoot Note", new InstantCommand(() -> j_Jukebox.runJukebox(Constants.Jukebox.shooterSpeed)));

        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> -driver.getRawAxis(d_translationAxis), 
                () -> -driver.getRawAxis(d_strafeAxis), 
                () -> -driver.getRawAxis(d_rotationAxis), 
                () -> d_robotCentric.getAsBoolean(),
                () -> d_slowMode.getAsBoolean()
            )
        );

        a_Arms.setDefaultCommand(
            new TeleopArm(
                a_Arms,
                () -> -weapons.getRawAxis(w_rotationAxis),
                () -> w_slowMode.getAsBoolean()
            )
        );

        j_Jukebox.setDefaultCommand(
            new TeleopIntake(
                j_Jukebox,
                () -> w_intakeNote.getAsBoolean() ? 1.0 : 0.0
            )
        );

        c_Climbers.setDefaultCommand(
            new TeleopClimb(
                c_Climbers,
                () -> weapons.getRawAxis(w_climbUpAxis),
                () -> weapons.getRawAxis(w_climbDownAxis)
            )
        );

        v_Vision.setDefaultCommand(
            new AutoAlign(
                v_Vision, 
                s_Swerve, 
                () -> d_angleAlign.getAsBoolean()
            )
        );

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        d_zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));

        /* Weapon Buttons */
        if (Constants.Drive.useMotionMagicPID) {
            w_resetArm.onTrue(new InstantCommand(() -> a_Arms.motionMagicAutoSetArmPosition(Constants.Arms.calculatedArmThetaAtDefault)));
            w_trapAutoArm.onTrue(new InstantCommand(() -> a_Arms.motionMagicAutoSetArmPosition(Constants.Arms.calculatedArmThetaAtTrap)));
            w_ampAutoArm.onTrue(new InstantCommand(() -> a_Arms.motionMagicAutoSetArmPosition(Constants.Arms.calculatedArmThetaAtAmp)));
            w_speakerAutoArm.onTrue(new InstantCommand(() -> a_Arms.motionMagicAutoSetArmPosition(Constants.Arms.calculatedArmThetaAtSpeaker)));
        } else {
            if (Constants.Drive.useIndividualMotorPID) {
                w_resetArm.onTrue(new InstantCommand(() -> a_Arms.autoSetLeftArmPosition(Constants.Arms.calculatedArmThetaAtDefault)));
                w_resetArm.onTrue(new InstantCommand(() -> a_Arms.autoSetRightArmPosition(Constants.Arms.calculatedArmThetaAtDefault)));
                w_trapAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetLeftArmPosition(Constants.Arms.calculatedArmThetaAtTrap)));
                w_trapAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetRightArmPosition(Constants.Arms.calculatedArmThetaAtTrap)));
                w_ampAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetLeftArmPosition(Constants.Arms.calculatedArmThetaAtAmp)));
                w_ampAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetRightArmPosition(Constants.Arms.calculatedArmThetaAtAmp)));
                w_speakerAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetLeftArmPosition(Constants.Arms.calculatedArmThetaAtSpeaker)));
                w_speakerAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetRightArmPosition(Constants.Arms.calculatedArmThetaAtSpeaker)));
            } else {
                w_resetArm.onTrue(new InstantCommand(() -> a_Arms.autoSetArmPosition(Constants.Arms.calculatedArmThetaAtDefault)));
                w_trapAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetArmPosition(Constants.Arms.calculatedArmThetaAtTrap)));
                w_ampAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetArmPosition(Constants.Arms.calculatedArmThetaAtAmp)));
                w_speakerAutoArm.onTrue(new InstantCommand(() -> a_Arms.autoSetArmPosition(Constants.Arms.calculatedArmThetaAtSpeaker)));
            }
        }
        w_shootNote.onTrue(new InstantCommand(() -> j_Jukebox.runJukebox(Constants.Jukebox.shooterSpeed)));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // The "BASIC Autonomous Path" will run
        return new PathPlannerAuto("jorge");
    }
}