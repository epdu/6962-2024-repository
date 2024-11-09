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
import org.firstinspires.ftc.teamcode.Subsystems.Hang;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name="Solo Full Robot TeleOp", group="Active TeleOps")
public class SoloFullRobotTeleOp extends OpMode {
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
    private Hang hang                         = new Hang();

    private boolean onRedAlliance = true;

    @Override
    public void init() {
        elapsedtime = new ElapsedTime();
        mecanum.initialize(this);
        horizontalSlides.initialize(this);
        intakeArm.initialize(this);
        verticalSlides.initialize(this);
        scoringArm.initialize(this);
        hang.initialize(this);
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
        // gamepad1: left-trigger > 0.5 - fastmode
        mecanum.operateFieldCentricVincent();

        // gyro reset
        if (currentGamepad1.left_stick_button && !previousGamepad1.left_stick_button) { mecanum.resetNavx(); }

        // manual control in case auto messes up, else only PID
        if (currentGamepad2.right_trigger > 0.1 && currentGamepad2.left_trigger > 0.1) { verticalSlides.operateFix(); }
        else { verticalSlides.operateVincent(); }

        // manual control in case auto messes up, else only PID
        if (currentGamepad2.right_trigger > 0.1 && currentGamepad2.left_trigger > 0.1) { horizontalSlides.operateFix(); }
        else { horizontalSlides.operateVincent(); }


        ////////////////////////////////////// GAMEPAD 1 CONTROLS /////////////////////////////////////

        // horizontal slides extend, intake arm grab, open intake claw
        if (currentGamepad1.right_trigger >= 0.1 && !(previousGamepad1.right_trigger >= 0.1)) {
            runningActions.add(
                    new SequentialAction(
                            new InstantAction(() -> horizontalSlides.extendHalfway()),
                            new SleepAction(0.15),
                            new InstantAction(() -> intakeArm.wrist.setFlipIntake()),
                            new InstantAction(() -> intakeArm.arm.setArmHover()),
                            new SleepAction(0.25),
                            new InstantAction(() -> intakeArm.claw.openClaw())
                    )
            );
        }
        // intake claw close, then horizontal slides retract and intake arm transfer
        else if (currentGamepad1.right_trigger < 0.1 && !(previousGamepad1.right_trigger < 0.1)) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> intakeArm.arm.setArmGrab()),
                                new SleepAction(0.2),
                                new InstantAction(() -> intakeArm.claw.closeClaw()),
                                new SleepAction(0.1),
                                new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                                new SleepAction(0.05), // possibly unnecessary
                                new InstantAction(() -> intakeArm.wrist.setWristTransfer()),
                                new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                                new SleepAction(0.3),
                                new InstantAction(() -> horizontalSlides.retract())
                        )
                );
            }

        // intake wrist rotate
        if      (currentGamepad1.right_trigger >= 0.1 && currentGamepad1.dpad_right)  { intakeArm.wrist.incrementalWristRotateActual(-1); }
        else if (currentGamepad1.right_trigger >= 0.1 && currentGamepad1.dpad_left) { intakeArm.wrist.incrementalWristRotateActual(1); }

        // macro grab clip
        if (currentGamepad1.x && !previousGamepad1.x) {
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
        if (currentGamepad1.y && !previousGamepad1.y) {
            runningActions.add(
                    new SequentialAction(
                        new InstantAction(() -> intakeArm.claw.openClaw()),
                        new InstantAction(() -> scoringArm.claw.closeClaw()),
                        new SleepAction(0.1),
                        new ParallelAction(
                            new InstantAction(() -> verticalSlides.raiseToPrepClip()),
                            new InstantAction(() -> scoringArm.wrist.setWristScoringClip()),
                            new InstantAction(() -> scoringArm.arm.setArmScoreClip())
                        )
                    )
            );
        }

        // slam score clip
        if (currentGamepad1.a && !previousGamepad1.a) {
            runningActions.add(
                    new SequentialAction(
                            new InstantAction(() -> verticalSlides.slamToScoreClip()),
                            new SleepAction(0.2),
                            new InstantAction(() -> scoringArm.claw.openClaw())
                    ));
        }


        // macro prep high bucket scoring
        if (currentGamepad1.b && !previousGamepad1.b) {
            runningActions.add(
                    new SequentialAction(
                        // both arms prep
                        new ParallelAction(
                            new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
                            new InstantAction(() -> scoringArm.arm.setArmTransfer()),
                            new InstantAction(() -> intakeArm.wrist.setWristTransfer()),
                            new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                            new InstantAction(() -> scoringArm.claw.openClaw())
                        ),

                        // acutally transfer
                        new InstantAction(() -> scoringArm.claw.closeClaw()),
                        new SleepAction(0.08),
                        new InstantAction(() -> intakeArm.claw.openClaw()),

                        // lift up to high bucket
                        new InstantAction(() -> verticalSlides.raiseToHighBucket()),
                        new InstantAction(() -> scoringArm.wrist.setWristScoringBucket()),
                        new SleepAction(0.6),
                        new InstantAction(() -> scoringArm.arm.setArmScoreBucket())
                    )
            );
        }

        // open claw, and stow all scoring (vertical slides, scoring arm)
        if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper) {
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

        //hang activation / reverse --NOT TESTED--
        if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
            runningActions.add(
                    new InstantAction(() -> hang.getHangSequence())
            );
        }

        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
            runningActions.add(
                    new InstantAction(() -> hang.reverseHangSequence())
            );
        }

        ////////////////////////////////////// GAMEPAD 2 CONTROLS /////////////////////////////////////
        /// all backup fixing stuff
        // none rn?


        // loop time
        dashboardTelemetry.addData("elapsed time (loop time)", elapsedtime.milliseconds());
        dashboardTelemetry.update();
        elapsedtime.reset();

    }
}
