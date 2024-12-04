//package org.firstinspires.ftc.teamcode.TeleOp;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.InstantAction;
//import com.acmerobotics.roadrunner.ParallelAction;
//import com.acmerobotics.roadrunner.SequentialAction;
//import com.acmerobotics.roadrunner.SleepAction;
//import com.qualcomm.hardware.lynx.LynxModule;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.Gamepad;
//
//import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
//import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
//import org.firstinspires.ftc.teamcode.Subsystems.Intake;
//import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
//import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
//import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@TeleOp(name="RED Action-based Whole Robot Test", group="Active TeleOps")
//public class FullRobotActionTeleOp extends OpMode {
//    // Action stuff
//    private FtcDashboard dash = FtcDashboard.getInstance();
//    private List<Action> runningActions = new ArrayList<>();
//    private List<LynxModule> allHubs;
//
//    // optimizing stuff apparently?
//
//    final Gamepad currentGamepad1 = new Gamepad();
//    final Gamepad currentGamepad2 = new Gamepad();
//    final Gamepad previousGamepad1 = new Gamepad();
//    final Gamepad previousGamepad2 = new Gamepad();
//
//    // subsystems
//    private Mecanum mecanum                   = new Mecanum();
//    private HorizontalSlides horizontalSlides = new HorizontalSlides();
//    private VerticalSlides verticalSlides     = new VerticalSlides();
//    private Intake intake                     = new Intake();
//    private ScoringArm scoringArm             = new ScoringArm();
//    private CustomTimer timer                 = new CustomTimer();
//
//    private boolean onRedAlliance = true;
//
//    @Override
//    public void init() {
//        mecanum.initialize(this);
//        horizontalSlides.initialize(this);
//        intake.initialize(this, timer, onRedAlliance);
//        verticalSlides.initialize(this);
//        scoringArm.initialize(this);
//        allHubs = hardwareMap.getAll(LynxModule.class);
//        // apparently optimizes reading from hardware (ex: getCurrentPosition) and makes runtime a bit faster
//        for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
//    }
//
//    @Override
//    public void start() {
//        // to make sure arm doesn't spasm when pressing
//        scoringArm.arm.setArmStow();
//        scoringArm.wrist.setWristStow();
//        scoringArm.claw.closeClaw();
//        intake.stopIntaking();
//    }
//
//    @Override
//    public void loop() {
//        for (LynxModule hub : allHubs) {
//            hub.clearBulkCache();
//        }
//
//        // for rising edge detection (just google it)
//        previousGamepad1.copy(currentGamepad1);
//        previousGamepad2.copy(currentGamepad2);
//
//        currentGamepad1.copy(gamepad1);
//        currentGamepad2.copy(gamepad2);
//
//        TelemetryPacket packet = new TelemetryPacket();
//        List<Action> newActions = new ArrayList<>();
//
//        for (Action action : runningActions) {
//            action.preview(packet.fieldOverlay());
//            if (action.run(packet)) { // actually running actions
//                newActions.add(action); // if failed (run() returns true), try again
//            }
//        }
//        runningActions = newActions;
//
//        dash.sendTelemetryPacket(packet);
//
//        // field centric drive
//        // gamepad 1: dpad-down - gyro reset
//        // gamepad1: left-trigger > 0.5 - fastmode
//        mecanum.operateTogglable();
//
//        // no manual control, only PID
//        verticalSlides.operateVincent();
//
//        // no manual control, only PID
//        horizontalSlides.operateVincent();
//
//        // intaking
//        if (currentGamepad1.right_bumper && previousGamepad1.right_bumper) {
//            if (horizontalSlides.slidesMostlyRetracted) {
//                gamepad1.rumble(0.5, 0.5, 200);
//            }
//            else {
//                intake.intakePieces();
//            }
//        } else if (!currentGamepad1.right_bumper && !intake.flippedUp) {
//            intake.stopIntaking();
//        }
//
//        // reverse intake
//        if (currentGamepad1.x && !previousGamepad1.x) {
//            runningActions.add(new SequentialAction(
//                new InstantAction(() -> intake.reverse()),
//                new SleepAction(2),
//                new InstantAction(() -> intake.stopServos())
//            ));
//        }
//
//        // claw toggle
//        if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
//            scoringArm.claw.toggleClaw();
//        }
//
//        // macro prep high bucket scoring
//        if (currentGamepad2.a && !previousGamepad2.a) {
//            if (!horizontalSlides.slidesMostlyRetracted) {
//                gamepad2.rumble(0.5, 0.5, 250);
//            } else {
//                runningActions.add(
//                    new SequentialAction(
//                        new InstantAction(() -> verticalSlides.raiseToHighBucket()),
//                        new SleepAction(0.65),
//                        new ParallelAction(
//                            new InstantAction(() -> scoringArm.claw.closeClaw()),
//                            new InstantAction(() -> scoringArm.wrist.setWristScoringBucket()),
//                            new InstantAction(() -> scoringArm.arm.setArmScoreBucket())
//                        )
//                    )
//                );
//            }
//        }
//
//        // macro grab clip
//        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
//            runningActions.add(
//                new ParallelAction(
//                    new InstantAction(() -> verticalSlides.retract()),
//                    new InstantAction(() -> scoringArm.claw.openClaw()),
//                    new InstantAction(() -> scoringArm.wrist.setWristGrabClip()),
//                    new InstantAction(() -> scoringArm.arm.setArmGrabClip())
//                )
//            );
//        }
//
//        // macro prep score clip
//        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
//            runningActions.add(
//                new ParallelAction(
//                    new InstantAction(() -> scoringArm.claw.closeClaw()),
//                    new InstantAction(() -> verticalSlides.raiseToPrepClip()),
//                    new InstantAction(() -> scoringArm.wrist.setWristScoringClip()),
//                    new InstantAction(() -> scoringArm.arm.setArmScoreClip())
//                )
//            );
//        }
//
//
//        // retract slides and stow arm whenever claw opens
//        if (scoringArm.claw.isClawOpen && !scoringArm.arm.isArmTransferring) {
//            runningActions.add(new SequentialAction(
//                new SleepAction(0.25),
//                new ParallelAction(
//                    new InstantAction(() -> scoringArm.wrist.setWristStow()),
//                    new InstantAction(() -> scoringArm.arm.setArmStow())
//                ),
//                new InstantAction(() -> verticalSlides.retract())
//            ));
//        }
//
//        // deposit clip
//        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
//            runningActions.add(
//                new SequentialAction(
//                    new InstantAction(() -> verticalSlides.raiseToScoreClip()),
//                    new SleepAction(0.2),
//                    new InstantAction(() -> scoringArm.claw.openClaw())
//            ));
//        }
//
//        // auto transfer
//        if (currentGamepad2.b && !previousGamepad2.b) {
//            runningActions.add(
//                    new SequentialAction(
//                            new InstantAction(() -> scoringArm.claw.openClaw()),
//                            new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
//                            new SleepAction(0.15),
//                            new InstantAction(() -> scoringArm.arm.setArmTransfer()),
//                            new SleepAction(0.4),
//                            new InstantAction(() -> scoringArm.claw.closeClaw()),
//                            new SleepAction(0.15),
//                            new InstantAction(() -> scoringArm.arm.setArmStow()),
//                            new InstantAction(() -> scoringArm.wrist.setWristStow())
//                    ));
//        }
//
////        // for linkage extendo
////        if (gamepad1.right_trigger >= 0.1 && currentGamepad1.right_trigger != previousGamepad1.rightTrigger) {
////            runningActions.add(new SequentialAction(
////                    new InstantAction(() -> horizontalSlides.extendAdjustable(currentGamepad1.right_trigger)),
////                    new SleepAction(0), // potential need to add delay
////                    new InstantAction(() -> intake.intakePieces())
////            ));
////        } else if (!intake.flippedUp) {
////            runningActions.add(new SequentialAction(
////                    new InstantAction(() -> intake.stopIntaking()),
////                    new InstantAction(() -> horizontalSlides.retract())
////            ));
////        }
//    }
//}
