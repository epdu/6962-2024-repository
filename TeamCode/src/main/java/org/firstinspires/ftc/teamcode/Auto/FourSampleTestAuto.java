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
//import org.firstinspires.ftc.teamcode.Subsystems.SubsystemCommands;

@Config
@Autonomous(name = "0+4 Test Auto", group = "Autonomous", preselectTeleOp = "Solo Full Robot TeleOp")
public class FourSampleTestAuto extends LinearOpMode{

    public static double startX = -39;
    public static double startY = -63.5;
    public static double startHeading = Math.toRadians(90);
    public static double scorePreloadX = 0;
    public static double scorePreloadY = -41;
    public static double intake1X = -46.5;
    public static double intake1Y = -52;
    public static double scoreBucketX = -55;
    public static double scoreBucketY = -57;
    public static double intake2X = -60;
    public static double intake2Y = -53;
    public static double intake3X = -58.5;
    public static double intake3Y = -52;
    public static double coord1X = -16;
    public static double coord1Y = -39;
    public static double coord2X = 12;
    public static double coord2Y = -39;
    public static double pickupX = 40;
    public static double pickupY = -58;
    public static double scoreClipX = 9;
    public static double scoreClipY = -34;
    public static double parkX = 40;
    public static double parkY = -60;

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX, startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
//        SubsystemCommands subsystems = new SubsystemCommands();

        VerticalSlides verticalSlides = new VerticalSlides();
        HorizontalSlides horizontalSlides = new HorizontalSlides();
        ScoringArm scoringArm = new ScoringArm();
        IntakeArm intakeArm = new IntakeArm();

