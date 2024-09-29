package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;

@Config
public class ScoringArm {

    // Main ScoringArm Class that creates instances of the subsystems
    OpMode opmode;
    public Claw claw = new Claw();
    public Arm arm = new Arm();
    public Wrist wrist = new Wrist();
    private RobotHardware rHardware = new RobotHardware();

    // constructor
    public ScoringArm() {}

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
        if (opmode.gamepad2.left_bumper) {
            claw.toggleClaw();
        }
    }

    // Operates the test mode for controlling the claw, arm, and wrist
    public void operateTest() {
        if (opmode.gamepad2.left_bumper) {
            claw.toggleClaw();
        }

        wrist.incrementalWristL(getSign(opmode.gamepad2.left_stick_y));
        wrist.incrementalWristR(getSign(opmode.gamepad2.right_stick_y));

        wrist.incrementalWristTurn(getSign(opmode.gamepad1.left_stick_y));
        wrist.incrementalWristRotate(getSign(opmode.gamepad1.left_stick_x));

        if (opmode.gamepad2.dpad_up) {
            arm.incrementalArm(1);
        } else if (opmode.gamepad2.dpad_down) {
            arm.incrementalArm(-1);
        }

        if (opmode.gamepad2.a) {
            arm.scoreArm();
            wrist.setWristScoring();
        } else if (opmode.gamepad2.b) {
            arm.stowArm();
            wrist.setWristStow();
        } else if (opmode.gamepad2.x) {
            arm.transferArm();
            wrist.setWristTransfer();
        }

        // Adding telemetry data for debugging
        opmode.telemetry.addData("Arm Pos: ", arm.arm.getPosition());
        opmode.telemetry.addData("Right Wrist Pos: ", wrist.wristR.getPosition());
        opmode.telemetry.addData("Left Wrist Pos: ", wrist.wristL.getPosition());
    }

    // Claw Subsystem Class
    public class Claw {
        public Servo claw;
        public boolean isClawOpen = true;
        private static final double clawClosedPosition = 0.245;
        private static final double clawOpenPosition = 0.5;

        public Claw() {}

        public void initialize(RobotHardware rHardware) {
            this.claw = rHardware.claw;
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

        public void openClaw() {
            claw.setPosition(clawClosedPosition);
            isClawOpen = false;
        }

        public void closeClaw() {
            claw.setPosition(clawOpenPosition);
            isClawOpen = true;
        }
    }

    // Arm Subsystem Class
    public class Arm {
        public Servo arm;
        public boolean isArmTransferring = true;
        private static final double armScoringPosition = 0.3;
        private static final double armStowPosition = 0.7;
        private static final double armTransferPosition = 0.8;
        private static final double armIncrement = 0.02;

        public Arm() {}
        public void initialize(RobotHardware hardware) {
            this.arm = hardware.armServo;
        }

        public void setArmPosition(double position) {
            arm.setPosition(position);
        }

        // Incremental arm movement function
        public void incrementalArm(int sign) {
            arm.setPosition(arm.getPosition() + sign * armIncrement);
        }

        public void scoreArm() {
            setArmPosition(armScoringPosition);
            isArmTransferring = false;
        }

        public void stowArm() {
            setArmPosition(armStowPosition);
            isArmTransferring = false;
        }

        public void transferArm() {
            setArmPosition(armTransferPosition);
            isArmTransferring = true;
        }

        public double telemetryArmPos() {
            return arm.getPosition();
        }
    }

    // Wrist Subsystem Class
    public class Wrist {
        public Servo wristL, wristR;
        public boolean isWristTransferring = true;
        private static final double wristLTransferPosition = 0.25;
        private static final double wristRTransferPosition = 0;
        private static final double wristLStowPosition = 0.5;
        private static final double wristRStowPosition = 0.5;
        private static final double wristLScorePosition = 0.75;
        private static final double wristRScorePosition = 0.75;
        private static final double wristIncrement = 0.02;

        public Wrist() {}
        public void initialize(RobotHardware hardware) {
            this.wristL = hardware.cWristServoL;
            this.wristR = hardware.cWristServoR;
        }

        // Sets wrist to transfer position
        public void setWristTransfer() {
            wristL.setPosition(wristLTransferPosition);
            wristR.setPosition(wristRTransferPosition);
            isWristTransferring = true;
        }

        // Sets wrist to stow position
        public void setWristStow() {
            wristL.setPosition(wristLStowPosition);
            wristR.setPosition(wristRStowPosition);
            isWristTransferring = false;
        }

        // Sets wrist to scoring position
        public void setWristScoring() {
            wristL.setPosition(wristLScorePosition);
            wristR.setPosition(wristRScorePosition);
            isWristTransferring = false;
        }

        // Incremental wrist rotation (both servos rotate in same direction)
        public void incrementalWristRotate(int sign) {
            wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
            wristL.setPosition(wristL.getPosition() + sign * wristIncrement);
        }

        // Incremental wrist turn (servos rotate in opposite directions)
        public void incrementalWristTurn(int sign) {
            wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
            wristL.setPosition(wristL.getPosition() - sign * wristIncrement);
        }

        public void incrementalWristL(int sign) {
            wristL.setPosition(wristL.getPosition() + sign * wristIncrement);
        }

        public void incrementalWristR(int sign) {
            wristR.setPosition(wristR.getPosition() + sign * wristIncrement);
        }

        public double telemetryWristRPos() {
            return wristR.getPosition();
        }
        public double telemetryWristLPos() {
            return wristR.getPosition();
        }
    }

    public void shutdown() {}
}
