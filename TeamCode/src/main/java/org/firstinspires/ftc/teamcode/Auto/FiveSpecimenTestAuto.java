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
    public static double field1X = 19;
    public static double field1Y = -36;
    public static double field2X = 29;
    public static double field2Y = -36;
    public static double field3X = 39;
    public static double field3Y = -36;
    public static double pickupX = 35;
    public static double pickupY = -54;
    public static double scoreX = 1;
    public static double scoreY = -34;
    public static double score2X = -2;
    public static double score2Y = -34;
    public static double score3X = -5;
    public static double score3Y = -34;
    public static double score4X = -10;
    public static double score4Y = -34;
    public static double parkX = 35;
    public static double parkY = -60;
    Pose2d startPose = new Pose2d(startX, startY, startHeading);
    Pose2d preloadPose = new Pose2d(scorePreloadX, scorePreloadY, Math.toRadians(-90));
    Pose2d field1Pose = new Pose2d(field1X, field1Y, Math.toRadians(15));
    Pose2d turn1Pose = new Pose2d(field1X, field1Y, Math.toRadians(-60));
    Pose2d field2Pose = new Pose2d(field2X, field2Y, Math.toRadians(10));
    Pose2d turn2Pose = new Pose2d(field2X, field2Y, Math.toRadians(-60));
    Pose2d field3Pose = new Pose2d(field3X, field3Y, Math.toRadians(10));
    Pose2d turn3Pose = new Pose2d(field3X, field3Y, Math.toRadians(-60));
    Pose2d pickupPose = new Pose2d(pickupX, pickupY, Math.toRadians(-90));
    Pose2d score1Pose = new Pose2d(scoreX, scoreY, Math.toRadians(-90));
    Pose2d score2Pose = new Pose2d(score2X, score2Y, Math.toRadians(-90));
    Pose2d score3Pose = new Pose2d(score3X, score3Y, Math.toRadians(-90));
    Pose2d score4Pose = new Pose2d(score4X, score4Y, Math.toRadians(-90));

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
                .splineToLinearHeading(new Pose2d(field1X, field1Y, Math.toRadians(15)), Math.toRadians(0));

        TrajectoryActionBuilder turn1 = drive.actionBuilder(field1Pose)
                .turnTo(Math.toRadians(-60));

        TrajectoryActionBuilder field2 = drive.actionBuilder(turn1Pose)
                .strafeToLinearHeading(new Vector2d(field2X, field2Y), Math.toRadians(10));

        TrajectoryActionBuilder turn2 = drive.actionBuilder(field2Pose)
                .turnTo(Math.toRadians(-60));

        TrajectoryActionBuilder field3 = drive.actionBuilder(turn2Pose)
                .strafeToLinearHeading(new Vector2d(field3X, field3Y), Math.toRadians(10));

        TrajectoryActionBuilder turn3 = drive.actionBuilder(field3Pose)
                .turnTo(Math.toRadians(-60));

        TrajectoryActionBuilder pickup1 = drive.actionBuilder(turn3Pose)
                .strafeToLinearHeading(new Vector2d(pickupX, pickupY), Math.toRadians(-90));

        TrajectoryActionBuilder pickup2 = drive.actionBuilder(score1Pose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder pickup3 = drive.actionBuilder(score2Pose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder pickup4 = drive.actionBuilder(score3Pose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder score1 = drive.actionBuilder(pickupPose)
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY));

        TrajectoryActionBuilder score2 = drive.actionBuilder(pickupPose)
                .strafeToConstantHeading(new Vector2d(score2X, score2Y));

        TrajectoryActionBuilder score3 = drive.actionBuilder(pickupPose)
                .strafeToConstantHeading(new Vector2d(score3X, score3Y));

        TrajectoryActionBuilder score4 = drive.actionBuilder(pickupPose)
                .strafeToConstantHeading(new Vector2d(score4X, score4Y));

        TrajectoryActionBuilder park = drive.actionBuilder(score4Pose)
                .strafeToConstantHeading(new Vector2d(parkX, parkY));

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
                        horizontalSlides.HorizontalFullExtend(),
                        intakeArm.IntakeHoverPerpendicular()
                );

        Action DROP_INTAKE1 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.2),
                        intakeArm.IntakeClose()
                );

        Action RAISE_INTAKE1 =
                new ParallelAction(
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHoverPerpendicular()
                );

        Action DROP_INTAKE2 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.2),
                        intakeArm.IntakeClose()
                );

        Action RAISE_INTAKE2 =
                new ParallelAction(
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHoverPerpendicular()
                );

        Action DROP_INTAKE3 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.2),
                        intakeArm.IntakeClose()
                );

        Action RETRACT_INTAKE3 =
                new SequentialAction(
                        new ParallelAction(
                                intakeArm.ClawOpen(),
                                intakeArm.IntakeHoverPerpendicular()
                        ),
                horizontalSlides.HorizontalRetract(),
                intakeArm.IntakeHover()
                );

        Action PICKUP1 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.2),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.2),
                        intakeArm.IntakeTransfer(),
                        scoringArm.StowWholeArm(),
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHover(),
                        new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                        )
                );

        Action TRANSFER_AND_EXTEND1 =
                new SequentialAction(
                        scoringArm.StowWholeArm(),
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
                        scoringArm.ArmInitPosition(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action PICKUP2 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.2),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.2),
                        intakeArm.IntakeTransfer(),
                        scoringArm.StowWholeArm(),
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHover(),
                        new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                        )
                );

        Action TRANSFER_AND_EXTEND2 =
                new SequentialAction(
                        scoringArm.StowWholeArm(),
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
                        scoringArm.ArmInitPosition(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action PICKUP3 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.2),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.2),
                        intakeArm.IntakeTransfer(),
                        scoringArm.StowWholeArm(),
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHover(),
                        new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                        )
                );

        Action TRANSFER_AND_EXTEND3 =
                new SequentialAction(
                        scoringArm.StowWholeArm(),
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
                        scoringArm.ArmInitPosition(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action PICKUP4 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.2),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.2),
                        intakeArm.IntakeTransfer(),
                        scoringArm.StowWholeArm(),
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen(),
                        intakeArm.IntakeHover(),
                        new ParallelAction(
                                verticalSlides.LiftUpToClip(),
                                scoringArm.ArmScoreClip()
                        )
                );

        Action TRANSFER_AND_EXTEND4 =
                new SequentialAction(
                        scoringArm.StowWholeArm(),
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

        Action INITIALIZE =
                new ParallelAction(
                        intakeArm.IntakeTransfer(),
                        scoringArm.ArmInitPosition()
                );

        while (!isStarted() && !opModeIsActive()){
            verticalSlides.autoInitialize(this);
            horizontalSlides.autoInitialize(this);
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
        Action DRIVE_PARK = park.build();

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                        PREP_CLIP,
                        DRIVE_SCORE_PRELOAD
                        ),
                        SCORE_PRELOAD,
                        new ParallelAction(
                                DRIVE_FIELD1,
                                EXTEND_HORIZONTAL1
                        ),
                        DROP_INTAKE1,
                        DRIVE_TURN1,
                        RAISE_INTAKE1,
                        DRIVE_FIELD2,
                        DROP_INTAKE2,
                        DRIVE_TURN2,
                        RAISE_INTAKE2,
                        DRIVE_FIELD3,
                        DROP_INTAKE3,
                        DRIVE_TURN3,
                        RETRACT_INTAKE3,
                        DRIVE_PICKUP1,
                        PICKUP1,
                        new SleepAction(1),
//                        TRANSFER_AND_EXTEND1,
                        DRIVE_SCORE1,
                        SCORE_CLIP1,
                        DRIVE_PICKUP2,
                        PICKUP2,
                        new SleepAction(1),
//                        TRANSFER_AND_EXTEND2,
                        DRIVE_SCORE2,
                        SCORE_CLIP2,
                        DRIVE_PICKUP3,
                        PICKUP3,
                        new SleepAction(1),
//                        TRANSFER_AND_EXTEND3,
                        DRIVE_SCORE3,
                        SCORE_CLIP3,
                        DRIVE_PICKUP4,
                        PICKUP4,
                        new SleepAction(1),
//                        TRANSFER_AND_EXTEND4,
                        DRIVE_SCORE4,
                        SCORE_CLIP4,
                        DRIVE_PARK
                )
        );
    }
}
