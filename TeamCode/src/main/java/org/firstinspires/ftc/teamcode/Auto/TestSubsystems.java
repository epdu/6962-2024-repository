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
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Config
@Autonomous(name = "Test Subsystems", group = "Autonomous")
public class TestSubsystems extends LinearOpMode{

    @Override
    public void runOpMode() {
        VerticalSlides lift = new VerticalSlides();

        while (!isStarted() && !opModeIsActive()) {}

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
//                        lift.scoreClip()
                )
        );
    }
}
