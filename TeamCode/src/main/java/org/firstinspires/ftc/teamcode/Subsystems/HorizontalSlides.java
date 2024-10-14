package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

// code for one motor spooled slides
@Config
public class HorizontalSlides
{
    OpMode opmode;

    private final RobotHardware rHardware = new RobotHardware();
    private DcMotorEx slideMotor;

    // constants
    /** all of the constants need to be tuned*/
    public static double joystickScalar = 1;
    public static double slideScalar = 1;
    public static double Kp = 0.004;
    public static double Ki = 0;
    public static double Kd = 0;
    public static double Kg = 0; // gravity constant, tune till the slide holds itself in place
    public static double upperLimit = 780;
    public static double lowerLimit = -2;
    public static double retractedThreshold = 5;

    public static int extendedPos = 700;
    public static int retractedPos = 0;

    public static double mappingExponent = 0.4;

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
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        slideMotor = rHardware.hSlideMotor;
//        slideMotor = opmode.hardwareMap.get(DcMotorEx.class, "");

        slideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
//        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void operateVincent() {
        // maps to percent of upper limit (ex: 1 -> 100%, 0.5 -> 80%, 0.1 -> 60%, 0 -> 0%)
        target = mapTriggerToTarget(opmode.gamepad1.right_trigger);
        PIDPower = PIDControl(target, slideMotor);
        slideMotor.setPower(PIDPower);

        // updates boolean
        horizontalSlidesRetracted = slideMotor.getCurrentPosition() < retractedThreshold;

        opmode.telemetry.addData("PID Power", PIDPower);
        opmode.telemetry.addData("Slide Target", target);
    }

    public void operateTest() {
        // manual control with right stick y
        slidePower = -opmode.gamepad2.left_stick_y;

        if (opmode.gamepad1.y) {
            extend();
        } else if (opmode.gamepad1.x) {
            retract();
        }

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
                target = upperLimit;
            }
            // if out of range, sets target to back in range
            else if (slideMotor.getCurrentPosition() < lowerLimit) {
                target = lowerLimit;
            }
        }
        // PID control
        else {
            PIDPower = PIDControl(target, slideMotor);
            slideMotor.setPower(PIDPower);
        }

        // updates boolean
        horizontalSlidesRetracted = slideMotor.getCurrentPosition() < retractedThreshold;

        opmode.telemetry.addData("PID Power", PIDPower);
        opmode.telemetry.addData("Slide Target", target);
        opmode.telemetry.addData("Encoder Position", slideMotor.getCurrentPosition());
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
        if (Math.abs(output) < 0.1) {
            output = 0;
        }
        else if (Math.abs(output) >= 1) {
            output = (output > 0 ? 1 : -1);
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

    public int mapTriggerToTarget(double input) {
        return (int) Math.round(Math.pow(input, mappingExponent) * upperLimit);
        // paste this into desmos to see graph: x^{0.4}\ \left\{0\le x\le1\right\}
        // making the mapping exponent smaller makes the graph steeper
    }

    public int telemetryMotorPos() {
        return slideMotor.getCurrentPosition();
    }
}
