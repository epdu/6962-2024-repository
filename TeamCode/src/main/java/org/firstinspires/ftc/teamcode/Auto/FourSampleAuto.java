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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@Config
@Autonomous(name = "0+4 Auto", group = "Autonomous", preselectTeleOp = "Solo Full Robot TeleOp")
public class FourSampleAuto extends LinearOpMode{

    public static double startX = -40;
    public static double startY = -63.5;
    public static double startHeading = Math.toRadians(90);
    public static double scorePreloadX = 0;
    public static double scorePreloadY = -41;
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

//    public ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() {
//        timer.reset();
        Pose2d startPose = new Pose2d(startX, startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
//        CustomTimer timer = new CustomTimer();

        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();
        IntakeArm intakeArm = new IntakeArm();
        HorizontalSlides horizontalSlides = new HorizontalSlides();
        CustomTimer timeout = new CustomTimer();

        TrajectoryActionBuilder traj = drive.actionBuilder(startPose)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                verticalSlides.LiftUpToHighBucket(),
                                scoringArm.ArmScoreBucket(),
                                horizontalSlides.HorizontalRetract()
                            )
                    );
                })
                .afterTime(1, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    scoringArm.DropBucket(),
                                    intakeArm.IntakeHover(),
                                    horizontalSlides.HorizontalExtend()
                            )
                    );
                })
                .waitSeconds(2)
                .strafeToLinearHeading(new Vector2d(intake1X, intake1Y), Math.toRadians(90))
                //first intake
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    verticalSlides.Retract(),
                                    scoringArm.StowWholeArm(),
                                    new SequentialAction(
                                            intakeArm.IntakePickup(),
                                            new SleepAction(0.5),
                                            intakeArm.IntakeTransfer(),
                                            new SleepAction(0.2),
                                            horizontalSlides.HorizontalRetract(),
                                            new SleepAction(0.5),
                                            scoringArm.WholeArmTransfer(),
                                            intakeArm.ClawOpen(),
                                            new ParallelAction(
                                                    verticalSlides.LiftUpToHighBucket(),
                                                    scoringArm.ArmScoreBucket()
                                            )
                                    )
                            )
                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                //first bucket
                .afterTime( 0.2, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    scoringArm.DropBucket(),
                                    intakeArm.IntakeHover(),
                                    horizontalSlides.HorizontalExtend()
                            )
                    );
                })
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y), Math.toRadians(90))
                //second intake
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    verticalSlides.Retract(),
                                    scoringArm.StowWholeArm(),
                                    new SequentialAction(
                                            intakeArm.IntakePickup(),
                                            new SleepAction(0.5),
                                            intakeArm.IntakeTransfer(),
                                            new SleepAction(0.2),
                                            horizontalSlides.HorizontalRetract(),
                                            new SleepAction(0.5),
                                            scoringArm.WholeArmTransfer(),
                                            intakeArm.ClawOpen(),
                                            new ParallelAction(
                                                    verticalSlides.LiftUpToHighBucket(),
                                                    scoringArm.ArmScoreBucket()
                                            )
                                    )
                            )

                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                //second bucket
                .afterTime( 0.2, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    scoringArm.DropBucket(),
                                    intakeArm.IntakeHoverPerpendicular(),
                                    horizontalSlides.HorizontalExtend()
                            )
                    );
                })
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(intake3X, intake3Y), Math.toRadians(180))
                //third intake
                .afterTime( 0, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    verticalSlides.Retract(),
                                    scoringArm.StowWholeArm(),
                                    new SequentialAction(
                                            intakeArm.IntakePickup(),
                                            new SleepAction(0.5),
                                            intakeArm.IntakeTransfer(),
                                            new SleepAction(0.5),
                                            horizontalSlides.HorizontalRetract(),
                                            new SleepAction(0.5),
                                            scoringArm.WholeArmTransfer(),
                                            intakeArm.ClawOpen(),
                                            new ParallelAction(
                                                    verticalSlides.LiftUpToHighBucket(),
                                                    scoringArm.ArmScoreBucket()
                                            )
                                    )
                            )

                    );
                })
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(scoreBucketX, scoreBucketY), Math.toRadians(45))
                //third bucket
                .afterTime( 0.2, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    scoringArm.DropBucket()
                            )
                    );
                })
                .waitSeconds(0.5)
                .afterTime(0.2, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    verticalSlides.Retract(),
                                    scoringArm.StowArmClose(),
                                    intakeArm.IntakeTransfer(),
                                    horizontalSlides.HorizontalRetract()
                            )
                    );
                })
                .strafeToLinearHeading(new Vector2d(parkX, parkY), Math.toRadians(90));

        TrajectoryActionBuilder traj2 = drive.actionBuilder(startPose)
                .waitSeconds(29)
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new SequentialAction(
                                    verticalSlides.Retract(),
                                    scoringArm.StowArmClose(),
                                    intakeArm.IntakeTransfer(),
                                    horizontalSlides.HorizontalRetract()
                            )
                    );
                });


        while (!isStarted() && !opModeIsActive()) {
            intakeArm.initialize(this);
            verticalSlides.autoInitialize(this);
            scoringArm.initialize(this);
            horizontalSlides.initialize(this);


            telemetry.addLine("Initialized 0+4 Auto");
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

        Action fourSampleTrajectory = traj.build();
        Action daniel = traj2.build();

        Actions.runBlocking(
                new ParallelAction(
                        fourSampleTrajectory,
                        daniel

                )
        );
    }
}
