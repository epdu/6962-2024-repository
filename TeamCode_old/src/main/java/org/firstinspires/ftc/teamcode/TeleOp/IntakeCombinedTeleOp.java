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
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Util.CameraCVPipeline;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Intake Combined TeleOp", group="Active TeleOps")
public class IntakeCombinedTeleOp extends OpMode {
    private OpMode opmode;

    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();
    private List<LynxModule> allHubs;
    final Gamepad currentGamepad1 = new Gamepad();
    final Gamepad previousGamepad1 = new Gamepad();
    private IntakeArm intakeSubsystem = new IntakeArm();
    private ScoringArm scoringSubsystem = new ScoringArm();
    private CameraPortal cameraPortal = new CameraPortal();
    private CameraCVPipeline cameraCVPipeline = new CameraCVPipeline();
    private HorizontalSlides horizontalSlides = new HorizontalSlides();

    private MultipleTelemetry dashboardTelemetry = new MultipleTelemetry(telemetry, dash.getTelemetry());

    private ElapsedTime elapsedTime;

    @Override
    public void init() {
        elapsedTime = new ElapsedTime();
        intakeSubsystem.initialize(this);
        cameraPortal.initialize(this);
        horizontalSlides.teleInitialize(this);
        scoringSubsystem.initialize(this);

        allHubs = hardwareMap.getAll(LynxModule.class);
        // apparently optimizes reading from hardware (ex: getCurrentPosition) and makes runtime a bit faster
        for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
    }

    @Override
    public void init_loop() {
        if (currentGamepad1.b) {
            cameraPortal.changeColor();
        }
    }

    @Override
    public void start() {
        elapsedTime.reset();
        // to make sure arms don't spasm when out of pos
        scoringSubsystem.arm.setArmTransfer();
        scoringSubsystem.wrist.setWristTransfer();
        scoringSubsystem.claw.openClaw();

        intakeSubsystem.arm.setArmTransfer();
        intakeSubsystem.wrist.setWristTransfer();
        intakeSubsystem.claw.closeClaw();

    }

    @Override
    public void loop() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        // for rising edge detection (just google it)
        previousGamepad1.copy(currentGamepad1);

        currentGamepad1.copy(gamepad1);


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
        horizontalSlides.operateVincent();

        // actions

        if (scoringSubsystem.arm.armPos == ScoringArm.Arm.STATE.TRANSFERRING) {
            // horizontal slides extend 100%, intake arm grab, open intake claw
            if (currentGamepad1.right_trigger >= 0.1 && !(previousGamepad1.right_trigger >= 0.1)) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> horizontalSlides.extend()),
                                new SleepAction(0.05),
                                new InstantAction(() -> intakeSubsystem.wrist.setFlipIntake()),
                                new InstantAction(() -> intakeSubsystem.arm.setArmHover()),
                                new SleepAction(0.3),
                                new InstantAction(() -> intakeSubsystem.claw.openClaw())
                        )
                );
            }
            // grab piece, then retract intake
            else if (currentGamepad1.right_trigger < 0.1 && !(previousGamepad1.right_trigger < 0.1)) {
                runningActions.add(
                        new SequentialAction(
                                new InstantAction(() -> intakeSubsystem.arm.setArmGrab()),
                                new SleepAction(0.15),
                                new InstantAction(() -> intakeSubsystem.claw.closeClaw()),
                                new SleepAction(0.1),
                                new InstantAction(() -> intakeSubsystem.arm.setArmTransfer()),
                                new InstantAction(() -> intakeSubsystem.wrist.setWristTransfer()),
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
                            new InstantAction(() -> intakeSubsystem.claw.closeClaw()),
                            new InstantAction(() -> intakeSubsystem.arm.setArmTransfer()),
                            new InstantAction(() -> intakeSubsystem.wrist.setWristTransfer()),
                            new SleepAction(0.1),
                            new InstantAction(() -> horizontalSlides.retract())
                    )
            );
        }

        if (currentGamepad1.right_trigger >= 0.1 && currentGamepad1.dpad_down) { cameraPortal.setWristCamera();}


        telemetry.addData("Detected Color:", cameraPortal.cameraColor);
    }
}
