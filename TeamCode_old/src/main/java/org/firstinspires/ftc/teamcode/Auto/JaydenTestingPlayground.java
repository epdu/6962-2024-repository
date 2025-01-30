package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.AutoOnlySlides;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.NewHorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.NewVerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Config
@Autonomous(name = "Jayden's Slides Testing", group = "Autonomous")
public class JaydenTestingPlayground extends LinearOpMode{

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        AutoOnlySlides vertSlides = new AutoOnlySlides();
        NewVerticalSlides newVertSlides = new NewVerticalSlides();

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(12, 0))
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    // Jayden testing new slides stuff for autonomous
                                    vertSlides.Extend(),
                                    new SleepAction(1),
                                    vertSlides.Retract()

                            )
                    );
                });
        TrajectoryActionBuilder move2 = drive.actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(0, 0));

        Action EXTEND_AND_RETRACT =
                new SequentialAction(
                        new InstantAction(() -> vertSlides.Extend()),
                        new SleepAction(2),
                        new InstantAction(() -> vertSlides.Retract())
                );

        while (!isStarted() && !opModeIsActive()) {
            vertSlides.initialize(this);
        }

        waitForStart();

        if (isStopRequested()) return;

        Action Traj1 = move1.build();
        Action Traj2 = move2.build();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(
                                Traj1,
                                new SleepAction(2),
//                                EXTEND_AND_RETRACT,
                                Traj2
                        ),
                        vertSlides.Loop()
                )
        );
    }
}

