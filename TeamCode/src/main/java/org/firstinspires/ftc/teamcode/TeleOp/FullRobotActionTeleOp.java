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
import com.qualcomm.robotcore.hardware.Gamepad;

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
    private List<LynxModule> allHubs;

    // optimizing stuff apparently?

    final Gamepad currentGamepad1 = new Gamepad();
    final Gamepad currentGamepad2 = new Gamepad();
    final Gamepad previousGamepad1 = new Gamepad();
    final Gamepad previousGamepad2 = new Gamepad();

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
        allHubs = hardwareMap.getAll(LynxModule.class);
        // apparently optimizes reading from hardware (ex: getCurrentPosition) and makes runtime a bit faster
        for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
    }

    @Override
    public void start() {
        // to make sure arm doesn't spasm when pressing
        scoringArm.arm.stowArm();
        scoringArm.claw.closeClaw();
        intake.stopIntaking();
    }

    @Override
    public void loop() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        // for rising edge detection (just google it)
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

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
        if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper) {
            scoringArm.claw.toggleClaw();
        }

        // macro prep high bucket scoring
        if (currentGamepad1.a && !previousGamepad1.a) {
            if (!horizontalSlides.slidesMostlyRetracted) {
                currentGamepad1.rumble(0.5, 0.5, 250);
            } else {
                runningActions.add(
                    new ParallelAction(
                        new InstantAction(() -> scoringArm.claw.closeClaw()),
                        new InstantAction(() -> verticalSlides.raiseToHighBucket()),
                        new InstantAction(() -> scoringArm.wrist.setWristScoringBucket()),
                        new InstantAction(() -> scoringArm.arm.scoreArm())
                    )
                );
            }
        }

        // macro grab clip
        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
            runningActions.add(
                    new ParallelAction(
                            new InstantAction(() -> scoringArm.claw.openClaw()),
                            new InstantAction(() -> scoringArm.wrist.setWristGrabClip()),
                            new InstantAction(() -> scoringArm.arm.grabClipArm())
                    )
            );
        }

        // macro prep score clip
        if (currentGamepad1.y && !previousGamepad1.y) {
            runningActions.add(
                    new ParallelAction(
                            new InstantAction(() -> scoringArm.claw.closeClaw()),
                            new InstantAction(() -> verticalSlides.raiseToPrepClip()),
                            new InstantAction(() -> scoringArm.wrist.setWristScoringClip()),
                            new InstantAction(() -> scoringArm.arm.scoreArm())
                    )
            );
        }


        // retract slides and stow arm whenever claw opens
        if (scoringArm.claw.isClawOpen && !scoringArm.arm.isArmTransferring) {
            runningActions.add(new SequentialAction(
                    new SleepAction(0.3),
                    new ParallelAction(
                            new InstantAction(() -> scoringArm.wrist.setWristStow()),
                            new InstantAction(() -> scoringArm.arm.stowArm())
                    ),
                    new InstantAction(() -> verticalSlides.retract())
            ));
        }

        // auto transfer
        if (currentGamepad1.b && !previousGamepad1.b) {
            // haha funny haptic feedback when pressing button at wrong time
//            if (!horizontalSlides.slidesRetracted || !intake.flippedUp) {
//                currentGamepad1.rumble(0.5, 0.5, 250);
//                runningActions.add(
//                        new ParallelAction(
//                                new InstantAction(() -> horizontalSlides.retract()),
//                                new InstantAction(() -> intake.stopIntaking()),
//                                new InstantAction(() -> scoringArm.arm.stowArm()),
//                                new InstantAction(() -> scoringArm.wrist.setWristStow())
//                        ));
//            } else {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> scoringArm.claw.openClaw()),
                                new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
                                new SleepAction(0.15),
                                new InstantAction(() -> scoringArm.arm.transferArm()),
                                new SleepAction(0.4),
                                new InstantAction(() -> scoringArm.claw.closeClaw()),
                                new SleepAction(0.15),
                                new InstantAction(() -> scoringArm.arm.stowArm()),
                                new InstantAction(() -> scoringArm.wrist.setWristStow())
                        ));
//            }
        }


        // intaking
        if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper && intake.flippedUp) {
            if (horizontalSlides.slidesMostlyRetracted) {
                gamepad1.rumble(0.5, 0.5, 200);
            }
            else {
                intake.intakePieces();
            }
        } else if (!intake.flippedUp) {
            intake.stopIntaking();
        }
        if (currentGamepad1.x && !previousGamepad1.x) {
            runningActions.add(new SequentialAction(
                    new InstantAction(() -> intake.reverse()),
                    new SleepAction(2),
                    new InstantAction(() -> intake.stopServos())
            ));
        }

//        // for linkage extendo
//        if (gamepad1.right_trigger >= 0.1 && currentGamepad1.right_trigger != previousGamepad1.rightTrigger) {
//            runningActions.add(new SequentialAction(
//                    new InstantAction(() -> horizontalSlides.extendAdjustable(currentGamepad1.right_trigger)),
//                    new SleepAction(0), // potential need to add delay
//                    new InstantAction(() -> intake.intakePieces())
//            ));
//        } else if (!intake.flippedUp) {
//            runningActions.add(new SequentialAction(
//                    new InstantAction(() -> intake.stopIntaking()),
//                    new InstantAction(() -> horizontalSlides.retract())
//            ));
//        }
    }
}
