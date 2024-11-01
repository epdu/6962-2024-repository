package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name="FINAL Full Robot TeleOp", group="Active TeleOps")
public class FinalFullRobotActionTeleOp extends OpMode {
    // Action stuff
    private FtcDashboard dash = FtcDashboard.getInstance();
    private Telemetry dashboardTelemetry = dash.getTelemetry();
    private List<Action> runningActions = new ArrayList<>();
    private List<LynxModule> allHubs;

    // optimizing stuff apparently?
    final Gamepad currentGamepad1 = new Gamepad();
    final Gamepad currentGamepad2 = new Gamepad();
    final Gamepad previousGamepad1 = new Gamepad();
    final Gamepad previousGamepad2 = new Gamepad();
    private ElapsedTime elapsedtime;

    // subsystems
    private Mecanum mecanum                   = new Mecanum();
    private HorizontalSlides horizontalSlides = new HorizontalSlides();
    private VerticalSlides verticalSlides     = new VerticalSlides();
    private IntakeArm intakeArm               = new IntakeArm();
    private ScoringArm scoringArm             = new ScoringArm();

    private boolean onRedAlliance = true;

    @Override
    public void init() {
        elapsedtime = new ElapsedTime();
        mecanum.initialize(this);
        horizontalSlides.initialize(this);
        intakeArm.initialize(this);
        verticalSlides.initialize(this);
        scoringArm.initialize(this);
        allHubs = hardwareMap.getAll(LynxModule.class);
        // apparently optimizes reading from hardware (ex: getCurrentPosition) and makes runtime a bit faster
        for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
    }

    @Override
    public void start() {
        elapsedtime.reset();
        // to make sure arms don't spasm when out of pos
        scoringArm.arm.setArmTransfer();
        scoringArm.wrist.setWristTransfer();
        scoringArm.claw.openClaw();

        intakeArm.arm.setArmTransfer();
        intakeArm.wrist.setWristTransfer();
        intakeArm.claw.closeClaw();
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
        // gamepad 1: dpad-down - gyro reset
        // gamepad1: left-trigger > 0.5 - fastmode
        mecanum.operateFieldCentricVincent();

        // manual control in case auto messes up, else only PID
        if (currentGamepad2.right_trigger > 0.1 && currentGamepad2.left_trigger > 0.1) {
            verticalSlides.operateFix();
        }
        else {
            verticalSlides.operateVincent();
        }

        // manual control in case auto messes up, else only PID
        if (currentGamepad2.right_trigger > 0.1 && currentGamepad2.left_trigger > 0.1) {
            horizontalSlides.operateFix();
        }
        else {
            horizontalSlides.operateVincent();
        }


        ////////////////////////////////////// GAMEPAD 1 CONTROLS /////////////////////////////////////

        if (scoringArm.arm.armPos == ScoringArm.Arm.STATE.TRANSFERRING) {
            // horizontal slides extend and intake arm hover
            if (currentGamepad1.right_trigger >= 0.01 && !(previousGamepad1.right_trigger >= 0.01)) {
                runningActions.add(
                    new SequentialAction(
                        new InstantAction(() -> horizontalSlides.extendHalfway()),
                        new SleepAction(0.1),
                        new ParallelAction(
                            new InstantAction(() -> intakeArm.arm.setArmHover()),
                            new InstantAction(() -> intakeArm.wrist.setWristIntake())
                        ),
                        new SleepAction(0.2),
                        new InstantAction(() -> intakeArm.claw.openClaw())
                    )
                );
            }
            // horizontal slides retract and intake arm transfer
            else if (currentGamepad1.right_trigger < 0.1 && !(previousGamepad1.right_trigger < 0.1)) {
                runningActions.add(
                    new SequentialAction(
                        new InstantAction(() -> intakeArm.claw.closeClaw()),
                        new InstantAction(() -> intakeArm.wrist.setWristTransfer()),
                        new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                        new SleepAction(0.2),
                        new InstantAction(() -> horizontalSlides.retract())
                    )
                );
            }
        }
        else if (!horizontalSlides.slidesMostlyRetracted) {
            runningActions.add(
                new ParallelAction(
                    new InstantAction(() -> intakeArm.claw.closeClaw()),
                    new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                    new InstantAction(() -> intakeArm.wrist.setWristTransfer()),
                    new SleepAction(0.2),
                    new InstantAction(() -> horizontalSlides.retract())
                )
            );
        }

        // auto prep intake arm for transfer (vincent proofing in case let go of both, but press right trigger immediately after)
        if (horizontalSlides.slidesMostlyRetracted && !currentGamepad1.right_bumper && !(currentGamepad1.right_trigger >= 0.1)) {
            runningActions.add(
                    new ParallelAction(
                            new InstantAction(() -> intakeArm.claw.closeClaw()),
                            new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                            new InstantAction(() -> intakeArm.wrist.setWristTransfer())
                    )
            );
        }

        if (currentGamepad1.a && !previousGamepad1.a) {
            runningActions.add(
                    new SequentialAction(
                            new InstantAction(() -> intakeArm.claw.closeClaw()),
                            new InstantAction(() -> intakeArm.wrist.setWristTransfer()),
                            new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                            new SleepAction(0.2),
                            new InstantAction(() -> horizontalSlides.retract())
                    )
            );
        }


        // intake wrist rotate
        if      (currentGamepad1.dpad_right)  { intakeArm.wrist.incrementalWristRotateActual(-1); }
        else if (currentGamepad1.dpad_left) { intakeArm.wrist.incrementalWristRotateActual(1); }

        // intake claw toggle
        if (currentGamepad1.right_trigger > 0.1) {
            if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> intakeArm.claw.openClaw()),
                                new InstantAction(() -> intakeArm.wrist.setFlipIntake()),
                                new SleepAction(!intakeArm.claw.isClawOpen ? 0.5 : 0.1),
                                new InstantAction(() -> intakeArm.arm.setArmGrab())
                        )
                );
            }
            else if (!currentGamepad1.right_bumper && previousGamepad1.right_bumper) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> intakeArm.claw.closeClaw()),
                                new SleepAction(0.5),
                                new InstantAction(() -> intakeArm.arm.setArmHover()),
                                new InstantAction(() -> intakeArm.wrist.setWristIntake())
                        )
                );
            }
        }

        // intake claw open (for emergencies)
        if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper) {
            runningActions.add(
                    new InstantAction(() -> intakeArm.claw.openClaw())
            );
        }


        ////////////////////////////////////// GAMEPAD 2 CONTROLS /////////////////////////////////////

        // scoring claw toggle
        if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) { scoringArm.claw.toggleClaw(); }

        // scoring arm grab off floor toggle
