package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Subsystems.HangSubsystem;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="HangSubsystem Test", group="Active TeleOps")
public class HangSubsystemTeleOp extends OpMode {
    private final HangSubsystem hangSubsystem = new HangSubsystem();
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    @Override
    public void init() {
        hangSubsystem.initialize(this);
        telemetry.addLine("Press Y to deploy, D-Pad Left to activate stage two.");
        telemetry.update();
    }

    @Override
    public void loop() {

        TelemetryPacket packet = new TelemetryPacket();
        List<Action> newActions = new ArrayList<>();

        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet)) { // actually running actions
                newActions.add(action); // if failed (run() returns true), try again
            }
        }
        runningActions = newActions;

        dash.sendTelemetryPacket(packet);

        if (gamepad1.y && !hangSubsystem.isDeployed()) {
            runningActions.add(hangSubsystem.getHangSequence());
        }

        if (gamepad1.dpad_left && hangSubsystem.isDeployed() && !hangSubsystem.isStageTwoActivated()) {
            runningActions.add(hangSubsystem.getHangSequenceTwo());
        }
    }
    @Override
    public void stop() {}
}
