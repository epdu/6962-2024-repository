package org.firstinspires.ftc.teamcode.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Subsystems.SubsystemsCombined;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;

@Config
@Autonomous(name = "Preload Auto", group = "Autonomous")
public class PreloadAuto extends LinearOpMode{
    public static double startX = 7;
    public static double startY = -63.75;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = 0;
    public static double scorePreloadY = -42;
    public static double prepPickupX = 65;
    public static double prepPickupY = -54;
    public static double pickupX = 65;
    public static double pickupY = -60;
    public static double parkX = 36;
    public static double parkY = -60;
    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX,startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        HorizontalSlides horizontalSlides = new HorizontalSlides();
        IntakeArm intakeArm = new IntakeArm();
        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();
        SubsystemsCombined subsystems = new SubsystemsCombined();

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(scorePreloadX, scorePreloadY))
                .afterTime(0.5, () -> {
                    subsystems.PrepClipSequence();
                })
//                .waitSeconds(1)
                .afterTime( 1.5, () -> {
                    new SequentialAction(
                            subsystems.ScoreClipSequence(),
                            new SleepAction(0.2),
                            subsystems.StowScoring()
                    );
                })
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90))
//                .afterTime(3, () -> {
//                    arm.PickupClip();
//                })
                .strafeTo(new Vector2d(pickupX, pickupY))
                .waitSeconds(1)
//                .afterTime(8, () -> {
//                    arm.ScoreClip();
//                    slides.LiftUpToClip();
//                })
                .strafeToLinearHeading(new Vector2d(scorePreloadX+10, scorePreloadX), Math.toRadians(-90))
                .waitSeconds(1)
//                .afterTime(11, () -> {
//                    new SequentialAction(
//                            slides.LiftScoreClip(),
//                            new SleepAction(0.2),
//                            arm.StowClose(),
//                            slides.Retract()
//                    );
//                })
                .strafeTo(new Vector2d(parkX, parkY));

        while(!isStarted() && !opModeIsActive()) {
            horizontalSlides.initialize(this);
            intakeArm.initialize(this);
            verticalSlides.initialize(this);
            scoringArm.initialize(this);
            subsystems.initialize(this, horizontalSlides, intakeArm, verticalSlides, scoringArm);

            telemetry.addLine("Initialized Preload Auto");
            telemetry.update();
            //run on init NO MOTORS
            Actions.runBlocking(
                    new ParallelAction(
                            scoringArm.StowWholeArm()
                    )
            );
        }
        waitForStart();
        if (isStopRequested()) return;

        Action preloadAutoTrajectory = move1.build();

        Actions.runBlocking(
                new SequentialAction(
                        preloadAutoTrajectory
                )
        );

    }
}