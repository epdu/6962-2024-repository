package org.firstinspires.ftc.teamcode.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;

@Config
@Autonomous(name = "Test Auto", group = "Autonomous")
public class TestAuto extends LinearOpMode{

    double startX = 7;
    double startY = -63.75;
    double startHeading = Math.toRadians(-90);
    double scorePreloadX = 0;
    double scorePreloadY = -33;
    double prepClipX = 62;
    double prepClipY = -47;
    double pickupClipX = 62;
    double pickupCLipY = -62;
    double scoreClipX = 8;
    double scoreClipY = -33;
    double intake1X = -48;
    double intake1Y = -43;
    double scoreBucketX = -57;
    double scoreBucketY = -57;
    double intake2X = -57;
    double intake2Y = -43;
    double prepParkX = -36;
    double prepParkY = -12;
    double parkX = -23;
    double parkY = -12;

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX, startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
        VerticalSlides lift = new VerticalSlides();
        //Claw claw = new Claw();

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(scorePreloadX, scorePreloadY))
                .afterTime(1, () -> {
                    lift.prepClip();
                    //claw.clip();
                })
                .waitSeconds(1)
                .afterTime(2, () -> {
                    lift.scoreClip();
                })
                .afterTime(2.1, () -> {
                    //claw.clawOpen();
                })
                .strafeTo(new Vector2d(prepClipX, prepClipY))
                .afterTime(3, () -> {
                    //claw.stow();
                    lift.retract();
                })
                .afterTime(6.2, () -> {
                    lift.pickupClip();
                    //claw.clip();
                })
                .lineToY(pickupCLipY)
                .afterTime(7.4, () -> {
                    //claw.clawClose();
                    lift.prepClip();
                })
                .waitSeconds(0.5)
                .strafeTo(new Vector2d(scoreClipX, scoreClipY))
                .afterTime(11.5, () -> {
                    lift.scoreClip();
                })
                .afterTime(11.6, () -> {
                    //claw.clawOpen();
                })
                .waitSeconds(1)
                .strafeTo(new Vector2d(intake1X, intake1Y))
                .afterTime(12.6, () -> {
                    lift.retract();
                    //claw.stow();
                })
                .waitSeconds(1)
                .strafeTo(new Vector2d(scoreBucketX, scoreBucketY))
                .afterTime(17.2, () -> {
                    lift.scoreBucket();
                    //claw.bucket();
                })
                .waitSeconds(0.5)
                .afterTime(18, () -> {
                    //claw.clawOpen();
                })
                .strafeTo(new Vector2d(intake2X, intake2Y))
                .afterTime(18.7, () -> {
                    lift.retract();
                    //claw.stow();
                })
                .waitSeconds(1)
                .strafeTo(new Vector2d(scoreBucketX, scoreBucketY))
                .afterTime(21, () -> {
                    lift.scoreBucket();
                    //claw.bucket();
                })
                .waitSeconds(0.5)
                .afterTime(21.8, () -> {
                    //claw.clawOpen();
                })
                .strafeTo(new Vector2d(intake2X, intake2Y))
                .afterTime(22.7, () -> {
                    lift.retract();
                    //claw.stow();
                })
                .waitSeconds(1)
                .strafeTo(new Vector2d(scoreBucketX, scoreBucketY))
                .afterTime(24.9, () -> {
                    lift.scoreBucket();
                    //claw.bucket();
                })
                .waitSeconds(0.5)
                .afterTime(25.8, () -> {
                    //claw.clawOpen();
                })
                .strafeTo(new Vector2d(prepParkX, prepParkY))
                .afterTime(26.7, () -> {
                    lift.retract();
                    //claw.stow();
                })
                .strafeTo(new Vector2d(parkX, parkY));

        while (!isStarted() && !opModeIsActive()) {
            //claw.clawClose();
            telemetry.addLine("Initialized Red Side Auto");
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        Action autoTrajectoryTest = move1.build();

        Actions.runBlocking(
                new SequentialAction(
                        autoTrajectoryTest
                )
        );
    }
}
