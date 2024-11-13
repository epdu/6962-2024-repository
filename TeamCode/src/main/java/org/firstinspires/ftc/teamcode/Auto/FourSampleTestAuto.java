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
import org.firstinspires.ftc.teamcode.Subsystems.SubsystemCommands;

@Config
@Autonomous(name = "0+4 Test", group = "Autonomous", preselectTeleOp = "Solo Full Robot TeleOp")
public class FourSampleTestAuto extends LinearOpMode{

    public static double startX = -40;
    public static double startY = -63.5;
    public static double startHeading = Math.toRadians(90);
    public static double intake1X = -48;
    public static double intake1Y = -49;
    public static double scoreBucketX = -58;
    public static double scoreBucketY = -56;
    public static double intake2X = -58.5;
    public static double intake2Y = -49;
    public static double intake3X = -46.5;
    public static double intake3Y = -26;
    public static double parkX = -30;
    public static double parkY = -18;
    Pose2d startPose = new Pose2d(startX, startY, startHeading);
    Pose2d bucketPose = new Pose2d(scoreBucketX, scoreBucketY, Math.toRadians(45));
    Pose2d intake1Pose = new Pose2d(intake1X, intake1Y, Math.toRadians(90));
    Pose2d intake2Pose = new Pose2d(intake2X, intake2Y, Math.toRadians(90));
    Pose2d intake3Pose = new Pose2d(intake3X, intake3Y, Math.toRadians(180));

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
        SubsystemCommands subsystems = new SubsystemCommands();

        //defining movement trajectories
        TrajectoryActionBuilder scorePreload = drive.actionBuilder(startPose)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder intake1 = drive.actionBuilder(bucketPose)
                .strafeToLinearHeading(new Vector2d(intake1X, intake1Y), Math.toRadians(90));

        TrajectoryActionBuilder score1 = drive.actionBuilder(intake1Pose)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder intake2 = drive.actionBuilder(bucketPose)
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y), Math.toRadians(90));

        TrajectoryActionBuilder score2 = drive.actionBuilder(intake2Pose)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder intake3 = drive.actionBuilder(bucketPose)
                .strafeToLinearHeading(new Vector2d(intake3X, intake3Y), Math.toRadians(180));

        TrajectoryActionBuilder score3 = drive.actionBuilder(intake3Pose)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45));

        TrajectoryActionBuilder park = drive.actionBuilder(bucketPose)
                .strafeToLinearHeading(new Vector2d(parkX, parkY), Math.toRadians(90));

        while (!isStarted() && !opModeIsActive()) {
//            subsystems.initialize(this);

            telemetry.addLine("Initialized Test 0+4 Auto");
            telemetry.update();
//            Actions.runBlocking(
////                    subsystems.INITIALIZE
//            );
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

        Actions.runBlocking(
                new SequentialAction(
//                            subsystems.RETRACT_ALL,
                        SCORE_PRELOAD,
                            subsystems.LIFT_BUCKET,
                            subsystems.SCORE_BUCKET,
                            subsystems.RETRACT_VERTICAL,
                        INTAKE1,
//                            subsystems.EXTEND_INTAKE,
//                            subsystems.INTAKE_AND_TRANSFER,
                        SCORE1,
                            subsystems.SCORE_BUCKET,
                            subsystems.RETRACT_VERTICAL,
                        INTAKE2,
//                            subsytems.EXTEND_INTAKE,
//                            subsystems.INTAKE_AND_TRANSFER,
                        SCORE2,
                            subsystems.SCORE_BUCKET,
                            subsystems.RETRACT_VERTICAL,
                        INTAKE3,
//                            subsystems.EXTEND_INTAKE,
//                            subsystems.INTAKE_AND_TRANSFER,
                        SCORE3,
                            subsystems.SCORE_BUCKET,
//                            subsystems.RETRACT_ALL,
                        PARK
                )
        );
    }
}
