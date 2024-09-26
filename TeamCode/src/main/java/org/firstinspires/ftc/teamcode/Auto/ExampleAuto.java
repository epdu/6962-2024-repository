package org.firstinspires.ftc.teamcode.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Config
@Autonomous(name = "Template Autoop", group = "Autonomous")
public class ExampleAuto extends LinearOpMode {
    // create subsystems
    @Override
    public void runOpMode() {
        // init()
        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        // defining trajectories
        TrajectoryActionBuilder traj1 = drive.actionBuilder(startPose)
                .lineToX(10)
                .turnTo(Math.toRadians(90));

        Action traj2 = traj1.fresh()
                .strafeTo(new Vector2d(15, 17))
                .waitSeconds(3)
                .lineToY(-2)
                .build();

        // init_loop()
        while(!isStarted() && !opModeIsActive()) {}

        // start()
        waitForStart();

        if (isStopRequested()) return;

        Action scoreTrajectory;
        scoreTrajectory = traj1.build();

        Actions.runBlocking(
                new SequentialAction(
                        scoreTrajectory, // Example of a drive action
                        (telemetryPacket) -> { // lambda function
                            telemetry.addLine("Action!");
                            telemetry.update();
                            return false; // Returning true causes the action to run again, returning false causes it to cease
                        },
                        new ParallelAction( // several actions being run in parallel
                                traj2, // Run second trajectory
                                (telemetryPacket) -> { // Run some action
                                    //motor1.setPower(1);
                                    return false;
                                }
                        )
                )
        );
    }
}
