package org.firstinspires.ftc.teamcode.Auto;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@Config
@Autonomous(name = "4+0", group = "Autonomous")
public class FourSpecimenAuto extends LinearOpMode {

    public static double startX = 19;
    public static double startY = -63.5;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = 5;
    public static double scorePreloadY = -34;
    public static double coord1X = 30;
    public static double coord1Y = -34;
    public static double push1X = 47;
    public static double push1Y = -10;
    public static double zone1X = 47;
    public static double zone1Y = -50;
    public static double push2X = 57;
    public static double push2Y = -10;
    public static double zone2X = 57;
    public static double zone2Y = -50;
    public static double pickupX = 24;
    public static double pickupY = -48;
    public static double scoreX = 9;
    public static double scoreY = -34;
    public static double parkX = 40;
    public static double parkY = -60;

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX, startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();
        IntakeArm intakeArm = new IntakeArm();
        HorizontalSlides horizontalSlides = new HorizontalSlides();

        TrajectoryActionBuilder traj = drive.actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(scorePreloadX, scorePreloadY))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(coord1X, coord1Y))
                .splineToConstantHeading(new Vector2d(push1X, push1Y), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(zone1X, zone1Y))
                .splineToConstantHeading(new Vector2d(push2X, push2Y), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(zone2X, zone2Y))
                .strafeToLinearHeading(new Vector2d(pickupX, pickupY), Math.toRadians(-45))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(-90))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(pickupX, pickupY), Math.toRadians(-45))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(-90))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(pickupX, pickupY), Math.toRadians(-45))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(-90))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(parkX, parkY), Math.toRadians(90))
                ;

        while (!isStarted() && !opModeIsActive()){
            verticalSlides.initialize(this);
            horizontalSlides.initialize(this);
            scoringArm.initialize(this);
            intakeArm.initialize(this);

            Actions.runBlocking(
                    new ParallelAction(
                            intakeArm.IntakeTransfer(),
                            scoringArm.StowWholeArm()
                    )
            );
        }

        waitForStart();

        if (isStopRequested()) return;

        Action fourSpecimenTrajectory = traj.build();

        Actions.runBlocking(
                fourSpecimenTrajectory
        );
    }
}
