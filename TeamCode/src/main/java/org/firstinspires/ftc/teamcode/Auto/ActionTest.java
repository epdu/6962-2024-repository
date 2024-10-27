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
@Autonomous(name = "Action test", group = "Autonomous")
public class ActionTest extends LinearOpMode{
    public static double startX = 7;
    public static double startY = -63.75;
    public static double startHeading = Math.toRadians(-90);
    public static double scorePreloadX = 0;
    public static double scorePreloadY = -39;
    public static double scorePreload2X = -6;
    public static double scorePreload2Y = -39;
    public static double prepPickupX = 64;
    public static double prepPickupY = -46;
    public static double pickupX = 65;
    public static double pickupY = -58;
    public static double parkX = 36;
    public static double parkY = -60;
    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX,startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        HorizontalSlides horizontalSlides = new HorizontalSlides();
        IntakeArm intakeArm = new IntakeArm();
        VerticalSlides verticalSlides = new VerticalSlides();
        ScoringArm scoringArm = new ScoringArm();

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .afterTime(0, () -> {
                    Actions.runBlocking(
                            new ParallelAction(
                                    verticalSlides.LiftUpToClip(),
                                    scoringArm.ArmScoreClip()
                            )
                    );
                })
                .waitSeconds(1)
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
                .strafeTo(new Vector2d(parkX, parkY))
                .turnTo(Math.toRadians(90));

        while(!isStarted() && !opModeIsActive()) {
            horizontalSlides.initialize(this);
            intakeArm.initialize(this);
            verticalSlides.initialize(this);
            scoringArm.initialize(this);

            telemetry.addLine("Initialized 2+0 Auto");
            telemetry.update();
            //run on init NO MOTORS
            Actions.runBlocking(
                    new ParallelAction(
                            scoringArm.WholeArmTransfer(),
                            horizontalSlides.HorizontalRetract(),
                            intakeArm.IntakeTransfer()
                    )
            );
        }
        waitForStart();
        if (isStopRequested()) {
            Actions.runBlocking(
                    new ParallelAction(
                            scoringArm.WholeArmTransfer(),
                            verticalSlides.Retract(),
                            intakeArm.IntakeTransfer(),
                            horizontalSlides.HorizontalRetract()
                    )
            );
            sleep(1000);
            return;
        }

        Action preloadAutoTrajectory = move1.build();

        Actions.runBlocking(
                new SequentialAction(
                        preloadAutoTrajectory
                )
        );

    }
}