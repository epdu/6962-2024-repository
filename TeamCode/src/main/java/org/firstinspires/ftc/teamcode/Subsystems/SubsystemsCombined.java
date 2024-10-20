//package org.firstinspires.ftc.teamcode.Subsystems;
//
//import androidx.annotation.NonNull;
//
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//public class SubsystemsCombined {
//    OpMode opmode;
//
//    HorizontalSlides horizontalSlides;
//    IntakeArm intakeArm;
//    VerticalSlides verticalSlides;
//    ScoringArm scoringArm;
//
//    public SubsystemsCombined() {}
//
//    public void initialize(OpMode opmode, HorizontalSlides horizontalSlides, IntakeArm intakeArm, VerticalSlides verticalSlides, ScoringArm scoringArm) {
//        this.opmode = opmode;
//        this.verticalSlides = verticalSlides;
//        this.horizontalSlides = horizontalSlides;
//        this.scoringArm = scoringArm;
//        this.intakeArm = intakeArm;
//    }
//
//    // Nick's autonomous Action functions
//    public class PrepClipSequence implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            verticalSlides.LiftUpToClip();
//            scoringArm.ArmScoreClip();
//            return false;
//        }
//    }
//    public Action PrepClipSequence() {
//        return new PrepClipSequence();
//    }
//
//    // slides and scoring arm snap the specimen onto bar
//    public class ScoreClipSequence implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            // This could likely break because the slides won't have reached their "target"
//            // and will keep killing themselves trying to get to it, and the claw won't open either
//            // because of the way the function is designed
//
//            // This might also just do nothing, since idk if you're allowed to call an Action within another Action's run()
//            verticalSlides.LiftScoreClip();
//            scoringArm.claw.openClaw();
//
//            return false;
//        }
//    }
//    public Action ScoreClipSequence() {
//        return new ScoreClipSequence();
//    }
//
//    // retract slides and stow arm
//    public class StowScoring implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            scoringArm.StowWholeArm();
//            verticalSlides.Retract();
//            return false;
//        }
//    }
//    public Action StowScoring() {
//        return new StowScoring();
//    }
//}
//
