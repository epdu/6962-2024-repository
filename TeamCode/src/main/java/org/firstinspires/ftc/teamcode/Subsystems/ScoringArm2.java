//package org.firstinspires.ftc.teamcode.Subsystems;
//
//import androidx.annotation.NonNull;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.SleepAction;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.teamcode.Util.RobotHardware;
//
//@Config
//public class ScoringArm {
/// NEW SCORING ARM WITH 2 DOF AND CLAW
/// NEW SCORING ARM WITH 2 DOF AND CLAW
/// NEW SCORING ARM WITH 2 DOF AND CLAW
/// NEW SCORING ARM WITH 2 DOF AND CLAW
/// PASTE INTO OLD FILE ONCE NEW ARM READY TO TEST
/// PASTE INTO OLD FILE ONCE NEW ARM READY TO TEST
/// PASTE INTO OLD FILE ONCE NEW ARM READY TO TEST
/// PASTE INTO OLD FILE ONCE NEW ARM READY TO TEST
//
//    // Main ScoringArm Class that creates instances of the subsystems
//    OpMode opmode;
//    public Claw claw = new Claw();
//    public Arm arm = new Arm();
//    public Wrist wrist = new Wrist();
//    private final RobotHardware rHardware = new RobotHardware();
//
//    // constructor
//    public ScoringArm() {}
//
//    // Initializes the sub-subsystems
//    public void initialize(OpMode opmode) {
//        this.opmode = opmode;
//        rHardware.init(opmode.hardwareMap);
//
//        claw.initialize(rHardware);
//        arm.initialize(rHardware);
//        wrist.initialize(rHardware);
//    }
//
//    public void operateVincent() {
//        // nothing, unused during teleop
//    }
//
//    public void operateTest() {
//        // gamepad 2, all incrementals to find servo values first try
//        if (opmode.gamepad2.left_bumper) {
//            claw.incremental(-1);
//        } else if (opmode.gamepad2.right_bumper) {
//            claw.incremental(1);
//        }
//
//        wrist.incremental(getSign(-opmode.gamepad2.left_stick_y));
//
//        if (opmode.gamepad2.dpad_up) {
//            arm.incrementalArm(-1);
//        } else if (opmode.gamepad2.dpad_down) {
//            arm.incrementalArm(1);
//        }
//
//        if (opmode.gamepad2.y) {wrist.setWristTransfer();}
//
//        // gamepad 1
//        if (opmode.gamepad1.right_bumper) {
//            claw.toggleClaw();
//        }
//
//        // Adding telemetry data for debugging
//        opmode.telemetry.addData("Arm Pos: ", arm.arm.getPosition());
//        opmode.telemetry.addData("Right Wrist Pos: ", wrist.wrist.getPosition());
//        opmode.telemetry.addData("Claw Pos: ", claw.claw.getPosition());
//    }
//
//    // Claw Subsystem Class
//    public static class Claw {
//        public Servo claw;
//        public boolean isClawOpen = true;
//        public static double clawClosedPosition = 0.7389;
//        public static double clawOpenPosition = 0.5011;
//        public static double clawIncrement = 0.001;
//
//        public Claw() {}
//
//        public void initialize(RobotHardware rHardware) {
//            this.claw = rHardware.cClawServo;
//        }
//
//        // Toggles the claw between open and closed positions
//        public void toggleClaw() {
//            if (isClawOpen) {
//                claw.setPosition(clawClosedPosition);
//                isClawOpen = false;
//            } else {
//                claw.setPosition(clawOpenPosition);
//                isClawOpen = true;
//            }
//        }
//
//        public void closeClaw() {
//            claw.setPosition(clawClosedPosition);
//            isClawOpen = false;
//        }
//
//        public void openClaw() {
//            claw.setPosition(clawOpenPosition);
//            isClawOpen = true;
//        }
//
//        public void incremental(int sign) {
//            claw.setPosition(claw.getPosition() + sign * clawIncrement);
//        }
//
//        public double telemetryClawPos() {
//            return claw.getPosition();
//        }
//    }
//
//    // Arm Subsystem Class
//    public static class Arm {
//        public Servo arm;
//        public enum STATE {
//            TRANSFERRING,
//            SCORING,
//            GRABBING_CLIP_WALL,
//            GRABBING_CLIP_FLOOR,
//            GRABBING_CLIP_FLOOR_HOVER
//        }
//        public Arm.STATE armPos = STATE.TRANSFERRING;
//        public static double armScoringPosition = 0.46;
//        public static double armScoringClipPosition = 0.43;
//        public static double armTransferPosition = 0.7622;
//        public static double armGrabClipWallPosition = 0;
//        public static double armGrabClipFloorPosition = 0;
//        public static double armGrabClipFloorHoverPosition = 0;
//        public static double armInitPosition = 0.4956;
//        public static double armStowPosition = 0.6183;
//        public static double armIncrement = 0.001;
//
//        public Arm() {}
//        public void initialize(RobotHardware hardware) {
//            this.arm = hardware.cArmServo;
//            arm.setDirection(Servo.Direction.REVERSE);
//        }
//
//        public void setArmPosition(double position) {
//            arm.setPosition(position);
//        }
//
//        // Incremental arm movement function
//        public void incrementalArm(int sign) {
//            arm.setPosition(arm.getPosition() + sign * armIncrement);
//        }
//
//        public void setArmScoreBucket() {
//            setArmPosition(armScoringPosition);
//            armPos = STATE.SCORING;
//        }
//
//        public void setArmScoreClip() {
//            setArmPosition(armScoringClipPosition);
//            armPos = STATE.SCORING;
//        }
//
//        public void setArmGrabClipWall() {
//            setArmPosition(armGrabClipWallPosition);
//            armPos = STATE.GRABBING_CLIP_WALL;
//        }
//
//        public void setArmGrabClipFloor() {
//            setArmPosition(armGrabClipFloorPosition);
//            armPos = STATE.GRABBING_CLIP_FLOOR;
//        }
//
//        public void setArmGrabClipFloorHover() {
//            setArmPosition(armGrabClipFloorHoverPosition);
//            armPos = STATE.GRABBING_CLIP_FLOOR_HOVER;
//        }
//
//        public void setArmTransfer() {
//            setArmPosition(armTransferPosition);
//            armPos = STATE.TRANSFERRING;
//        }
//
//        public void setArmInitPosition() {
//            setArmPosition(armInitPosition);
//        }
//
//        public void setArmStow() {
//            setArmPosition(armStowPosition);
//        }
//
//        public double telemetryArmPos() {
//            return arm.getPosition();
//        }
//    }
//
//    // Wrist Subsystem Class
//    public static class Wrist {
//        public Servo wrist;
//        public boolean isWristTransferring = true;
//        public static double wristTransferPosition = 0.0844;
//        public static double wristScoreBucketPosition = 0.2156;
//        public static double wristScoreClipPosition = 0.645;
//        public static double wristGrabClipWallPosition = 0.1794;
//        public static double wristGrabClipFloorPosition = 0;
//        public static double wristGrabClipFloorHoverPosition = 0;
//        public static double wristIncrement = 0.001;
//
//        public Wrist() {}
//        public void initialize(RobotHardware hardware) {
//            this.wrist = hardware.cWristServoL;
////            wrist.setDirection(Servo.Direction.REVERSE);
//        }
//
//        // Sets wrist to transfer position
//        public void setWristTransfer() {
//            wrist.setPosition(wristTransferPosition);
//            isWristTransferring = true;
//        }
//
//        // Sets wrist to scoring position
//        public void setWristScoringBucket() {
//            wrist.setPosition(wristScoreBucketPosition);
//            isWristTransferring = false;
//        }
//
//        public void setWristScoringClip() {
//            wrist.setPosition(wristScoreClipPosition);
//            isWristTransferring = false;
//        }
//
//        public void setWristGrabClipWall() {
//            wrist.setPosition(wristGrabClipWallPosition);
//            isWristTransferring = false;
//        }
//
//        public void setWristGrabClipFloor() {
//            wrist.setPosition(wristGrabClipFloorPosition);
//            isWristTransferring = false;
//        }
//
//        public void setWristGrabClipFloorHover() {
//            wrist.setPosition(wristGrabClipFloorHoverPosition);
//            isWristTransferring = false;
//        }
//
//        // Incremental wrist turn (servos rotate in opposite directions)
//        public void incremental(int sign) {
//            wrist.setPosition(wrist.getPosition() + sign * wristIncrement);
//        }
//
//        public double telemetryWristPos() {
//            return wrist.getPosition();
//}
//        }
//
//    // Action classes and functions for auto
//    public class DropBucket implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            claw.openClaw();
//            return false;
//        }
//    }
//    public Action DropBucket(){return new DropBucket();}
//
//    public class StowWholeArm implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            arm.setArmTransfer();
//            wrist.setWristTransfer();
//            claw.openClaw();
//            return false;
//        }
//    }
//    public Action StowWholeArm() {
//        return new StowWholeArm();
//    }
//
//    public class WholeArmTransfer implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            arm.setArmTransfer();
//            wrist.setWristTransfer();
//            new SleepAction(0.2);
//            claw.closeClaw();
//            return false;
//        }
//    }
//    public Action WholeArmTransfer() {
//        return new WholeArmTransfer();
//    }
//
//    public class ArmScoreClip implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            claw.closeClaw();
//            arm.setArmScoreClip();
//            wrist.setWristScoringClip();
//            return false;
//        }
//    }
//    public Action ArmScoreClip() {
//        return new ArmScoreClip();
//    }
//
//    public class ArmScoreBucket implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            arm.setArmScoreBucket();
//            wrist.setWristScoringBucket();
//            return false;
//        }
//    }
//    public Action ArmScoreBucket() {
//        return new ArmScoreBucket();
//    }
//
//    public class ArmGrabClip implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            arm.setArmGrabClipWall();
//            wrist.setWristGrabClip();
//            claw.openClaw();
//            return false;
//        }
//    }
//    public Action ArmGrabClip() {
//        return new ArmGrabClip();
//    }
//
//    public class ArmInitPosition implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            arm.setArmInitPosition();
//            return false;
//        }
//    }
//    public Action ArmInitPosition() {return new ArmInitPosition();}
//
//    public class ArmStow implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            wrist.setWristTransfer();
//            arm.setArmStow();
//            return false;
//        }
//    }
//    public Action ArmStow() {return new ArmStow();}
//
//    // math util functions
//    public boolean isClose(double a, double b) {
//        return Math.abs(a - b) < 0.001;
//    }
//    public int getSign(double input) {
//        return input > 0 ? 1 : input == 0 ? 0 : -1;
//    }
//}
