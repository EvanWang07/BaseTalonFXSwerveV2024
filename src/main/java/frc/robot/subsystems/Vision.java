package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.ArrayList;

public class Vision extends SubsystemBase {
    private final NetworkTable m_limelightTable;
    private double tx, ta;
    private ArrayList<Double> m_targetList;
    private final int MAX_ENTRIES = 50;

    /**
    * Creates a new Vision.
    */
    public Vision() {
        m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
        m_targetList = new ArrayList<Double>(MAX_ENTRIES);
    }

    @Override
    public void periodic() {
        m_limelightTable.getEntry("tv").getDouble(0);
        tx = m_limelightTable.getEntry("tx").getDouble(0);
        ta = m_limelightTable.getEntry("ta").getDouble(0);
                
        if (m_targetList.size() >= MAX_ENTRIES) {
            m_targetList.remove(0);
        }
        
        m_targetList.add(ta);
    }

    public double getTX() {
        return tx;
    }

    public double getTA() {
        double sum = 0;

        for (Double num : m_targetList) { 		      
            sum += num.doubleValue();
        }
        return sum/m_targetList.size();
    }

    public boolean hasTarget() {
        NetworkTableEntry tv = m_limelightTable.getEntry("tv");
        return tv.getDouble(0) >= 1.0; //tv represents whether it has a vision target
    }

}
