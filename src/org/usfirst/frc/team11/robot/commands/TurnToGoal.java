package org.usfirst.frc.team11.robot.commands;


import org.usfirst.frc.team11.robot.Robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * AdjustToGoal - Adjust robot to goal using the camera
 *
 * @author Sahit Chintalapudi
 * @author Jakob Shortell
 * @author Matt Krzyzanowski
 */
public class TurnToGoal extends Command {       
    private double area = 0;
    private boolean isFinished = false;
    private int curr_index = 0,target_index = 0;
    private double curr_pix = -1,error;
    Timer timer;    
    double thistime = 0,lasttime = 0;
    double netError;
    double kP = 0.0055,kI = 0.000;
    boolean kangaroo = true;
    Talon left,right;
    public TurnToGoal() {           
        timer = new Timer();
        setInterruptible(true);
        left = new Talon(0);
        right = new Talon(1);
    }    
    public TurnToGoal(boolean kangaroo) {           
        timer = new Timer();
        setInterruptible(true);
    	this.kangaroo = kangaroo;
    }
    
    protected void initialize() {    	
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");                     
        timer.start();
    }
    protected void execute() {      	
    		Robot.cam.threshold(Robot.cam.getPicture());
    		curr_pix = Robot.cam.getBlob();
    		System.out.println("curr pix: " + curr_pix);
	        thistime = timer.get(); 
	    	lasttime = thistime;
	        error = curr_pix - 158;
	    	netError += (error * (thistime - lasttime));
	        //right.set(-(error * kP + netError * kI));
	        //left.set((error * kP + netError * kI));
    }    	    
    protected boolean isFinished() {
//    	return (Math.abs(distance - currentDistanceLeft) < 8 &&
    	System.out.println("time: " + timer.get());
    	return timer.get() > 2.5 || isFinished;
    	//return ((Math.abs(error) < 0.1 && loopStarted)) || (debug && this.isFinished);
    	//return false;
    }
    protected void end() {
    	this.isFinished = false;    	
    	
    }
    protected void interrupted() {
    }
}