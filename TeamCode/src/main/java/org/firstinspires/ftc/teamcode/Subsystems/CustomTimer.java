package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

public class CustomTimer {

    VerticalSlides verticalSlides = new VerticalSlides();
    ScoringArm scoringArm = new ScoringArm();
    IntakeArm intakeArm = new IntakeArm();
    HorizontalSlides horizontalSlides = new HorizontalSlides();

    public ElapsedTime timer = new ElapsedTime();
    public CustomTimer() {}
    public void initialize() {}
    public void operate() {}
    public void safeDelay(double delayInMillis){
        long start = System.currentTimeMillis();
        while((System.currentTimeMillis() - start < delayInMillis)){
            // wait, do nothing
        }
    }

    public class Timeout implements Action {
        public boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            initialized = true;
            timer.reset();
            while (initialized) {
                if (timer.time() >= 29000) {
                    Actions.runBlocking(
                            new ParallelAction(
                                    scoringArm.StowWholeArm(),
                                    verticalSlides.Retract(),
                                    horizontalSlides.HorizontalRetract(),
                                    intakeArm.IntakeTransfer()
                            )
                    );
                }
            }
            return false;
        }
    }

    public Action Timeout() {return new Timeout();}
}
