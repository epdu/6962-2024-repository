package org.firstinspires.ftc.teamcode.Util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.GyroEx;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class RobotHardware {
    public HardwareMap hMap;

    // drive motors
    public MotorEx leftFrontMotor = null;
    public MotorEx rightFrontMotor = null;
    public MotorEx leftBackMotor = null;
    public MotorEx rightBackMotor = null;

    // vertical slides
    public DcMotorEx vRslideMotor = null;
    public DcMotorEx vLslideMotor = null;

    // horizontal slides
    public DcMotorEx hSlideMotor = null;

    // intake arm
    public Servo iArmServo = null, iWristServoR = null, iWristServoF = null, iClawServo = null;

    // scoring arm
    public Servo cWristServoR = null, cWristServoL = null, cArmServo = null, cClawServo = null;

    public NavxMicroNavigationSensor navx;

    public GamepadEx gamepad1, gamepad2;

    public void init(@NonNull HardwareMap hardwareMap) {
        this.hMap = hardwareMap;

        navx = hMap.get(NavxMicroNavigationSensor.class, "navx");
        leftFrontMotor = new MotorEx(hMap, "Fl/Re");
        rightFrontMotor = new MotorEx(hMap, "Fr");
        leftBackMotor = new MotorEx(hMap, "Bl/Le");
        rightBackMotor = new MotorEx(hMap, "Br/Fe");

        cArmServo = hMap.get(Servo.class, "aServo");
        cWristServoR = hMap.get(Servo.class, "cWristR");
        cWristServoL = hMap.get(Servo.class, "cWristL");
        cClawServo = hMap.get(Servo.class, "cServo");

        iArmServo = hMap.get(Servo.class, "iArmServo");
        iWristServoR = hMap.get(Servo.class, "iWristServoR");
        iWristServoF = hMap.get(Servo.class, "iWristServoF");
        iClawServo = hMap.get(Servo.class, "iClawServo");

        hSlideMotor = hMap.get(DcMotorEx.class , "hSlide");

        vRslideMotor = hMap.get(DcMotorEx.class, "vRslide");
        vLslideMotor = hMap.get(DcMotorEx.class, "vLslide");
    }
}
