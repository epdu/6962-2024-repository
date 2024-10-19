package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;

@TeleOp(name="Only Scoring Arm Test", group="Active TeleOps")
public class ScoringArmTeleOp extends OpMode {
    // creating subsystems
    private ScoringArm scoringArm = new ScoringArm();
    private CustomTimer timer = new CustomTimer();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        scoringArm.initialize(this);
        telemetry.addLine("Gamepad 2: incrementals for tuning");
        telemetry.addLine("Left and Right Bumper - toggle claw open and close");
        telemetry.addLine("D-Pad Up and Down - incremental arm");
        telemetry.addLine("Left Joystick Y - incremental left servo (might be reversed)");
        telemetry.addLine("Right Joystick Y - incremental right servo");
        telemetry.addLine("\nGamepad 1: set pos, only after gamepad 2 incrementals");
        telemetry.addLine("Right Bumper - claw toggle");
        telemetry.addLine("Left Joystick Y - incremental wrist turning");
        telemetry.addLine("Left Joystick X - incremental wrist rotation");
        telemetry.addLine("A - whole arm score position");
        telemetry.addLine("B - whole arm stow position");
        telemetry.addLine("X - whole arm transfer position");
        telemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        scoringArm.operateTest();
        // Gamepad 2:
        // Left Bumper - incremental open claw
        // Right Bumper - incremental close claw
        // Left Joystick Y - incremental left servo
        // Right Joystick Y - incremental right servo
        // D-Pad Up - incremental arm up
        // D-Pad Down - incremental arm down

        // Gamepad 1:
        // Left Joystick Y - wrist turn
        // Left Joystick X - wrist rotate
        // Y - whole arm score position
        // B - whole arm stow position
        // X - whole arm transfer position

        telemetry.addData("Arm Transferring: ", scoringArm.arm.isArmTransferring);
        telemetry.addData("Claw Open: ", scoringArm.claw.isClawOpen);
        telemetry.addData("Wrist Transferring", scoringArm.wrist.isWristTransferring);
        telemetry.addData("Arm Pos: ", scoringArm.arm.telemetryArmPos());
        telemetry.addData("Wrist R Pos: ", scoringArm.wrist.telemetryWristRPos());
        telemetry.addData("Wrist L Pos: ",scoringArm.wrist.telemetryWristLPos());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {}
}
