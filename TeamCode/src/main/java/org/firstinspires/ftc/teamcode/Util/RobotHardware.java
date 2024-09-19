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
    public Servo wristServo = null, armServo = null, clawServo = null;

    //TODO: replace with OTOS
    public NavxMicroNavigationSensor navx;

    //TODO: Replace with names in config
    public void init(@NonNull HardwareMap hardwareMap) {
        this.hMap = hMap;

        this.navx = hMap.get(NavxMicroNavigationSensor.class, "navx");

        this.leftFrontMotor = hMap.get(MotorEx.class, "");
        this.rightFrontMotor = hMap.get(MotorEx.class, "");
        this.leftBackMotor = hMap.get(MotorEx.class, "");
        this.rightBackMotor = hMap.get(MotorEx.class, "");

        this.armServo = hMap.get(Servo.class, "");
        this.wristServo = hMap.get(Servo.class, "");
        this.clawServo = hMap.get(Servo.class, "");

        this.intakeMotor = hMap.get(DcMotorEx.class,"");

        this.hSlideMotor = hMap.get(DcMotorEx.class , "");
        this.vRslideMotor = hMap.get(DcMotorEx.class, "");
        this.vLslideMotor = hMap.get(DcMotorEx.class, "");
    }
}
