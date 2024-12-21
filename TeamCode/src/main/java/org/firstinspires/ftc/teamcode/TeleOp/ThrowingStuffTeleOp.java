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


@TeleOp(name="Throwing Stuff Teleop", group="Active TeleOps")
public class ThrowingStuffTeleOp extends OpMode {
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
        horizontalSlides.autoInitialize(this);
        intakeArm.initialize(this);
        verticalSlides.autoInitialize(this);
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
        //vincent doing nothing
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
//        intakeArm.cPortal.run(this);

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
        mecanum.operateTogglable();

        // gyro reset
        if (currentGamepad2.y && !previousGamepad2.y) { mecanum.resetNavx(); }

        // only PID
        verticalSlides.operateVincent();
        horizontalSlides.operateVincent();

        ////////////////////////////////////// GAMEPAD 1 CONTROLS /////////////////////////////////////


        // throw piece (hopefully, might need to tune timings)
        if (currentGamepad1.right_trigger >= 0.1 && !(previousGamepad1.right_trigger >= 0.1)) {
            runningActions.add(
                    new SequentialAction(
                            new InstantAction(() -> horizontalSlides.extendHalfway()),
                            new SleepAction(0),
                            new InstantAction(() -> intakeArm.wrist.setFlipIntake()),
                            new SleepAction(0),
                            new InstantAction(() -> intakeArm.arm.setArmHover()),
                            new SleepAction(0.04),
                            new InstantAction(() -> intakeArm.claw.openClaw())
                    )
            );
        }
        // grab piece
        else if (currentGamepad1.right_trigger < 0.1 && !(previousGamepad1.right_trigger < 0.1)) {
            runningActions.add(
                    new SequentialAction(
                            new InstantAction(() -> intakeArm.arm.setArmGrab()),
                            new SleepAction(0.15),
                            new InstantAction(() -> intakeArm.claw.closeClaw()),
                            new SleepAction(0.1),
                            new ParallelAction(
                                new InstantAction(() -> intakeArm.arm.setArmTransfer()),
                                new InstantAction(() -> intakeArm.wrist.setWristTransfer()),
                                new InstantAction(() -> horizontalSlides.retract())
                            )
                    )
            );
        }

        // transfer, then outtake throws forward
        if (currentGamepad1.a && !previousGamepad1.a) {
            runningActions.add(
                    new SequentialAction(
                            // transfer
                            new InstantAction(() -> scoringArm.claw.closeClaw()),
                            new SleepAction(0.08),
                            new InstantAction(() -> intakeArm.claw.openClaw()),

                            // wind up
                            new ParallelAction(
                                    new InstantAction(() -> scoringArm.wrist.setWristGrabClip()),
                                    new InstantAction(() -> scoringArm.arm.setArmGrabClip())
                            ),

                            // pause
                            new SleepAction(0.5),

                            // initiate
                            new ParallelAction(
                                    new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
                                    new InstantAction(() -> scoringArm.arm.setArmTransfer())
                            ),
                            new SleepAction(0.05), // probably tune this

                            // release
                            new InstantAction(() -> scoringArm.claw.openClaw())
                    )
            );
        }

        // transfer, then outtake throws upwards
        if (currentGamepad1.b && !previousGamepad1.b) {
            runningActions.add(
                    new SequentialAction(

                            // transfer
                            new InstantAction(() -> scoringArm.claw.closeClaw()),
                            new SleepAction(0.08),
                            new InstantAction(() -> intakeArm.claw.openClaw()),

                            // gain momentum
                            new InstantAction(() -> verticalSlides.raiseToPrepClip()),
                            new SleepAction(0.2), // probably tune this

                            // initiate
                            new InstantAction(() -> scoringArm.arm.setArmGrabClip()),
                            new InstantAction(() -> scoringArm.wrist.setWristGrabClip()),

                            new SleepAction(0.05), // probably tune this
                            // release
                            new InstantAction(() -> scoringArm.claw.openClaw()),

                            // return
                            new SleepAction(1),
                            new ParallelAction(
                                    new InstantAction(() -> verticalSlides.retract()),
                                    new InstantAction(() -> scoringArm.wrist.setWristTransfer()),
                                    new InstantAction(() -> scoringArm.arm.setArmTransfer())
                            )
                    )
            );
        }

        // stow all scoring (vertical slides, scoring arm)
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

        // intake wrist rotate
        if      (currentGamepad1.right_trigger >= 0.1 && currentGamepad1.dpad_right)  { intakeArm.wrist.incrementalWristRotateActual(-1); }
        else if (currentGamepad1.right_trigger >= 0.1 && currentGamepad1.dpad_left) { intakeArm.wrist.incrementalWristRotateActual(1); }


        ////////////////////////////////////// GAMEPAD 2 CONTROLS /////////////////////////////////////
        // none

    }
}

