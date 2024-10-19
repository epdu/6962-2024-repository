package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class IntakeArm {

    OpMode opmode;
    public Claw claw = new Claw();
    public Arm arm = new Arm();
    public Wrist wrist = new Wrist();
    private final RobotHardware rHardware = new RobotHardware();

    // constructor
    public IntakeArm() {}

    // Initializes the sub-subsystems
    public void initialize(OpMode opmode) {
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        claw.initialize(rHardware);
        arm.initialize(rHardware);
        wrist.initialize(rHardware);
    }

    // Operates the claw for Vincent configuration
    public void operateVincent() {
        if (opmode.gamepad1.left_bumper) {
            claw.toggleClaw();
        }


    }

    // Operates the test mode for controlling the claw, arm, and wrist
    public void operateTest() {
        // gamepad 2, all incrementals to find servo values first try
        if (opmode.gamepad2.left_bumper) { claw.incrementalClaw(-1); }
        else if (opmode.gamepad2.right_bumper) { claw.incrementalClaw(1); }

        wrist.incrementalWristFlip(getSign(-opmode.gamepad2.left_stick_y));
        wrist.incrementalWristRotateTest(getSign(opmode.gamepad2.left_stick_x));

        if (opmode.gamepad2.dpad_up) { arm.incrementalArm(-1); }
        else if (opmode.gamepad2.dpad_down) { arm.incrementalArm(1); }

        if (opmode.gamepad2.y) {wrist.setWristIntake();}
        if (opmode.gamepad2.x) {wrist.setWristTransfer();}

        // Adding telemetry data for debugging
        opmode.telemetry.addData("Arm Pos: ", arm.arm.getPosition());
        opmode.telemetry.addData("Flip Wrist Pos: ", wrist.wristFlip.getPosition());
        opmode.telemetry.addData("Rotate Wrist Pos: ", wrist.wristRotate.getPosition());
        opmode.telemetry.addData("Claw Pos: ", claw.claw.getPosition());
    }

    // Claw Subsystem Class
    public static class Claw {
        public Servo claw;
        public boolean isClawOpen = true;
        public static double clawClosedPosition = 0.7389;
        public static double clawOpenPosition = 0.5011;
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
        public boolean isArmHovering = false;
        public static double armIntakeHoverPosition = 0;
        public static double armIntakeGrabPosition = 0;
        public static double armTransferPosition = 0;
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
            isArmHovering = false;
        }

        public void setArmHover() {
            setArmPosition(armIntakeHoverPosition);
            isArmTransferring = false;
            isArmHovering = true;
        }

        public void setArmGrab() {
            setArmPosition(armIntakeGrabPosition);
            isArmTransferring = false;
            isArmHovering = false;
        }

        public void toggleHoverGrab() {
            if (isArmHovering) {
                setArmGrab();
            }
            else {
                setArmHover();
            }
        }
    }

    // Wrist Subsystem Class
    public static class Wrist {
        public Servo wristRotate, wristFlip;
        public boolean isWristTransferring = true;
        public static double wristRotateTransferPosition = 0;
        public static double wristFlipTransferPosition = 0;
        public static double wristRotateDefaultIntakePosition = 0;
        public static double wristFlipIntakePosition = 0;
        public static double wristTestIncrement = 0.001;
        public static double wristActualIncrement = 0.005;

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

}
