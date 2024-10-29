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
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@Config
@Autonomous(name = "1+3 Slides Auto", group = "Autonomous", preselectTeleOp = "FinalFullRobotActionTeleOp")
public class SampleWithHoriAuto extends LinearOpMode{

    public static double startX = -8;
    public static double startY = -63.5;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = 0;
    public static double scorePreloadY = -38;
    public static double intake1X = -51;
    public static double intake1Y = -48.5;
    public static double scoreBucketX = -58;
    public static double scoreBucketY = -56;
    public static double intake2X = -59;
    public static double intake2Y = -48.5;
    public static double intake3X = -45;
    public static double intake3Y = -25;
    public static double parkX = -30;
    public static double parkY = -18;

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX, startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();
        IntakeArm intakeArm = new IntakeArm();
        HorizontalSlides horizontalSlides = new HorizontalSlides();


        TrajectoryActionBuilder move2 = drive.actionBuilder(startPose)
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip(),
                                horizontalSlides.HorizontalRetract()
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
                            new ParallelAction(
                                    intakeArm.IntakeHover(),
                                    horizontalSlides.HorizontalExtend()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(intake1X, intake1Y), Math.toRadians(90))
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    intakeArm.IntakePickup(),
                                    new SleepAction(0.5),
                                    intakeArm.IntakeTransfer(),
                                    new SleepAction(0.2),
                                    horizontalSlides.HorizontalRetract(),
                                    new SleepAction(0.5),
                                    scoringArm.WholeArmTransfer(),
                                    intakeArm.ClawOpen()
                            )
                    );
                })
                .waitSeconds(1)
                .afterTime(0.2, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    verticalSlides.LiftUpToHighBucket(),
                                    scoringArm.ArmScoreBucket()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.DropBucket(),
                                    intakeArm.IntakeHover(),
                                    horizontalSlides.HorizontalExtend()
                            )
                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y), Math.toRadians(90))
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    verticalSlides.Retract(),
                                    scoringArm.StowWholeArm(),
                                    intakeArm.IntakePickup(),
                                    new SleepAction(0.5),
                                    intakeArm.IntakeTransfer(),
                                    new SleepAction(0.2),
                                    horizontalSlides.HorizontalRetract(),
                                    new SleepAction(0.5),
                                    scoringArm.WholeArmTransfer(),
                                    intakeArm.ClawOpen()
                            )
                    );
                })
                .waitSeconds(1)
                .afterTime(0.2, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    verticalSlides.LiftUpToHighBucket(),
                                    scoringArm.ArmScoreBucket()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.DropBucket(),
                                    intakeArm.IntakeHoverPerpendicular(),
                                    horizontalSlides.HorizontalExtend()
                            )
                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(intake3X, intake3Y), Math.toRadians(180))
                .afterTime(0.5, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    verticalSlides.Retract(),
                                    scoringArm.StowWholeArm(),
                                    intakeArm.IntakePickup(),
                                    new SleepAction(0.5),
                                    intakeArm.IntakeTransfer(),
                                    new SleepAction(0.2),
                                    horizontalSlides.HorizontalRetract(),
                                    new SleepAction(0.5),
                                    scoringArm.WholeArmTransfer(),
                                    intakeArm.ClawOpen()
                            )
                    );
                })
                .waitSeconds(1)
                .afterTime(0.2, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    verticalSlides.LiftUpToHighBucket(),
                                    scoringArm.ArmScoreBucket()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime( 0.2, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.DropBucket()
                            )
                    );
                })
                .waitSeconds(1)
                .afterTime( 0.2, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    verticalSlides.Retract(),
                                    scoringArm.ArmInitPosition()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(parkX, parkY), Math.toRadians(90));

        while (!isStarted() && !opModeIsActive()) {
            intakeArm.initialize(this);
            verticalSlides.initialize(this);
            scoringArm.initialize(this);
            horizontalSlides.initialize(this);

            telemetry.addLine("Initialized 1+3 Auto");
            telemetry.update();
            Actions.runBlocking(
                    new ParallelAction(
                        intakeArm.IntakeTransfer(),
                        scoringArm.WholeArmTransfer()

                    )
            );
        }

        waitForStart();

        if (isStopRequested()) return;

        Action autoTrajectoryTest = move2.build();

        Actions.runBlocking(
                new SequentialAction(
                        autoTrajectoryTest,
                        new ParallelAction(
                            verticalSlides.Retract(),
                            horizontalSlides.HorizontalRetract(),
                            scoringArm.ArmInitPosition(),
                            intakeArm.IntakeTransfer()
                        )
                )
        );
    }
}
