package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class NewVerticalSlides {

    OpMode opmode;

    private final RobotHardware rHardware = new RobotHardware();
    private PIDController controller;
    private DcMotorEx leftSlideMotor, rightSlideMotor;

    // constants
    public static double Kp = 0.013;
    public static double Ki = 0;
    public static double Kd = 0.0003;
    public static double Kg = 0.1;
    public static double powerCachingThreshold = 0.005;
    public static double retractedThreshold = 10;
    public static int upperLimit = 1600; // this is for 1150s
    public static int lowerLimit = -2;

    // encoder positions
    public static int highBucketPos = 1300;
    public static int retractedPos = 0;
    public static int pickupClipPos = 0;
    public static int prepClipPos = 555;
    public static int slamClipPos = 220;

    //declaring variables for later modification
    private volatile double target = 0;
    private volatile double slidePower;
    private volatile double output = 0;
    private volatile double previousOutput = 0;
    public volatile boolean slidesRetracted = true;

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

    public void operateVincent() {
        int currentPos = rightSlideMotor.getCurrentPosition();
        output = controller.calculate(currentPos, target) + Kg;
        if (Math.abs(output) >= 1.0) {
            output = (output >= 0 ? 1 : -1);
        }

        if (isDifferent(output, previousOutput)) {
            leftSlideMotor.setPower(output);
            rightSlideMotor.setPower(output);
        }

        previousOutput = output;

        // updates boolean
        slidesRetracted = currentPos < retractedThreshold;
    }

    public void operateTuning() {
        if (opmode.gamepad2.y) {
            target = highBucketPos;
        } else if (opmode.gamepad2.a) {
            target = retractedPos;
        }

        int currentPos = rightSlideMotor.getCurrentPosition();

        slidePower = -1 * opmode.gamepad2.left_stick_y;
        if (Math.abs(slidePower) > 0.05) {
            // move freely
            leftSlideMotor.setPower(slidePower);
            rightSlideMotor.setPower(slidePower);

            // if out of range, sets target to back in range
            if (currentPos > upperLimit) {
                target = upperLimit;
            } else if (currentPos < lowerLimit) {
                target = lowerLimit;
            }
        } else {
            output = controller.calculate(currentPos, target) + Kg;

            // hehe funny syntax
            output = (Math.abs(output) > 1 ? (output > 0 ? 1 : -1): output);


            if (isDifferent(output, previousOutput)) {
                leftSlideMotor.setPower(output);
                rightSlideMotor.setPower(output);
            }

            previousOutput = output;
        }

        // updates boolean
        slidesRetracted = currentPos < retractedThreshold;
    }

    public void operateIncremental() {
        if (opmode.gamepad2.y) {
            raiseToHighBucket();
        }
        else if (opmode.gamepad2.x) {
            retract();
        }

        int currentPos = rightSlideMotor.getCurrentPosition();

        slidePower = -opmode.gamepad2.right_stick_y;

        if (Math.abs(slidePower) > 0.05) {
            // move freely
            leftSlideMotor.setPower(slidePower);
            rightSlideMotor.setPower(slidePower);

            // if out of range, sets target to back in range
            if (currentPos > upperLimit) {
                target = upperLimit;
            } else if (currentPos < lowerLimit) {
                target = lowerLimit;
            }
        } else {
            output = controller.calculate(currentPos, target) + Kg;

            leftSlideMotor.setPower(output);
            rightSlideMotor.setPower(output);
        }

        opmode.telemetry.addData("Vertical Encoder Position:", currentPos);
        opmode.telemetry.addData("Vertical Slide Target:", target);
        opmode.telemetry.addData("Vertical Slide Power:", slidePower);
        opmode.telemetry.update();
    }

    public void operateFix() {
        // manual control
        slidePower = -opmode.gamepad2.left_stick_y;

        if (Math.abs(slidePower) > 0.05)
        {
            leftSlideMotor.setPower(slidePower);
            rightSlideMotor.setPower(slidePower);
        }

        if (opmode.gamepad2.left_stick_button) {
            leftSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            rightSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        }

    }

    public void moveToPosition(int targetPos) {
        target = targetPos;
    }
    public void raiseToHighBucket() { moveToPosition(highBucketPos); }
    public void raiseToPickupClip() { moveToPosition(pickupClipPos);}
    public void raiseToPrepClip() { moveToPosition(prepClipPos);}
    public void retract() { moveToPosition(retractedPos); }
    public void slamToScoreClip() { moveToPosition(slamClipPos);}

    public double telemetryMotorPos() {
        return rightSlideMotor.getCurrentPosition();
    }
    public double telemetryTarget() {
        return target;
    }
    public double telemetryOutput() {
        return output;
    }

    private boolean isDifferent(double val1, double val2)
    {
        return Math.abs(val1 - val2) < powerCachingThreshold;
    }

    // Functioning autonomous implementation
    public class RunToPosition implements Action {
        private boolean initialized = false;
        private int rtpTarget = 0;

        public RunToPosition(int targetPos) {
            rtpTarget = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int error = rtpTarget - rightSlideMotor.getCurrentPosition();
            if (!initialized) {
                int sign = (error >= 0 ? 1 : -1);
                leftSlideMotor.setPower(sign * 0.7);
                rightSlideMotor.setPower(sign * 0.7);
                initialized = true;
            }

            if (Math.abs(error) > 25) {
                return true;
            } else {
                leftSlideMotor.setPower(0.1);
                rightSlideMotor.setPower(0.1);
                return false;
            }
        }
    }

    public Action LiftUpToClip() {
        return new RunToPosition(prepClipPos);
    }

    public Action SlamScoreClip() {
        return new RunToPosition(slamClipPos);
    }

    public Action Retract() {
        return new RunToPosition(retractedPos);
    }

    public Action LiftUpToHighBucket() {
        return new RunToPosition(highBucketPos);
    }

}

