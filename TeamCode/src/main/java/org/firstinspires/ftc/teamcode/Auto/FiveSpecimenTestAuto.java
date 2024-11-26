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
@Autonomous(name = "5+0 Auto Test", group = "Autonomous", preselectTeleOp = "A Solo Full Robot TeleOp")
public class FiveSpecimenTestAuto extends LinearOpMode {

    public static double startX = 9;
    public static double startY = -62.5;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = 9;
    public static double scorePreloadY = -34;
    public static double field1X = 27;
    public static double field1Y = -36;
    public static double field2X = 37;
    public static double field2Y = -36;
    public static double field3X = 46;
    public static double field3Y = -36;
    public static double pickupX = 24;
    public static double pickupY = -48;
    public static double scoreX = 1;
    public static double scoreY = -34;
    public static double score2X = -2;
    public static double score2Y = -34;
    public static double score3X = -5;
    public static double score3Y = -34;
    public static double score4X = -10;
    public static double score4Y = -34;
    public static double intake1X = -47;
    public static double intake1Y = -48;
    public static double scoreBucketX = -55;
    public static double scoreBucketY = -55;
    public static double intake2X = -58;
    public static double intake2Y = -48;
    Pose2d startPose = new Pose2d(startX, startY, startHeading);
    Pose2d preloadPose = new Pose2d(scorePreloadX, scorePreloadY, Math.toRadians(-90));
    Pose2d field1Pose = new Pose2d(field1X, field1Y, Math.toRadians(30));
    Pose2d turn1Pose = new Pose2d(field1X, field1Y, Math.toRadians(-40));
    Pose2d field2Pose = new Pose2d(field2X, field2Y, Math.toRadians(30));
    Pose2d turn2Pose = new Pose2d(field2X, field2Y, Math.toRadians(-40));
    Pose2d field3Pose = new Pose2d(field3X, field3Y, Math.toRadians(30));
    Pose2d turn3Pose = new Pose2d(field3X, field3Y, Math.toRadians(-40));
    Pose2d pickupPose = new Pose2d(pickupX, pickupY, Math.toRadians(-45));
    Pose2d score1Pose = new Pose2d(scoreX, scoreY, Math.toRadians(-90));
    Pose2d score2Pose = new Pose2d(score2X, score2Y, Math.toRadians(-90));
    Pose2d score3Pose = new Pose2d(score3X, score3Y, Math.toRadians(-90));
    Pose2d score4Pose = new Pose2d(score4X, score4Y, Math.toRadians(-90));
    Pose2d intake1Pose = new Pose2d(intake1X, intake1Y, Math.toRadians(90));
    Pose2d intake2Pose = new Pose2d(intake2X, intake2Y, Math.toRadians(90));
    Pose2d bucketPose = new Pose2d(scoreBucketX, scoreBucketY, Math.toRadians(45));

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();
        IntakeArm intakeArm = new IntakeArm();
        HorizontalSlides horizontalSlides = new HorizontalSlides();

