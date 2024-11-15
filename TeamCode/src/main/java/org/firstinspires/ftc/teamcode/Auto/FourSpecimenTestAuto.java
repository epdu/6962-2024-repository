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
//import org.firstinspires.ftc.teamcode.Subsystems.SubsystemCommands;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@Config
@Autonomous(name = "4+0 Test", group = "Autonomous", preselectTeleOp = "Solo Full Robot TeleOp")
public class FourSpecimenTestAuto extends LinearOpMode {

    public static double startX = 8;
    public static double startY = -63.5;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = 5;
    public static double scorePreloadY = -34;
    public static double coord1X = 30;
    public static double coord1Y = -35;
    public static double push1X = 47;
    public static double push1Y = -13;
    public static double zone1X = 47;
    public static double zone1Y = -52;
    public static double push2X = 57;
    public static double push2Y = -13;
    public static double zone2X = 57;
    public static double zone2Y = -54;
    public static double prepPickupX = 48;
    public static double prepPickupY = -49;
    public static double pickupX = 48;
    public static double pickupY = -56;
    public static double scoreX = 3;
    public static double scoreY = -36;
    public static double score2X = 1;
    public static double score2Y = -36;
    public static double score3X = -1;
    public static double score3Y = -36;
    public static double parkX = 45;
    public static double parkY = -60;
    Pose2d startPose = new Pose2d(startX, startY, startHeading);
    Pose2d preloadPose = new Pose2d(scorePreloadX, scorePreloadY, Math.toRadians(-90));
    Pose2d pushPose = new Pose2d(zone2X, zone2Y, Math.toRadians(-90));
    Pose2d prepPickupPose = new Pose2d(prepPickupX, prepPickupY, Math.toRadians(90));
    Pose2d pickupPose = new Pose2d(pickupX, pickupY, Math.toRadians(90));
    Pose2d scorePose = new Pose2d(scoreX, scoreY, Math.toRadians(-90));

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
//        SubsystemCommands subsystems = new SubsystemCommands();

        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();
        IntakeArm intakeArm = new IntakeArm();
        HorizontalSlides horizontalSlides = new HorizontalSlides();

        TrajectoryActionBuilder scorePreload = drive.actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(scorePreloadX, scorePreloadY));

        TrajectoryActionBuilder push = drive.actionBuilder(preloadPose)
                .strafeToConstantHeading(new Vector2d(coord1X, coord1Y))
                .splineToConstantHeading(new Vector2d(push1X, push1Y), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(zone1X, zone1Y))
                .splineToConstantHeading(new Vector2d(push2X, push2Y), Math.toRadians(0))
                .strafeToConstantHeading(new Vector2d(zone2X, zone2Y));

        TrajectoryActionBuilder pickup1 = drive.actionBuilder(pushPose)
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder actualPickup = drive.actionBuilder(prepPickupPose)
                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));

        TrajectoryActionBuilder score = drive.actionBuilder(pickupPose)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(-90));

        TrajectoryActionBuilder prepPickup2 = drive.actionBuilder(scorePose)
                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));

        TrajectoryActionBuilder park = drive.actionBuilder(scorePose)
                .strafeToConstantHeading(new Vector2d(parkX, parkY));

        Action parkTest = score.fresh()
                .strafeToConstantHeading(new Vector2d(parkX, parkY))
                .build();

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
                verticalSlides.Retract()
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
                        scoringArm.ArmInitPosition()
                );

        while (!isStarted() && !opModeIsActive()){
            verticalSlides.initialize(this);
            horizontalSlides.initialize(this);
            scoringArm.initialize(this);
            intakeArm.initialize(this);

//            subsystems.initialize(this);

            Actions.runBlocking(
                    INITIALIZE
            );
        }

        waitForStart();

        if (isStopRequested()) return;

        Action SCORE_PRELOAD = scorePreload.build();
        Action PUSH = push.build();
        Action PICKUP1 = pickup1.build();
        Action ACTUAL_PICKUP = actualPickup.build();
        Action SCORE = score.build();
        Action PICKUP2 = prepPickup2.build();
        Action PARK = park.build();

        Actions.runBlocking(
                new SequentialAction(
                            PREP_CLIP,
                        SCORE_PRELOAD,
                            SCORE_CLIP,
                        PUSH,
                        PICKUP1,
                            PICKUP_CLIP,
                        ACTUAL_PICKUP,
                            PREP_CLIP2,
                        SCORE,
                            SCORE_CLIP2,
                        PICKUP2,
                            PICKUP_CLIP2,
                        ACTUAL_PICKUP,
                            PREP_CLIP3,
                        SCORE,
                            SCORE_CLIP3,
                        PICKUP2,
                            PICKUP_CLIP3,
                        ACTUAL_PICKUP,
                            PREP_CLIP4,
                        SCORE,
                            SCORE_CLIP4,
                        PARK
                        //parkTest
                )
        );
    }
}
