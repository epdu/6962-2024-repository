package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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

import org.firstinspires.ftc.teamcode.Subsystems.CameraPortal;
import org.firstinspires.ftc.teamcode.Subsystems.Hang;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.Util.CameraCVPipeline;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name="A Solo Full Robot TeleOp", group="Active TeleOps")
public class SoloFullRobotTeleOp extends OpMode {
    private OpMode opmode;
    // Action stuff
    private FtcDashboard dash = FtcDashboard.getInstance();
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
    private Hang hang                         = new Hang();
    private CameraCVPipeline pipeline          = new CameraCVPipeline();


    private MultipleTelemetry dashboardTelemetry = new MultipleTelemetry(telemetry, dash.getTelemetry());

    private CameraPortal cameraPortal         = new CameraPortal();

    private boolean onRedAlliance = true;

    @Override
    public void init() {
        elapsedtime = new ElapsedTime();
        mecanum.initialize(this);
        horizontalSlides.teleInitialize(this);
        intakeArm.initialize(this);
        verticalSlides.teleInitialize(this);
        scoringArm.initialize(this);
        hang.initialize(this);
        cameraPortal.initialize(this);
        allHubs = hardwareMap.getAll(LynxModule.class);
        // apparently optimizes reading from hardware (ex: getCurrentPosition) and makes runtime a bit faster
        for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
        mecanum.navxReset180();
    }

    @Override
    public void init_loop() {
        if (gamepad1.b) {
            cameraPortal.setRed();
        }
        else if (gamepad1.x) {
            cameraPortal.setBlue();
        }
        telemetry.addData("camera alliance color: ", cameraPortal.cameraColor);
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

        hang.ptoServoSustain();
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
//        horizontalSlides.operateVincent();
        // camera
        cameraPortal.run(this);

        // toggle between field centric and robot centric
        if (currentGamepad1.left_stick_button && !previousGamepad1.left_stick_button && currentGamepad1.right_stick_button) {
            mecanum.toggleCentric();
        }

        // mecanum drive
        mecanum.operateTogglable();

        // gyro reset
        if (currentGamepad2.y && !previousGamepad2.y) { mecanum.resetNavx(); }

        // only PID
        horizontalSlides.operateVincent();
        verticalSlides.operateVincent();
        hang.operateL2();

        ////////////////////////////////////// GAMEPAD 1 CONTROLS /////////////////////////////////////

        // gives scoring priority over intaking
        if (scoringArm.arm.armPos == ScoringArm.Arm.STATE.TRANSFERRING) {
            // horizontal slides extend 100%, intake arm grab, open intake claw
            if (currentGamepad1.right_trigger >= 0.3 && !(previousGamepad1.right_trigger >= 0.3)) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> horizontalSlides.extend()),
                                new SleepAction(0.05),
                                new InstantAction(() -> intakeArm.wrist.setFlipIntake()),
                                new InstantAction(() -> intakeArm.arm.setArmHover()),
                                new SleepAction(0.3),
                                new InstantAction(() -> intakeArm.claw.openClaw())
                        )
                );
            }
            // grab piece, then retract intake
            else if (currentGamepad1.right_trigger < 0.3 && !(previousGamepad1.right_trigger < 0.3)) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> intakeArm.arm.setArmGrab()),
                                new SleepAction(0.15),
                                new InstantAction(() -> intakeArm.claw.closeClaw()),
                                new SleepAction(0.1),
                                new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                                new InstantAction(() -> intakeArm.wrist.setWristTransfer()),
                                new SleepAction(0.05),
                                new InstantAction(() -> horizontalSlides.retract())
