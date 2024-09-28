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
@Autonomous(name = "Preload Auto", group = "Autonomous")
public class PreloadAuto extends LinearOpMode{
    private double startX = 7;
    private double startY = -63.75;
    private double startHeading = Math.toRadians(-90);
    private double scorePreloadX = 0;
    private double scorePreloadY = -33;
    private double parkX = 36;
    private double parkY = -60;

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(startX,startY, startHeading);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(scorePreloadX, scorePreloadY))
                .waitSeconds(1)
                .strafeTo(new Vector2d(parkX, parkY));

        while(!isStarted() && !opModeIsActive()) {
            telemetry.addLine("Initialized Preload Auto");
            telemetry.update();
        }
        waitForStart();
        if (isStopRequested()) return;

        Action preloadAutoTrajectory = move1.build();

        Actions.runBlocking(
                new SequentialAction(
                        preloadAutoTrajectory
                )
        );

    }
}
