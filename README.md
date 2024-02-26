**CHANGELOG**:  <br> 
&ensp;&ensp;1. ADDED an alternative PID (via MotionMagic) for the arm motors  <br> 
&ensp;&ensp;2. ADDED the Jukebox subsystem & TeleopIntake command  <br> 
&ensp;&ensp;3. ADDED the Climbers subsystem & TeleopClimb command  <br>   
**ISSUES & POTENTIAL ERRORS**:  <br> 
&ensp;&ensp;1. *Potential* issue with arm angle offset values bugging the code  <br> 
&ensp;&ensp;2. The original PID within the Arms.java instant command method **has accuracy issues**  <br> 
&ensp;&ensp;3. *Potential* issue with the swerve subsystem's instant command not functioning properly  <br> 
&ensp;&ensp;4. Vision subsystem and AutoAlign command currently **do not work**  <br>   
**TODO LIST**:  <br> 
&ensp;&ensp;1. ADD the correct motor IDs for the NEO motors used in the Jukebox subsystem  <br> 
&ensp;&ensp;2. TEST and TUNE the new MotionMagic PID instant command within the Arms.java subsystem  <br> 
&ensp;&ensp;3. TEST the new Jukebox subsystem and TeleopIntake command  <br> 
&ensp;&ensp;4. TEST the new Climbers subsystem and TeleopClimb command  <br> 
&ensp;&ensp;5. FIX Vision subsystem and AutoAlign command  <br> 
&ensp;&ensp;6. CREATE a viable vision code  <br>   
**UNUSED CODE**:  <br>
&ensp;&ensp;1. correctArmMotorPositions() METHOD within Arms.java  <br> 
&ensp;&ensp;2. returnSpeed(double speed) METHOD within Arms.java  <br> 
&ensp;&ensp;3. AutoArm.java COMMAND  <br>   
**NOTES**:  <br> 
&ensp;&ensp;1. Ethan is currently working on the autonomous code; as such, issues and to-dos relating to the autonomous code has been removed  <br> 
&ensp;&ensp;2. **ALL** code *ABSOLUTELY* needs to be peer-reviewed **AND** tested!!!  <br>   
**CREDITS**:  <br> 
&ensp;&ensp;1. Code Created By: Drake Nguyen, Evan Wang, and Ethan Jiang  <br> 
&ensp;&ensp;2. Robot Created By: FRC Robotics Team 6934 (ACHS Scorpions)  <br>   
**LAST UPDATED**: 2/25/24  <br>   

**DRIVER'S GUIDE**:  <br> 
&ensp;&ensp;1. **DRIVE CONTROLLER** (**PORT 0**):  <br> 
&ensp;&ensp;&ensp;&ensp;* Field-Centric Driving: *MOVE* Left Joystick (x & y)  <br> 
&ensp;&ensp;&ensp;&ensp;* Field-Centric Strafing: *MOVE* Right Joystick (x)  <br> 
&ensp;&ensp;&ensp;&ensp;* Robot-Centric Driving: *HOLD* Left Bumper + *MOVE* Left Joystick (x & y)  <br> 
&ensp;&ensp;&ensp;&ensp;* Robot-Centric Strafing: *HOLD* Left Bumper + *MOVE* Right Joystick (x)  <br> 
&ensp;&ensp;&ensp;&ensp;* Slow-Mode (ALL Driving & ALL Strafing): *HOLD* Right Bumper + *USE* Driving/Strafing Controls  <br> 
&ensp;&ensp;&ensp;&ensp;* Reset Gyro (Field-Centric Driving ONLY): *PRESS* Y-Button  <br> 
&ensp;&ensp;2. **WEAPONS CONTROLLER** (**PORT 1**): <br> 
&ensp;&ensp;&ensp;&ensp;* Arm Movement: *MOVE* Left Joystick (y)  <br> 
&ensp;&ensp;&ensp;&ensp;* Slow-Mode (Arm ONLY): *HOLD* Left Joystick **BUTTON**  <br> 
&ensp;&ensp;&ensp;&ensp;* Arm PID to Reset Position: *PRESS* Y-Button  <br> 
&ensp;&ensp;&ensp;&ensp;* Arm PID to Trap Position: *PRESS* A-Button  <br> 
&ensp;&ensp;&ensp;&ensp;* Arm PID to Amp Position: *PRESS* B-Button  <br> 
&ensp;&ensp;&ensp;&ensp;* Arm PID to Speaker Position: *PRESS* X-Button  <br> 
&ensp;&ensp;&ensp;&ensp;* Intake Note: *HOLD* Right Bumper  <br> 
&ensp;&ensp;&ensp;&ensp;* Shoot Note: *PRESS* Left Bumper  <br> 
&ensp;&ensp;&ensp;&ensp;* Climber Upwards Movement: *HOLD* Left Trigger  <br> 
&ensp;&ensp;&ensp;&ensp;* Climber Downwards Movement: *HOLD* Right Trigger  <br>
