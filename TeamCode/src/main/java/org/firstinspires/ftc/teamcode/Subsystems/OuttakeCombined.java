package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Util.RobotHardware;
public class OuttakeCombined {
    OpMode opmode;

    VerticalSlides verticalSlides;
    ScoringArm scoringArm;
    CustomTimer timer;

    public OuttakeCombined() {}

    public void initialize(OpMode opmode, VerticalSlides verticalSlides, ScoringArm scoringArm, CustomTimer timer) {
        this.opmode = opmode;
        this.verticalSlides = verticalSlides;
        this.scoringArm = scoringArm;
        this.timer = timer;
    }

    public void operateTest() {
        verticalSlides.operateVincent(); // using operateVincent is not a mistake, please leave here
        scoringArm.operateVincent();

        if (opmode.gamepad1.y) {
            autoPrepHighBucket();
        } else if (opmode.gamepad1.x) {
            autoTransfer();
        }
    }

    public void shutdown() {
        verticalSlides.shutdown();
        scoringArm.shutdown();
        timer.shutdown();
    }


    /** This is guaranteed not to work first try, but idk which section is wrong until I can CameraPortal it*/
    public void autoTransferAndScore() {
        /** this is probably what will break, but idk what will break*/
//        if (intake.correctPiece()) {
        autoTransfer();
        verticalSlides.raiseToHighBucket();     // extending slides
        scoringArm.wrist.setWristScoringBucket();
        scoringArm.arm.scoreArm();              // flip arm over to score
        timer.safeDelay(2000);                   // wait for __ milliseconds
        scoringArm.wrist.setWristScoringBucket();     // prepping wrist to drop pixel (currently useless because the intake is now starting horizontal)
        timer.safeDelay(2000);
//            claw.openClaw();                      // drop sample
        verticalSlides.retract();               // retract slides
        scoringArm.arm.stowArm();               // stow arm while retracting (might switch order)
//        }
    }

    public void autoTransfer() {
        // check both slides fully retracted and ready to transfer
        /** this is probably what will break, but idk what will break*/
        if (!scoringArm.claw.isClawOpen) {scoringArm.claw.openClaw();}
        if (!verticalSlides.verticalSlidesRetracted) {verticalSlides.retract();}

        scoringArm.wholeArmTransfer();
        timer.safeDelay(750);

        scoringArm.claw.closeClaw();
        scoringArm.arm.stowArm();
        scoringArm.wrist.setWristStow();
    }

    // extends vertical slides, and sets arm to scoring, but doesn't open claw, then retracts once claw opens
    // idk if the while loop will break everything, but it shouldn't
    public void autoPrepHighBucket() {
        verticalSlides.raiseToHighBucket();     // extending slides
        scoringArm.wrist.setWristScoringBucket();
        scoringArm.arm.scoreArm();              // flip arm over to score
        while(!scoringArm.claw.isClawOpen) {}/** THIS LINE MIGHT PAUSE THE WHOLE SUBSYSTEM AND NOT ALLOW THE CLAW TO OPEN, BUT HOPEFULLY IT'S FINE*/
        scoringArm.arm.stowArm();
        scoringArm.wrist.setWristStow();
        verticalSlides.retract();
    }

    public void autoPrepLowBucket() {
        verticalSlides.raiseToLowBucket();
        scoringArm.wrist.setWristScoringBucket();
        scoringArm.arm.scoreArm();
        while(!scoringArm.claw.isClawOpen) {}
        scoringArm.arm.stowArm();
        scoringArm.wrist.setWristStow();
        verticalSlides.retract();
    }
}

