import Behaviours.AdvancedRotatingSensor;
import Behaviours.Bumper;
import Behaviours.DriveForward;
import Behaviours.FrontSensing;
import Behaviours.RotatingSensor;
import lejos.hardware.*;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import main.RobotController;

public class Robot {
	private Arbitrator arbitrator = null;
	
	private Behavior forward;
	private Behavior rightBumper;
	private Behavior leftBumper;
	private Behavior frontSensing;
	private Behavior rotatingSensor;
	private Behavior advancedRotatingSensor;
	
	public Robot(RobotController controller) {
		int numScans = 7;
		
		forward = new DriveForward(controller);
		rightBumper = new Bumper(controller, true);
		leftBumper = new Bumper(controller, false);
		frontSensing = new FrontSensing(controller);
		rotatingSensor = new RotatingSensor(controller);
		advancedRotatingSensor = new AdvancedRotatingSensor(controller, numScans);
	}
	
	public void start() {
		if(this.arbitrator != null) {
			this.arbitrator.go();
		}
	}
	
	/*
	 * Front sonic sensor and bumpers
	 */
	public void frontSensing() {
		Behavior [] bArray = {forward, rightBumper, leftBumper, frontSensing};
	    arbitrator = new Arbitrator(bArray);
	}
	
	/*
	 * Sensor stops robot crashing, then scans right side, then left or goes backwards, goes to first space
	 */
	public void basicRotatingSensor() {
		Behavior [] bArray = {forward, rightBumper, leftBumper, rotatingSensor};
	    arbitrator = new Arbitrator(bArray);
	}
	
	/*
	 * Sensor stops robot crashing, then scans multiple locations in front of robot, moves to location with most space. 
	 */
	public void advancedRotatingSensor() {
		Behavior [] bArray = {forward, rightBumper, leftBumper, advancedRotatingSensor};
	    arbitrator = new Arbitrator(bArray);
	}

	public static void main(String[] args) {
		float stoppingThreshold = .1f;
		
		RobotController controller = new RobotController(Motor.A, Motor.D, SensorPort.S1, SensorPort.S4, SensorPort.S2, Motor.B, stoppingThreshold);
		
		Robot robot = new Robot(controller);
		
		robot.advancedRotatingSensor();	
		robot.start();
	}
}
