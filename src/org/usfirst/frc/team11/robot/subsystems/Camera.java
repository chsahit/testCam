package org.usfirst.frc.team11.robot.subsystems;

import com.ni.vision.NIVision; 
import com.ni.vision.NIVision.Image; 
import com.ni.vision.NIVision.Range;
import edu.wpi.first.wpilibj.command.Subsystem; 

//This system locates the target on the field using a camera
public class Camera extends Subsystem {
	int session;
	//holds the camera image
	Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

	//holds the thresholded image
	Image thresh  = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
	public Camera() {
		session = NIVision.IMAQdxOpenCamera("cam0", 
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session); // configures grab for set session
	}
		
					
	
	//takes a picture
	public Image getPicture() {
		System.out.println("taking picture");		
		frame.free(); //clears the filespace for the frame variable
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
								
		NIVision.IMAQdxStartAcquisition(session); // starts aquisition for set session
		NIVision.IMAQdxGrab(session, frame, 1); // grabs image taken by camera for editing
		
		NIVision.IMAQdxStopAcquisition(session); // stops aquisition for set session
		return frame; //returns the picture that takePicture() method took
	}
	
	//runs and HSL thresh on the image to find the green
	public void threshold(Image picture) {
				
		thresh.free(); //clears the filespace for the thresh variable
		thresh =  NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
		//CameraServer.getInstance().setImage(picture); 
		//store the thresh results in the thresh variable		
	    NIVision.imaqColorThreshold(thresh, picture, 255, NIVision.ColorMode.HSV, 
	    		new Range(52,96),new Range(83,255), new Range(157,255));
	}
	
    
    public double getBlob() {
    	Image image = thresh;
		double numParticles = NIVision.imaqCountParticles(image, 0);
		double largestSize = 0,size;
		int particleIndex = 0;
		if(numParticles > 0) {
			for(int i = 0; i < numParticles; i++) {
				size = NIVision.imaqMeasureParticle(image, i, 0,
						NIVision.MeasurementType.MT_AREA);
				if(size > largestSize) {
					particleIndex = i;
					largestSize = size;					
				}
			}
		double loc_x = NIVision.imaqMeasureParticle(
				image, particleIndex, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_X);
		double loc_y = NIVision.imaqMeasureParticle(
				image, particleIndex, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_Y);
		image.free();
		return loc_x;
		} else {
			image.free();
			return -1;
		}
	}
    
    public void clearBuffs() {
    	frame.free();
    	thresh.free();
    }



	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
