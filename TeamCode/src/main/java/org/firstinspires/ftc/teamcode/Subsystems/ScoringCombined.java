package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class ScoringCombined {
    OpMode opmode;
    HorizontalSlides horizontalSlides;
    Intake intake;
    VerticalSlides verticalSlides;
    Claw claw;

    public ScoringCombined() {}

    public void initialize(OpMode opmode, HorizontalSlides horizontalSlides, Intake intake, VerticalSlides verticalSlides, Claw claw) {
        this.opmode = opmode;
        this.verticalSlides = verticalSlides;
        this.horizontalSlides = horizontalSlides;
        this.claw = claw;
        this.intake = intake;
    }

    public void operateVincent() {
        verticalSlides.operateVincent();
        horizontalSlides.operateVincent();
        claw.operateVincent();
        intake.operateVincent();

        // DO NOT USE UNTIL ALL SUBSYSTEMS TESTED INDEPENDENTLY
//        if (opmode.gamepad1.x) {
//            autoPickUp();
//        }
//        if (opmode.gamepad1.y) {
//            autoScore();
//        }
        if (opmode.gamepad1.dpad_up) {
            // without slide extension, just starting intake until it detects a valid piece
            intake.fullIntakeSequence();
        }

        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlides.verticalSlidesRetracted);
        opmode.telemetry.addData("Horizontal Slides Retracted: ", horizontalSlides.horizontalSlidesRetracted);
        opmode.telemetry.addData("Claw Transferring: ", claw.isArmTransferring);
        opmode.telemetry.addData("Claw Open: ", claw.isClawOpen);
        opmode.telemetry.addData("Piece Taken In: ", intake.pieceTakenInBool());
        opmode.telemetry.addData("Detected Piece Color: ", intake.identifyColor());
        opmode.telemetry.addData("Correct Color: ", intake.correctColorBool());
        opmode.telemetry.addData("Has Sample & Correct Color: ", intake.correctPiece());
    }

    public void operateTest() {
        verticalSlides.operateTest();
        horizontalSlides.operateTest();
        claw.operateTest();
        intake.operateTest();

        opmode.telemetry.addData("Vertical Slides Retracted: ", verticalSlides.verticalSlidesRetracted);
        opmode.telemetry.addData("Horizontal Slides Retracted: ", horizontalSlides.horizontalSlidesRetracted);
        opmode.telemetry.addData("Claw Transferring: ", claw.isArmTransferring);
        opmode.telemetry.addData("Claw Open: ", claw.isClawOpen);
        opmode.telemetry.addData("Piece Taken In: ", intake.pieceTakenInBool());
        opmode.telemetry.addData("Detected Piece Color: ", intake.identifyColor());
        opmode.telemetry.addData("Correct Color: ", intake.correctColorBool());
        opmode.telemetry.addData("Has Sample & Correct Color: ", intake.correctPiece());
    }

    public void shutdown() {
        verticalSlides.shutdown();
        horizontalSlides.shutdown();
        claw.shutdown();
        intake.shutdown();
    }

    public void autoPickUp() {
        horizontalSlides.extend();          // extend slides
//        timer.safeDelay(0);      // wait for __ milliseconds
        intake.intakePieces();              // start intake and flip down
        while (!intake.correctPiece()) {    // wait until a piece is picked up
            if (intake.pieceTakenInBool() && !intake.correctColorBool())
                intake.eject();
                intake.intakePieces();
        }
        intake.stopIntaking();              // stop and flip up
        horizontalSlides.retract();         // retract slides
    }

    /** This is guaranteed not to work first try, but idk which section is wrong until I can test it*/
    public void autoScore() {
        // check both slides fully retracted and ready to transfer
        /** this is probably what will break, but idk what will break*/
        if (intake.correctPiece()) {
            if (!claw.isArmTransferring) {claw.stowArm();}
            if (!claw.isWristVertical) {claw.setWristHorizontal();}
            if (!claw.isClawOpen) {claw.openClaw();}
            if (!verticalSlides.verticalSlidesRetracted) {verticalSlides.retract();}
            if (!horizontalSlides.horizontalSlidesRetracted) {horizontalSlides.retract();}
        }
        claw.closeClaw();                   // transfer (just grabbing the sample)
        verticalSlides.raiseToHighBucket(); // extending slides
//        timer.safeDelay(0);      // wait for __ milliseconds
        claw.scoreArm();                    // flipping arm over to score (this could possibly go before extending depending on the design of the robot)
        claw.setWristHorizontal();          // prepping wrist to drop pixel (currently useless because the intake is now starting horizontal)
        claw.openClaw();                    // drop sample
        claw.stowArm();                     // flip arm back over
        verticalSlides.retract();           // retract slides
    }

    public void autoTransfer() {
        // check both slides fully retracted and ready to transfer
        /** this is probably what will break, but idk what will break*/
        if (intake.correctPiece()) {
            if (!claw.isArmTransferring) {claw.stowArm();}
            if (!claw.isClawOpen) {claw.openClaw();}
            if (!claw.isWristVertical) {claw.setWristHorizontal();}
            if (!verticalSlides.verticalSlidesRetracted) {verticalSlides.retract();}
            if (!horizontalSlides.horizontalSlidesRetracted) {horizontalSlides.retract();}
        }
        claw.closeClaw(); // transfer (just grabbing the sample)
    }
}

