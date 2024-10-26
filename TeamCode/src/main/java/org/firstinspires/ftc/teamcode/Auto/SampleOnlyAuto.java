package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
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
    public static double scorePreloadY = -39;
    public static double intake1X = -49;
    public static double intake1Y = -43;
    public static double scoreBucketX = -56;
    public static double scoreBucketY = -56;
    public static double intake2X = -58;
    public static double intake2Y = -43;
    public static double intake3X = -55;
    public static double intake3Y = -25;
    public static double parkX = -30;
    public static double parkY = -18;

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX, startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        VerticalSlides verticalSlides = new VerticalSlides();
        HorizontalSlides horizontalSlides = new HorizontalSlides();
        ScoringArm scoringArm = new ScoringArm();
        IntakeArm intakeArm = new IntakeArm();


        TrajectoryActionBuilder move2 = drive.actionBuilder(startPose)
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                            )
                    );
                })
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
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    intakeArm.IntakeHover()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(intake1X, intake1Y), Math.toRadians(90))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    intakeArm.IntakePickup(),
                                    new SleepAction(0.5),
                                    intakeArm.IntakeTransfer()
                            )
                    );
                })
                .waitSeconds(2)
                .afterTime( 0, () -> {
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
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.StowWholeArm(),
                                    verticalSlides.Retract(),
                                    intakeArm.IntakeHover()
                            )
                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y), Math.toRadians(90))
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    intakeArm.IntakePickup(),
                                    new SleepAction(0.5),
                                    intakeArm.IntakeTransfer()
                            )
                    );
                })
                .waitSeconds(2)
                .afterTime( 0, () -> {
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
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.StowWholeArm(),
                                    verticalSlides.Retract(),
                                    intakeArm.IntakeHoverPerpendicular()
                            )
                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(intake3X, intake3Y), Math.toRadians(180))
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    intakeArm.IntakePickup(),
                                    new SleepAction(0.5),
                                    intakeArm.IntakeTransfer()
                            )
                    );
                })
                .waitSeconds(2)
                .afterTime( 0, () -> {
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
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.2, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.StowWholeArm(),
                                    verticalSlides.Retract()
                            )
                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(parkX, parkY), Math.toRadians(90));

        while (!isStarted() && !opModeIsActive()) {
            intakeArm.initialize(this);
            verticalSlides.initialize(this);
            scoringArm.initialize(this);

            telemetry.addLine("Initialized 1+3 Auto");
            telemetry.update();
            Actions.runBlocking(
                    new ParallelAction(
                        scoringArm.WholeArmTransfer(),
                        intakeArm.IntakeTransfer(),
                        horizontalSlides.HorizontalRetract()
                    )
            );
        }

        waitForStart();

        if (isStopRequested()) {
            Actions.runBlocking(
                    new ParallelAction(
                            scoringArm.WholeArmTransfer(),
                            verticalSlides.Retract(),
                            intakeArm.IntakeTransfer(),
                            horizontalSlides.HorizontalRetract()
                    )
            );
            return;
        }

        Action autoTrajectoryTest = move2.build();

        Actions.runBlocking(
                new SequentialAction(
                        autoTrajectoryTest
                )
        );
    }
}
