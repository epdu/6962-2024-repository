package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

public class Claw {
    // hardware
    OpMode opmode;

    public RobotHardware rHardware = new RobotHardware();
    private Servo claw, arm, wrist;

    // constants
    /** all of the constants need to be tuned*/
    public static double clawClosedPosition = 0.245;
    public static double clawOpenPosition = 0.5;

    public static double armScoringPosition = 0.3;
    public static double armStowPosition = 0.7;
    public static double armTransferPosition = 0.8;

    public static double wristVerticalPosition = 0; // vertical mean the prongs open forward and back
    public static double wristHorizontalPosition = 0.25; // horizontal means the prongs open left and right

    private double TOLERANCE = 0.001;

    public static double wristIncrement = 0.02;
    public static double armIncrement = 0.02;

    //    public boolean autoClosing = true;
    public volatile boolean isClawOpen = true;
    public volatile boolean isArmTransferring = true;
    public volatile boolean isWristVertical = true;
//    private double detectionInches = 0.8;

    public Claw() {}

    public void initialize(OpMode opmode)
    {
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);
        this.claw = rHardware.claw;
        this.arm = rHardware.armServo;
        this.wrist = rHardware.cWristServo;
    }

    // for when we implement RobotHardware.java
//    public void initialize(OpMode opmode, RobotHardware robotHardware)
//    {
//        this.opmode = opmode;
//        this.arm = robotHardware.armServo;
//        this.wrist = robotHardware.cWristServo;
//        this.claw = robotHardware.claw;
//    }

    public void operateVincent() {
        if (opmode.gamepad2.left_bumper) {
            toggleClaw();
        }
    }

    public void operateTest() {
        if (opmode.gamepad2.left_bumper) {
            toggleClaw();
        }

        if (opmode.gamepad2.dpad_left) {
            incrementalWrist(1);
        }
        else if (opmode.gamepad2.dpad_right) {
            incrementalWrist(-1);
        }
        else if (opmode.gamepad2.left_stick_button) {
            setWristHorizontal();
        }
        else if (opmode.gamepad2.right_stick_button) {
            setWristVertical();
        }

        if (opmode.gamepad2.dpad_up) {
            incrementalArm(1);
        }
        else if (opmode.gamepad2.dpad_down) {
            incrementalArm(-1);
        }
        else if (opmode.gamepad2.a) {
            scoreArm();
        }
        else if (opmode.gamepad2.b) {
            stowArm();
        }
        else if (opmode.gamepad2.x) {
            transferArm();
        }



        opmode.telemetry.addData("Arm Pos: ", arm.getPosition());
        opmode.telemetry.addData("Wrist Pos: ", wrist.getPosition());
    }

    public void shutdown() {};

    public void toggleClaw() {
        if (isClawOpen) {
            claw.setPosition(clawClosedPosition);
            isClawOpen = false;
        }
        else {
            claw.setPosition(clawOpenPosition);
            isClawOpen = true;
        }
    }

    public void openClaw() { claw.setPosition(clawClosedPosition); isClawOpen = false;}
    public void closeClaw() { claw.setPosition(clawOpenPosition); isClawOpen = true;}

//    public void toggleArm() {
//        if (isArmTransferring) {
//            arm.setPosition(armScoringPosition);
//            isArmTransferring = false;
//        }
//        else {
//            arm.setPosition(armTransferPosition);
//            isArmTransferring = true;
//        }
//    }

    public void scoreArm() { arm.setPosition(armScoringPosition); isArmTransferring = false;}
    public void transferArm() { arm.setPosition(armTransferPosition); isArmTransferring = true;}
    public void stowArm() {arm.setPosition(armStowPosition); isArmTransferring = false;}

    public void incrementalArm(int sign)
    {
        arm.setPosition(arm.getPosition() + sign * armIncrement);
    }

    public void toggleWrist() {
        if (isWristVertical) {
            wrist.setPosition(wristVerticalPosition);
            isWristVertical = false;
        }
        else {
            wrist.setPosition(wristHorizontalPosition);
            isWristVertical = true;
        }
    }

    public void setWristHorizontal() { wrist.setPosition(wristHorizontalPosition); isWristVertical = true; }
    public void setWristVertical() { wrist.setPosition(wristVerticalPosition); isWristVertical = false; }

    public void incrementalWrist(int sign)
    {
        wrist.setPosition(wrist.getPosition() + sign * wristIncrement);
    }

    public boolean isClose(double a, double b) {
        return Math.abs(a - b) < TOLERANCE;
    }

    public double telemetryArmPos() {
        return arm.getPosition();
    }
    public double telemetryWristPos() {
        return wrist.getPosition();
    }

    public class clawOpen implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            openClaw();
            return false;
        }
    }
    public Action clawOpen() {
        return new clawOpen();
    }
    public class clawClose implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            closeClaw();
            return false;
        }
    }
    public Action clawClose() {
        return new clawClose();
    }
}