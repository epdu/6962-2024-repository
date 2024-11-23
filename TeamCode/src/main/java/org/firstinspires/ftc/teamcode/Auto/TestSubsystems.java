package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.NewVerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Config
@Autonomous(name = "Test Subsystems", group = "Autonomous")
public class TestSubsystems extends LinearOpMode{

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();
        IntakeArm intakeArm = new IntakeArm();
        HorizontalSlides horizontalSlides = new HorizontalSlides();
        NewVerticalSlides newVertSlides = new NewVerticalSlides();

        TrajectoryActionBuilder move3 = drive.actionBuilder(startPose)
                .afterTime(0, () -> {
                    new SequentialAction(
                        // Jayden testing new slides stuff for autonomous
                        newVertSlides.BuiltInRTPExtend(), // my big concern is that slides will not even move
                        scoringArm.ArmDropClip(), // this is here to test if the arm moves immediately
                        new SleepAction(3),
                        newVertSlides.BuiltInRTPRetract()
                    );
                });

        while (!isStarted() && !opModeIsActive()) {
            intakeArm.initialize(this);
            verticalSlides.initialize(this);
            scoringArm.initialize(this);
            horizontalSlides.initialize(this);
            newVertSlides.autonomousInitialize(this);

            Actions.runBlocking(
                    new ParallelAction(
                            horizontalSlides.HorizontalRetract(),
                            scoringArm.StowWholeArm(),
                            intakeArm.IntakeTransfer()
                    )
            );
        }

        waitForStart();

        if (isStopRequested()) return;

        Action TestSubsystemTrajectory = move3.build();

        Actions.runBlocking(
                new SequentialAction(
                        TestSubsystemTrajectory
//                        verticalSlides.LiftUpToClip(),
//                        new SleepAction(1),
//                        verticalSlides.Retract()
                )
        );
    }
}
