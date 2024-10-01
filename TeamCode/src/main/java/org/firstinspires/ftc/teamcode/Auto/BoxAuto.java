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

import java.util.Vector;

@Config
@Autonomous(name = "Box Auto", group = "Autonomous")
public class BoxAuto extends LinearOpMode{

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(36, -36, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        TrajectoryActionBuilder box = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(36, 36))
                .strafeTo(new Vector2d(-36, 36))
                .strafeTo(new Vector2d(-36,-36))
                .strafeTo(new Vector2d(36, -36));

        while (!isStarted() && !opModeIsActive()) {}

        waitForStart();

        if (isStopRequested()) return;

        Action boxDrill = box.build();

        Actions.runBlocking(
                new SequentialAction(
                        boxDrill
                )
        );
    }
}
