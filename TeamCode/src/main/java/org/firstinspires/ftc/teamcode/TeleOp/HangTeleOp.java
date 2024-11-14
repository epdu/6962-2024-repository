package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Subsystems.Hang;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Only Hang Test", group="Active TeleOps")
public class HangTeleOp extends OpMode {
    private final Hang hang = new Hang();
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    @Override
    public void init() {
        hang.initialize(this);
        telemetry.addLine("Use Gamepad 2 Left Joystick for manual control.");
        telemetry.update();
    }

    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();
        List<Action> newActions = new ArrayList<>();

        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (!action.run(packet)) { // Run each action; if incomplete, keep it in the list
                newActions.add(action);
            }
        }
        runningActions = newActions; // Update runningActions to keep only incomplete actions

        dash.sendTelemetryPacket(packet);

        // Manual control for CR servos using Gamepad 2 Left Joystick
        if (gamepad2.left_stick_y > 0.1 || gamepad2.left_stick_y < -0.1) { // check for joystick movement in either direction
            hang.runServos(gamepad2.left_stick_y); // will run at different amount of power depending on how far the joystick is moved
        } else {
            hang.stopServos(); // stop servos when joystick is unmoved
        }

        // Automated sequences
        if (gamepad1.y && !hang.isDeployed()) { // Trigger sequence if gamepad1 Y is pressed and hang is not deployed
            runningActions.add(hang.getHangSequence());
        }

        if (gamepad1.dpad_left && hang.isDeployed() && !hang.isStageTwoActivated()) {
            runningActions.add(hang.getHangSequenceTwo());
        }
    }
    @Override
    public void stop() {
        runningActions.clear();
    }
}
