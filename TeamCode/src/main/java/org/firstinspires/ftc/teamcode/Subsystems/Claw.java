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
    private Servo claw, arm;

    // constants
    /** all of the constants need to be tuned*/
    private double clawClosedPosition = 0.245;
    private double clawOpenPosition = 0.5;

    private double armScoringPosition = 0.3;
    private double armTransferPosition = 0.8;

    private double TOLERANCE = 0.001;

    public boolean autoClosing = true;
    public boolean isOpen = true;
    public boolean isTransferring = true;
    private double detectionInches = 0.8;

    public Claw() {}

    public void initialize(OpMode opmode)
    {
        this.opmode = opmode;
        this.claw = opmode.hardwareMap.get(Servo.class, "");
        this.arm = opmode.hardwareMap.get(Servo.class, "");
    }

    public void operate() {
        if (opmode.gamepad1.a) {
            toggleClaw();
        }
    }

    public void shutdown() {};

    public void toggleClaw() {
        if (isOpen) {
            claw.setPosition(clawClosedPosition);
            isOpen = false;
        }
        else {
            claw.setPosition(clawOpenPosition);
            isOpen = true;
        }
    }

    public void openClaw() { claw.setPosition(clawClosedPosition); isOpen = false;}
    public void closeClaw() { claw.setPosition(clawOpenPosition); isOpen = true;}

    public void toggleArm() {
        if (isTransferring) {
            arm.setPosition(armScoringPosition);
            isTransferring = false;
        }
        else {
            arm.setPosition(armTransferPosition);
            isTransferring = true;
        }
    }

    public void scoreArm() { arm.setPosition(armScoringPosition); isTransferring = false;}
    public void stowArm() { arm.setPosition(armTransferPosition); isTransferring = true;}


    public boolean isClose(double a, double b) {
        return Math.abs(a - b) < TOLERANCE;
    }
}
