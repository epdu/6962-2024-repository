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
@Autonomous(name = "Spline Auto", group = "Autonomous")
public class SplineTestAuto extends LinearOpMode{

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

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(scorePreloadX, scorePreloadY))
                .waitSeconds(1)
                .splineTo(new Vector2d(intake1X, intake1Y), Math.toRadians(90))
                .splineTo(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        while (!isStarted() && !opModeIsActive()) {
            telemetry.addLine("Initialized Spline Auto");
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