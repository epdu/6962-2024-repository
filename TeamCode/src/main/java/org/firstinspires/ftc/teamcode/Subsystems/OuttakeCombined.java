//package org.firstinspires.ftc.teamcode.Subsystems;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//public class OuttakeCombined {
//    OpMode opmode;
//
//    VerticalSlides verticalSlides;
//    ScoringArm scoringArm;
//    CustomTimer timer;
//
//    public OuttakeCombined() {}
//
//    public void initialize(OpMode opmode, VerticalSlides verticalSlides, ScoringArm scoringArm, CustomTimer timer) {
//        this.opmode = opmode;
//        this.verticalSlides = verticalSlides;
//        this.scoringArm = scoringArm;
//        this.timer = timer;
//    }
//
//    public void operateTest() {
//        if (opmode.gamepad1.y) {
//            autoPrepHighBucket();
//        } else if (opmode.gamepad1.x) {
//            autoTransfer();
//        }
//    }
//
//    public void autoTransferAndScore() {
////        if (intake.correctPiece()) {
//        autoTransfer();
//        verticalSlides.raiseToHighBucket();     // extending slides
//        scoringArm.wrist.setWristScoringBucket();
//        scoringArm.arm.setArmScoreBucket();              // flip arm over to score
//        timer.safeDelay(2000);                   // wait for __ milliseconds
//        scoringArm.wrist.setWristScoringBucket();     // prepping wrist to drop pixel (currently useless because the intake is now starting horizontal)
//        timer.safeDelay(2000);
////            claw.openClaw();                      // drop sample
//        verticalSlides.retract();               // retract slides
//        scoringArm.arm.setArmStow();               // stow arm while retracting (might switch order)
////        }
//    }
//
//    public void autoTransfer() {
//        // check both slides fully retracted and ready to transfer
//        if (!scoringArm.claw.isClawOpen) {scoringArm.claw.openClaw();}
//        if (!verticalSlides.verticalSlidesRetracted) {verticalSlides.retract();}
//        while (!verticalSlides.atTarget) {verticalSlides.operateVincent();}
//        scoringArm.wholeArmTransfer();
//        timer.safeDelay(750);
//        scoringArm.claw.closeClaw();
//        timer.safeDelay(200);
//        scoringArm.arm.setArmStow();
//        scoringArm.wrist.setWristStow();
//    }
//
//    // extends vertical slides, and sets arm to scoring, but doesn't open claw, then retracts once claw opens
//    public void autoPrepHighBucket() {
//        scoringArm.wrist.setWristScoringBucket();
//        scoringArm.arm.setArmScoreBucket();
//        verticalSlides.raiseToHighBucket();
//        timer.safeDelay(100);
//        while (!verticalSlides.atTarget) {verticalSlides.operateVincent();}
//        timer.safeDelay(1000);
//        scoringArm.claw.openClaw();
//        timer.safeDelay(500);
//        scoringArm.arm.setArmStow();
//        scoringArm.wrist.setWristStow();
//        verticalSlides.retract();
//    }
//
//    public void autoPrepLowBucket() {
//        verticalSlides.raiseToLowBucket();
//        scoringArm.wrist.setWristScoringBucket();
//        scoringArm.arm.setArmScoreBucket();
//        while(!scoringArm.claw.isClawOpen) {}
//        scoringArm.arm.setArmStow();
//        scoringArm.wrist.setWristStow();
//        verticalSlides.retract();
//    }
//}