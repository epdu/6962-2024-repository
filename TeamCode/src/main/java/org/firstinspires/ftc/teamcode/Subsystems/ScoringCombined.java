//package org.firstinspires.ftc.teamcode.Subsystems;
//
//import androidx.annotation.NonNull;
//
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//public class ScoringCombined {
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//    // DEPRECATED CLASS     DEPRECATED CLASS
//
//    OpMode opmode;
//
//    HorizontalSlides horizontalSlides;
//    Intake intake;
//    VerticalSlides verticalSlides;
//    ScoringArm scoringArm;
//    CustomTimer timer;
//
//    public ScoringCombined() {}
//
//    public void initialize(OpMode opmode, HorizontalSlides horizontalSlides, Intake intake, VerticalSlides verticalSlides, ScoringArm scoringArm, CustomTimer timer) {
//        this.opmode = opmode;
//        this.verticalSlides = verticalSlides;
//        this.horizontalSlides = horizontalSlides;
//        this.scoringArm = scoringArm;
//        this.intake = intake;
//        this.timer = timer;
//    }
//
//    public void operateVincent() {
//        verticalSlides.operateVincent();
//        horizontalSlides.operateVincent();
//        scoringArm.operateVincent();
//        intake.operateVincent();
//
//        // DO NOT USE UNTIL ALL SUBSYSTEMS TESTED INDEPENDENTLY
////        if (opmode.gamepad1.x) {
////            autoPickUp();
////        }
////        if (opmode.gamepad1.y) {
////            autoTransferAndScore();
////        }
////        if (opmode.gamepad1.dpad_up) {
////            // without slide extension, just starting intake until it detects a valid piece
////            intake.fullIntakeSequence();
////        }
//
//        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlides.verticalSlidesRetracted);
//        opmode.telemetry.addData("Horizontal Slides Retracted: ", horizontalSlides.slidesRetracted);
//        opmode.telemetry.addData("Claw Transferring: ", scoringArm.arm.isArmTransferring);
//        opmode.telemetry.addData("Claw Open: ", scoringArm.claw.isClawOpen);
////        opmode.telemetry.addData("Piece Taken In: ", intake.pieceTakenInBool());
////        opmode.telemetry.addData("Detected Piece Color: ", intake.identifyColor());
////        opmode.telemetry.addData("Correct Color: ", intake.correctColorBool());
////        opmode.telemetry.addData("Has Sample & Correct Color: ", intake.correctPiece());
//    }
//
//    public void operateTest() {
////        verticalSlides.operateVincent();
////        horizontalSlides.operateVincent();
////        scoringArm.operateVincent();
//////        intake.operateTest();
////
////        if (opmode.gamepad1.a) {
////            autoPrepHighBucket();
////        } else if (opmode.gamepad1.b) {
////            autoPrepLowBucket();
////        }
////        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlides.verticalSlidesRetracted);
////        opmode.telemetry.addData("Horizontal Slides Retracted: ", horizontalSlides.horizontalSlidesRetracted);
////        opmode.telemetry.addData("Claw Transferring: ", scoringArm.arm.isArmTransferring);
////        opmode.telemetry.addData("Claw Open: ", scoringArm.claw.isClawOpen);
////        opmode.telemetry.addData("Piece Taken In: ", intake.pieceTakenInBool());
////        opmode.telemetry.addData("Detected Piece Color: ", intake.identifyColor());
////        opmode.telemetry.addData("Correct Color: ", intake.correctColorBool());
////        opmode.telemetry.addData("Has Sample & Correct Color: ", intake.correctPiece());
//    }
//
////    public void autoPickUpColorSensor() {
////        if (scoringArm.arm.isArmTransferring) {scoringArm.arm.setArmStow();}
////        horizontalSlides.extend();          // extend slides
//////        timer.safeDelay(0);                 // wait for __ milliseconds
////        intake.intakePieces();              // start intake and flip down
////        while (!intake.correctPiece()) {    // wait until a piece is picked up
////            if (intake.pieceTakenInBool() && !intake.correctColorBool()) {
////                intake.eject();
////                intake.intakePieces();
////            }
////        }
////        intake.stopIntaking();              // stop and flip up
////        horizontalSlides.retract();         // retract slides
////    }
//
//    public void autoPickUpTimed() {
//        if (scoringArm.arm.isArmTransferring) {scoringArm.arm.setArmStow();}
//        horizontalSlides.extend();          // extend slides
////        timer.safeDelay(0);                 // wait for __ milliseconds
//        intake.intakePieces();              // start intake and flip down
//        timer.safeDelay(2000);
//        intake.stopIntaking();              // stop and flip up
//        horizontalSlides.retract();         // retract slides
//    }
//
//
//    /** This is guaranteed not to work first try, but idk which section is wrong until I can CameraPortal it*/
//    public void autoTransferAndScore() {
//        /** this is probably what will break, but idk what will break*/
////        if (intake.correctPiece()) {
//        autoTransfer();
//        verticalSlides.raiseToHighBucket();     // extending slides
//        scoringArm.wrist.setWristScoringBucket();
//        scoringArm.arm.setArmScore();              // flip arm over to score
////            timer.safeDelay(0);                   // wait for __ milliseconds
//        scoringArm.wrist.setWristScoringBucket();     // prepping wrist to drop pixel (currently useless because the intake is now starting horizontal)
//        while (!scoringArm.claw.isClawOpen) {}// wait until driver drops piece
////            claw.openClaw();                      // drop sample
//        verticalSlides.retract();               // retract slides
//        scoringArm.arm.setArmStow();               // stow arm while retracting (might switch order)
////        }
//    }
//
//    public void autoTransfer() {
//        // check both slides fully retracted and ready to transfer
//        /** this is probably what will break, but idk what will break*/
////        if (intake.correctPiece()) {
//        if (!scoringArm.claw.isClawOpen) {scoringArm.claw.openClaw();}
//        if (!verticalSlides.verticalSlidesRetracted) {verticalSlides.retract();}
//        if (!horizontalSlides.slidesRetracted) {horizontalSlides.retract();}
//        //set the arm out of the way of transfer?
//        scoringArm.arm.setArmScore();
//        scoringArm.wrist.setWristScoringBucket();
//
//        //intake command
//        scoringArm.arm.setArmTransfer();
//        scoringArm.wrist.setWristTransfer();
////        timer.safeDelay(0);// might need delay
//
//
//        scoringArm.claw.closeClaw();
//        scoringArm.arm.setArmStow();
//        scoringArm.wrist.setWristStow();
////        }
//    }
//
//    // extends vertical slides, and sets arm to scoring, but doesn't open claw, then retracts once claw opens
//    // idk if the while loop will break everything, but it shouldn't
//    public void autoPrepHighBucket() {
//        verticalSlides.raiseToHighBucket();     // extending slides
//        scoringArm.wrist.setWristScoringBucket();
//        scoringArm.arm.setArmScore();              // flip arm over to score
//        while(!scoringArm.claw.isClawOpen) {}
//        scoringArm.arm.setArmStow();
//        scoringArm.wrist.setWristStow();
//        verticalSlides.retract();
//    }
//
//    public void autoPrepLowBucket() {
//        verticalSlides.raiseToLowBucket();
//        scoringArm.wrist.setWristScoringBucket();
//        scoringArm.arm.setArmScore();
//        while(!scoringArm.claw.isClawOpen) {}
//        scoringArm.arm.setArmStow();
//        scoringArm.wrist.setWristStow();
//        verticalSlides.retract();
//    }
//
//    // Nick's autonomous Action functions
//    public class clawOpen implements Action {
//        @Override
//        public boolean run(@NonNull TelemetryPacket packet) {
//            scoringArm.claw.openClaw();
//            return false;
//        }
//    }
//    public Action clawOpen() {
//        return new clawOpen();
//    }
//
//
//}
//
