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
public class AutoOnlySlides {

    OpMode opmode;

    private final RobotHardware rHardware = new RobotHardware();
    private PIDController controller;
    private DcMotorEx leftSlideMotor, rightSlideMotor;

    // constants
    public static double Kp = 0.013;
    public static double Ki = 0;
    public static double Kd = 0.0003;
    public static double Kg = 0.1;
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
    public volatile boolean slidesRetracted = true;

    public AutoOnlySlides() {}

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


    // Autonomous PID implementation
    public class PIDUpdate implements Action {

        // THIS LOOP WILL NEVER END, lets see what happens
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int currentPos = rightSlideMotor.getCurrentPosition();
            output = controller.calculate(currentPos, target) + Kg;

            leftSlideMotor.setPower(output);
            rightSlideMotor.setPower(output);

            return true;
        }
    }

    public Action Loop() {
        return new PIDUpdate();
    }

    public class ExtendClass implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            target = 500;
            return false;
        }
    }

    public class RetractClass implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            target = 0;
            return false;
        }
    }

    public Action Extend() { return new ExtendClass(); }

    public Action Retract() { return new RetractClass(); }

    // non-constantly running impelmentation
    // this might work, or it might not
    public class PIDRunToPos implements Action {
        private boolean initialized = false;
        private int rtpTarget = 0;

        public PIDRunToPos(int targetPos) {
            rtpTarget = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            int currentPos = rightSlideMotor.getCurrentPosition();
            output = controller.calculate(currentPos, rtpTarget) + Kg;

            leftSlideMotor.setPower(output);
            rightSlideMotor.setPower(output);

            if (Math.abs(rtpTarget - currentPos) > 30) {
                return true;
            } else {
                leftSlideMotor.setPower(0.1);
                rightSlideMotor.setPower(0.1);
                return false;
            }
        }
    }

    public Action AutonomousPIDextend() { return new PIDRunToPos(prepClipPos); }
}


