package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
public class Intake
{
    OpMode opmode;
    CustomTimer timer;
    private DcMotorEx intakeMotor;
    private Servo wristServo;
    private ColorRangeSensor colorSensor;
    private boolean ON_RED_ALLIANCE;

    // variables created for later modification, not constants
    public volatile boolean flippedUp = true;

    // constants
    /** all of the constants need to be tuned*/
    private double intakePower = 0.5;
    private double stowedPosition = 0.4;
    private double intakePosition = 0;
    private double detectionThreshold = 0.5; //inches
    private int colorThreshold = 200;

    public Intake() {}

    public void initialize(OpMode opmode, CustomTimer timer, boolean redAlliance) {
        this.ON_RED_ALLIANCE = redAlliance;
        this.intakeMotor = opmode.hardwareMap.get(DcMotorEx.class, "");
        this.wristServo = opmode.hardwareMap.get(Servo.class, "");
        this.timer = timer;
//        intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void operateVincent()
    {
        if (opmode.gamepad2.right_bumper) {
            // start intaking
            intakeMotor.setPower(intakePower);
            // flip out
            wristServo.setPosition(intakePosition);
        }
        else {
            // stop
            intakeMotor.setPower(0);
            // flip in
            wristServo.setPosition(stowedPosition);
        }
    }

    public void operateTest() {
        if (opmode.gamepad2.a) {
            intakePieces();
        }
        else if (opmode.gamepad2.b) {
            stopIntaking();
        }
        else if (opmode.gamepad2.x) {
            eject();
        }
    }

    public void shutdown() {}

    public void intakePieces() {
        // right trigger extend hori slides
        intakeMotor.setPower(intakePower);
        // flip out
        wristServo.setPosition(intakePosition);
        flippedUp = false;
    }
    public void stopIntaking() {
        // stop
        intakeMotor.setPower(0);
        // flip in
        wristServo.setPosition(stowedPosition);
        flippedUp = true;
    }
    public void eject() {
        // reverse direction for 2 seconds, then return
        intakeMotor.setPower(-intakePower);
        timer.safeDelay(2000);
    }

    public void fullIntakeSequence() {
        intakePieces();              // start intake and flip down
        while (!correctPiece()) {    // wait until a piece is picked up
            if (pieceTakenInBool() && !correctColorBool())
                eject();
        }
        stopIntaking();              // stop and flip up
    }

    public boolean correctPiece() {
        return pieceTakenInBool() && correctColorBool();
    }
    public boolean pieceTakenInBool() { return colorSensor.getDistance(DistanceUnit.INCH) < detectionThreshold; }
    public boolean correctColorBool() {
        String color = identifyColor();

        if (color == "YELLOW") {
            return true;
        }
        else if (ON_RED_ALLIANCE){
            return color == "RED";
        }
        else {
            return color == "BLUE";
        }
    }

    public String identifyColor() {
        // Read the color sensor values
        int red = colorSensor.red();
        int green = colorSensor.green();
        int blue = colorSensor.blue();

        // Define thresholds for color detection
        if (red > 2 * blue && green > 2 * blue) {
            // Yellow: high red and green values, low blue value
            return "YELLOW";
        } else if (red > blue && red > green) {
            // Red: dominant red value
            return "RED";
        } else {
            // Default to Blue
            return "BLUE";
        }
    }
}