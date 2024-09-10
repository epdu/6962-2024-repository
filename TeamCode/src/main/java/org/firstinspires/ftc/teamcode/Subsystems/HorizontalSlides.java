package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

// code for one motor spooled slides
@Config
public class HorizontalSlides
{
    OpMode opmode;
    private DcMotorEx slideMotor;

    // constants
    /** all of the constants need to be tuned*/
    private double joystickScalar = 1;
    private double slideScalar = 0.45;
    private double Kp = 0.005;
    private double Ki = 0;
    private double Kd = 0;
    private double Kg = 0; // gravity constant, tune till the slide holds itself in place
    private double upperLimit = 400;
    private double lowerLimit = -2;
    private double retractedThreshold = 10;

    private int extendedPos = 100;
    private int retractedPos = 0;


    // declaring variables for later modification
    private volatile double slidePower;
    private volatile double target = 0;
    public volatile boolean horizontalSlidesRetracted = true;

    // PID stuff
    private double PIDPower;
    ElapsedTime timer = new ElapsedTime();
    double integralSum = 0;
    private double lastError = 0;

    public HorizontalSlides() {}

    public void initialize(OpMode opmode) {
        // TODO: assign motor names, then reverse the correct motor
        slideMotor = opmode.hardwareMap.get(DcMotorEx.class, "");

        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
//        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void operate() {
        // adds inputs, which accounts for both triggers as well as mistakes
        slidePower = opmode.gamepad2.right_trigger + (-1 * opmode.gamepad2.left_trigger);

        // manual control
        if (Math.abs(slidePower) > 0.05) {
            // if position positive, then can move freely
            if (slideMotor.getCurrentPosition() > lowerLimit) {
                slideMotor.setPower(slidePower * slideScalar);
                target = slideMotor.getCurrentPosition();
            }
            // if position negative, but want to move positive, then can move
            else if (slideMotor.getCurrentPosition() <= lowerLimit && slidePower > 0) {
                slideMotor.setPower(slidePower * slideScalar);
                target = slideMotor.getCurrentPosition();
            }
            // if out of range, sets target to back in range
            if (slideMotor.getCurrentPosition() > upperLimit) {
                target = upperLimit-2;
            }
            // if out of range, sets target to back in range
            else if (slideMotor.getCurrentPosition() < lowerLimit) {
                target = lowerLimit+2;
            }
        }
        // PID control
        else {
            PIDPower = PIDControl(target, slideMotor);
            slideMotor.setPower(PIDPower);
        }

        // updates boolean
        if (slideMotor.getCurrentPosition() < retractedThreshold) {
            horizontalSlidesRetracted = true;
        }
        else {
            horizontalSlidesRetracted = false;
        }

        opmode.telemetry.addData("PID Power", PIDPower);
        opmode.telemetry.addData("Slide Target ", target);
    }

    public void shutdown() {
        // not sure if necessary, but including just in case
        PIDPower = 0;
        slideMotor.setPower(0);
    }

    private double PIDControl(double target, DcMotorEx motor)
    {
        // PID logic and then return the output
        // obtain the encoder position
        double encoderPosition = motor.getCurrentPosition();

        // calculate the error
        double error = target - encoderPosition;
        // rate of change of the error
        double derivative = (error - lastError) / timer.seconds();

        // sum of all error over time
        integralSum += (error * timer.seconds());

        // saves error to use next time
        lastError = error;

        // resets timer for next calculations
        timer.reset();

        // calculates output and returns
        double output = (Kp * error) + (Ki * integralSum) + (Kd * derivative) + Kg;

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

    /** untested and not especially confident in it, so please be cautious when testing */
    // for use during auto or preset button during teleop
    public void moveToPosition(int targetPos) {
        target = targetPos;
    }

    public void extend() { moveToPosition(extendedPos); }
    public void retract() { moveToPosition(retractedPos); }
}
