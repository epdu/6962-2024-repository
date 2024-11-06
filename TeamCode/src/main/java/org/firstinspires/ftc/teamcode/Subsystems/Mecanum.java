package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


import org.firstinspires.ftc.teamcode.Subsystems.NavxManager;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class Mecanum {
    private OpMode opmode;

    private final RobotHardware rHardware = new RobotHardware();
    private MotorEx frontLeft, backLeft, backRight, frontRight;
    private NavxMicroNavigationSensor navx;
    private NavxManager gyroManager;
    private MecanumDrive drive;

    // constants
    private final double slowModeFactor = 0.5;

    private boolean slowModeBool = false;

    public Mecanum() {}

    public void initialize(OpMode opmode)
    {
        rHardware.init(opmode.hardwareMap);
        frontLeft = rHardware.leftFrontMotor;
        backLeft = rHardware.leftBackMotor;
        backRight = rHardware.rightBackMotor;
        frontRight = rHardware.rightFrontMotor;

        frontLeft.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.BRAKE);

        navx = rHardware.navx;
//        navx = opmode.hardwareMap.get(NavxMicroNavigationSensor.class, "navx");
        gyroManager = new NavxManager(navx);
        frontLeft.setInverted(true);
        backLeft.setInverted(true);
        drive = new MecanumDrive(false, frontLeft, frontRight, backLeft, backRight);
        this.opmode = opmode;
    }

    public void operateFieldCentricVincent() {

        // driving field centric
        if (opmode.gamepad1.left_trigger > 0.5) {
            drive.driveFieldCentric(opmode.gamepad1.left_stick_x, -opmode.gamepad1.left_stick_y, opmode.gamepad1.right_stick_x * 0.75, gyroManager.getHeading());
        }
        else {
            drive.driveFieldCentric(opmode.gamepad1.left_stick_x * slowModeFactor, -opmode.gamepad1.left_stick_y * slowModeFactor, (opmode.gamepad1.right_stick_x * 0.75) * slowModeFactor, gyroManager.getHeading());
        }

//        opmode.telemetry.addData("Test drive motor accel: ", frontRight.getAcceleration());
    }

    public void operateFieldCentricTest() {
//        if (opmode.gamepad1.dpad_down) { gyroManager.reset(); }

        slowModeBool = opmode.gamepad1.right_trigger > 0.5;

        // driving field centric
        if (slowModeBool) {
            drive.driveFieldCentric(opmode.gamepad1.left_stick_x/slowModeFactor, -opmode.gamepad1.left_stick_y/slowModeFactor, (opmode.gamepad1.right_stick_x * 0.75)/slowModeFactor, gyroManager.getHeading());
        }
        else {
            drive.driveFieldCentric(opmode.gamepad1.left_stick_x, -opmode.gamepad1.left_stick_y, opmode.gamepad1.right_stick_x * 0.75, gyroManager.getHeading());
        }
    }

    public void operateRoboCentric() {
        // Y button toggles slow mode
        if (opmode.gamepad1.y) {
            toggleSlowMode();
        }

        if (slowModeBool) {
            drive.driveRobotCentric(opmode.gamepad1.left_stick_x/slowModeFactor, -opmode.gamepad1.left_stick_y/slowModeFactor, (opmode.gamepad1.right_stick_x * 0.75)/slowModeFactor);
        }
        else {
            drive.driveRobotCentric(opmode.gamepad1.left_stick_x, -opmode.gamepad1.left_stick_y, opmode.gamepad1.right_stick_x * 0.75);
        }
    }

    private void toggleSlowMode() {
        slowModeBool = !slowModeBool;
    }

//    public Command alignToBackdrop()
//    {
//        if (distSensor.getDistance(DistanceUnit.INCH) > aligningInches)
//        {
//            return new InstantCommand(()->drive.driveRobotCentric(0, -1, 0));
//        }
//        else if (distSensor.getDistance(DistanceUnit.INCH) < aligningInches)
//        {
//            return new RunCommand(() -> drive.driveRobotCentric(0, 1, 0));
//        }
//        return new RunCommand(() -> drive.driveRobotCentric(0, 0, 0));
//    }

//    public void autoDriveToAprilTag() {
//        // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
//        double rangeError = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
//        double headingError = desiredTag.ftcPose.bearing;
//        double yawError = desiredTag.ftcPose.yaw;
//
//        // Use the speed and turn "gains" to calculate how we want the robot to move.
//        drive = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
//        turn = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
//        strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);
//
//        drive.driveRobotCentric(strafe, drive, turn)
//        telemetry.addData("Auto", "Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
//    }
    public double navxHeading() {
        return gyroManager.getHeading();
    }

    public void resetNavx() {gyroManager.reset();}
}
