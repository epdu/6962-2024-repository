package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class VerticalSlides
{
    OpMode opmode;

    private final RobotHardware rHardware = new RobotHardware();
    private DcMotorEx leftSlideMotor, rightSlideMotor;

    // constants
    /** all constants need to be tuned*/
    public static double joystickScalar = 1;
    public static double slideScalar = 1;
    public static double KpUp = 0.01;
    public static double KpDown = 0.006;
    public static double Ki = 0;
    public static double Kd = 0;
    public static double Kg = 0; // gravity constant, tune till the slide holds itself in place
    public static double upperLimit = 1500;
    public static double lowerLimit = -2;
    public static double retractedThreshold = 5;

    public static int highBucketPos = 1300;
    public static int lowBucketPos = 600;
    public static int lowChamberPos = 100;
    public static int highChamberPos = 600;
    public static int retractedPos = 0;
    public static int pickupClipPos = 200;
    public static int prepClipPos = 400;

    //declaring variables for later modification
    private volatile double slidePower;
    private volatile double target = 0;
    private volatile boolean movingDown = false;
    public volatile boolean verticalSlidesRetracted = true;
    public volatile boolean atTarget = true;

    // PID stuff
    private double PIDPowerL, PIDPowerR;
    ElapsedTime timer = new ElapsedTime();
    double integralSum = 0;
    private double lastError = 0;

    public VerticalSlides() {}

    public void initialize(OpMode opmode) {
        // TODO: assign motor names, then reverse the correct motor
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        leftSlideMotor = rHardware.vLslideMotor;
        rightSlideMotor = rHardware.vRslideMotor;

        leftSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        rightSlideMotor.setDirection(DcMotorEx.Direction.REVERSE);

        leftSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        leftSlideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightSlideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void operateVincent() {
//        PIDPowerL = PIDControl(target, leftSlideMotor);
        PIDPowerR = PIDControl(target, rightSlideMotor);
//        leftSlideMotor.setPower(PIDPowerL);
        leftSlideMotor.setPower(PIDPowerR);
        rightSlideMotor.setPower(PIDPowerR);

        // updates boolean
        verticalSlidesRetracted = leftSlideMotor.getCurrentPosition() < retractedThreshold;

        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlidesRetracted);
        opmode.telemetry.addData("Right Motor Encoder Pos: ", telemetryRightMotorPos());
        opmode.telemetry.addData("PID Power R ", PIDPowerR);
        opmode.telemetry.addData("Slide Target ", target);
        opmode.telemetry.update();
    }

    public void operateTest() {
        // PID auto extension
        if (opmode.gamepad1.dpad_up) {
            raiseToHighBucket();
        }
        else if (opmode.gamepad1.dpad_down) {
            retract();
        }

        // manual control
        slidePower = -1 * opmode.gamepad2.left_stick_y;

        if (Math.abs(slidePower) > 0.05)
        {
            // if position positive, then can move freely
            if (rightSlideMotor.getCurrentPosition() > lowerLimit) {
                leftSlideMotor.setPower(slidePower * slideScalar);
                rightSlideMotor.setPower(slidePower * slideScalar);
                target = rightSlideMotor.getCurrentPosition();
            }
            // if position negative, but want to move positive, then can move
            else if (leftSlideMotor.getCurrentPosition() <= lowerLimit && slidePower > 0)
            {
                leftSlideMotor.setPower(slidePower * slideScalar);
                rightSlideMotor.setPower(slidePower * slideScalar);
                target = rightSlideMotor.getCurrentPosition();
            }

            // if out of range, sets target to back in range
            if (rightSlideMotor.getCurrentPosition() > upperLimit)
            {
                target = upperLimit;
            }
            else if (rightSlideMotor.getCurrentPosition() < lowerLimit)
            {
                target = lowerLimit;
            }
        }
        // PID control
        else
        {
//            PIDPowerL = PIDControl(target, leftSlideMotor);
            PIDPowerR = PIDControl(target, rightSlideMotor);
//            leftSlideMotor.setPower(PIDPowerL);
            leftSlideMotor.setPower(PIDPowerR);
            rightSlideMotor.setPower(PIDPowerR);
        }

        // updates boolean
        verticalSlidesRetracted = rightSlideMotor.getCurrentPosition() < retractedThreshold;

//        opmode.telemetry.addData("PID Power L ", PIDPowerL);

        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlidesRetracted);
        opmode.telemetry.addData("Right Motor Encoder Pos: ", telemetryRightMotorPos());
        opmode.telemetry.addData("PID Power R ", PIDPowerR);
        opmode.telemetry.addData("Slide Target ", target);
        opmode.telemetry.update();
    }

    private double PIDControl(double target, DcMotorEx motor)
    {
        // PID logic and then return the output
        // obtain the encoder position
        double encoderPosition = motor.getCurrentPosition();

        // calculate the error
        double error = target - encoderPosition;

        // error is negative when moving down
        movingDown = error <= 0;

        // rate of change of the error
        double derivative = (error - lastError) / timer.seconds();

        // sum of all error over time
        integralSum += (error * timer.seconds());

        // saves error to use next time
        lastError = error;

        // resets timer for next calculations
        timer.reset();

        // calculates output and returns
        double output = ((movingDown ? KpDown : KpUp) * error) + (Ki * integralSum) + (Kd * derivative) + Kg;

        // deadband-esque behavior to avoid returning super small decimal values
        if (Math.abs(output) < 0.2) {
            atTarget = true;
        }
        if (Math.abs(output) < 0.1) {
            output = 0;
        }
        else if (Math.abs(output) >= 1) {
            output = (output > 0 ? 1 : -1);
        }
        else {
            atTarget = false;
        }

        return output;
    }


    // for use in auto or preset button during teleop
    /** untested and not especially confident in it, so please be cautious when testing */
    public void moveToPosition(int targetPos) {
        target = targetPos;
    }
    public void raiseToHighBucket() { moveToPosition(highBucketPos); }
    public void raiseToLowBucket() { moveToPosition(lowBucketPos); }
    public void raiseToLowChamber() { moveToPosition(lowChamberPos); }
    public void raiseToHighChamber() { moveToPosition(highChamberPos);}
    public void raiseToPickupClip() { moveToPosition(pickupClipPos);}
    public void raiseToPrepClip() { moveToPosition(prepClipPos);}
    public void retract() { moveToPosition(retractedPos); }

    public int telemetryLeftMotorPos() {
        return leftSlideMotor.getCurrentPosition();
    }
    public int telemetryRightMotorPos() {
        return rightSlideMotor.getCurrentPosition();
    }

    // math util
    public double mapToParabola(double output) {
        // if too high and slides start oscillating (aka spasming), try the commented out ones below
        return (70.0/81)*Math.pow(Math.abs(output)-0.1,2) + 0.3; // desmos visual: \frac{280}{361}\left(x-0.05\right)^{2}+0.3
//        return ((300.0/361)*Math.pow(Math.abs(output)-0.05,2) + 0.25); // desmos visual: \frac{300}{361}\left(x-0.05\right)^{2}+0.25
//        return ((320.0/361)*Math.pow(Math.abs(output)-0.05,2) + 0.20); // desmos visual: \frac{300}{361}\left(x-0.05\right)^{2}+0.25
    }

//    public class retractSlides implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            retract();
//            return false;
//        }
//    }
//    public Action retractSlides() {
//        return new retractSlides();
//    }
//    public class scoreBucket implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            raiseToHighBucket();
//            return false;
//        }
//    }
//    public Action scoreBucket() {
//        return new scoreBucket();
//    }
//    public class pickupClip implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            raiseToPickupClip();
//            return false;
//        }
//    }
//    public Action pickupClip() {
//        return new pickupClip();
//    }
//    public class prepClip implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            raiseToPrepClip();
//            return false;
//        }
//    }
//    public Action prepClip() {
//        return new prepClip();
//    }
//    public class scoreClip implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            raiseToHighChamber();
//            return false;
//        }
//    }
//    public Action scoreClip() {
//        return new scoreClip();
//    }
}

