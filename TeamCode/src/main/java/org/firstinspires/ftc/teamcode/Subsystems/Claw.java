package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Claw {
    // hardware
    OpMode opmode;
    private Servo claw, arm, wrist;

    // constants
    /** all of the constants need to be tuned*/
    private double clawClosedPosition = 0.245;
    private double clawOpenPosition = 0.5;

    private double armScoringPosition = 0.3;
    private double armTransferPosition = 0.8;

    private double wristVerticalPosition = 0; // vertical mean the prongs open forward and back
    private double wristHorizontalPosition = 0.25; // horizontal means the prongs open left and right

    private double TOLERANCE = 0.001;

    private double wristIncrement = 0.02;
    private double armIncrement = 0.02;

    public boolean autoClosing = true;
    public volatile boolean isClawOpen = true;
    public volatile boolean isArmTransferring = true;
    public volatile boolean isWristVertical = true;
    private double detectionInches = 0.8;

    public Claw() {}

    public void initialize(OpMode opmode)
    {
        this.opmode = opmode;
        this.claw = opmode.hardwareMap.get(Servo.class, "");
        this.arm = opmode.hardwareMap.get(Servo.class, "");
        this.wrist = opmode.hardwareMap.get(Servo.class, "");
    }

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

        if (opmode.gamepad2.dpad_up) {
            incrementalArm(1);
        }
        else if (opmode.gamepad2.dpad_down) {
            incrementalArm(-1);
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

    public void toggleArm() {
        if (isArmTransferring) {
            arm.setPosition(armScoringPosition);
            isArmTransferring = false;
        }
        else {
            arm.setPosition(armTransferPosition);
            isArmTransferring = true;
        }
    }

    public void scoreArm() { arm.setPosition(armScoringPosition); isArmTransferring = false;}
    public void stowArm() { arm.setPosition(armTransferPosition); isArmTransferring = true;}

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
}
