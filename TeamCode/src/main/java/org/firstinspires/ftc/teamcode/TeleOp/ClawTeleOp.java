package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.OTOSManager;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@TeleOp(name="Only Claw Test", group="Active TeleOps")
public class ClawTeleOp extends OpMode {
    // creating subsystems
    private Claw claw = new Claw();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        claw.initialize(this);
        telemetry.addLine("Gamepad 2:");
        telemetry.addLine("Left Bumper - toggle claw open and close");
        telemetry.addLine("D-Pad Up and Down - incremental arm");
        telemetry.addLine("D-Pad Left and Right- incremental wrist");
        telemetry.addLine("A - arm score position");
        telemetry.addLine("B - arm stow position");
        telemetry.addLine("X - arm transfer position");
        telemetry.addLine("Left Stick Button - wrist horizontal");
        telemetry.addLine("Right Stick Button - wrist vertical");
        telemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */

    /*
     * allows driver to indicate that the IMU should not be reset
     * used when starting TeleOp after auto or if program crashes in the middle of match
     * relevant because of field-centric controls
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
        claw.operateTest();
        // Gamepad 2:
        // Left Bumper - toggle claw open and close
        // D-Pad Up - incremental arm up (might be reversed on first try)
        // D-Pad Down - incremental arm down
        // D-Pad Left - incremental wrist left (might be reversed on first attempt)
        // D-Pad Right - incremental wrist right
        // A - arm score position
        // B - arm stow position
        // X - arm transfer position
        // Left Stick Button - wrist horizontal
        // Right Stick Button - wrist vertical
        telemetry.addData("Arm Transferring: ", claw.isArmTransferring);
        telemetry.addData("Claw Open: ", claw.isClawOpen);
        telemetry.addData("Wrist Vertical", claw.isWristVertical);
        telemetry.addData("Arm Pos: ",claw.telemetryArmPos());
        telemetry.addData("Wrist Pos: ",claw.telemetryWristPos());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        claw.shutdown();
    }
}



