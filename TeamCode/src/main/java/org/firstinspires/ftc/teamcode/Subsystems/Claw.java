//package org.firstinspires.ftc.teamcode.Subsystems;
//
//import com.qualcomm.robotcore.hardware.ColorRangeSensor;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.robot.Robot;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.ftc.Actions;
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import androidx.annotation.NonNull;
//
//import org.firstinspires.ftc.teamcode.Util.RobotHardware;
//
//public class Claw {
//    // hardware
//    OpMode opmode;
//
//    public RobotHardware rHardware = new RobotHardware();
//    private Servo claw, arm, wristL, wristR;
//
//    // constants
//    /** all of the constants need to be tuned*/
//    public static double clawClosedPosition = 0.245;
//    public static double clawOpenPosition = 0.5;
//
//    public static double armScoringPosition = 0.3;
//    public static double armStowPosition = 0.7;
//    public static double armTransferPosition = 0.8;
//
//    /** These should work, but only assuming the wrist was assembled with both servos' 0.5 position (out of 355°)
//     * directly correspond to the claw's actual physical middle position */
//    public static double wristRTransferPosition = 0; // claw facing down and rotated to open up and down
//    public static double wristLTransferPosition = 0.25;
//    public static double wristRStowPosition = 0.5; // claw in line with arm and prongs side to side
//    public static double wristLStowPosition = 0.5;
//    public static double wristRScorePosition = 0.75; // claw facing away from robot and prongs side to side
//    public static double wristLScorePosition = 0.75;
//
//    private double TOLERANCE = 0.001;
//
//    public static double wristIncrement = 0.02;
//    public static double armIncrement = 0.02;
//
//    //    public boolean autoClosing = true;
//    public volatile boolean isClawOpen = true;
//    public volatile boolean isArmTransferring = true;
//    public volatile boolean isWristTransferring = true; // means prongs are facing forward and backward on robot
////    private double detectionInches = 0.8;
//
//    public Claw() {}
//
//    public void initialize(OpMode opmode)
//    {
//        this.opmode = opmode;
//        rHardware.init(opmode.hardwareMap);
//        this.claw = rHardware.claw;
//        this.arm = rHardware.armServo;
//        this.wristL = rHardware.cWristServoL;
//        this.wristR = rHardware.cWristServoR;
//    }
//
//    // for when we implement RobotHardware.java
////    public void initialize(OpMode opmode, RobotHardware robotHardware)
////    {
////        this.opmode = opmode;
////        this.arm = robotHardware.armServo;
////        this.wrist = robotHardware.cWristServo;
////        this.claw = robotHardware.claw;
////    }
//
//    public void operateVincent() {
//        if (opmode.gamepad2.left_bumper) {
//            toggleClaw();
//        }
//    }
//
//    public void operateTest() {
//        if (opmode.gamepad2.left_bumper) {
//            toggleClaw();
//        }
//
//        incrementalWristL(getSign(opmode.gamepad2.left_stick_y));
//        incrementalWristR(getSign(opmode.gamepad2.right_stick_y));
//
//        incrementalWristTurn(getSign(opmode.gamepad1.left_stick_y));
//        incrementalWristRotate(getSign(opmode.gamepad1.left_stick_x));
//
//        if (opmode.gamepad2.dpad_up) {
//            incrementalArm(1);
//        }
//        else if (opmode.gamepad2.dpad_down) {
//            incrementalArm(-1);
//        }
//
//        if (opmode.gamepad2.a) {
//            scoreArm();
//            setWristScoring();
//        }
//        else if (opmode.gamepad2.b) {
//            stowArm();
//            setWristStow();
//        }
//        else if (opmode.gamepad2.x) {
//            transferArm();
//            setWristTransfer();
//        }
//
//        opmode.telemetry.addData("Arm Pos: ", arm.getPosition());
//        opmode.telemetry.addData("Right Wrist Pos: ", wristR.getPosition());
//        opmode.telemetry.addData("Left Wrist Pos: ", wristL.getPosition());
//    }
//
//    public void shutdown() {};
//
//    public void toggleClaw() {
//        if (isClawOpen) {
//            claw.setPosition(clawClosedPosition);
//            isClawOpen = false;
//        }
//        else {
//            claw.setPosition(clawOpenPosition);
//            isClawOpen = true;
//        }
//    }
//
//    public void openClaw() { claw.setPosition(clawClosedPosition); isClawOpen = false;}
//    public void closeClaw() { claw.setPosition(clawOpenPosition); isClawOpen = true;}
//
////    public void toggleArm() {
////        if (isArmTransferring) {
////            arm.setPosition(armScoringPosition);
////            isArmTransferring = false;
////        }
////        else {
////            arm.setPosition(armTransferPosition);
////            isArmTransferring = true;
////        }
////    }
//
//    public void transferArm() { arm.setPosition(armTransferPosition); isArmTransferring = true;}
//    public void stowArm() {arm.setPosition(armStowPosition); isArmTransferring = false;}
//    public void scoreArm() { arm.setPosition(armScoringPosition); isArmTransferring = false;}
//
//    public void incrementalArm(int sign)
//    {
//        arm.setPosition(arm.getPosition() + sign * armIncrement);
//    }
//
////    public void toggleWrist() {
////        if (isWristVertical) {
////            wrist.setPosition(wristVerticalPosition);
////            isWristVertical = false;
////        }
////        else {
////            wrist.setPosition(wristHorizontalPosition);
////            isWristVertical = true;
////        }
////    }
//
//    public void setWristTransfer() { wristL.setPosition(wristLTransferPosition); wristR.setPosition(wristRTransferPosition); isWristTransferring = true; }
//    public void setWristStow() { wristL.setPosition(wristLStowPosition); wristR.setPosition(wristRStowPosition); isWristTransferring = false; }
//    public void setWristScoring() { wristL.setPosition(wristLScorePosition); wristR.setPosition(wristRScorePosition); isWristTransferring = false; }
//
//    // for synchronizing wrist servos
//    public void setWristServosToMiddles() {wristR.setPosition(0.5); wristL.setPosition(0.5);}
//
//    public void incrementalWristRotate(int sign)
//    {
//        wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
//        wristL.setPosition(wristL.getPosition() + sign * wristIncrement);
//        // servos are opposite so both rotating the same way rotates the claw
//    }
//    public void incrementalWristTurn(int sign)
//    {
//        wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
//        wristL.setPosition(wristL.getPosition() - sign * wristIncrement);
//        // servos are opposite so rotating opposite ways turns the claw
//    }
//
//    public void incrementalWristL(int sign) {
//        wristL.setPosition(wristL.getPosition() + sign * wristIncrement);
//    }
//    public void incrementalWristR(int sign) {
//        wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
//    }
//
//    public double telemetryArmPos() {
//        return arm.getPosition();
//    }
//    public double telemetryWristRPos() {
//        return wristR.getPosition();
//    }
//    public double telemetryWristLPos() {
//        return wristR.getPosition();
//    }
//
//
//    public class clawOpen implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            openClaw();
//            return false;
//        }
//    }
//    public Action clawOpen() {
//        return new clawOpen();
//    }
//    public class clawClose implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            closeClaw();
//            return false;
//        }
//    }
//    public Action clawClose() {
//        return new clawClose();
//    }
//    public class clip implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            scoreArm();
//            setWristScoring();
//            return false;
//        }
//    }
//    public Action clip() {
//        return new clip();
//    }
//    public class bucket implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            scoreArm();
//            setWristScoring();
//            return false;
//        }
//    }
//    public Action bucket() {
//        return new bucket();
//    }
//    public class stow implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            stowArm();
//            setWristStow();
//            return false;
//        }
//    }
//    public Action stow() {
//        return new stow();
//    }
//
//    // math util functions
//    public boolean isClose(double a, double b) {
//        return Math.abs(a - b) < TOLERANCE;
//    }
//
//    public int getSign(double input) {
//        // if input is pos, return 1
//        // if input is 0, return 0
//        // else, return -1
//        return input > 0 ? 1: input == 0 ? 0: -1;
//    }
//}