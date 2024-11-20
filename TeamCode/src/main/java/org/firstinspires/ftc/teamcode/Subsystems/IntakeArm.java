package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Camera;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.CameraCVPipeline;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class IntakeArm {

    OpMode opmode;

    private FtcDashboard dash = FtcDashboard.getInstance();
    private Telemetry dashboardTelemetry = dash.getTelemetry();
    public Claw claw = new Claw();
    public Arm arm = new Arm();
    public Wrist wrist = new Wrist();
    private final RobotHardware rHardware = new RobotHardware();

    private static CameraPortal cPortal = new CameraPortal();
    private static CameraCVPipeline pipeline = new CameraCVPipeline();

    // constructor
    public IntakeArm() {}

    // Initializes the sub-subsystems
    public void initialize(OpMode opmode) {
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        claw.initialize(rHardware);
        arm.initialize(rHardware);
        wrist.initialize(rHardware);
        pipeline.initialize();
        cPortal.init(opmode);
    }

    // Operates the claw for Vincent configuration
    public void operateVincent() {
        // empty, not used in final teleop
    }

    // Operates the test mode for controlling the claw, arm, and wrist
    public void operateTest(OpMode opmode) {
        cPortal.run(opmode);

        // gamepad 2, all incrementals to find servo values first try
        if (opmode.gamepad2.left_bumper) { claw.incrementalClaw(-1); }
        else if (opmode.gamepad2.right_bumper) { claw.incrementalClaw(1); }

        wrist.incrementalWristFlip(getSign(-opmode.gamepad2.left_stick_y));
        wrist.incrementalWristRotateTest(getSign(opmode.gamepad2.left_stick_x));

        if (opmode.gamepad2.dpad_up) { arm.incrementalArm(-1); }
        else if (opmode.gamepad2.dpad_down) { arm.incrementalArm(1); }

        if (opmode.gamepad2.y) {wrist.setWristIntake();}
        if (opmode.gamepad2.x) {wrist.setWristTransfer();}
        if (opmode.gamepad2.a) {wrist.setWristCamera();}
        // Adding telemetry data for debugging
        opmode.telemetry.addData("Arm Pos: ", arm.arm.getPosition());
        opmode.telemetry.addData("Flip Wrist Pos: ", wrist.wristFlip.getPosition());
        opmode.telemetry.addData("Rotate Wrist Pos: ", wrist.wristRotate.getPosition());
        opmode.telemetry.addData("Claw Pos: ", claw.claw.getPosition());
        opmode.telemetry.addData("Camera Servo Rotation: ", cPortal.getServoRotation());
        opmode.telemetry.update();
    }

    public void operateIncremental() {

        if (opmode.gamepad1.a) {
            arm.setArmHover();
            wrist.setWristIntake();
            claw.openClaw();
        }
        else if (opmode.gamepad1.b) {
            arm.setArmTransfer();
            wrist.setWristTransfer();
            claw.closeClaw();
        }
        else if (opmode.gamepad1.left_bumper) {
            arm.setArmGrab();
//            claw.closeClaw();
        }

        if (opmode.gamepad1.y) { claw.closeClaw(); }
        if (opmode.gamepad1.x) { claw.openClaw(); }

//        if (opmode.gamepad1.left_bumper) { claw.incrementalClaw(-1); }

//        else if (opmode.gamepad1.right_bumper) { claw.incrementalClaw(1); }

        wrist.incrementalWristFlip(getSign(-opmode.gamepad1.left_stick_y));

        if (opmode.gamepad1.dpad_left) {
            wrist.incrementalWristRotateTest(1);
        }
        else if (opmode.gamepad1.dpad_right) {
            wrist.incrementalWristRotateTest(-1);
        }

        if (opmode.gamepad1.dpad_up) { arm.incrementalArm(-1); }
        else if (opmode.gamepad1.dpad_down) { arm.incrementalArm(1); }

        opmode.telemetry.addData("Arm Pos: ", arm.arm.getPosition());
        opmode.telemetry.addData("Flip Wrist Pos: ", wrist.wristFlip.getPosition());
        opmode.telemetry.addData("Rotate Wrist Pos: ", wrist.wristRotate.getPosition());
        opmode.telemetry.addData("Claw Pos: ", claw.claw.getPosition());
        dashboardTelemetry.addData("Arm Pos: ", arm.arm.getPosition());
        dashboardTelemetry.addData("Flip Wrist Pos: ", wrist.wristFlip.getPosition());
        dashboardTelemetry.update();
        opmode.telemetry.update();
    }

    // Claw Subsystem Class
    public static class Claw {
        public Servo claw;
        public boolean isClawOpen = true;
        public static double clawClosedPosition = 0.4828;
        public static double clawOpenPosition = 0.0339;
        public static double clawIncrement = 0.001;

        public Claw() {}

        public void initialize(RobotHardware rHardware) {
            this.claw = rHardware.iClawServo;
        }

        // Toggles the claw between open and closed positions
        public void toggleClaw() {
            if (isClawOpen) {
                claw.setPosition(clawClosedPosition);
                isClawOpen = false;
            } else {
                claw.setPosition(clawOpenPosition);
                isClawOpen = true;
            }
        }

        public void closeClaw() {
            claw.setPosition(clawClosedPosition);
            isClawOpen = false;
        }

        public void openClaw() {
            claw.setPosition(clawOpenPosition);
            isClawOpen = true;
        }

        public void incrementalClaw(int sign) {
            claw.setPosition(claw.getPosition() + sign * clawIncrement);
        }
    }

    // Arm Subsystem Class
    public static class Arm {
        public Servo arm;
        public boolean isArmTransferring = true;
        public static double armIntakeHoverPosition = 0.1;
        public static double armIntakeGrabPosition = 0;
        public static double armTransferPosition = 0.46;
        public static double armIncrement = 0.001;

        public Arm() {}

        public void initialize(RobotHardware hardware) {
            this.arm = hardware.iArmServo;
        }

        public void setArmPosition(double position) {
            arm.setPosition(position);
        }

        public void incrementalArm(int sign) {
            arm.setPosition(arm.getPosition() + sign * armIncrement);
        }

        public void setArmTransfer() {
            setArmPosition(armTransferPosition);
            isArmTransferring = true;
        }

        public void setArmHover() {
            setArmPosition(armIntakeHoverPosition);
            isArmTransferring = false;
        }

        public void setArmGrab() {
            setArmPosition(armIntakeGrabPosition);
            isArmTransferring = false;
        }
    }

    // Wrist Subsystem Class
    public static class Wrist {
        public Servo wristRotate, wristFlip;
        public boolean isWristTransferring = true;
        public static double wristRotateTransferPosition = 0.5056;
        public static double wristFlipTransferPosition = 0.42;
        public static double wristRotateDefaultIntakePosition = 0.5378;
        public static double wristRotateIntakePerpendicularPosition = 0.163;
        public static double wristFlipIntakePosition = 0.96;
        public static double wristTestIncrement = 0.001;
        public static double wristActualIncrement = 0.02;

        public Wrist() {}

        public void initialize(RobotHardware hardware) {
            this.wristFlip = hardware.iWristServoF;
            this.wristRotate = hardware.iWristServoR;
        }

        public void setWristTransfer() {
            wristFlip.setPosition(wristFlipTransferPosition);
            wristRotate.setPosition(wristRotateTransferPosition);
            isWristTransferring = true;
        }

        public void setWristIntake() {
            wristFlip.setPosition(wristFlipIntakePosition);
            wristRotate.setPosition(wristRotateDefaultIntakePosition);
            isWristTransferring = false;
        }

        public void setFlipIntake() {
            wristFlip.setPosition(wristFlipIntakePosition);
            isWristTransferring = false;
        }

        public void setWristIntakePerpendicular() {
            wristFlip.setPosition(wristFlipIntakePosition);
            wristRotate.setPosition(wristRotateIntakePerpendicularPosition);
            isWristTransferring = false;
        }

        public void setWristCamera() {
            wristRotate.setPosition(cPortal.getServoRotation());
        }

        public void incrementalWristRotateTest(int sign) {
            wristRotate.setPosition(wristRotate.getPosition() + sign * wristTestIncrement);
        }

        public void incrementalWristRotateActual(int sign) {
            wristRotate.setPosition(wristRotate.getPosition() + sign * wristActualIncrement);
        }

        public void incrementalWristFlip(int sign) {
            wristFlip.setPosition(wristFlip.getPosition() + sign * wristTestIncrement);
        }


    }

    // math util functions
    public boolean isClose(double a, double b) {
        return Math.abs(a - b) < 0.001;
    }
    public int getSign(double input) {
        return input > 0 ? 1 : input == 0 ? 0 : -1;
    }

    public class IntakeHover implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            arm.setArmHover();
            wrist.setWristIntake();
            claw.openClaw();
            return false;
        }
    }

    public Action IntakeHover() {
        return new IntakeHover();
    }

    public class IntakePickup implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            arm.setArmGrab();
            return false;
        }
    }

    public Action IntakePickup() {
        return new IntakePickup();
    }

    public class IntakeClose implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            claw.closeClaw();
            return false;
        }
    }

    public Action IntakeClose() {return new IntakeClose();}

    public class IntakeTransfer implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            wrist.setWristTransfer();
            arm.setArmTransfer();
            return false;
        }
    }

    public Action IntakeTransfer() {
        return new IntakeTransfer();
    }

    public class ClawOpen implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            claw.openClaw();
            return false;
        }
    }

    public Action ClawOpen() {
        return new ClawOpen();
    }

    public class IntakeHoverPerpendicular implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            wrist.setWristIntakePerpendicular();
            arm.setArmHover();
            claw.openClaw();
            return false;
        }
    }

    public Action IntakeHoverPerpendicular() {
        return new IntakeHoverPerpendicular();
    }
}
