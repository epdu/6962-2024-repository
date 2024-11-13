package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

public class Hang {
    private final RobotHardware rHardware = new RobotHardware();
    private CRServo hangServoL, hangServoR;
    private DcMotorEx slideMotor;
    private OpMode opmode;
    private NavxManager navxManager; // New instance for NavxManager

    private static final double CR_SERVO_POWER = 1.0;
    private static final double CR_SERVO_REVERSE_POWER = -1.0;
    private static final double SLIDE_POWER = 1.0;
    private static final double SLIDE_RETRACTION_POWER = -1.0;
    private static final double SERVO_RUN_TIME = 2.0; // adjust as necessary
    private static final double SLIDE_EXTENSION_TIME = 1.5; // adjust as necessary
    private static final double SLIDE_RETRACTION_TIME = 1.5; // adjust as necessary
    private static final double TARGET_PITCH = 30.0; // Desired pitch angle (adjust as necessary)

    private boolean deployed = false; // this is for starting the hang (stage 1 hang)
    private boolean stageTwoActivated = false; // this is for the stage 2 part of the hang

    public Hang() {}
    public void initialize(OpMode opmode) {
        rHardware.init(opmode.hardwareMap);
        this.opmode = opmode;
        this.hangServoL = rHardware.hangServoL;
        this.hangServoR = rHardware.hangServoR;
        this.slideMotor = rHardware.hSlideMotor;
        this.navxManager = new NavxManager(rHardware.navx); // Initialize NavxManager

        hangServoR.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void operateTest() {
        hangServoL.setPower(-opmode.gamepad2.left_stick_y);
        hangServoR.setPower(-opmode.gamepad2.left_stick_y);
    }

    public Action getHangSequence() {
        this.deployed = true;
        return new SequentialAction(
                new InstantAction(() -> runServos(CR_SERVO_POWER)),
                new SleepAction(SERVO_RUN_TIME),
                new InstantAction(this::stopServos)
        );
    }

    public Action getHangSequenceTwo() {
        this.stageTwoActivated = true;
        return new ParallelAction(
                // First action: Monitor pitch and stop servos when target pitch is reached
                new SequentialAction(
                        new InstantAction(() -> runServos(CR_SERVO_REVERSE_POWER)),
                        new Action() {
                            @Override
                            public boolean run(TelemetryPacket packet) {
                                // Continuously check if the target pitch is reached
                                if (navxManager.getPitch() <= TARGET_PITCH) {
                                    stopServos(); // Stop servos when the target pitch is achieved
                                    return true; // Action complete
                                }
                                return false; // Continue running
                            }
                        }
                ),

                // Second action: Extend and retract slides
                new SequentialAction(
                        new InstantAction(() -> slideMotor.setPower(SLIDE_POWER)),
                        new SleepAction(SLIDE_EXTENSION_TIME),

                        // Retract slides
                        new InstantAction(() -> slideMotor.setPower(SLIDE_RETRACTION_POWER)),
                        new SleepAction(SLIDE_RETRACTION_TIME),
                        new InstantAction(() -> slideMotor.setPower(0))
                )
        );
    }

    public Action reverseHangSequence() {
        this.deployed = false;
        return new SequentialAction(
                // step 1: Deploy (run servos forward)
                new InstantAction(() -> reverseServos(CR_SERVO_POWER)),
                new SleepAction(SERVO_RUN_TIME),
                new InstantAction(this::stopServos)
        );
    }
    // Helper methods to control servos and slide motor
    private void runServos(double power) {
        hangServoR.setPower(power);
        hangServoL.setPower(power);
    }

    private void stopServos() {
        hangServoR.setPower(0);
        hangServoL.setPower(0);
    }

    private void reverseServos(double power) {
        hangServoR.setPower(-power);
        hangServoL.setPower(-power);
    }
    public boolean isDeployed() {
        return deployed;
    }
    public boolean isStageTwoActivated() {
        return stageTwoActivated;
    }
}
