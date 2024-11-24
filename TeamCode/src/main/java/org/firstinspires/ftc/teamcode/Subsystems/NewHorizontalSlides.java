package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class NewHorizontalSlides {

    OpMode opmode;

    private final RobotHardware rHardware = new RobotHardware();
    private PIDController controller;
    private DcMotorEx slideMotor;

    // constants
    public static double Kp = 0.006;
    public static double Ki = 0;
    public static double Kd = 0.00001;
    public static double Kg = 0;
    public static int upperLimit = 650; // this is for 1150s
    public static int lowerLimit = -2;
    public static double retractedThreshold = 10;
    public static double mostlyRetractedThreshold = 60;

    // encoder positions
    public static int extendedPos = 600;
    public static int halfExtendedPos = 450;
    public static int retractedPos = 0;

    //declaring variables for later modification
    private volatile double target = 0;
    private volatile double slidePower;
    private volatile double output = 0;
    public volatile boolean slidesRetracted = true;
    public volatile boolean slidesMostlyRetracted = true;

    public NewHorizontalSlides() {}

    public void teleInitialize(OpMode opmode) {
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        controller = new PIDController(Kp, Ki, Kd);

        slideMotor = rHardware.hSlideMotor;

        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

//        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        slideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void autonomousInitialize(OpMode opmode) {
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        slideMotor = rHardware.hSlideMotor;

        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

//        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
    }

    public void operateVincent() {
        int currentPos = slideMotor.getCurrentPosition();
        output = controller.calculate(currentPos, target) + Kg;

        slideMotor.setPower(output);

        // updates boolean
        slidesRetracted = currentPos < retractedThreshold;
        slidesMostlyRetracted = currentPos < mostlyRetractedThreshold;
    }

    public void operateTuning() {
        if (opmode.gamepad2.y) {
            target = extendedPos;
        } else if (opmode.gamepad2.a) {
            target = retractedPos;
        } else if (opmode.gamepad2.b) {
            target = halfExtendedPos;
        }

        int currentPos = slideMotor.getCurrentPosition();

        slidePower = -1 * opmode.gamepad2.left_stick_y;
        if (Math.abs(slidePower) > 0.05) {
            // move freely
            slideMotor.setPower(slidePower);

            // if out of range, sets target to back in range
            if (currentPos > upperLimit) {
                target = upperLimit;
            } else if (currentPos < lowerLimit) {
                target = lowerLimit;
            }
        } else {
            output = controller.calculate(currentPos, target) + Kg;

            slideMotor.setPower(output);
        }

        // updates boolean
        slidesRetracted = currentPos < retractedThreshold;
    }

    public void operateIncremental() {
        if (opmode.gamepad2.y) {
            extend();
        }
        else if (opmode.gamepad2.x) {
            retract();
        }

        int currentPos = slideMotor.getCurrentPosition();

        slidePower = -opmode.gamepad2.right_stick_y;

        if (Math.abs(slidePower) > 0.05) {
            // move freely
            slideMotor.setPower(slidePower);

            // if out of range, sets target to back in range
            if (currentPos > upperLimit) {
                target = upperLimit;
            } else if (currentPos < lowerLimit) {
                target = lowerLimit;
            }
        } else {
            output = controller.calculate(currentPos, target) + Kg;

            slideMotor.setPower(output);
        }

        opmode.telemetry.addData("Vertical Encoder Position:", currentPos);
        opmode.telemetry.addData("Vertical Slide Target:", target);
        opmode.telemetry.addData("Vertical Slide Power:", slidePower);
        opmode.telemetry.update();
    }

    public void operateFix() {
        // manual control
        slidePower = -opmode.gamepad2.right_stick_y;

        if (Math.abs(slidePower) > 0.05)
        {
            slideMotor.setPower(slidePower);
        }

        if (opmode.gamepad2.left_stick_button) {
            slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        }

    }

    public void moveToPosition(int targetPos) {
        target = targetPos;
    }

    public void extend() {
        moveToPosition(extendedPos);
    }
    public void extendHalfway() {
        moveToPosition(halfExtendedPos);
    }
    public void retract() {
        moveToPosition(retractedPos);
    }

    public double telemetryMotorPos() {
        return slideMotor.getCurrentPosition();
    }
    public double telemetryTarget() {
        return target;
    }
    public double telemetryOutput() {
        return output;
    }

    // Functioning autonomous commands
    public class RunToPosition implements Action {
        private boolean initialized = false;
        private int rtpTarget = 0;

        public RunToPosition(int targetPos) {
            rtpTarget = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            int error = rtpTarget - slideMotor.getCurrentPosition();
            if (!initialized) {
                int sign = (error >= 0 ? 1 : -1);
                slideMotor.setPower(sign * 0.8);
                initialized = true;
            }

            if (Math.abs(error) > 20) {
                return true;
            } else {
                slideMotor.setPower(0);
                return false;
            }
        }
    }

    public Action HorizontalExtend() {
        return new RunToPosition(halfExtendedPos);
    }

    public Action HorizontalRetract() {
        return new RunToPosition(retractedPos);
    }

    // autonomous action stuff test
    // this might not work though
    public class BuiltInRTP implements Action {
        private int rtpTarget = 0;

        // constructor so this class can be reused without needing to duplicate
        public BuiltInRTP(int targetPos) {
            this.rtpTarget = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            slideMotor.setTargetPosition(rtpTarget);
            return false;
        }
    }

    public Action BuiltInRTPExtend() {return new BuiltInRTP(extendedPos);}

    public Action BuiltInRTPRetract() {return new BuiltInRTP(retractedPos);}
}


