package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Config
@Autonomous(name = "Drive Test", group = "Autonomous")
public class DriveTest extends LinearOpMode {

    public Pose2d startPose = new Pose2d(-7, -63.5, Math.toRadians(-90));
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        TrajectoryActionBuilder traj = drive.actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(-5, -36))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(30,-38))
                .splineToConstantHeading(new Vector2d(43,-10), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(45, -50))
                .splineToConstantHeading(new Vector2d(53, -10), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(57, -50))
                .splineToConstantHeading(new Vector2d(57, -10), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(57, -50))
                .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
                .strafeToConstantHeading(new Vector2d(26,-50))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(-3, -34), Math.toRadians(-90))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
                .strafeToConstantHeading(new Vector2d(26,-50))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(-0, -34), Math.toRadians(-90))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
                .strafeToConstantHeading(new Vector2d(26,-50))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(3, -34), Math.toRadians(-90))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
                .strafeToConstantHeading(new Vector2d(26,-50))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(6, -34), Math.toRadians(-90))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(40, -60));

        while(!isStarted() && !opModeIsActive()) {}
        waitForStart();
        if (isStopRequested()) return;

        Action trajectory = traj.build();

        Actions.runBlocking(
                new SequentialAction(
                        trajectory
                )
        );


    }
}