        //defining movement trajectories
        TrajectoryActionBuilder scorePreload = drive.actionBuilder(startPose)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder intake1 = drive.actionBuilder(new Pose2d(scoreBucketX, scoreBucketY, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(intake1X, intake1Y), Math.toRadians(90));

        TrajectoryActionBuilder score1 = drive.actionBuilder(new Pose2d(intake1X, intake1Y, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder intake2 = drive.actionBuilder(new Pose2d(scoreBucketX, scoreBucketY, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y), Math.toRadians(90));

        TrajectoryActionBuilder score2 = drive.actionBuilder(new Pose2d(intake2X, intake2Y, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder intake3 = drive.actionBuilder(new Pose2d(scoreBucketX, scoreBucketY, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(intake3X, intake3Y), Math.toRadians(115));

        TrajectoryActionBuilder score3 = drive.actionBuilder(new Pose2d(intake3X, intake3Y, Math.toRadians(115)))
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder pass = drive.actionBuilder(new Pose2d(scoreBucketX, scoreBucketY, Math.toRadians(45)))
                .splineToLinearHeading(new Pose2d(coord1X, coord1Y, Math.toRadians(90)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(coord2X, coord2Y), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder scoreClip = drive.actionBuilder(new Pose2d(pickupX, pickupY, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(scoreClipX, scoreClipY), Math.toRadians(-90));

        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(scoreClipX, scoreClipY, Math.toRadians(-90)))
                .strafeToLinearHeading(new Vector2d(parkX, parkY), Math.toRadians(90));

        Action PICKUP_CLIP =
                new SequentialAction(
                        scoringArm.ArmGrabClip()
                );

        Action PREP_CLIP1 =
                new ParallelAction(
                        scoringArm.ArmScoreClip(),
                        verticalSlides.LiftUpToClip(),
                        horizontalSlides.HorizontalRetract()
                );

        Action SCORE_CLIP1 =
                new SequentialAction(
                        verticalSlides.SlamScoreClip(),
                        scoringArm.StowWholeArm(),
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract()
                );

        Action EXTEND_INTAKE =
                new ParallelAction(
                        intakeArm.IntakeHover(),
                        horizontalSlides.HorizontalExtend()
                );
        Action EXTEND_INTAKE2 = new ParallelAction(
                intakeArm.IntakeHover(),
                horizontalSlides.HorizontalExtend()
        );
        Action EXTEND_INTAKE3 = new ParallelAction(
                intakeArm.IntakeHover(),
                horizontalSlides.HorizontalExtend()
        );

        Action INTAKE_AND_TRANSFER =
                new SequentialAction(
                        intakeArm.IntakePickup(),
                        new SleepAction(0.5),
                        intakeArm.IntakeClose(),
                        new SleepAction(0.2),
                        intakeArm.IntakeTransfer(),
                        new SleepAction(0.2),
                        horizontalSlides.HorizontalRetract(),
                        new SleepAction(0.5),
                        scoringArm.WholeArmTransfer(),
                        intakeArm.ClawOpen()
                );
        Action INTAKE_AND_TRANSFER2 = new SequentialAction(
                intakeArm.IntakePickup(),
                new SleepAction(0.5),
                intakeArm.IntakeClose(),
                new SleepAction(0.2),
                intakeArm.IntakeTransfer(),
                new SleepAction(0.2),
                horizontalSlides.HorizontalRetract(),
                new SleepAction(0.5),
                scoringArm.WholeArmTransfer(),
                intakeArm.ClawOpen()
        );
        Action INTAKE_AND_TRANSFER3 = new SequentialAction(
                intakeArm.IntakePickup(),
                new SleepAction(0.5),
                intakeArm.IntakeClose(),
                new SleepAction(0.2),
                intakeArm.IntakeTransfer(),
                new SleepAction(0.2),
                horizontalSlides.HorizontalRetract(),
                new SleepAction(0.5),
                scoringArm.WholeArmTransfer(),
                intakeArm.ClawOpen()
        );

        Action EXTEND1 =
                new SequentialAction(
                        new ParallelAction(
                                verticalSlides.LiftUpToHighBucket(),
                                scoringArm.ArmPrepScoreBucket()
                        ),
                        scoringArm.ArmScoreBucket()
                );
        Action EXTEND2 =
                new SequentialAction(
                        new ParallelAction(
                                verticalSlides.LiftUpToHighBucket(),
                                scoringArm.ArmPrepScoreBucket()
                        ),
                        scoringArm.ArmScoreBucket()
                );
        Action EXTEND3 =
                new SequentialAction(
                        new ParallelAction(
                                verticalSlides.LiftUpToHighBucket(),
                                scoringArm.ArmPrepScoreBucket()
                        ),
                        scoringArm.ArmScoreBucket()
                );

        Action SCORE_BUCKET =
                new SequentialAction(
                        scoringArm.DropBucket(),
                        new SleepAction(0.2),
                        scoringArm.StowWholeArm(),
                        verticalSlides.Retract()
                );
        Action SCORE_BUCKET2 = new SequentialAction(
                scoringArm.DropBucket(),
                new SleepAction(0.2),
                scoringArm.StowWholeArm(),
                verticalSlides.Retract()
        );
        Action SCORE_BUCKET3 = new SequentialAction(
                scoringArm.DropBucket(),
                new SleepAction(0.2),
                scoringArm.StowWholeArm(),
                verticalSlides.Retract()
        );
        Action SCORE_BUCKET4 = new SequentialAction(
                scoringArm.DropBucket(),
                new SleepAction(0.2),
                scoringArm.StowWholeArm(),
                verticalSlides.Retract()
        );

        Action LIFT_BUCKET =
                new SequentialAction(
                        new ParallelAction(
                                verticalSlides.LiftUpToHighBucket(),
                                scoringArm.ArmPrepScoreBucket()
                        ),
                        scoringArm.ArmScoreBucket()
                );

        Action RETRACT_ALL =
                new ParallelAction(
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract(),
                        scoringArm.StowArmClose(),
                        intakeArm.IntakeTransfer()
                );

        Action INITIALIZE =
                new ParallelAction(
                        intakeArm.IntakeTransfer(),
                        scoringArm.ArmInitPosition()
                );


        while (!isStarted() && !opModeIsActive()) {
            verticalSlides.initialize(this);
            horizontalSlides.initialize(this);
            scoringArm.initialize(this);
            intakeArm.initialize(this);

            telemetry.addLine("Initialized Test 0+4 Auto");
            telemetry.update();
            Actions.runBlocking(
                    INITIALIZE
            );
        }

        waitForStart();

        if (isStopRequested()) return;

        //drive action builds
        Action SCORE_PRELOAD = scorePreload.build();
        Action INTAKE1 = intake1.build();
        Action SCORE1 = score1.build();
        Action INTAKE2 = intake2.build();
        Action SCORE2 = score2.build();
        Action INTAKE3 = intake3.build();
        Action SCORE3 = score3.build();
        Action PARK = park.build();
        Action CLIP = scoreClip.build();
        Action PASS = pass.build();

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                LIFT_BUCKET,
                                SCORE_PRELOAD
                        ),
                        new SleepAction(0.2),
                        SCORE_BUCKET,
                        new ParallelAction(
                                INTAKE1,
                                EXTEND_INTAKE
                        ),
                            INTAKE_AND_TRANSFER,
                        new ParallelAction(
                        SCORE1,
                            EXTEND1
                        ),
                        new SleepAction(0.2),
                            SCORE_BUCKET2,
                        new ParallelAction(
                        INTAKE2,
                            EXTEND_INTAKE2
                        ),
                            INTAKE_AND_TRANSFER2,
                        new ParallelAction(
                                SCORE2,
                                EXTEND2
                        ),
                        new SleepAction(0.2),
                        SCORE_BUCKET3,
                        new ParallelAction(
                                INTAKE3,
                                EXTEND_INTAKE3
                        ),
                            INTAKE_AND_TRANSFER3,
                        new ParallelAction(
                                SCORE3,
                                EXTEND3
                        ),
                        new SleepAction(0.2),
                        SCORE_BUCKET4,
                        new ParallelAction(
                        PASS,
                                PICKUP_CLIP
                        ),
                        PREP_CLIP1,
                        CLIP,
                        SCORE_CLIP1,
                        PARK
                )
        );
    }
}
