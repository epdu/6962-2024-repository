//package org.firstinspires.ftc.teamcode.Subsystems;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.teamcode.Util.RobotHardware;
//
//// code for two set servo linkage extendo
//@Config
//public class LinkageHorizontalSlides
//{
//    OpMode opmode;
//
//    private final RobotHardware rHardware = new RobotHardware();
//    private Servo hSlideServoL, hSlideServoR;
//
//    // constants
//    public static double increment = 0.005;
//    public static double extendedPos = 0.5;
//    public static double retractedPos = 0;
//    public static double retractedThreshold = 0.05;
//    public static double mostlyRetractedThreshold = 0.2;
//    public static double mappingExponent = 0.4; // paste this into desmos to see graph: x^{0.4}\ \left\{0\le x\le1\right\}
//                                                // making the mapping exponent smaller makes graph steeper
//
//    // declaring variables for later modification
//    private volatile double target = 0;
//    public volatile boolean slidesRetracted = true;
//    public volatile boolean slidesMostlyRetracted = true;
//
//    public LinkageHorizontalSlides() {}
//
//    public void initialize(OpMode opmode) {
//        // TODO: assign motor names, then reverse the correct motor
//        this.opmode = opmode;
//        rHardware.init(opmode.hardwareMap);
//
//        this.hSlideServoL = rHardware.hSlideServoL;
//        this.hSlideServoR = rHardware.hSlideServoR;
//
//        hSlideServoR.setDirection(Servo.Direction.REVERSE);
//    }
//
//    public void operateVincent() {
//        // maps to percent of upper limit (ex: 1 -> 100%, 0.5 -> 80%, 0.1 -> 60%, 0 -> 0%)
//        target = mapTriggerToTarget(opmode.gamepad1.right_trigger);
//        hSlideServoR.setPosition(target);
//        hSlideServoL.setPosition(target);
//
//        // updates boolean
//        slidesRetracted = hSlideServoR.getPosition() < retractedThreshold;
//        slidesMostlyRetracted = hSlideServoR.getPosition() < mostlyRetractedThreshold;
//
//        opmode.telemetry.addData("Left Extendo Servo Pos: ", hSlideServoL.getPosition());
//        opmode.telemetry.addData("Right Extendo Servo Pos: ", hSlideServoR.getPosition());
//        opmode.telemetry.update();
//    }
//
//    public void operateTest() {
//        // maps to percent of upper limit (ex: 1 -> 100%, 0.5 -> 80%, 0.1 -> 60%, 0 -> 0%)
//        if (opmode.gamepad2.dpad_up) {
//            incremental(1);
//        } else if (opmode.gamepad2.dpad_down) {
//            incremental(-1);
//        }
//
//        // updates boolean
//        slidesRetracted = hSlideServoR.getPosition() < retractedThreshold;
//        slidesMostlyRetracted = hSlideServoR.getPosition() < mostlyRetractedThreshold;
//
//        opmode.telemetry.addData("Horizontal Slides Retracted: ", slidesRetracted);
//        opmode.telemetry.addData("Horizontal Slides Mostly Retracted: ", slidesMostlyRetracted);
//        opmode.telemetry.addData("Left Extendo Servo Pos: ", hSlideServoL.getPosition());
//        opmode.telemetry.addData("Right Extendo Servo Pos: ", hSlideServoR.getPosition());
//        opmode.telemetry.update();
//    }
//
//    public void incremental(int sign) {
//        hSlideServoR.setPosition(hSlideServoR.getPosition() + sign * increment);
//        hSlideServoL.setPosition(hSlideServoR.getPosition() + sign * increment);
//    }
//
//    public void setTarget(double targetPos) {
//        target = targetPos;
//    }
//    public void moveToPosition(double pos) {
//        hSlideServoR.setPosition(pos);
//        hSlideServoL.setPosition(pos);
//    }
//
//    public void extend() { moveToPosition(extendedPos); }
//    public void retract() { moveToPosition(retractedPos); }
//
//    public void extendAdjustable(double trigger_input) {
//        moveToPosition(mapTriggerToTarget(trigger_input));
//    }
//
//    // math util
//    public double mapTriggerToTarget(double input) {
//        return Math.pow(input, mappingExponent) * extendedPos;
//    }
//    public int getSign(double input) {
//        return input > 0 ? 1 : input == 0 ? 0 : -1;
//    }
//
//}
