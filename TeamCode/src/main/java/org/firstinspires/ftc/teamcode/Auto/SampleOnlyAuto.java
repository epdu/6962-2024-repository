package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.config.Config;
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

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;

@Config
@Autonomous(name = "1+3 Auto", group = "Autonomous")
public class SampleOnlyAuto extends LinearOpMode{

    public static double startX = -8;
    public static double startY = -63.75;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = 0;
    public static double scorePreloadY = -42;
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
        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();
        HorizontalSlides horizontalSlides = new HorizontalSlides();
        IntakeArm intakeArm = new IntakeArm();

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    verticalSlides.LiftUpToClip(),
                                    scoringArm.ArmScoreClip()
                            )
                    );
                })
                .waitSeconds(1)
                .strafeTo(new Vector2d(scorePreloadX, scorePreloadY))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    verticalSlides.SlamScoreClip(),
                                    scoringArm.StowWholeArm(),
                                    verticalSlides.Retract()
                            )
                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(intake1X, intake1Y), Math.toRadians(180))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
//                                    horizontalSlides.HorizontalExtend(),
                                    intakeArm.IntakeHover()
                            )
                    );
                })
                .waitSeconds(1)
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    intakeArm.IntakePickup(),
                                    intakeArm.IntakeTransfer()
//                                    horizontalSlides.HorizontalRetract()

                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.WholeArmTransfer(),
                                    intakeArm.ClawOpen(),
                                    new SleepAction(0.2),
                                    verticalSlides.LiftUpToHighBucket(),
                                    scoringArm.ArmScoreBucket()
                            )
                    );
                })
                .waitSeconds(0.5)
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.StowWholeArm(),
                                    verticalSlides.Retract(),
//                                    horizontalSlides.HorizontalExtend(),
                                    intakeArm.IntakeHover()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y), Math.toRadians(95))
                .waitSeconds(1)
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    intakeArm.IntakePickup(),
                                    intakeArm.IntakeTransfer()
//                                    horizontalSlides.HorizontalRetract()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.WholeArmTransfer(),
                                    intakeArm.ClawOpen(),
                                    new SleepAction(0.2),
                                    verticalSlides.LiftUpToHighBucket(),
                                    scoringArm.ArmScoreBucket()
                            )
                    );
                })
                .waitSeconds(0.5)
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.StowWholeArm(),
                                    verticalSlides.Retract(),
//                                    horizontalSlides.HorizontalExtend(),
                                    intakeArm.IntakeHover()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y), Math.toRadians(125))
                .waitSeconds(1)
                .afterTime(0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    intakeArm.IntakePickup(),
                                    intakeArm.IntakeTransfer()
//                                    horizontalSlides.HorizontalRetract()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.WholeArmTransfer(),
                                    intakeArm.ClawOpen(),
                                    new SleepAction(0.2),
                                    verticalSlides.LiftUpToHighBucket(),
                                    scoringArm.ArmScoreBucket()
                            )
                    );
                })
                .waitSeconds(0.5)
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.StowWholeArm(),
                                    verticalSlides.Retract()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(prepParkX, prepParkY), Math.toRadians(90))
                .strafeTo(new Vector2d(parkX, parkY));

        while (!isStarted() && !opModeIsActive()) {
            telemetry.addLine("Initialized 1+3 Auto");
            telemetry.update();
            Actions.runBlocking(
                    scoringArm.WholeArmTransfer()
            );
        }

        waitForStart();

        if (isStopRequested()) return;

        Action autoTrajectoryTest = move1.build();

        Actions.runBlocking(
                new SequentialAction(
                        autoTrajectoryTest,
                        scoringArm.WholeArmTransfer()
                )
        );
    }
}
