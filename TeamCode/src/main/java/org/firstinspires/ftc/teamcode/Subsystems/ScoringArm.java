package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class ScoringArm {

    // Main ScoringArm Class that creates instances of the subsystems
    OpMode opmode;
    public Claw claw = new Claw();
    public Arm arm = new Arm();
    public Wrist wrist = new Wrist();
    private static CustomTimer timer = new CustomTimer();
    private final RobotHardware rHardware = new RobotHardware();

    // constructor
    public ScoringArm() {}

    // Initializes the sub-subsystems
    public void initialize(OpMode opmode, CustomTimer timer) {
        this.opmode = opmode;
        this.timer = timer;
        rHardware.init(opmode.hardwareMap);

        claw.initialize(rHardware);
        arm.initialize(rHardware);
        wrist.initialize(rHardware);
    }

    // Operates the claw for Vincent configuration
    public void operateVincent() {
        if (opmode.gamepad1.left_bumper) {
            claw.toggleClaw();
        }

        // Adding telemetry data for debugging
        opmode.telemetry.addData("Arm Pos: ", arm.arm.getPosition());
        opmode.telemetry.addData("Wrist Transferring Bool", wrist.isWristTransferring);
        opmode.telemetry.addData("Arm Transferring Bool: ", arm.isArmTransferring);
        opmode.telemetry.addData("Right Wrist Pos: ", wrist.wristR.getPosition());
        opmode.telemetry.addData("Left Wrist Pos: ", wrist.wristL.getPosition());
        opmode.telemetry.addData("Claw Pos: ", claw.claw.getPosition());
        opmode.telemetry.addData("Claw Pos: ", claw.isClawOpen);
    }

    // Operates the test mode for controlling the claw, arm, and wrist
    public void operateTest() {
        // gamepad 2, all incrementals to find servo values first try
        if (opmode.gamepad2.left_bumper) {
            claw.incremental(-1);
        }
        else if (opmode.gamepad2.right_bumper) {
            claw.incremental(1);
        }

        wrist.incrementalWristTurn(getSign(opmode.gamepad2.left_stick_y));
        wrist.incrementalWristRotate(getSign(opmode.gamepad2.left_stick_x));

        if (opmode.gamepad2.dpad_up) {
            arm.incrementalArm(-1);
        } else if (opmode.gamepad2.dpad_down) {
            arm.incrementalArm(1);
        }

        if (opmode.gamepad2.y) {wrist.setWristStow();}
        if (opmode.gamepad2.x) {wrist.setWristTransfer();}

        // gamepad 1
        if (opmode.gamepad1.y) {
//            arm.scoreArm();
            wrist.setWristScoringBucket();
        }
        else if (opmode.gamepad1.a) {
//            arm.scoreArm();
            wrist.setWristScoringClip();
        }
        else if (opmode.gamepad1.b) {
//            arm.stowArm();
            wrist.setWristStow();
        }
        else if (opmode.gamepad1.x) {
//            wholeArmTransfer();
            wrist.setWristTransfer();
        }
        else if (opmode.gamepad1.dpad_down) {
            wrist.setWristGrabClip();
        }

        if (opmode.gamepad1.right_bumper) {
            claw.toggleClaw();
        }

        // Adding telemetry data for debugging
        opmode.telemetry.addData("Arm Pos: ", arm.arm.getPosition());
        opmode.telemetry.addData("Right Wrist Pos: ", wrist.wristR.getPosition());
        opmode.telemetry.addData("Left Wrist Pos: ", wrist.wristL.getPosition());
        opmode.telemetry.addData("Claw Pos: ", claw.claw.getPosition());
    }

    // Claw Subsystem Class
    public static class Claw {
        public Servo claw;
        public boolean isClawOpen = true;
        public static double clawClosedPosition = 0.7389;
        public static double clawOpenPosition = 0.5011;
        public static double clawIncrement = 0.001;

        public Claw() {}

        public void initialize(RobotHardware rHardware) {
            this.claw = rHardware.claw;
        }

        // Toggles the claw between open and closed positions
        public void toggleClaw() {
            if (isClawOpen) {
                claw.setPosition(clawClosedPosition);
                isClawOpen = false;
            } else {
                claw.setPosition(clawOpenPosition);
                isClawOpen = true;
            }
        }

        public void closeClaw() {
            claw.setPosition(clawClosedPosition);
            isClawOpen = false;
        }

        public void openClaw() {
            claw.setPosition(clawOpenPosition);
            isClawOpen = true;
        }

        public void incremental(int sign) {
            claw.setPosition(claw.getPosition() + sign * clawIncrement);
        }
    }

    // Arm Subsystem Class
    public static class Arm {
        public Servo arm;
        public boolean isArmTransferring = true;
        public static double armScoringPosition = 0.46;
        public static double armScoringClipPosition = 0.43;
        public static double armStowPosition = 0.826;
        public static double armTransferPosition = 0.9911;
        public static double armGrabClipPosition = 0.04;
        public static double armIncrement = 0.001;

        public Arm() {}
        public void initialize(RobotHardware hardware) {
            this.arm = hardware.armServo;
        }

        public void setArmPosition(double position) {
            arm.setPosition(position);
        }

        // Incremental arm movement function
        public void incrementalArm(int sign) {
            arm.setPosition(arm.getPosition() + sign * armIncrement);
        }

        public void scoreArm() {
            setArmPosition(armScoringPosition);
            isArmTransferring = false;
        }

        public void scoreClipArm() {
            setArmPosition(armScoringClipPosition);
            isArmTransferring = false;
        }

        public void grabClipArm() {
            setArmPosition(armGrabClipPosition);
            isArmTransferring = true;
        }

        public void stowArm() {
            setArmPosition(armStowPosition);
            isArmTransferring = true;
        }

        public void transferArm() {
            setArmPosition(armTransferPosition);
            isArmTransferring = true;
        }

        public double telemetryArmPos() {
            return arm.getPosition();
        }
    }

    // Wrist Subsystem Class
    public static class Wrist {
        public Servo wristL, wristR;
        public boolean isWristTransferring = true;
        public static double wristLTransferPosition = 0;
        public static double wristRTransferPosition = 0;
        public static double wristLStowPosition = 0;
        public static double wristRStowPosition = 0;
        public static double wristLScoreBucketPosition = 0.2156;
        public static double wristRScoreBucketPosition = 0.89;
        public static double wristLScoreClipPosition = 0.645;
        public static double wristRScoreClipPosition = 0.645;
        public static double wristLGrabClipPosition = 0.1794;
        public static double wristRGrabClipPosition = 0.1794;
        public static double wristIncrement = 0.001;

        public Wrist() {}
        public void initialize(RobotHardware hardware) {
            this.wristL = hardware.cWristServoL;
            this.wristR = hardware.cWristServoR;
            wristR.setDirection(Servo.Direction.REVERSE);
        }

        // Sets wrist to transfer position
        public void setWristTransfer() {
            wristL.setPosition(wristLTransferPosition);
            wristR.setPosition(wristRTransferPosition);
            isWristTransferring = true;
        }

        // Sets wrist to stow position
        public void setWristStow() {
            wristL.setPosition(wristLStowPosition);
            wristR.setPosition(wristRStowPosition);
            isWristTransferring = false;
        }

        // Sets wrist to scoring position
        public void setWristScoringBucket() {
            wristL.setPosition(wristLScoreBucketPosition);
            wristR.setPosition(wristRScoreBucketPosition);
            isWristTransferring = false;
        }

        public void setWristScoringClip() {
            wristL.setPosition(wristLScoreClipPosition);
            wristR.setPosition(wristRScoreClipPosition);
            isWristTransferring = false;
        }

        public void setWristGrabClip() {
            wristL.setPosition(wristLGrabClipPosition);
            wristR.setPosition(wristRGrabClipPosition);
            isWristTransferring = false;
        }



        // Incremental wrist rotation (both servos rotate in same direction)
        public void incrementalWristRotate(int sign) {
            wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
            wristL.setPosition(wristL.getPosition() - sign * wristIncrement);
        }

        // Incremental wrist turn (servos rotate in opposite directions)
        public void incrementalWristTurn(int sign) {
            wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
            wristL.setPosition(wristL.getPosition() + sign * wristIncrement);
        }

        public void incrementalWristL(int sign) {
            wristL.setPosition(wristL.getPosition() + sign * wristIncrement);
        }

        public void incrementalWristR(int sign) {
            wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
        }

        public double telemetryWristRPos() {
            return wristR.getPosition();
        }
        public double telemetryWristLPos() {
            return wristR.getPosition();
        }
    }

    // whole arm transfer macro
    public void wholeArmTransfer() {
        claw.openClaw();
        timer.safeDelay(100);
        wrist.setWristTransfer();
        timer.safeDelay(200);
        arm.transferArm();
    }

//    public class clawOpen implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            claw.openClaw();
//            return false;
//        }
//    }
//    public Action clawOpen() {
//        return new clawOpen();
//    }
//    public class clawClose implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            claw.closeClaw();
//            return false;
//        }
//    }
//    public Action clawClose() {
//        return new clawClose();
//    }
    public class clip implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            arm.scoreClipArm();
            wrist.setWristScoringClip();
            return false;
        }
    }
    public Action clip() {
        return new clip();
    }
//    public class bucket implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            arm.scoreArm();
//            wrist.setWristScoring();
//            return false;
//        }
//    }
//    public Action bucket() {
//        return new bucket();
//    }
//    public class stow implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            arm.stowArm();
//            wrist.setWristStow();
//            return false;
//        }
//    }
//    public Action stow() {
//        return new stow();
//    }
//
//    public class wristSetHorizontal implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            wrist.setWristScoring();
//            return false;
//        }
//    }
//    public Action wristSetHorizontal() {
//        return new wristSetHorizontal();
//    }

    // math util functions
    public boolean isClose(double a, double b) {
        return Math.abs(a - b) < 0.001;
    }
    public int getSign(double input) {
        return input > 0 ? 1 : input == 0 ? 0 : -1;
    }

}
