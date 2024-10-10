package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@Config
@Autonomous(name = "Sample Auto", group = "Autonomous")
public class SampleOnlyAuto extends LinearOpMode{

    public static double startX = 8;
    public static double startY = -63.75;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = 0;
    public static double scorePreloadY = -42;
    public static double afterPreloadX = 0;
    public static double afterPreloadY = -46;
    public static double intake1X = -48;
    public static double intake1Y = -43;
    public static double scoreBucketX = -57;
    public static double scoreBucketY = -57;
    public static double intake2X = -57;
    public static double intake2Y = -43;
    public static double prepParkX = -36;
    public static double prepParkY = -12;
    public static double parkX = -21;
    public static double parkY = -12;

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX, startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
//        VerticalSlides lift = new VerticalSlides();
//        ScoringArm scoringArm = new ScoringArm();

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(scorePreloadX, scorePreloadY))
                .afterTime(1, () -> {
//                    lift.prepClip();
//                    scoringArm.clip();
                })
                .waitSeconds(1)
                .afterTime(2, () -> {
//                    lift.scoreClip();
                })
                .afterTime(2.1, () -> {
//                    scoringArm.clawOpen();
                })
                //.strafeTo(new Vector2d(afterPreloadX, afterPreloadY))
                .turnTo(Math.toRadians(90))
                .afterTime(3, () -> {
//                    scoringArm.stow();
//                    lift.retractSlides();
                })
                .afterTime(6.2, () -> {
//                    lift.pickupClip();
//                    scoringArm.clip();
                })
                .afterTime(7.4, () -> {
//                    scoringArm.clawClose();
//                    lift.prepClip();
                })
                .waitSeconds(0.5)
                .afterTime(11.5, () -> {
//                    lift.scoreClip();
                })
                .afterTime(11.6, () -> {
//                    scoringArm.clawOpen();
                })
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(intake1X, intake1Y))
                .afterTime(12.6, () -> {
//                    lift.retractSlides();
//                    scoringArm.stow();
                })
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(scoreBucketX, scoreBucketY))
                .turnTo(Math.toRadians(45))
                .afterTime(17.2, () -> {
//                    lift.scoreBucket();
//                    scoringArm.bucket();
                })
                .waitSeconds(0.5)
                .afterTime(18, () -> {
//                    scoringArm.clawOpen();
                })
                .strafeToConstantHeading(new Vector2d(intake2X, intake2Y))
                .turnTo(Math.toRadians(95))
                .afterTime(18.7, () -> {
//                    lift.retractSlides();
//                    scoringArm.stow();
                })
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(scoreBucketX, scoreBucketY))
                .turnTo(Math.toRadians(45))
                .afterTime(21, () -> {
//                    lift.scoreBucket();
//                    scoringArm.bucket();
                })
                .waitSeconds(0.5)
                .afterTime(21.8, () -> {
//                    scoringArm.clawOpen();
                })
                .strafeToConstantHeading(new Vector2d(intake2X, intake2Y))
                .turnTo(Math.toRadians(125))
                .afterTime(22.7, () -> {
//                    lift.retractSlides();
//                    scoringArm.stow();
                })
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(scoreBucketX, scoreBucketY))
                .turnTo(Math.toRadians(45))
                .afterTime(24.9, () -> {
//                    lift.scoreBucket();
//                    scoringArm.bucket();
                })
                .waitSeconds(0.5)
                .afterTime(25.8, () -> {
//                    scoringArm.clawOpen();
                })
                .turnTo(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(prepParkX, prepParkY))
                .afterTime(26.7, () -> {
//                    lift.retractSlides();
//                    scoringArm.stow();
                })
                .strafeTo(new Vector2d(parkX, parkY));

//        Actions.runBlocking(claw.clawClose()); //runs on init

        while (!isStarted() && !opModeIsActive()) {
            telemetry.addLine("Initialized Sample Auto");
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        Action autoTrajectoryTest = move1.build();

        Actions.runBlocking(
                new SequentialAction(
                        autoTrajectoryTest
                )
        );
    }
}