//                                new InstantAction(() -> scoringArm.arm.setArmInitPosition()),
//                                new SleepAction(0.35),
//                                new InstantAction(() -> scoringArm.arm.setArmTransfer())
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
                            new SleepAction(0.1),
                            new InstantAction(() -> horizontalSlides.retract())
                    )
            );
        }

        // macro prep grab clip
        if (currentGamepad1.left_trigger >= 0.5  && !(previousGamepad1.left_trigger >= 0.5)) {
            runningActions.add(
                    new ParallelAction(
                            new InstantAction(() -> verticalSlides.retract()),
                            new InstantAction(() -> scoringArm.claw.openClaw()),
                            new InstantAction(() -> scoringArm.wrist.setWristGrabClip()),
                            new InstantAction(() -> scoringArm.arm.setArmGrabClip())
                    )
            );
        }
        // macro grab clip and prep score clip
        else if (currentGamepad1.left_trigger < 0.5  && !(previousGamepad1.left_trigger < 0.5)) {
            runningActions.add(
                    new SequentialAction(
                            new InstantAction(() -> scoringArm.claw.closeClaw()),
                            new SleepAction(0.2),
                            new ParallelAction(
                                    new InstantAction(() -> verticalSlides.raiseToPrepClip()),
                                    new InstantAction(() -> scoringArm.wrist.setWristScoringClip()),
                                    new InstantAction(() -> scoringArm.arm.setArmScoreClip())
                            )
                    )
            );
        }

        // when not intaking, bumpers do scoring stuff
        if (intakeArm.arm.isArmTransferring) {
            // if horizontal slides not all the way retracted, press to scooch
            if (currentGamepad1.x && !previousGamepad1.x) {
                scoringArm.arm.scoochForward();
            }
            // macro prep high bucket scoring
            if ((currentGamepad1.right_bumper && !previousGamepad1.right_bumper) || (currentGamepad1.b && !previousGamepad1.b)) {
                runningActions.add(
                        new SequentialAction(
                                // transfer
                                new InstantAction(() -> scoringArm.claw.closeClaw()),
                                new SleepAction(0.08),
                                new InstantAction(() -> intakeArm.claw.openClaw()),

                                // lift up to high bucket
                                new InstantAction(() -> verticalSlides.raiseToHighBucket()),
                                new InstantAction(() -> scoringArm.wrist.setWristScoringBucket()),
                                new SleepAction(0.8),
                                new InstantAction(() -> scoringArm.arm.setArmScoreBucket())
                        )
                );
            }

            // open claw, and stow all scoring (vertical slides, scoring arm)
            if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper && scoringArm.arm.armPos != ScoringArm.Arm.STATE.SCORING_CLIP) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> scoringArm.claw.openClaw()),
                                new SleepAction(0.3),
                                new ParallelAction(
                                        new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
                                        new InstantAction(() -> scoringArm.arm.setArmTransfer())
                                ),
                                new InstantAction(() -> verticalSlides.retract())
                        )
                );
            }
            // slam score clip
            else if ((currentGamepad1.left_bumper && !previousGamepad1.left_bumper) || (currentGamepad1.a && !previousGamepad1.a)) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> verticalSlides.slamToScoreClip()),
                                new SleepAction(0.2),
                                new InstantAction(() -> scoringArm.claw.openClaw()),
                                new InstantAction(() -> verticalSlides.retract()),
                                new InstantAction(() -> scoringArm.arm.setArmTransfer()),
                                new InstantAction(() -> scoringArm.wrist.setWristTransfer())
                        ));
            }
        }
        // when intaking, bumpers rotate intake claw
        else {
            if      (currentGamepad1.dpad_right || currentGamepad1.right_bumper)  { intakeArm.wrist.incrementalWristRotateActual(-1); }
            else if (currentGamepad1.dpad_left || currentGamepad1.left_bumper) { intakeArm.wrist.incrementalWristRotateActual(1); }
        }


        // camera auto rotate
        if (currentGamepad1.right_trigger >= 0.1 && currentGamepad1.dpad_down && !previousGamepad1.dpad_down) { cameraPortal.setWristCamera();}

        ////////////////////////////////////// GAMEPAD 2 CONTROLS /////////////////////////////////////

        // PTO release
//        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down && currentGamepad2.a) {
//            hang.ptoServoRelease();
//        }

        // transfer, then prep to drop spec behind robot
        if (currentGamepad2.x && !previousGamepad2.x) {
            runningActions.add(
                    new SequentialAction(
                            new InstantAction(() -> scoringArm.claw.closeClaw()),
                            new SleepAction(0.08),
                            new InstantAction(() -> intakeArm.claw.openClaw()),
                            new ParallelAction(
                                    new InstantAction(() -> scoringArm.wrist.setWristScoringBucket()),
                                    new InstantAction(() -> scoringArm.arm.setArmScoreBucket())
                            )
                    )
            );
        }

        // drop spec behind robot and return
        if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
            runningActions.add(
                    new SequentialAction(
                            new InstantAction(() -> scoringArm.claw.openClaw()),
                            new SleepAction(0.3),
                            new ParallelAction(
                                    new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
                                    new InstantAction(() -> scoringArm.arm.setArmTransfer())
                            )
                    )
            );
        }

        // intake wrist rotate
        if      (currentGamepad2.right_trigger >= 0.1)  { intakeArm.wrist.incrementalWristRotateActual(-1); }
        else if (currentGamepad2.left_trigger >= 0.1) { intakeArm.wrist.incrementalWristRotateActual(1); }

//        dashboardTelemetry.addData("hang takeover vertical slides bool: ", hangToggleBool);
        // loop time
        dashboardTelemetry.addData("elapsed time (loop time)", elapsedtime.milliseconds());
        telemetry.addData("Color Detection: ", cameraPortal.cameraColor);
        telemetry.addData("is field centric? ", mecanum.isFieldCentric);
//        dashboardTelemetry.addData("Camera Color:", cPortal.cameraColor);
        dashboardTelemetry.update();
        elapsedtime.reset();

    }
}