        TrajectoryActionBuilder scorePreload = drive.actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(scorePreloadX, scorePreloadY));

        TrajectoryActionBuilder field1 = drive.actionBuilder(preloadPose)
                .splineToLinearHeading(new Pose2d(field1X, field1Y, Math.toRadians(30)), Math.toRadians(0));

        TrajectoryActionBuilder turn1 = drive.actionBuilder(field1Pose)
                .turnTo(Math.toRadians(-40));

        TrajectoryActionBuilder field2 = drive.actionBuilder(turn1Pose)
                .strafeToLinearHeading(new Vector2d(field2X, field2Y), Math.toRadians(30));

        TrajectoryActionBuilder turn2 = drive.actionBuilder(field2Pose)
                .turnTo(Math.toRadians(-40));

        TrajectoryActionBuilder field3 = drive.actionBuilder(turn2Pose)
                .strafeToLinearHeading(new Vector2d(field3X, field3Y), Math.toRadians(30));

        TrajectoryActionBuilder turn3 = drive.actionBuilder(field3Pose)
                .turnTo(Math.toRadians(-40));

        TrajectoryActionBuilder pickup1 = drive.actionBuilder(turn3Pose)
                .strafeToLinearHeading(new Vector2d(pickupX, pickupY), Math.toRadians(-45))
                .strafeToConstantHeading(new Vector2d(pickupX+2, pickupY-2));

        TrajectoryActionBuilder pickup2 = drive.actionBuilder(score1Pose)
                .strafeToLinearHeading(new Vector2d(pickupX, pickupY), Math.toRadians(-45))
                .strafeToConstantHeading(new Vector2d(pickupX+2, pickupY-2));

        TrajectoryActionBuilder pickup3 = drive.actionBuilder(score2Pose)
                .strafeToLinearHeading(new Vector2d(pickupX, pickupY), Math.toRadians(-45))
                .strafeToConstantHeading(new Vector2d(pickupX+2, pickupY-2));

        TrajectoryActionBuilder pickup4 = drive.actionBuilder(score3Pose)
                .strafeToLinearHeading(new Vector2d(pickupX, pickupY), Math.toRadians(-45))
                .strafeToConstantHeading(new Vector2d(pickupX+2, pickupY-2));

        TrajectoryActionBuilder score1 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(-90));

        TrajectoryActionBuilder score2 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(score2X, score2Y), Math.toRadians(-90));

        TrajectoryActionBuilder score3 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(score3X, score3Y), Math.toRadians(-90));

        TrajectoryActionBuilder score4 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(score4X, score4Y), Math.toRadians(-90));

        TrajectoryActionBuilder intake1 = drive.actionBuilder(score4Pose)
                .strafeToLinearHeading(new Vector2d(intake1X, intake1Y), Math.toRadians(90.1));

        TrajectoryActionBuilder scoreBucket1 = drive.actionBuilder(intake1Pose)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder intake2 = drive.actionBuilder(bucketPose)
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y), Math.toRadians(90));

        TrajectoryActionBuilder scoreBucket2 = drive.actionBuilder(intake2Pose)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder turn = drive.actionBuilder(bucketPose)
                .turnTo(Math.toRadians(90));

        Action PREP_CLIP =
                new ParallelAction(
                        verticalSlides.LiftUpToClip(),
                        scoringArm.ArmScoreClip()
                );

        Action SCORE_PRELOAD =
                new SequentialAction(
                        verticalSlides.SlamScoreClip(),
                        scoringArm.StowWholeArm(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action EXTEND_HORIZONTAL1 =
                new ParallelAction(
                        verticalSlides.Retract(),
                        scoringArm.StowWholeArm(),
                        horizontalSlides.HorizontalExtend(),
                        intakeArm.IntakeHoverPerpendicular()
                );

        Action DROP_INTAKE1 =
                new ParallelAction(
                        intakeArm.IntakeDrop()
                );

        Action RAISE_INTAKE1 =
                new ParallelAction(
                        intakeArm.IntakeHoverPerpendicular()
                );

        Action DROP_INTAKE2 =
                new ParallelAction(
                        intakeArm.IntakeDrop()
                );

        Action RAISE_INTAKE2 =
                new ParallelAction(
                        intakeArm.IntakeHoverPerpendicular()
                );

        Action DROP_INTAKE3 =
                new ParallelAction(
                        intakeArm.IntakeDrop()
                );

        Action RETRACT_INTAKE3 =
                new ParallelAction(
                        horizontalSlides.HorizontalRetract(),
                        intakeArm.IntakeTransfer()
                );

        Action PICKUP1 =
                new SequentialAction(
                        new ParallelAction(
                                horizontalSlides.HorizontalExtend(),
                                intakeArm.IntakeHover()
                        ),
                        intakeArm.IntakePickup(),
                        new SleepAction(0.15),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.1),
                        new ParallelAction(
                                horizontalSlides.HorizontalRetract(),
                                intakeArm.IntakeTransfer()
                        )
                );

        Action TRANSFER_AND_EXTEND1 =
                new SequentialAction(
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHover(),
                        new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                        )
                );

        Action SCORE_CLIP1 =
                new SequentialAction(
                        verticalSlides.SlamScoreClip(),
                        scoringArm.StowWholeArm(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action EXTEND_INTAKE2 =
                new ParallelAction(
                        verticalSlides.Retract(),
                        scoringArm.StowWholeArm(),
                        horizontalSlides.HorizontalExtend()
                );

        Action PICKUP2 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.15),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.1),
                        new ParallelAction(
                                horizontalSlides.HorizontalRetract(),
                                intakeArm.IntakeTransfer()
                        )
                );

        Action TRANSFER_AND_EXTEND2 =
                new SequentialAction(
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHover(),
                        new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                        )
                );

        Action SCORE_CLIP2 =
                new SequentialAction(
                        verticalSlides.SlamScoreClip(),
                        scoringArm.StowWholeArm(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action EXTEND_INTAKE3 =
                new ParallelAction(
                        verticalSlides.Retract(),
                        scoringArm.StowWholeArm(),
                        horizontalSlides.HorizontalExtend()
                );

        Action PICKUP3 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.15),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.1),
                        new ParallelAction(
                                horizontalSlides.HorizontalRetract(),
                                intakeArm.IntakeTransfer()
                        )
                );

        Action TRANSFER_AND_EXTEND3 =
                new SequentialAction(
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHover(),
                        new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                        )
                );

        Action SCORE_CLIP3 =
                new SequentialAction(
                        verticalSlides.SlamScoreClip(),
                        scoringArm.StowWholeArm(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action EXTEND_INTAKE4 =
                new ParallelAction(
                        verticalSlides.Retract(),
                        scoringArm.StowWholeArm(),
                        horizontalSlides.HorizontalExtend()
                );

        Action PICKUP4 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.15),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.1),
                        new ParallelAction(
                                horizontalSlides.HorizontalRetract(),
                                intakeArm.IntakeTransfer()
                        )
                );

        Action TRANSFER_AND_EXTEND4 =
                new SequentialAction(
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHover(),
                        new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                        ),
                        intakeArm.IntakeTransfer()
                );

        Action SCORE_CLIP4 =
                new SequentialAction(
                        verticalSlides.SlamScoreClip(),
                        scoringArm.StowWholeArm(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action EXTEND_INTAKE5 =
                new ParallelAction(
                        verticalSlides.Retract(),
                        scoringArm.StowWholeArm(),
                        horizontalSlides.HorizontalExtend(),
                        intakeArm.IntakeHover()
                );

        Action INTAKE_SAMPLE1 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.15),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.1),
                        intakeArm.IntakeTransfer(),
