package org.firstinspires.ftc.teamcode.Subsystems;

import android.security.keystore.StrongBoxUnavailableException;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;


import org.firstinspires.ftc.teamcode.Util.RobotHardware;
public class l3Linkage {
    private final RobotHardware rHardware = new RobotHardware();
    private CRServo l3LinkageServo;
    private OpMode opmode;
    private MotorEx backRight, frontRight;
    private DcMotorEx leftSlideMotor, rightSlideMotor;
    private static final double ServoPower = 1.0;
    private static final double ReverseSlidePower = -1.0;
    private static final double ServoRuntime = 2.0; // adjust
    private static final double SlideRetract = 1.5; // adjust
    private static final double DriveMotorPower = 1;
    private static final double DriveMotorRuntime = 5; //adjust

    public l3Linkage() {}
    public void initialize(OpMode opmode) {
        rHardware.init(opmode.hardwareMap);
        this.opmode = opmode;
        this.l3LinkageServo = rHardware.l3LinkageServo;
        this.backRight = rHardware.rightBackMotor;
        this.frontRight = rHardware.rightFrontMotor;
        this.leftSlideMotor = rHardware.vLslideMotor;
        this.rightSlideMotor = rHardware.vRslideMotor;

        leftSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE); // or rightSlideMotor
    }
    public void operateTest() {
        l3LinkageServo.setPower(opmode.gamepad2.left_stick_x); //change if need, or i can redo code if it needs to be a button/boolean
        leftSlideMotor.setPower(opmode.gamepad2.left_stick_x);
        rightSlideMotor.setPower(opmode.gamepad2.left_stick_x);
        backRight.setVelocity(opmode.gamepad2.left_stick_x); // i dont think this is right
        backRight.setVelocity(opmode.gamepad2.left_stick_x);


    }

    private void vSlides() {
        leftSlideMotor.setPower(ReverseSlidePower);
        rightSlideMotor.setPower(ReverseSlidePower);
    }
    private void vSlides0() {
        leftSlideMotor.setPower(0);
        rightSlideMotor.setPower(0);
    }
    private void RightMotors(){
        backRight.setVelocity(DriveMotorPower);
        frontRight.setVelocity(DriveMotorPower);
    }
    private void RightMotors0(){
        backRight.setVelocity(0);
        frontRight.setVelocity(0);
    }

    public Action l3LinkServo() {
        return new SequentialAction(
                new InstantAction(() -> l3LinkageServo.setPower(ServoPower)),
                new SleepAction(ServoRuntime),
                new InstantAction(() -> l3LinkageServo.setPower(0)));
    }
    public Action l3LinkVSlides(){
        return new SequentialAction(
            new InstantAction(this::vSlides),
            new SleepAction(SlideRetract),
            new InstantAction(this::vSlides0));
    }
    public Action l3LinkDrive(){
        return new SequentialAction(
                new InstantAction(this::RightMotors),
                new SleepAction(DriveMotorRuntime),
                new InstantAction(this::RightMotors0)
        );
    }

}

