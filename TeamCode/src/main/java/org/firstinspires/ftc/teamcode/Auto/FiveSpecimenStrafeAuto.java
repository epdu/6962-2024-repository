package org.firstinspires.ftc.teamcode.Auto;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Hang;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@Config
@Autonomous(name = "5+0 Strafe Auto", group = "1 Autonomous", preselectTeleOp = "A Solo Full Robot TeleOp")
public class FiveSpecimenStrafeAuto extends LinearOpMode {

    public static double startX = 8;
    public static double startY = -63.5;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = -3;
    public static double scorePreloadY = -33.5;
    public static double coord1X = 28;
    public static double coord1Y = -36;
    public static double push1X = 43;
    public static double push1Y = -15;
    public static double zone1X = 45;
    public static double zone1Y = -48;
    public static double push2X = 53;
    public static double push2Y = -15;
    public static double zone2X = 61;
    public static double zone2Y = -48;
    public static double push3X = 60;
    public static double push3Y = -15;
    public static double zone3X = 61;
    public static double zone3Y = -48;
    public static double prepPickupX = 40;
    public static double prepPickupY = -54;
    public static double prepPickup1X = 40;
    public static double prepPickup1Y = -48;
    public static double pickupX = 40;
    public static double pickupY = -55;
    public static double pickup2X = 40;
    public static double pickup2Y = -63;
    public static double pickup3X = 40;
    public static double pickup3Y = -66;
    public static double pickup4X = 40;
    public static double pickup4Y = -69;
    public static double scoreX = 0;
    public static double scoreY = -36;
    public static double score2X = 0;
    public static double score2Y = -40;
    public static double score3X = 0;
    public static double score3Y = -44;
    public static double score4X = 0;
    public static double score4Y = -46;
    public static double parkX = 43;
    public static double parkY = -67;
    Pose2d startPose = new Pose2d(startX, startY, startHeading);
    Pose2d preloadPose = new Pose2d(scorePreloadX, scorePreloadY, Math.toRadians(-90));
    Pose2d pushPose = new Pose2d(zone3X, zone3Y, Math.toRadians(-90));
    Pose2d prepPickupPose = new Pose2d(prepPickupX, prepPickupY, Math.toRadians(90));
    Pose2d prepPickup1Pose = new Pose2d(prepPickup1X, prepPickup1Y, Math.toRadians(90));
    Pose2d pickupPose = new Pose2d(pickupX, pickupY, Math.toRadians(90));
    Pose2d pickup2Pose = new Pose2d(pickup2X, pickup2Y, Math.toRadians(90));
    Pose2d pickup3Pose = new Pose2d(pickup3X, pickup3Y, Math.toRadians(90));
    Pose2d pickup4Pose = new Pose2d(pickup4X, pickup4Y, Math.toRadians(90));
    Pose2d scorePose = new Pose2d(scoreX, scoreY, Math.toRadians(-90));
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
        Hang hang = new Hang();

