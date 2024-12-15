package org.firstinspires.ftc.teamcode.Util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.GyroEx;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.hardware.bosch.BNO055IMU;
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
    public DcMotorEx DcLeftBackMotor = null;
    public DcMotorEx DcLeftFrontMotor = null;
    public DcMotorEx vRslideMotor = null;
    public DcMotorEx vLslideMotor = null;

    // horizontal slides
    public DcMotorEx hSlideMotor = null;

    // intake arm
    public Servo iArmServo = null, iWristServoR = null, iWristServoF = null, iClawServo = null;

    // scoring arm
    public Servo cWristServo = null, cArmServo = null, cClawServo = null;

    public NavxMicroNavigationSensor navx;
    public BNO055IMU imu;

    public WebcamName webcam = null;
    public int cameraMonitorViewId;

    public GamepadEx gamepad1, gamepad2;
    public CRServo hangServoL = null, hangServoR = null;
    public Servo ptoActivationServo = null;

    public void init(@NonNull HardwareMap hardwareMap) {
        this.hMap = hardwareMap;

        navx = hMap.get(NavxMicroNavigationSensor.class, "navx"); //CHUB Bus 2
//        imu = hMap.get(BNO055IMU.class, "imu");
        leftFrontMotor = new MotorEx(hMap, "Fl/Re"); //CHUB 1
        rightFrontMotor = new MotorEx(hMap, "Fr"); //CHUB 2
        leftBackMotor = new MotorEx(hMap, "Bl/Le"); //CHUB 0
        rightBackMotor = new MotorEx(hMap, "Br/Fe"); //CHUB 3

        DcLeftBackMotor = hMap.get(DcMotorEx.class, "Bl/Le");
        DcLeftFrontMotor = hMap.get(DcMotorEx.class, "Fl/Re");

        cArmServo = hMap.get(Servo.class, "cArmServo"); //CHUB 4
        cWristServo = hMap.get(Servo.class, "cWristServo"); //CHUB 2
        cClawServo = hMap.get(Servo.class, "cClawServo"); //CHUB 0


        iArmServo = hMap.get(Servo.class, "iArmServo"); //EHUB 5
        iWristServoR = hMap.get(Servo.class, "iWristServoR"); //EHUB 2
        iWristServoF = hMap.get(Servo.class, "iWristServoF"); //EHUB 3
        iClawServo = hMap.get(Servo.class, "iClawServo"); //EHUB 4

        hSlideMotor = hMap.get(DcMotorEx.class , "hSlide"); //EHUB 0

        vRslideMotor = hMap.get(DcMotorEx.class, "vRslide"); //EHUB 1
        vLslideMotor = hMap.get(DcMotorEx.class, "vLslide"); //EHUB 2


        hangServoL = hMap.get(CRServo.class, "hangServoL"); //EHUB 0
        hangServoR = hMap.get(CRServo.class, "hangServoR"); //CHUB 3
        ptoActivationServo = hMap.get(Servo.class, "ptoServo"); //EHUB 1
        //micro EHUB 1

//        webcam = hMap.get(WebcamName.class, "Webcam 1");
//        cameraMonitorViewId = hMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hMap.appContext.getPackageName());

    }
}
