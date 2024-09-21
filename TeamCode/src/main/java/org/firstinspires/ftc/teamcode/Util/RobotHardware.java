package org.firstinspires.ftc.teamcode.Util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
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

    public DcMotorEx intakeMotor = null;
    public Servo iWristServo = null;

    public Servo cWristServo = null, armServo = null, claw = null;

    //TODO: replace with OTOS
    public NavxMicroNavigationSensor navx;

    //TODO: Replace with names in config
    public void init(@NonNull HardwareMap hardwareMap) {
        this.hMap = hardwareMap;

        navx = hMap.get(NavxMicroNavigationSensor.class, "navx");

        leftFrontMotor = hMap.get(MotorEx.class, "");
        rightFrontMotor = hMap.get(MotorEx.class, "");
        leftBackMotor = hMap.get(MotorEx.class, "");
        rightBackMotor = hMap.get(MotorEx.class, "");

        armServo = hMap.get(Servo.class, "");
        cWristServo = hMap.get(Servo.class, "");
        claw = hMap.get(Servo.class, "");

        intakeMotor = hMap.get(DcMotorEx.class,"");
        iWristServo = hMap.get(Servo.class, "");

        hSlideMotor = hMap.get(DcMotorEx.class , "");

        vRslideMotor = hMap.get(DcMotorEx.class, "");
        vLslideMotor = hMap.get(DcMotorEx.class, "");
    }
}
