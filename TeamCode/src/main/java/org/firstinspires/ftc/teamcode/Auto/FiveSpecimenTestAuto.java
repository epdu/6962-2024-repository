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

    public static double startX = 8;
    public static double startY = -63.5;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = -3;
    public static double scorePreloadY = -35;
    public static double field1X = 48;
    public static double field1Y = -45;
    public static double field2X = 59;
    public static double field2Y = -45;
    public static double field3X = 54;
    public static double field3Y = -45;
    public static double prepPickupX = 48;
    public static double prepPickupY = -49;
    public static double pickupX = 48;
    public static double pickupY = -60;
    public static double scoreX = 0;
    public static double scoreY = -35;
    public static double score2X = 3;
    public static double score2Y = -35;
    public static double score3X = 6;
    public static double score3Y = -34;
    public static double score4X = 9;
    public static double score4Y = -34;
    public static double parkX = 45;
    public static double parkY = -60;
    Pose2d startPose = new Pose2d(startX, startY, startHeading);
    Pose2d preloadPose = new Pose2d(scorePreloadX, scorePreloadY, Math.toRadians(-90));
    Pose2d field1Pose = new Pose2d(field1X, field1Y, Math.toRadians(90));
    Pose2d field2Pose = new Pose2d(field2X, field2Y, Math.toRadians(90));
    Pose2d field3Pose = new Pose2d(field3X, field3Y, Math.toRadians(45));
    Pose2d prepPickupPose = new Pose2d(prepPickupX, prepPickupY, Math.toRadians(90));
    Pose2d pickupPose = new Pose2d(pickupX, pickupY, Math.toRadians(90));
    Pose2d scorePose = new Pose2d(scoreX, scoreY, Math.toRadians(-90));

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
                .splineToLinearHeading(new Pose2d(field1X, field1Y, Math.toRadians(90)), Math.toRadians(0));

        TrajectoryActionBuilder field2 = drive.actionBuilder(field1Pose)
                .strafeToConstantHeading(new Vector2d(field2X, field2Y));

        TrajectoryActionBuilder field3 = drive.actionBuilder(field2Pose)
                .strafeToLinearHeading(new Vector2d(field3X, field3Y), Math.toRadians(45));

        TrajectoryActionBuilder prepPickup1 = drive.actionBuilder(field3Pose)
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder prepPickup2 = drive.actionBuilder(scorePose)
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder prepPickup3 = drive.actionBuilder(scorePose)
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder prepPickup4 = drive.actionBuilder(scorePose)
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder actualPickup1 = drive.actionBuilder(prepPickupPose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder actualPickup2 = drive.actionBuilder(prepPickupPose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder actualPickup3 = drive.actionBuilder(prepPickupPose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder actualPickup4 = drive.actionBuilder(prepPickupPose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder score1 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(-90));

        TrajectoryActionBuilder score2 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(score2X, score2Y), Math.toRadians(-90));

        TrajectoryActionBuilder score3 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(score3X, score3Y), Math.toRadians(-90));

        TrajectoryActionBuilder score4 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(score4X, score4Y), Math.toRadians(-90));

        TrajectoryActionBuilder park = drive.actionBuilder(scorePose)
                .strafeToConstantHeading(new Vector2d(parkX, parkY));

        Action INTAKE_FLOOR1 =
                new SequentialAction(
                        new ParallelAction(
                                intakeArm.IntakeHover(),
                                horizontalSlides.HorizontalExtend()
                        ),
                        intakeArm.IntakePickup(),
                        new SleepAction(0.5),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.2),
                        intakeArm.IntakeTransfer(),
                        new SleepAction(0.2),
                        horizontalSlides.HorizontalRetract()
                );

        Action INTAKE_FLOOR2 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.5),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.2),
                        intakeArm.IntakeTransfer(),
                        new SleepAction(0.2),
                        horizontalSlides.HorizontalRetract()
                );

        Action INTAKE_FLOOR3 =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.5),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.2),
                        intakeArm.IntakeTransfer(),
                        new SleepAction(0.2),
                        horizontalSlides.HorizontalRetract()
                );

        Action TRANSFER_AND_DROP1 =
                new ParallelAction(
                        new SequentialAction(
                                scoringArm.WholeArmTransfer(),
                                intakeArm.ClawOpen(),
                                scoringArm.ArmDropClip(),
                                scoringArm.DropBucket(),
                                scoringArm.StowWholeArm()
                        ),
                        intakeArm.IntakeHover(),
                        horizontalSlides.HorizontalExtend()
                );

        Action TRANSFER_AND_DROP2 =
                new ParallelAction(
                        new SequentialAction(
                                scoringArm.WholeArmTransfer(),
                                intakeArm.ClawOpen(),
                                scoringArm.ArmDropClip(),
                                scoringArm.DropBucket(),
                                scoringArm.StowWholeArm()
                        ),
                        intakeArm.IntakeHover(),
                        horizontalSlides.HorizontalExtend()
                );

        Action TRANSFER_AND_DROP3 =
                new ParallelAction(
                        new SequentialAction(
                                scoringArm.WholeArmTransfer(),
                                intakeArm.ClawOpen(),
                                scoringArm.ArmDropClip(),
                                scoringArm.DropBucket(),
                                scoringArm.StowWholeArm()
                        ),
                        intakeArm.IntakeHover(),
                        horizontalSlides.HorizontalExtend()
                );

        Action PREP_CLIP =
                new ParallelAction(
                        scoringArm.ArmScoreClip(),
                        verticalSlides.LiftUpToClip(),
                        horizontalSlides.HorizontalRetract()
                );
        Action PREP_CLIP2 = new ParallelAction(
                scoringArm.ArmScoreClip(),
                verticalSlides.LiftUpToClip(),
                horizontalSlides.HorizontalRetract()
        );
        Action PREP_CLIP3 = new ParallelAction(
                scoringArm.ArmScoreClip(),
                verticalSlides.LiftUpToClip(),
                horizontalSlides.HorizontalRetract()
        );
        Action PREP_CLIP4 = new ParallelAction(
                scoringArm.ArmScoreClip(),
                verticalSlides.LiftUpToClip(),
                horizontalSlides.HorizontalRetract()
        );
        Action PREP_CLIP5 = new ParallelAction(
                scoringArm.ArmScoreClip(),
                verticalSlides.LiftUpToClip(),
                horizontalSlides.HorizontalRetract()
        );

        Action SCORE_CLIP =
                new SequentialAction(
                        verticalSlides.SlamScoreClip(),
                        scoringArm.StowWholeArm(),
                        verticalSlides.Retract()
                );
        Action SCORE_CLIP2 = new SequentialAction(
                verticalSlides.SlamScoreClip(),
                scoringArm.StowWholeArm(),
                verticalSlides.Retract()
        );
        Action SCORE_CLIP3 = new SequentialAction(
                verticalSlides.SlamScoreClip(),
                scoringArm.StowWholeArm(),
                verticalSlides.Retract()
        );
        Action SCORE_CLIP4 = new SequentialAction(
                verticalSlides.SlamScoreClip(),
                scoringArm.StowWholeArm(),
                verticalSlides.Retract(),
                horizontalSlides.HorizontalRetract()
        );
        Action SCORE_CLIP5 = new SequentialAction(
                verticalSlides.SlamScoreClip(),
                scoringArm.StowWholeArm(),
                verticalSlides.Retract(),
                horizontalSlides.HorizontalRetract()
        );

        Action PICKUP_CLIP =
                new SequentialAction(
                        scoringArm.ArmGrabClip()
                );
        Action PICKUP_CLIP2 = new SequentialAction(
                scoringArm.ArmGrabClip()
        );
        Action PICKUP_CLIP3 = new SequentialAction(
                scoringArm.ArmGrabClip()
        );
        Action PICKUP_CLIP4 = new SequentialAction(
                scoringArm.ArmGrabClip()
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

        Action SCORE_PRELOAD = scorePreload.build();
        Action FIELD1 = field1.build();
        Action FIELD2 = field2.build();
        Action FIELD3 = field3.build();
        Action PICKUP1 = prepPickup1.build();
        Action PICKUP2 = prepPickup2.build();
        Action PICKUP3 = prepPickup3.build();
        Action PICKUP4 = prepPickup4.build();
        Action ACTUAL_PICKUP = actualPickup1.build();
        Action ACTUAL_PICKUP2 = actualPickup2.build();
        Action ACTUAL_PICKUP3 = actualPickup3.build();
        Action ACTUAL_PICKUP4 = actualPickup4.build();
        Action SCORE1 = score1.build();
        Action SCORE2 = score2.build();
        Action SCORE3 = score3.build();
        Action SCORE4 = score4.build();
        Action PARK = park.build();

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                PREP_CLIP,
                        SCORE_PRELOAD
                        ),
                                SCORE_CLIP,
                        FIELD1,
                                INTAKE_FLOOR1,
                        new ParallelAction(
                        FIELD2,
                                TRANSFER_AND_DROP1
                        ),
                                INTAKE_FLOOR2,
                        new ParallelAction(
                        FIELD3,
                                TRANSFER_AND_DROP2
                        ),
                                INTAKE_FLOOR3,
                        new ParallelAction(
                        PICKUP1,
                                new SequentialAction(
                                        TRANSFER_AND_DROP3,
                                        PICKUP_CLIP
                                )
                        ),
                        ACTUAL_PICKUP,
                                PREP_CLIP2,
                        SCORE1,
                                SCORE_CLIP2,
                        new ParallelAction(
                        PICKUP2,
                                PICKUP_CLIP2
                        ),
                        ACTUAL_PICKUP2,
                                PREP_CLIP3,
                        SCORE2,
                                SCORE_CLIP3,
                        new ParallelAction(
                        PICKUP3,
                                PICKUP_CLIP3
                        ),
                        ACTUAL_PICKUP3,
                                PREP_CLIP4,
                        SCORE3,
                            SCORE_CLIP4,
                        new ParallelAction(
                        PICKUP4,
                                PICKUP_CLIP4
                        ),
                        ACTUAL_PICKUP4,
                                PREP_CLIP5,
                        SCORE4,
                                SCORE_CLIP5,
                        PARK
                )
        );
    }
}
