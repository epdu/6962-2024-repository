package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringCombined;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@TeleOp(name="RED Action-based Whole Robot Test", group="Active TeleOps")
public class FullRobotActionTeleOp extends OpMode {
    // Action stuff
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    // optimizing stuff apparently?
    List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

    // subsystems
    private Mecanum mecanum                   = new Mecanum();
    private HorizontalSlides horizontalSlides = new HorizontalSlides();
    private VerticalSlides verticalSlides     = new VerticalSlides();
    private Intake intake                     = new Intake();
    private ScoringArm scoringArm             = new ScoringArm();
    private CustomTimer timer                 = new CustomTimer();

    private boolean onRedAlliance = true;

    @Override
    public void init() {
        mecanum.initialize(this);
        horizontalSlides.initialize(this);
        intake.initialize(this, timer, onRedAlliance);
        verticalSlides.initialize(this);
        scoringArm.initialize(this, timer);

        // apparently optimizes reading from hardware (ex: getCurrentPosition) and makes runtime a bit faster
        for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
    }

    @Override
    public void loop() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

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

        // field centric drive
        // dpad-down - gyro reset
        // left-trigger > 0.5 - slow mode
        mecanum.operateFieldCentricVincent();

        // no manual control, only PID
        verticalSlides.operateVincent();

        // right trigger manual control (very fast, be careful, can force to slow down if necessary)
        horizontalSlides.operateVincent();

        // claw toggle
        if (gamepad1.left_bumper) {
            runningActions.add(new InstantAction(() -> scoringArm.claw.toggleClaw()));
        }

        // macro prep high bucket scoring
        if (gamepad1.y) {
            runningActions.add(
                    new ParallelAction(
                        new InstantAction(() -> verticalSlides.raiseToHighBucket()),
                        new InstantAction(() -> scoringArm.wrist.setWristScoringBucket()),
                        new InstantAction(() -> scoringArm.arm.scoreArm())
                    )
            );
        }

        // retract slides and stow arm whenever claw opens
        if (scoringArm.claw.isClawOpen && !scoringArm.arm.isArmTransferring) {
            runningActions.add(new ParallelAction(
                    new InstantAction(() -> verticalSlides.retract()),
                    new InstantAction(() -> scoringArm.wrist.setWristStow()),
                    new InstantAction(() -> scoringArm.arm.stowArm())
            ));
        }

        // could potentially fill up queue with duplicates

        // auto transfer
        if (gamepad1.b) {
            // haha funny haptic feedback when pressing button at wrong time
            if (!horizontalSlides.horizontalSlidesRetracted || !intake.flippedUp || scoringArm.arm.isArmTransferring) {gamepad1.rumble(0.5, 0.5, 250);}
            else {
                runningActions.add(
                    new SequentialAction(
                        new ParallelAction(
                            new InstantAction(() -> scoringArm.claw.openClaw()),
                            new InstantAction(() -> scoringArm.arm.transferArm())
                        ),
                        new SleepAction(0.6), // this could stop the entire robot, but
                        new InstantAction(() -> scoringArm.claw.closeClaw()),
                        new SleepAction(0.2),
                        new ParallelAction(
                            new InstantAction(() -> scoringArm.arm.stowArm()),
                            new InstantAction(() -> scoringArm.wrist.setWristStow())
                        )
                    ));
            }
        }


        // intaking
        if (gamepad1.right_bumper && intake.flippedUp) {
            runningActions.add(new InstantAction(() -> intake.intakePieces()));
        } else if (!intake.flippedUp) {
            runningActions.add(new InstantAction(() -> intake.stopIntaking()));
        }
        if (gamepad1.x) {
            runningActions.add(new SequentialAction(
                    new InstantAction(() -> intake.reverse()),
                    new SleepAction(2),
                    new InstantAction(() -> intake.stopServos())
            ));
        }

    }

}
