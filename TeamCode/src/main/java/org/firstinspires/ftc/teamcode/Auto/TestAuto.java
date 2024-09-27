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
        Claw claw = new Claw();

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(scorePreloadX, scorePreloadY))
                .afterTime(1, () -> {
                    //lift.prepClip(); not defined yet
                })
                .waitSeconds(1)
                .afterTime(2, () -> {
                    lift.scoreClip();
                    claw.clawOpen();
                })
                .strafeTo(new Vector2d(prepClipX, prepClipY))
                .afterTime(6.2, () -> {
                    //lift.pickupClip(); not defined yet
                })
                .lineToY(pickupCLipY)
                .afterTime(7.4, () -> {
                    claw.clawClose();
                    lift.scoreClip();
                })
                .waitSeconds(0.5)
                .strafeTo(new Vector2d(scoreClipX, scoreClipY))
                .waitSeconds(1)
                .strafeTo(new Vector2d(intake1X, intake1Y))
                .waitSeconds(1)
                .strafeTo(new Vector2d(scoreBucketX, scoreBucketY))
                .waitSeconds(0.5)
                .strafeTo(new Vector2d(intake2X, intake2Y))
                .waitSeconds(1)
                .strafeTo(new Vector2d(scoreBucketX, scoreBucketY))
                .waitSeconds(0.5)
                .strafeTo(new Vector2d(intake2X, intake2Y))
                .waitSeconds(1)
                .strafeTo(new Vector2d(scoreBucketX, scoreBucketY))
                .waitSeconds(0.5)
                .strafeTo(new Vector2d(prepParkX, prepParkY))
                .strafeTo(new Vector2d(parkX, parkY));

        while (!isStarted() && !opModeIsActive()) {
            claw.clawClose();
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