//                        new SleepAction(0.2),
                        horizontalSlides.HorizontalRetract(),
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen()
                );

        Action EXTEND_SAMPLE1 =
                new ParallelAction(
                        verticalSlides.LiftUpToHighBucket(),
                        scoringArm.ArmScoreBucket()
                );

        Action SCORE_BUCKET1 =
                scoringArm.DropBucket();

        Action RETRACT_SAMPLE1 =
                new ParallelAction(
                        verticalSlides.Retract(),
                        scoringArm.StowWholeArm()
                );

        Action INTAKE_SAMPLE2 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.15),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.1),
                        intakeArm.IntakeTransfer(),
//                        new SleepAction(0.2),
                        horizontalSlides.HorizontalRetract(),
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen()
                );

        Action EXTEND_SAMPLE2 =
                new ParallelAction(
                        verticalSlides.LiftUpToHighBucket(),
                        scoringArm.ArmScoreBucket()
                );

        Action SCORE_BUCKET2 =
                scoringArm.DropBucket();

        Action RETRACT_SAMPLE2 =
                new ParallelAction(
                        verticalSlides.Retract(),
                        scoringArm.StowWholeArm(),
                        horizontalSlides.HorizontalRetract()
                );

        Action INITIALIZE =
                new ParallelAction(
                        intakeArm.IntakeTransfer(),
                        scoringArm.ArmInitPosition()
                );

        while (!isStarted() && !opModeIsActive()){
            verticalSlides.initialize(this);
            horizontalSlides.initialize(this);
            scoringArm.initialize(this);
            intakeArm.initialize(this);

            Actions.runBlocking(
                    INITIALIZE
            );
        }

        waitForStart();

        if (isStopRequested()) return;

        Action DRIVE_SCORE_PRELOAD = scorePreload.build();
        Action DRIVE_FIELD1 = field1.build();
        Action DRIVE_TURN1 = turn1.build();
        Action DRIVE_FIELD2 = field2.build();
        Action DRIVE_TURN2 = turn2.build();
        Action DRIVE_FIELD3 = field3.build();
        Action DRIVE_TURN3 = turn3.build();
        Action DRIVE_PICKUP1 = pickup1.build();
        Action DRIVE_PICKUP2 = pickup2.build();
        Action DRIVE_PICKUP3 = pickup3.build();
        Action DRIVE_PICKUP4 = pickup4.build();
        Action DRIVE_SCORE1 = score1.build();
        Action DRIVE_SCORE2 = score2.build();
        Action DRIVE_SCORE3 = score3.build();
        Action DRIVE_SCORE4 = score4.build();
        Action DRIVE_INTAKE1 = intake1.build();
        Action DRIVE_SCORE_BUCKET1 = scoreBucket1.build();
        Action DRIVE_INTAKE2 = intake2.build();
        Action DRIVE_SCORE_BUCKET2 = scoreBucket2.build();
        Action DRIVE_PARK = turn.build();

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                        PREP_CLIP,
                        DRIVE_SCORE_PRELOAD
                        ),
                        SCORE_PRELOAD,
                        new ParallelAction(
                                DRIVE_FIELD1,
                                new SleepAction(0.5),
                                EXTEND_HORIZONTAL1
                        ),
                        new ParallelAction(
                                DROP_INTAKE1,
                                DRIVE_TURN1
                        ),
                        new ParallelAction(
                                RAISE_INTAKE1,
                                DRIVE_FIELD2
                        ),
                        new ParallelAction(
                                DROP_INTAKE2,
                                DRIVE_TURN2
                        ),
                        new ParallelAction(
                                RAISE_INTAKE2,
                                DRIVE_FIELD3
                        ),
                        new ParallelAction(
                                DROP_INTAKE3,
                                DRIVE_TURN3
                        ),
                        new ParallelAction(
                                RETRACT_INTAKE3,
                                DRIVE_PICKUP1
                        ),
                        PICKUP1,
                        new SleepAction(0.5),
                        new ParallelAction(
                                TRANSFER_AND_EXTEND1,
                                DRIVE_SCORE1
                        ),
                        SCORE_CLIP1,
                        new ParallelAction(
                                DRIVE_PICKUP2,
                                EXTEND_INTAKE2
                        ),
                        PICKUP2,
                        new SleepAction(0.5),
                        new ParallelAction(
                                TRANSFER_AND_EXTEND2,
                                DRIVE_SCORE2
                        ),
                        SCORE_CLIP2,
                        new ParallelAction(
                                DRIVE_PICKUP3,
                                EXTEND_INTAKE3
                        ),
                        PICKUP3,
                        new SleepAction(0.5),
                        new ParallelAction(
                                TRANSFER_AND_EXTEND3,
                                DRIVE_SCORE3
                        ),
                        SCORE_CLIP3,
                        new ParallelAction(
                                DRIVE_PICKUP4,
                                EXTEND_INTAKE4
                        ),
                        PICKUP4,
                        new SleepAction(0.5),
                        new ParallelAction(
                                TRANSFER_AND_EXTEND4,
                                DRIVE_SCORE4
                        ),
                        SCORE_CLIP4
//                        new ParallelAction(
//                                DRIVE_INTAKE1,
//                                new SleepAction(0.5),
//                                EXTEND_INTAKE5
//                        ),
//                        INTAKE_SAMPLE1,
//                        new ParallelAction(
//                                DRIVE_SCORE_BUCKET1,
//                                EXTEND_SAMPLE1
//                        ),
//                        SCORE_BUCKET1,
//                        new ParallelAction(
//                                DRIVE_INTAKE2,
//                                RETRACT_SAMPLE1
//                        ),
//                        INTAKE_SAMPLE2,
//                        new ParallelAction(
//                                DRIVE_SCORE_BUCKET2,
//                                EXTEND_SAMPLE2
//                        ),
//                        SCORE_BUCKET2,
//                        new ParallelAction(
//                                DRIVE_PARK,
//                                RETRACT_SAMPLE2
//                        )
                )
        );
    }
}
