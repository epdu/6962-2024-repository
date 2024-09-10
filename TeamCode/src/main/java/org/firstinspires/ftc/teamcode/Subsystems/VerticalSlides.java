package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
@Config
public class VerticalSlides
{
    OpMode opmode;
    private DcMotorEx leftSlideMotor, rightSlideMotor;

    // constants
    /** all of the constants need to be tuned*/
    private double joystickScalar = 1;
    private double slideScalar = 0.45;
    private double KpUp = 0.005;
    private double KpDown = 0.001;
    private double Ki = 0;
    private double Kd = 0;
    private double Kg = 0; // gravity constant, tune till the slide holds itself in place
    private double upperLimit = 800;
    private double lowerLimit = -2;
    private double retractedThreshold = 0;

    private int highBucketPos = 500;
    private int lowBucketPos = 250;
    private int lowChamberPos = 200;
    private int retractedPos = 0;

    //declaring variables for later modification
    private volatile double slidePower;
    private volatile double target = 0;
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
        leftSlideMotor = opmode.hardwareMap.get(DcMotorEx.class, "");
        rightSlideMotor = opmode.hardwareMap.get(DcMotorEx.class, "");

        leftSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

//        leftSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void operate() {
        // manual control
        slidePower = -1 * opmode.gamepad2.left_stick_y;
        if (Math.abs(slidePower) > 0.05)
        {
            // if position positive, then can move freely
            if (leftSlideMotor.getCurrentPosition() > lowerLimit || rightSlideMotor.getCurrentPosition() > lowerLimit) {
                leftSlideMotor.setPower(slidePower * slideScalar);
                rightSlideMotor.setPower(slidePower * slideScalar);
                target = (leftSlideMotor.getCurrentPosition() + rightSlideMotor.getCurrentPosition()) * 0.5;
            }
            // if position negative, but want to move positive, then can move
            else if (leftSlideMotor.getCurrentPosition() <= lowerLimit && slidePower > 0)
            {
                leftSlideMotor.setPower(slidePower * slideScalar);
                rightSlideMotor.setPower(slidePower * slideScalar);
                target = (leftSlideMotor.getCurrentPosition() + rightSlideMotor.getCurrentPosition()) * 0.5;
            }

            // if out of range, sets target to back in range
            if (leftSlideMotor.getCurrentPosition() > upperLimit || rightSlideMotor.getCurrentPosition() > upperLimit)
            {
                target = upperLimit-2;
            }
            else if (leftSlideMotor.getCurrentPosition() < lowerLimit || rightSlideMotor.getCurrentPosition() < lowerLimit)
            {
                target = lowerLimit+2;
            }
        }
        // PID control
        else
        {

            PIDPowerL = PIDControl(target, leftSlideMotor);
            PIDPowerR = PIDControl(target, rightSlideMotor);

            leftSlideMotor.setPower(PIDPowerL);
            rightSlideMotor.setPower(PIDPowerR);
        }

        // updates boolean
        if (leftSlideMotor.getCurrentPosition() < retractedThreshold && leftSlideMotor.getCurrentPosition() < retractedThreshold) {
            verticalSlidesRetracted = true;
        }
        else {
            verticalSlidesRetracted = false;
        }

        opmode.telemetry.addData("PID Power L ", PIDPowerL);
        opmode.telemetry.addData("PID Power R ", PIDPowerL);
        opmode.telemetry.addData("Slide Target ", target);
    }

    public void shutdown() {
        // not sure if necessary, but including just in case
        PIDPowerL = 0;
        PIDPowerR = 0;
        leftSlideMotor.setPower(0);
        rightSlideMotor.setPower(0);
    }

    private double PIDControl(double target, DcMotorEx motor)
    {
        // PID logic and then return the output
        // obtain the encoder position
        double encoderPosition = motor.getCurrentPosition();

        // calculate the error
        double error = target - encoderPosition;

        // error is negative when moving down
        if (error <= 0) {
            movingDown = true;
        }
        // error is positive when moving up
        else {
            movingDown = false;
        }
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
        if (Math.abs(output) < 0.01) {
            output = 0;
        }
        // keeping output ≤ 1
        else {
            /** if weird things are happening, it could be this, because idk if the logic actually works */
            // crazy syntax, but basically means if output > 0, then variable is assigned 1, else -1
            int outputSign = output > 0 ? 1 : -1;
            output = outputSign * Math.min(1.0, Math.abs(output));
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
    public void retract() { moveToPosition(retractedPos); }
}

