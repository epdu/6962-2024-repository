package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.roadrunner.Action;
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
    public static int upperLimit = 4000; // this is for 435s
//    public static int upperLimit = 1500; // this is for 1150s

    public static int lowerLimit = -2;
    public static int retractedThreshold = 5;

    public static int highBucketPos = 3700; // this is for 435s
//    public static int highBucketPos = 1300; // this is for 1150s
//    public static int lowBucketPos = 600;
    public static int retractedPos = 0;
    public static int pickupClipPos = 0;
    public static int prepClipPos = 1250; // this is for 435s
//    public static int prepClipPos = 555; // this is for 1150s
    public static int slamClipPos = 600; // this is for 435s
//    public static int slamClipPos = 600; // this is for 1150s

    //declaring variables for later modification
    private volatile double slidePower;
    private volatile int target = 0;
    private volatile boolean movingDown = false;
    public volatile boolean verticalSlidesRetracted = true;

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
        PIDPowerR = PIDControl(target, rightSlideMotor);
        leftSlideMotor.setPower(PIDPowerR);
        rightSlideMotor.setPower(PIDPowerR);

        // updates boolean
        verticalSlidesRetracted = rightSlideMotor.getCurrentPosition() < retractedThreshold;

//        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlidesRetracted);
//        opmode.telemetry.addData("Right Motor Encoder Pos: ", rightSlideMotor.getCurrentPosition());
//        opmode.telemetry.addData("PID Power R ", PIDPowerR);
//        opmode.telemetry.addData("Slide Target ", target);
//        opmode.telemetry.addData("Test slide power draw: ", rightSlideMotor.getCurrent(CurrentUnit.AMPS));
//        opmode.telemetry.addData("Test slide power alert: ", rightSlideMotor.getCurrentAlert(CurrentUnit.AMPS));
//        opmode.telemetry.update();
    }

    public void operateFix() {

        // manual control
        slidePower = -opmode.gamepad2.left_stick_y;

        if (Math.abs(slidePower) > 0.05)
        {
            leftSlideMotor.setPower(slidePower * slideScalar);
            rightSlideMotor.setPower(slidePower * slideScalar);
        }

        if (opmode.gamepad2.left_stick_button) {
            leftSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            rightSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        }

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
        else {
            PIDPowerR = PIDControl(target, rightSlideMotor);
            leftSlideMotor.setPower(PIDPowerR);
            rightSlideMotor.setPower(PIDPowerR);
        }

        opmode.telemetry.addData("Vertical Encoder Position:", rightSlideMotor.getCurrentPosition());
        opmode.telemetry.addData("Vertical Slide Target:", target);
        opmode.telemetry.addData("PID Power:", PIDPowerR);
        opmode.telemetry.addData("Vertical Slide Power:", slidePower);
    }

    public void operateIncremental() {

        if (opmode.gamepad2.y) {
            raiseToHighBucket();
        }
        else if (opmode.gamepad2.x) {
            retract();
        }

        slidePower = -1 * opmode.gamepad2.right_stick_y;
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

        opmode.telemetry.addData("Vertical Encoder Position:", rightSlideMotor.getCurrentPosition());
        opmode.telemetry.addData("Vertical Slide Target:", target);
        opmode.telemetry.addData("Vertical Slide Power:", slidePower);
        opmode.telemetry.update();
    }

    private double PIDControl(int target, DcMotorEx motor)
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

        return output;
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
    public void slamToScoreClip() { moveToPosition(slamClipPos);}

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

    // Action class stuff
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
                leftSlideMotor.setPower(sign * 0.8);
                rightSlideMotor.setPower(sign * 0.8);
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

    public class RTP implements Action {
        private boolean initialized = false;
        private int rtpTarget = 0;

        public RTP(int targetPos) {
            rtpTarget = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                rtpTarget = prepClipPos;
                initialized = true;
            }

            PIDPowerR = PIDControl(rtpTarget, rightSlideMotor);
            leftSlideMotor.setPower(PIDPowerR);
            rightSlideMotor.setPower(PIDPowerR);

            if (Math.abs(rtpTarget - rightSlideMotor.getCurrentPosition()) > 30) {
                return true;
            } else {
                leftSlideMotor.setPower(0);
                rightSlideMotor.setPower(0);
                return false;
            }
        }
    }

    public Action RTP() {
        return new RTP(200);
    }

    public Action PIDTest() {return new RTP(prepClipPos);}
}