//        if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
//            runningActions.add(
//                    new SequentialAction(
//                            new InstantAction(() -> verticalSlides.retract()),
//                            new InstantAction(() -> scoringArm.wrist.setWristGrabClipFloorHover()),
//                            new InstantAction(() -> scoringArm.claw.openClaw())
//                    )
//            );
//        }
//        else if (!currentGamepad2.right_bumper && previousGamepad2.right_bumper) {
//            runningActions.add(
//                    new SequentialAction(
//                            new InstantAction(() -> scoringArm.arm.setArmGrabClipFloor()),
//                            new SleepAction(0.2),
//                            new InstantAction(() ->  scoringArm.claw.closeClaw()),
//                            new SleepAction(0.2),
//                            new InstantAction(() -> scoringArm.arm.setArmGrabClipFloorHover())
//                    )
//            );
//        }

        // macro prep high bucket scoring
        if (currentGamepad2.x && !previousGamepad2.x) {
            runningActions.add(
                new SequentialAction(
                    new InstantAction(() -> scoringArm.claw.closeClaw()),
                    new InstantAction(() -> verticalSlides.raiseToHighBucket()),
                    new InstantAction(() -> scoringArm.wrist.setWristScoringBucket()),
                    new SleepAction(0.6),
                    new ParallelAction(
                        new InstantAction(() -> scoringArm.arm.setArmScoreBucket())
                    )
                )
            );
        }

        // macro grab clip
        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
            runningActions.add(
                new ParallelAction(
                    new InstantAction(() -> verticalSlides.retract()),
                    new InstantAction(() -> scoringArm.claw.openClaw()),
                    new InstantAction(() -> scoringArm.wrist.setWristGrabClip()),
                    new InstantAction(() -> scoringArm.arm.setArmGrabClip())
                )
            );
        }

        // macro prep score clip
        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
            runningActions.add(
                new ParallelAction(
                    new InstantAction(() -> scoringArm.claw.closeClaw()),
                    new InstantAction(() -> verticalSlides.raiseToPrepClip()),
                    new InstantAction(() -> scoringArm.wrist.setWristScoringClip()),
                    new InstantAction(() -> scoringArm.arm.setArmScoreClip())
                )
            );
        }

        // auto retract slides and stow arm whenever claw opens
        if (scoringArm.claw.isClawOpen && scoringArm.arm.armPos == ScoringArm.Arm.STATE.SCORING) {
            runningActions.add(new SequentialAction(
                    new SleepAction(0.5),
                    new ParallelAction(
                        new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
                        new InstantAction(() -> scoringArm.arm.setArmTransfer())
                    ),
                    new InstantAction(() -> verticalSlides.retract())
            ));
        }

        // deposit clip
        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
            runningActions.add(
                    new SequentialAction(
                        new InstantAction(() -> verticalSlides.slamToScoreClip()),
                        new SleepAction(0.2),
                        new InstantAction(() -> scoringArm.claw.openClaw())
                    ));
        }

        // auto transfer
        if (currentGamepad2.b && !previousGamepad2.b) {
            runningActions.add(
                    new SequentialAction(
                        // both arms prep
                        new ParallelAction(
                            new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
                            new InstantAction(() -> scoringArm.arm.setArmTransfer()),
                            new InstantAction(() -> intakeArm.wrist.setWristTransfer()),
                            new SleepAction(0.1),
                            new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                            new InstantAction(() -> scoringArm.claw.openClaw())
                        ),

                        // acutally transfer
//                            new InstantAction(() -> )
                        new InstantAction(() -> scoringArm.claw.closeClaw()),
                        new SleepAction(0.1),
                        new InstantAction(() -> intakeArm.claw.openClaw())
                    ));
        }

        // loop time
        dashboardTelemetry.addData("elapsed time (loop time)", elapsedtime.milliseconds());
        dashboardTelemetry.update();
        elapsedtime.reset();

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
