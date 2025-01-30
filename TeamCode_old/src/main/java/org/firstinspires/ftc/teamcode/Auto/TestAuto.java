//package org.firstinspires.ftc.teamcode.Auto;
//
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.ParallelAction;
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.SequentialAction;
//import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
//import com.acmerobotics.roadrunner.Vector2d;
//import com.acmerobotics.roadrunner.ftc.Actions;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
//import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
//import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
//import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
//import org.firstinspires.ftc.teamcode.Subsystems.SubsystemCommands;
//import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;
//
//public class TestAuto extends OpMode {
//    public static double startX = 8;
//    public static double startY = -63.5;
//    public static double startHeading = Math.toRadians(-90);
//    public static double scorePreloadX = -3;
//    public static double scorePreloadY = -35;
//    public static double coord1X = 30;
//    public static double coord1Y = -35;
//    public static double push1X = 47;
//    public static double push1Y = -15;
//    public static double zone1X = 47;
//    public static double zone1Y = -52;
//    public static double push2X = 55;
//    public static double push2Y = -15;
//    public static double zone2X = 57;
//    public static double zone2Y = -54;
//    public static double prepPickupX = 48;
//    public static double prepPickupY = -49;
//    public static double pickupX = 48;
//    public static double pickupY = -60;
//    public static double scoreX = 0;
//    public static double scoreY = -35;
//    public static double score2X = 3;
//    public static double score2Y = -35;
//    public static double score3X = 6;
//    public static double score3Y = -34;
//    public static double parkX = 45;
//    public static double parkY = -60;
//    Pose2d startPose = new Pose2d(startX, startY, startHeading);
//    Pose2d preloadPose = new Pose2d(scorePreloadX, scorePreloadY, Math.toRadians(-90));
//    Pose2d pushPose = new Pose2d(zone2X, zone2Y, Math.toRadians(-90));
//    Pose2d prepPickupPose = new Pose2d(prepPickupX, prepPickupY, Math.toRadians(90));
//    Pose2d pickupPose = new Pose2d(pickupX, pickupY, Math.toRadians(90));
//    Pose2d scorePose = new Pose2d(scoreX, scoreY, Math.toRadians(-90));
//
//    MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
//
//    VerticalSlides verticalSlides = new VerticalSlides();
//    ScoringArm scoringArm = new ScoringArm();
//    IntakeArm intakeArm = new IntakeArm();
//    HorizontalSlides horizontalSlides = new HorizontalSlides();
//    SubsystemCommands subsystemCommands = new SubsystemCommands();
//
//    @Override
//    public void init() {
//
//        scorePreload = drive.actionBuilder(startPose)
//                .strafeToConstantHeading(new Vector2d(scorePreloadX, scorePreloadY))
//                .build();
//
//        TrajectoryActionBuilder push = drive.actionBuilder(preloadPose)
//                .strafeToConstantHeading(new Vector2d(coord1X, coord1Y))
//                .splineToConstantHeading(new Vector2d(push1X, push1Y), Math.toRadians(0))
//                .strafeToConstantHeading(new Vector2d(zone1X, zone1Y))
//                .splineToConstantHeading(new Vector2d(push2X, push2Y), Math.toRadians(0))
//                .strafeToConstantHeading(new Vector2d(zone2X, zone2Y));
//
//        TrajectoryActionBuilder prepPickup1 = drive.actionBuilder(pushPose)
//                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));
//
//        TrajectoryActionBuilder prepPickup2 = drive.actionBuilder(scorePose)
//                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));
//
//        TrajectoryActionBuilder prepPickup3 = drive.actionBuilder(scorePose)
//                .strafeToLinearHeading(new Vector2d(prepPickupX, prepPickupY), Math.toRadians(90));
//
//        TrajectoryActionBuilder actualPickup1 = drive.actionBuilder(prepPickupPose)
//                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));
//
//        TrajectoryActionBuilder actualPickup2 = drive.actionBuilder(prepPickupPose)
//                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));
//
//        TrajectoryActionBuilder actualPickup3 = drive.actionBuilder(prepPickupPose)
//                .strafeToConstantHeading(new Vector2d(pickupX, pickupY));
//
//        TrajectoryActionBuilder score1 = drive.actionBuilder(pickupPose)
//                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(-90));
//
//        TrajectoryActionBuilder score2 = drive.actionBuilder(pickupPose)
//                .strafeToLinearHeading(new Vector2d(score2X, score2Y), Math.toRadians(-90));
//
//        TrajectoryActionBuilder score3 = drive.actionBuilder(pickupPose)
//                .strafeToLinearHeading(new Vector2d(score3X, score3Y), Math.toRadians(-90));
//
//        TrajectoryActionBuilder park = drive.actionBuilder(scorePose)
//                .strafeToConstantHeading(new Vector2d(parkX, parkY));
//
//        verticalSlides.initialize(this);
//        horizontalSlides.initialize(this);
//        scoringArm.initialize(this);
//        intakeArm.initialize(this);
//
//        Actions.runBlocking(
//                subsystemCommands.INITIALIZE()
//        );
//
//
//        Action Test4AutoTrajectory = new SequentialAction(
//                new ParallelAction(
//                        subsystemCommands.PREP_CLIP(),
//                        SCORE_PRELOAD
//                ),
//                subsystemCommands.SCORE_CLIP(),
//                PUSH,
//                new ParallelAction(
//                        PICKUP1,
//                        subsystemCommands.PICKUP_CLIP()
//                ),
//                ACTUAL_PICKUP,
//                subsystemCommands.PREP_CLIP(),
//                SCORE1,
//                subsystemCommands.SCORE_CLIP(),
//                new ParallelAction(
//                        PICKUP2,
//                        subsystemCommands.PICKUP_CLIP()
//                ),
//                ACTUAL_PICKUP2,
//                subsystemCommands.PREP_CLIP(),
//                SCORE2,
//                subsystemCommands.SCORE_CLIP(),
//                new ParallelAction(
//                        PICKUP3,
//                        subsystemCommands.PICKUP_CLIP()
//                ),
//                ACTUAL_PICKUP3,
//                subsystemCommands.PREP_CLIP(),
//                SCORE3,
//                subsystemCommands.SCORE_CLIP(),
//                PARK
//        );
//    }
//
//    @Override
//    public void loop() {
//
//    }
//}
