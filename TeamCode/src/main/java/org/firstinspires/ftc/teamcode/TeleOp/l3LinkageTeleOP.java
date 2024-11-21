package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Subsystems.l3Linkage;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="l3 Linkage", group="Active TeleOps")
public class l3LinkageTeleOP extends OpMode{
    private final l3Linkage l3Linkage = new l3Linkage();
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    @Override public void init() {
        l3Linkage.initialize(this);
        telemetry.addLine("Press X to run l3 Linkage");
        telemetry.update();
    }
    @Override public void loop(){

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

        l3Linkage.operateTest();

//        if (gamepad2.x) {
//            runningActions.add(l3Linkage.l3LinkServo());
//            new SleepAction(1);
//            runningActions.add(l3Linkage.l3LinkDrive());
//            runningActions.add(l3Linkage.l3LinkVSlides());
//        }
    }
    @Override
    public void stop() {}
}



