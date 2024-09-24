//package org.firstinspires.ftc.teamcode.Subsystems;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.teamcode.Util.RobotHardware;
//
//import java.util.Timer;
//
//public class ScoringCombined {
//    OpMode opmode;
//
//    HorizontalSlides horizontalSlides;
//    Intake intake;
//    VerticalSlides verticalSlides;
//    Claw claw;
//    CustomTimer timer;
//
//    public ScoringCombined() {}
//
//    public void initialize(OpMode opmode, HorizontalSlides horizontalSlides, Intake intake, VerticalSlides verticalSlides, Claw claw, CustomTimer timer) {
//        this.opmode = opmode;
//        this.verticalSlides = verticalSlides;
//        this.horizontalSlides = horizontalSlides;
//        this.claw = claw;
//        this.intake = intake;
//        this.timer = timer;
//    }
//
//    public void operateVincent() {
//        verticalSlides.operateVincent();
//        horizontalSlides.operateVincent();
//        claw.operateVincent();
//        intake.operateVincent();
//
//        // DO NOT USE UNTIL ALL SUBSYSTEMS TESTED INDEPENDENTLY
////        if (opmode.gamepad1.x) {
////            autoPickUp();
////        }
////        if (opmode.gamepad1.y) {
////            autoScore();
////        }
////        if (opmode.gamepad1.dpad_up) {
////            // without slide extension, just starting intake until it detects a valid piece
////            intake.fullIntakeSequence();
////        }
//
//        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlides.verticalSlidesRetracted);
//        opmode.telemetry.addData("Horizontal Slides Retracted: ", horizontalSlides.horizontalSlidesRetracted);
//        opmode.telemetry.addData("Claw Transferring: ", claw.isArmTransferring);
//        opmode.telemetry.addData("Claw Open: ", claw.isClawOpen);
//        opmode.telemetry.addData("Piece Taken In: ", intake.pieceTakenInBool());
//        opmode.telemetry.addData("Detected Piece Color: ", intake.identifyColor());
//        opmode.telemetry.addData("Correct Color: ", intake.correctColorBool());
//        opmode.telemetry.addData("Has Sample & Correct Color: ", intake.correctPiece());
//    }
//
//    public void operateTest() {
//        verticalSlides.operateTest();
//        horizontalSlides.operateTest();
//        claw.operateTest();
//        intake.operateTest();
//
//        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlides.verticalSlidesRetracted);
//        opmode.telemetry.addData("Horizontal Slides Retracted: ", horizontalSlides.horizontalSlidesRetracted);
//        opmode.telemetry.addData("Claw Transferring: ", claw.isArmTransferring);
//        opmode.telemetry.addData("Claw Open: ", claw.isClawOpen);
//        opmode.telemetry.addData("Piece Taken In: ", intake.pieceTakenInBool());
//        opmode.telemetry.addData("Detected Piece Color: ", intake.identifyColor());
//        opmode.telemetry.addData("Correct Color: ", intake.correctColorBool());
//        opmode.telemetry.addData("Has Sample & Correct Color: ", intake.correctPiece());
//    }
//
//    public void shutdown() {
//        verticalSlides.shutdown();
//        horizontalSlides.shutdown();
//        claw.shutdown();
//        intake.shutdown();
//        timer.shutdown();
//    }
//
//    public void autoPickUp() {
//        if (claw.isArmTransferring) {claw.stowArm();}
//        horizontalSlides.extend();          // extend slides
////        timer.safeDelay(0);                 // wait for __ milliseconds
//        intake.intakePieces();              // start intake and flip down
//        while (!intake.correctPiece()) {    // wait until a piece is picked up
//            if (intake.pieceTakenInBool() && !intake.correctColorBool()) {
//                intake.eject();
//                intake.intakePieces();
//            }
//        }
//        intake.stopIntaking();              // stop and flip up
//        horizontalSlides.retract();         // retract slides
//    }
//
//    /** This is guaranteed not to work first try, but idk which section is wrong until I can CameraPortal it*/
//    public void autoScore() {
//        // check both slides fully retracted and ready to transfer
//        /** this is probably what will break, but idk what will break*/
//        if (intake.correctPiece()) {
//            if (claw.isWristVertical) {claw.setWristHorizontal();}
//            if (!claw.isClawOpen) {claw.openClaw();}
//            if (!verticalSlides.verticalSlidesRetracted) {verticalSlides.retract();}
//            if (!horizontalSlides.horizontalSlidesRetracted) {horizontalSlides.retract();}
//
//            claw.transferArm();                 // flip arm over
//            claw.closeClaw();                   // grab sample
//            claw.stowArm();                     // remove piece from intake
//            verticalSlides.raiseToHighBucket(); // extending slides
//            claw.scoreArm();                    // flip arm over to score
////            timer.safeDelay(0);                 // wait for __ milliseconds
//            claw.setWristHorizontal();          // prepping wrist to drop pixel (currently useless because the intake is now starting horizontal)
//            while (claw.isClawOpen) {};         // wait until driver drops piece
////            claw.openClaw();                    // drop sample
//            verticalSlides.retract();           // retract slides
//            claw.stowArm();                     // stow arm while retracting (might switch order)
//        }
//    }
//
//    public void autoTransfer() {
//        // check both slides fully retracted and ready to transfer
//        /** this is probably what will break, but idk what will break*/
//        if (intake.correctPiece()) {
//            if (!claw.isClawOpen) {claw.openClaw();}
//            if (claw.isArmTransferring) {claw.stowArm();}
//            if (!claw.isWristVertical) {claw.setWristHorizontal();}
//            if (!verticalSlides.verticalSlidesRetracted) {verticalSlides.retract();}
//            if (!horizontalSlides.horizontalSlidesRetracted) {horizontalSlides.retract();}
//
//            claw.transferArm(); // flip arm over to intake side
////            timer.safeDelay(0);  // might need delay
//            claw.closeClaw(); // grab piece
//            claw.stowArm(); // flip back over
//        }
//    }
//}
//
