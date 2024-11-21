package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class NewVerticalSlides {

    OpMode opmode;

    private final RobotHardware rHardware = new RobotHardware();
    private PIDController controller;
    private DcMotorEx leftSlideMotor, rightSlideMotor;

    // constants
    /** all constants need to be tuned*/
    public static double joystickScalar = 1;
    public static double slideScalar = 1;
    public static double Kp = 0.013;
    public static double Ki = 0;
    public static double Kd = 0.0003;
    public static double Kg = 0.1;
    public static double retractedThreshold = 10;
    public static int upperLimit = 1600; // this is for 1150s
    public static int lowerLimit = -2;

    public static int highBucketPos = 1300;
    public static int retractedPos = 0;
    public static int pickupClipPos = 0;
    public static int prepClipPos = 555;
    public static int scoreClipPos = 220;

    //declaring variables for later modification
    private volatile double target = 0;
    private volatile double slidePower;
    private volatile double output = 0;

    public NewVerticalSlides() {}

    public void initialize(OpMode opmode) {
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        leftSlideMotor = rHardware.vLslideMotor;
        rightSlideMotor = rHardware.vRslideMotor;

        controller = new PIDController(Kp, Ki, Kd);
        leftSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

//        leftSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightSlideMotor.setDirection(DcMotorEx.Direction.REVERSE);

        leftSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        leftSlideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightSlideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void autonomousInitialize(OpMode opmode) {
        // TODO: assign motor names, then reverse the correct motor
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        leftSlideMotor = rHardware.vLslideMotor;
        rightSlideMotor = rHardware.vRslideMotor;

        controller = new PIDController(Kp, Ki, Kd);
        leftSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

//        leftSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightSlideMotor.setDirection(DcMotorEx.Direction.REVERSE);

        leftSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        leftSlideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        rightSlideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
    }

    public void operateVincent() {
        int currentPos = rightSlideMotor.getCurrentPosition();
        output = controller.calculate(currentPos, target) + Kg;

        leftSlideMotor.setPower(output);
        rightSlideMotor.setPower(output);
    }

    public void operateTest() {
        if (opmode.gamepad2.y) {
            target = highBucketPos;
        } else if (opmode.gamepad2.a) {
            target = retractedPos;
        }

        slidePower = -1 * opmode.gamepad2.right_stick_y;
        if (Math.abs(slidePower) > 0.05) {
            // if position positive, then can move freely
            if (rightSlideMotor.getCurrentPosition() > lowerLimit) {
                leftSlideMotor.setPower(slidePower * slideScalar);
                rightSlideMotor.setPower(slidePower * slideScalar);
                target = rightSlideMotor.getCurrentPosition();
            }
            // if position negative, but want to move positive, then can move
            else if (leftSlideMotor.getCurrentPosition() <= lowerLimit && slidePower > 0) {
                leftSlideMotor.setPower(slidePower * slideScalar);
                rightSlideMotor.setPower(slidePower * slideScalar);
                target = rightSlideMotor.getCurrentPosition();
            }

            // if out of range, sets target to back in range
            if (rightSlideMotor.getCurrentPosition() > upperLimit) {
                target = upperLimit;
            } else if (rightSlideMotor.getCurrentPosition() < lowerLimit) {
                target = lowerLimit;
            }

        } else {
            int currentPos = rightSlideMotor.getCurrentPosition();
            output = controller.calculate(currentPos, target) + Kg;

            leftSlideMotor.setPower(output);
            rightSlideMotor.setPower(output);
        }
    }

    public void moveToPosition(int targetPos) {
        target = targetPos;
    }
    public void raiseToHighBucket() { moveToPosition(highBucketPos); }
    public void raiseToPickupClip() { moveToPosition(pickupClipPos);}
    public void raiseToPrepClip() { moveToPosition(prepClipPos);}
    public void retract() { moveToPosition(retractedPos); }
    public void slamToScoreClip() { moveToPosition(scoreClipPos);}

    public double telemetryMotorPos() {
        return rightSlideMotor.getCurrentPosition();
    }

    public double telemetryTarget() {
        return target;
    }

    public double telemetryOutput() {
        return output;
    }

}

