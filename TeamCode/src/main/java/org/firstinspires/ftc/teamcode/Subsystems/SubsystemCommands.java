package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;

public class SubsystemCommands {
    OpMode opmode;
    RobotHardware rHardware;
    VerticalSlides verticalSlides = new VerticalSlides();
    HorizontalSlides horizontalSlides = new HorizontalSlides();
    ScoringArm scoringArm = new ScoringArm();
    IntakeArm intakeArm = new IntakeArm();

    public void initialize(OpMode opmode) {
        this.opmode = opmode;
        rHardware.init(opmode.hardwareMap);

        verticalSlides.initialize(opmode);

        horizontalSlides.initialize(opmode);

        scoringArm.initialize(opmode);

        intakeArm.initialize(opmode);
    }

    public class intake_and_transfer implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
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
            );
            return false;
        }
    }
    public Action INTAKE_AND_TRANSFER() {
        return new intake_and_transfer();
    }

    public class extend_intake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            new ParallelAction(
                    intakeArm.IntakeHover(),
                    horizontalSlides.HorizontalExtend()
            );
            return false;
        }
    }
    public Action EXTEND_INTAKE() {
        return new extend_intake();
    }

    public class score_bucket implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            scoringArm.DropBucket();
            return false;
        }
    }
    public Action SCORE_BUCKET() {return new score_bucket();}

    public class lift_bucket implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            new ParallelAction(
                    verticalSlides.LiftUpToHighBucket(),
                    scoringArm.ArmScoreBucket()
            );
            return false;
        }
    }
    public Action LIFT_BUCKET() {
        return new lift_bucket();
    }

    public class retract_vertical implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            new ParallelAction(
                    scoringArm.StowWholeArm(),
                    verticalSlides.Retract()
            );
            return false;
        }
    }
    public Action RETRACT_VERTICAL() {
        return new retract_vertical();
    }

    public class retract_all implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            new ParallelAction(
                    verticalSlides.Retract(),
                    horizontalSlides.HorizontalRetract(),
                    scoringArm.StowArmClose(),
                    intakeArm.IntakeTransfer()
            );
            return false;
        }
    }
    public Action RETRACT_ALL() {
        return new retract_all();
    }

    public class initialize implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            new ParallelAction(
                    intakeArm.IntakeTransfer(),
                    scoringArm.ArmInitPosition()
            );
            return false;
        }
    }
    public Action INITIALIZE() {
        return new initialize();
    }

    public class prep_clip implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            new ParallelAction(
                    scoringArm.ArmScoreClip(),
                    verticalSlides.LiftUpToClip(),
                    horizontalSlides.HorizontalRetract()
            );
            return false;
        }
    }
    public Action PREP_CLIP() {
        return new prep_clip();
    }

    public class score_clip implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            new SequentialAction(
                    verticalSlides.SlamScoreClip(),
                    scoringArm.StowWholeArm(),
                    verticalSlides.Retract()
            );
            return false;
        }
    }
    public Action SCORE_CLIP() {return new score_clip();}

    public class pickup_clip implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            new SequentialAction(
                    scoringArm.ArmGrabClip()
            );
            return false;
        }
    }
    public Action PICKUP_CLIP() {
        return new pickup_clip();
    }
}
