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

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.names.WebcamNameImpl;

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
    public Servo cWristServo = null, cArmServo = null, cClawServo = null;

    public NavxMicroNavigationSensor navx;

    public WebcamName webcam = null;
    public int cameraMonitorViewId;

    public GamepadEx gamepad1, gamepad2;
    public CRServo hangServoL = null, hangServoR = null;
    public CRServo l3LinkageServo = null;

    public void init(@NonNull HardwareMap hardwareMap) {
        this.hMap = hardwareMap;

        navx = hMap.get(NavxMicroNavigationSensor.class, "navx");
        leftFrontMotor = new MotorEx(hMap, "Fl/Re");
        rightFrontMotor = new MotorEx(hMap, "Fr");
        leftBackMotor = new MotorEx(hMap, "Bl/Le");
        rightBackMotor = new MotorEx(hMap, "Br/Fe");

        cArmServo = hMap.get(Servo.class, "cArmServo");
        cWristServo = hMap.get(Servo.class, "cWristServo");
        cClawServo = hMap.get(Servo.class, "cClawServo");

        iArmServo = hMap.get(Servo.class, "iArmServo");
        iWristServoR = hMap.get(Servo.class, "iWristServoR");
        iWristServoF = hMap.get(Servo.class, "iWristServoF");
        iClawServo = hMap.get(Servo.class, "iClawServo");

        hSlideMotor = hMap.get(DcMotorEx.class , "hSlide");

        vRslideMotor = hMap.get(DcMotorEx.class, "vRslide");
        vLslideMotor = hMap.get(DcMotorEx.class, "vLslide");


        hangServoL = hMap.get(CRServo.class, "hangServoL");
        hangServoR = hMap.get(CRServo.class, "hangServoR");

//        webcam = hMap.get(WebcamName.class, "Webcam 1");
//        cameraMonitorViewId = hMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hMap.appContext.getPackageName());

//        webcam = hMap.get(WebcamName.class, "Webcam 1");
//        cameraMonitorViewId = hMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hMap.appContext.getPackageName());

    }
}
