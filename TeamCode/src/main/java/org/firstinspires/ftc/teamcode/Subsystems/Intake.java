package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class Intake
{
    OpMode opmode;

    private final RobotHardware rHardware = new RobotHardware();
    CustomTimer timer;
    private DcMotorEx intakeMotor;
    private Servo wristServo;

    private CRServo iCRServoL, iCRServoR;
    private ColorRangeSensor colorSensor;
    private boolean ON_RED_ALLIANCE;

    // variables created for later modification, not constants
    public volatile boolean flippedUp = true;


    // constants
    /** all of the constants need to be tuned*/
    public static double intakePower = 0.6;
    public static double transferPosition = 0.4;
    public static double intakePosition = 0;
    public static double wristIncrement = 0.001;
//    public static double detectionThreshold = 0.5; //inches
//    public static int colorThreshold = 200;

    public Intake() {}

    public void initialize(OpMode opmode, CustomTimer timer, boolean redAlliance) {
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);
        ON_RED_ALLIANCE = redAlliance;

        this.timer = timer;
        this.intakeMotor = rHardware.intakeMotor;
        this.wristServo = rHardware.iWristServo;

        this.iCRServoL = rHardware.iCRServoL;
        this.iCRServoR = rHardware.iCRServoR;
//        intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void operateVincent()
    {
        if (opmode.gamepad1.right_bumper) {
            // start intaking
            intakePieces();// flip out
            if (opmode.gamepad1.a) {
                eject();
            }
        }
        else {
            // stop
            stopIntaking();
        }
    }

    public void operateTest() {
        intakeMotor.setPower(-opmode.gamepad2.left_stick_y);

        if (opmode.gamepad2.dpad_up) {
            incremental(1);
        } else if (opmode.gamepad2.dpad_down) {
            incremental(-1);
        }

        // gamepad 1
        if (opmode.gamepad1.dpad_down) {
            intakePieces();
        }
        else if (opmode.gamepad1.dpad_up) {
            stopIntaking();
        }
        else if (opmode.gamepad1.x) {
            eject();
        } else if (opmode.gamepad1.y) {
            transfer();
        }
    }

    public void shutdown() {}

    public void intakePieces() {
//        intakeMotor.setPower(intakePower);
        //Countinous servo
        iCRServoR.setPower(intakePower);
        iCRServoL.setPower(intakePower);
        // flip out
        wristServo.setPosition(intakePosition);
        flippedUp = false;
    }
    public void stopIntaking() {
        // stop
//        intakeMotor.setPower(0);
        //CR servo
        iCRServoL.setPower(0);
        iCRServoR.setPower(0);
        // flip in
        wristServo.setPosition(transferPosition);
        flippedUp = true;
    }
    public void eject() {
        // reverse direction for 2 seconds, then return
        intakeMotor.setPower(-0.5*intakePower);
        //CRServo
        iCRServoL.setPower(-0.5*intakePower);
        iCRServoR.setPower(-0.5*intakePower);
        timer.safeDelay(2000);
    }

    public void transfer() {
        //TODO: Tune intake wrist transfer position
        if (!flippedUp) {wristServo.setPosition(transferPosition);}

        iCRServoL.setPower(-0.3*intakePower);
        iCRServoR.setPower(-0.3*intakePower);
        //TODO: tune timer delay seconds
        timer.safeDelay(2000);
    }

    public void incremental(int sign) {
        wristServo.setPosition(wristServo.getPosition() + sign * wristIncrement);
    }

//    public void fullIntakeSequence() {
//        intakePieces();              // start intake and flip down
//        while (!correctPiece()) {    // wait until a piece is picked up
//            if (pieceTakenInBool() && !correctColorBool())
//                eject();
//        }
//        stopIntaking();              // stop and flip up
//    }

//    public boolean correctPiece() {
//        return pieceTakenInBool() && correctColorBool();
//    }
//    public boolean pieceTakenInBool() { return colorSensor.getDistance(DistanceUnit.INCH) < detectionThreshold; }
//    public boolean correctColorBool() {
//        String color = identifyColor();
//
//        if (color == "YELLOW") {
//            return true;
//        }
//        else if (ON_RED_ALLIANCE){
//            return color == "RED";
//        }
//        else {
//            return color == "BLUE";
//        }
//    }
//
//    public String identifyColor() {
//        // Read the color sensor values
//        int red = colorSensor.red();
//        int green = colorSensor.green();
//        int blue = colorSensor.blue();
//
//        // Define thresholds for color detection
//        if (red > 2 * blue && green > 2 * blue) {
//            // Yellow: high red and green values, low blue value
//            return "YELLOW";
//        } else if (red > blue && red > green) {
//            // Red: dominant red value
//            return "RED";
//        } else {
//            // Default to Blue
//            return "BLUE";
//        }
//    }
}