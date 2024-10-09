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

    public MotorEx leftFrontMotor = null;
    public MotorEx rightFrontMotor = null;
    public MotorEx leftBackMotor = null;
    public MotorEx rightBackMotor = null;

    public DcMotorEx vRslideMotor = null;
    public DcMotorEx vLslideMotor = null;

    public DcMotorEx hSlideMotor = null;

//    public DcMotorEx intakeMotor = null;
    public Servo iWristServo = null;
    public CRServo iCRServoL = null, iCRServoR = null;


    public Servo cWristServoR = null, cWristServoL = null, armServo = null, claw = null;

    public NavxMicroNavigationSensor navx;

    public GamepadEx gamepad1, gamepad2;

    public void init(@NonNull HardwareMap hardwareMap) {
        this.hMap = hardwareMap;

        navx = hMap.get(NavxMicroNavigationSensor.class, "navx");
        leftFrontMotor = new MotorEx(hMap, "Fl/Re");
        rightFrontMotor = new MotorEx(hMap, "Fr");
        leftBackMotor = new MotorEx(hMap, "Bl/Le");
        rightBackMotor = new MotorEx(hMap, "Br/Fe");

        armServo = hMap.get(Servo.class, "aServo");
        cWristServoR = hMap.get(Servo.class, "cWristR");
        cWristServoL = hMap.get(Servo.class, "cWristL");
        claw = hMap.get(Servo.class, "cServo");

//        intakeMotor = hMap.get(DcMotorEx.class,"intake");
//        iCRServoL = hMap.get(CRServo.class, "iCRServoL");
//        iCRServoR = hMap.get(CRServo.class, "iCRServoR");
        iWristServo = hMap.get(Servo.class, "iWrist");

        hSlideMotor = hMap.get(DcMotorEx.class , "hSlide");

        vRslideMotor = hMap.get(DcMotorEx.class, "vRslide");
        vLslideMotor = hMap.get(DcMotorEx.class, "vLslide");
    }
}
