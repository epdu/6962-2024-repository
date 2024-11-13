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
        telemetry.addLine("Press Y to deploy, D-Pad Left to activate stage two.");
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
        hang.operateTest(); // Manually control hang for testing purposes..

        if (gamepad1.y && !hang.isDeployed()) {
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
