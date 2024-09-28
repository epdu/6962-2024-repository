package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.OTOSManager;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@TeleOp(name="Only Intake Test", group="Active TeleOps")
public class IntakeTeleOp extends OpMode {
    // creating subsystems
    private Intake intake = new Intake();
    private CustomTimer timer = new CustomTimer();

    private boolean onRedAlliance = true;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        intake.initialize(this, timer, onRedAlliance);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */

    /*
     * allows driver to indicate that the IMU should not be reset
     * used when starting TeleOp after auto or if program crashes in the middle of match
     * relevant because of field-centric controls
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        telemetry.clear();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
//        intake.operateTest();
        intake.operateTest();
        // Gamepad 2:
        // A - start intake and flip down
        // B - stop intake and flip up
        // X - reverse intake for 2 seconds
//        telemetry.addData("Piece Taken In: ", intake.pieceTakenInBool());
//        telemetry.addData("Detected Piece Color: ", intake.identifyColor());
//        telemetry.addData("Correct Color: ", intake.correctColorBool());
//        telemetry.addData("Has Sample & Correct Color: ", intake.correctPiece());
        telemetry.addData("Is Flipped Up: ", intake.flippedUp);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        intake.shutdown();
        timer.shutdown();
    }
}


