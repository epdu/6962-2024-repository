package org.firstinspires.ftc.teamcode.Subsystems;

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
//    IntakeArm intakeArm = new IntakeArm();

//    public void initialize(OpMode opmode) {
//        this.opmode = opmode;
//        rHardware.init(opmode.hardwareMap);
//
//        verticalSlides.initialize(opmode);
//
//        horizontalSlides.initialize(opmode);
//
//        scoringArm.initialize(opmode);
//
//        intakeArm.initialize(opmode);
//    }

//    public Action INTAKE_AND_TRANSFER =
//            new SequentialAction(
//                    intakeArm.IntakePickup(),
//                    new SleepAction(0.5),
//                    intakeArm.IntakeTransfer(),
//                    new SleepAction(0.2),
//                    horizontalSlides.HorizontalRetract(),
//                    new SleepAction(0.5),
//                    scoringArm.WholeArmTransfer(),
//                    intakeArm.ClawOpen(),
//                    intakeArm.IntakeHover(),
//                    new ParallelAction(
//                            verticalSlides.LiftUpToHighBucket(),
//                            scoringArm.ArmScoreBucket()
//                    )
//            );
//    public Action EXTEND_INTAKE =
//            new ParallelAction(
//                    intakeArm.IntakeHover(),
//                    horizontalSlides.HorizontalExtend()
//            );
    public Action SCORE_BUCKET = scoringArm.DropBucket();
    public Action LIFT_BUCKET =
            new ParallelAction(
                    verticalSlides.LiftUpToHighBucket(),
                    scoringArm.ArmScoreBucket()
            );
    public Action RETRACT_VERTICAL =
            new ParallelAction(
                    scoringArm.StowWholeArm(),
                    verticalSlides.Retract()
            );
//    public Action RETRACT_ALL =
//            new ParallelAction(
//                    verticalSlides.Retract(),
//                    horizontalSlides.HorizontalRetract(),
//                    scoringArm.StowArmClose(),
//                    intakeArm.IntakeTransfer()
//            );
//    public Action INITIALIZE =
//            new ParallelAction(
//                    intakeArm.IntakeTransfer(),
//                    scoringArm.ArmInitPosition()
//            );
    public Action PREP_CLIP =
            new ParallelAction(
                    scoringArm.ArmScoreClip(),
                    verticalSlides.LiftUpToClip(),
                    horizontalSlides.HorizontalRetract()
            );
    public Action SCORE_CLIP =
            new SequentialAction(
                    verticalSlides.SlamScoreClip(),
                    scoringArm.StowWholeArm(),
                    verticalSlides.Retract()
            );
    public Action PICKUP_CLIP =
            new SequentialAction(
                    scoringArm.ArmGrabClip()
            );
}