        TrajectoryActionBuilder scorePreload = drive.actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(scorePreloadX, scorePreloadY));

        TrajectoryActionBuilder push = drive.actionBuilder(preloadPose)
                .strafeToConstantHeading(new Vector2d(coord1X, coord1Y))
                .splineToConstantHeading(new Vector2d(push1X, push1Y), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(zone1X, zone1Y))
                .strafeToConstantHeading(new Vector2d(push2X, push2Y))
                .strafeToConstantHeading(new Vector2d(push2X, push2Y))
                .strafeToConstantHeading(new Vector2d(zone2X, zone2Y))
                .strafeToConstantHeading(new Vector2d(push3X, push3Y))
                .strafeToConstantHeading(new Vector2d(zone3X, zone3Y));

        TrajectoryActionBuilder prepPickup1 = drive.actionBuilder(pushPose)
                .afterTime(0.5, () -> {
                    Actions.runBlocking(
                            scoringArm.ArmGrabClip()
                    );
                })
                .strafeToLinearHeading(new Vector2d(prepPickup1X, prepPickup1Y), Math.toRadians(90));

        TrajectoryActionBuilder prepPickup2 = drive.actionBuilder(scorePose)
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY-10))
                .afterTime(0.5, () -> {
                    Actions.runBlocking(
                            scoringArm.ArmGrabClip()
                    );
                })
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder prepPickup3 = drive.actionBuilder(score2Pose)
                .strafeToConstantHeading(new Vector2d(score2X, score2Y-10))
                .afterTime(0.5, () -> {
                    Actions.runBlocking(
                            scoringArm.ArmGrabClip()
                    );
                })
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder prepPickup4 = drive.actionBuilder(score3Pose)
                .strafeToConstantHeading(new Vector2d(score3X, score3Y-10))
                .afterTime(0.5, () -> {
                    Actions.runBlocking(
                            scoringArm.ArmGrabClip()
                    );
                })
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder actualPickup1 = drive.actionBuilder(prepPickup1Pose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder actualPickup2 = drive.actionBuilder(prepPickupPose)
                .strafeToConstantHeading(new Vector2d(pickup2X, pickup2Y));

        TrajectoryActionBuilder actualPickup3 = drive.actionBuilder(prepPickupPose)
                .strafeToConstantHeading(new Vector2d(pickup3X, pickup3Y));

        TrajectoryActionBuilder actualPickup4 = drive.actionBuilder(prepPickupPose)
                .strafeToConstantHeading(new Vector2d(pickup4X, pickup4Y));

        TrajectoryActionBuilder score1 = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(-90));

        TrajectoryActionBuilder score2 = drive.actionBuilder(pickup2Pose)
                .strafeToLinearHeading(new Vector2d(score2X, score2Y), Math.toRadians(-90));

        TrajectoryActionBuilder score3 = drive.actionBuilder(pickup3Pose)
                .strafeToLinearHeading(new Vector2d(score3X, score3Y), Math.toRadians(-90));

        TrajectoryActionBuilder score4 = drive.actionBuilder(pickup4Pose)
                .strafeToLinearHeading(new Vector2d(score4X, score4Y), Math.toRadians(-90));

        TrajectoryActionBuilder park = drive.actionBuilder(score4Pose)
                .strafeToConstantHeading(new Vector2d(score4X, score4Y-10))
                .strafeToConstantHeading(new Vector2d(parkX, parkY));

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

        Action INITIALIZE =
                new ParallelAction(
                        intakeArm.IntakeTransfer(),
                        scoringArm.ArmInitPosition(),
                        hang.PTO()
                );

        Action RETRACT_ALL =
                new ParallelAction(
                        verticalSlides.Retract(),
                        horizontalSlides.HorizontalRetract(),
                        scoringArm.StowArmClose(),
                        intakeArm.IntakeTransfer()
                );

        while (!isStarted() && !opModeIsActive()){
            verticalSlides.autoInitialize(this);
            horizontalSlides.autoInitialize(this);
            scoringArm.initialize(this);
            intakeArm.initialize(this);
            hang.initialize(this);


//            subsystems.initialize(this);

            Actions.runBlocking(
                    INITIALIZE
            );
        }

        waitForStart();

        if (isStopRequested()) return;

        Action SCORE_PRELOAD = scorePreload.build();
        Action PUSH = push.build();
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
                        PUSH,
                            PICKUP1,
                        ACTUAL_PICKUP,
                            PREP_CLIP2,
                        SCORE1,
                            SCORE_CLIP2,
                            PICKUP2,
                        ACTUAL_PICKUP2,
                            PREP_CLIP3,
                        SCORE2,
                            SCORE_CLIP3,
                            PICKUP3,
                        ACTUAL_PICKUP3,
                            PREP_CLIP4,
                        SCORE3,
                            SCORE_CLIP4,
                        PICKUP4,
                        ACTUAL_PICKUP4,
                        PREP_CLIP5,
                        SCORE4,
                        SCORE_CLIP5,
                        PARK
                )
        );
    }
}
