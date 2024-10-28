//package org.firstinspires.ftc.teamcode.Subsystems;
//
//// CRServo, motor, timer
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.Util.RobotHardware;
//public class HangSubsystem {
//    OpMode opmode;
//
//    // allows access to hardware
//    private final RobotHardware rHardware = new RobotHardware();
//
//    // the two required servos
//    private CRServo hangServo1, hangServo2;
//
//    // the motor for the sliding mechanism
//    private DcMotorEx slideMotor;
//
//    // booleans to keep track of deployment and stage activation status --> allows to restrict amount of times button is pressed
//    private boolean deployed = false; // this is for starting the hang (stage 1 hang)
//    private boolean stageTwoActivated = false; // this is for the stage 2 part of the hang
//
//    // the timer for tracking the duration of servo and slide actions --> used to determine servo runtime
//    private ElapsedTime timer = new ElapsedTime();
//
//    // constants for power and time --> adjust all as needed
//    private static final double CR_SERVO_POWER = 1.0; // power for CR servos to run
//    private static final double CR_SERVO_REVERSE_POWER = -1.0; // power for CR servos to run (in the opposite direction)
//    private static final double SLIDE_POWER = 1.0; // power for the slide motor
//    private static final double SERVO_RUN_TIME = 2.0; // time (seconds) for CR servos runtime --> adjust ASAP
//    private static final double SLIDE_EXTENSION_TIME = 1.5; // time (seconds) for slide extension
//    private static final double SLIDE_RETRACTION_POWER = -1.0; // power for reversing the slides
//    private static final double SLIDE_RETRACTION_TIME = 1.5; // time (seconds) for slide retraction
//
//
//    public HangSubsystem() {}
//    public void initialize(OpMode opmode) {
//        this.opmode = opmode;
//        rHardware.init(opmode.hardwareMap);
//
//        // assign stuff from RobotHardware
//        hangServo1 = rHardware.hangServo1;
//        hangServo2 = rHardware.hangServo2;
//        slideMotor = rHardware.hSlideMotor; //
//
//        // initial power to 0
//        hangServo1.setPower(0);
//        hangServo2.setPower(0);
//        slideMotor.setPower(0);
//    }
//
//    // method for starting stage 1 --> only allows one-time activation (by using booleans above)
//    // both servos run by x power for x time
//    public void deploy() {
//        if (deployed == false) { // checks if the first stage has not been started/activated
//            deployed = true; // deployed boolean = true
//            stageTwoActivated = false; // makes sure that second stage is not activated
//            timer.reset();           // timer reset to track runtime
//            runServos(CR_SERVO_POWER); // run CR servos by power set above (runServos is defined in its own method (helper method))
//        }
//    }
//
//    // method for starting stage 2 --> reverses servos and runs slides
//    public void activateStageTwo() {
//        if (deployed == true && stageTwoActivated == false && (timer.seconds() >= SERVO_RUN_TIME)) { // checks if stage 1 is done (started and time exceeded runtime) and that stage 2 hasnt started yet
//            stageTwoActivated = true;  // boolean for stage 2
//            timer.reset();             // reset timer for tracking CR servo runtime
//            runServos(CR_SERVO_REVERSE_POWER); // set power in other direction --> amount is defined above, runServos is defined in its own method (helper method)
//        }
//    }
//
//    // operate() --> sequence of deployment actions
//    public void operate() {
//        if (deployed == true && stageTwoActivated == false) { // check if first stage is active but not completed (bc when completed stage2 auto activates)
//            if (timer.seconds() >= SERVO_RUN_TIME) { // stops CR servos if the time exceeds desired runtime
//                stopServos(); // (stopServos defined below (helper method))
//            }
//        }
//        else if (stageTwoActivated) { // if second stage is active then...
//            // run the servos (in the opposite direction until desired runtime)
//            if (timer.seconds() <= SERVO_RUN_TIME) {
//                runServos(CR_SERVO_REVERSE_POWER); // (runServos is defined in its own method (helper method))
//            }
//            // after servos are done reversing, run the slides
//            else if (timer.seconds() <= SERVO_RUN_TIME + SLIDE_EXTENSION_TIME) {
//                stopServos(); //(stopServos defined below (helper method))
//                slideMotor.setPower(SLIDE_POWER); // run slides for x power (defined above)
//            }
//            else if (timer.seconds() < SERVO_RUN_TIME + SLIDE_EXTENSION_TIME + SLIDE_RETRACTION_TIME) {
//                slideMotor.setPower(SLIDE_RETRACTION_POWER);
//            }
//            // after the slides have ran, stop the slide motor
//            else {
//                slideMotor.setPower(0); // Stop the slide motor
//                deployed = false; // once the entire thing is done, reset the "deployed" boolean
//            }
//        }
//    }
//
//    // setting power for both CR servos (helper method)
//    private void runServos(double power) {
//        hangServo1.setPower(power); // set power for hangServo1
//        hangServo2.setPower(power); // set power for hangServo2
//    }
//
//    // stopping both CR servos (helper method)
//    private void stopServos() {
//        hangServo1.setPower(0); // stop hangServo1 (power = 0)
//        hangServo2.setPower(0); // stop hangServo2 (power = 0)
//    }
//
//    // For TeleOp ...
//    public boolean isDeployed() {
//        return deployed;
//    }
//    public boolean isStageTwoActivated() {
//        return stageTwoActivated;
//    }
//
//
//}