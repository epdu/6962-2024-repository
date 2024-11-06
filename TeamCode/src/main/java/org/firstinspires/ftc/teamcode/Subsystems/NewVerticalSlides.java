package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
    public static double Kp = 0;
    public static double Ki = 0;
    public static double Kd = 0;
    public static double Kg = 0;
    public static double retractedThreshold = 10;

    public static int highBucketPos = 1300;
    //    public static int lowBucketPos = 600;
    public static int retractedPos = 0;
    public static int pickupClipPos = 0;
    public static int prepClipPos = 555;
    public static int scoreClipPos = 220;

    //declaring variables for later modification
    private volatile double target = 0;
    public volatile boolean verticalSlidesRetracted = true;

    public NewVerticalSlides() {}

    public void initialize(OpMode opmode) {
        // TODO: assign motor names, then reverse the correct motor
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        leftSlideMotor = rHardware.vLslideMotor;
        rightSlideMotor = rHardware.vRslideMotor;

        controller = new PIDController(Kp, Ki, Kd);
        leftSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        rightSlideMotor.setDirection(DcMotorEx.Direction.REVERSE);

        leftSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        leftSlideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightSlideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void operateTest() {
        if (opmode.gamepad1.a) {
            target = 700;
        } else if (opmode.gamepad1.b) {
            target = 0;
        }

        // this line is possibly redundant
        controller.setPID(Kp, Ki, Kd);

        int currentPos = rightSlideMotor.getCurrentPosition();
        double output = controller.calculate(currentPos, target) + Kg;

        leftSlideMotor.setPower(output);
        rightSlideMotor.setPower(output);

        // updates boolean
        verticalSlidesRetracted = currentPos < retractedThreshold;

        opmode.telemetry.addData("Right Motor Encoder Pos: ", currentPos);
        opmode.telemetry.addData("Target: ", target);
        opmode.telemetry.addData("PID Power R: ", output);
        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlidesRetracted);
        opmode.telemetry.update();
    }


    // for use in auto or preset button during teleop
    /** untested and not especially confident in it, so please be cautious when testing */
    public void moveToPosition(int targetPos) {
        target = targetPos;
    }
    public void raiseToHighBucket() { moveToPosition(highBucketPos); }
    public void raiseToPickupClip() { moveToPosition(pickupClipPos);}
    public void raiseToPrepClip() { moveToPosition(prepClipPos);}
    public void retract() { moveToPosition(retractedPos); }
    public void slamToScoreClip() { moveToPosition(scoreClipPos);}

//    // Action class stuff
//    public class RunToPosition implements Action {
//        private boolean initialized = false;
//        private int rtpTarget = 0;
//
//        public RunToPosition(int targetPos) {
//            rtpTarget = targetPos;
//        }
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            int error = rtpTarget - rightSlideMotor.getCurrentPosition();
//            if (!initialized) {
//                int sign = (error >= 0 ? 1 : -1);
//                leftSlideMotor.setPower(sign * 0.8);
//                rightSlideMotor.setPower(sign * 0.8);
//                initialized = true;
//            }
//
//            if (Math.abs(error) > 20) {
//                return true;
//            } else {
//                leftSlideMotor.setPower(0);
//                rightSlideMotor.setPower(0);
//                return false;
//            }
//        }
//    }
//
//    public Action LiftUpToClip() {
//        return new RunToPosition(prepClipPos);
//    }
//
//    public Action SlamScoreClip() {
//        return new RunToPosition(slamClipPos);
//    }
//
//    public Action Retract() {
//        return new RunToPosition(retractedPos);
//    }
//
//    public Action LiftUpToHighBucket() {
//        return new RunToPosition(highBucketPos);
//    }
//
//    public class RTP implements Action {
//        private boolean initialized = false;
//        private int rtpTarget = 0;
//
//        public RTP(int targetPos) {
//            rtpTarget = targetPos;
//        }
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            if (!initialized) {
//                rtpTarget = prepClipPos;
//                initialized = true;
//            }
//
//            PIDPowerR = PIDControl(rtpTarget, rightSlideMotor);
//            leftSlideMotor.setPower(PIDPowerR);
//            rightSlideMotor.setPower(PIDPowerR);
//
//            if (Math.abs(rtpTarget - rightSlideMotor.getCurrentPosition()) > 20) {
//                return true;
//            } else {
//                leftSlideMotor.setPower(0);
//                rightSlideMotor.setPower(0);
//                return false;
//            }
//        }
//    }
//
//    public Action RTP() {
//        return new RTP(200);
//    }
}

