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

    public void operate() {
        verticalSlides.operate();
        horizontalSlides.operate();
        claw.operate();
        intake.operate();

        if (opmode.gamepad1.x) {
            autoPickUp();
        }
        if (opmode.gamepad1.y) {
            autoScore();
        }
    }

    public void shutdown() {
        verticalSlides.shutdown();
        horizontalSlides.shutdown();
        claw.shutdown();
        intake.shutdown();
    }

    public void autoPickUp() {
        horizontalSlides.extend();          // extend slides
        intake.intakePieces();              // start intake and flip down
        while (!intake.correctPiece()) {    // wait until a piece is picked up
            if (intake.pieceTakenInBool() && !intake.correctColorBool())
                intake.eject();
        }
        intake.stopIntaking();              // stop and flip up
        horizontalSlides.retract();         // retract slides
    }

    /** This is guaranteed not to work first try, but idk which section is wrong until I can test it*/
    public void autoScore() {
        // check both slides fully retracted and ready to transfer
        /** this is probably what will break, but idk how else to check and fix these things*/
        if (!verticalSlides.verticalSlidesRetracted) {verticalSlides.retract();}
        if (!horizontalSlides.horizontalSlidesRetracted) {}
        if (!claw.isTransferring) {claw.stowArm();}
        if (!claw.isOpen) {claw.openClaw();}
        claw.closeClaw();                   // transfer (just grabbing the sample)
        verticalSlides.raiseToHighBucket(); // extending slides
        /** might need delay here */
        claw.scoreArm();                    // flipping arm over to score (this could possibly go before extending depending on the design of the robot)
        claw.openClaw();                    // drop sample
        claw.stowArm();                     // flip arm back over
        verticalSlides.retract();           // retract slides
    }
}

