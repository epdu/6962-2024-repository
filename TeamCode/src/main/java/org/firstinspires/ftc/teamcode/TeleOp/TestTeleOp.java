package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringCombined;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@TeleOp(name="RED Whole Robot Test", group="Active TeleOps")
public class TestTeleOp extends OpMode {
    // creating subsystems
    private Mecanum mecanum                   = new Mecanum();
    private HorizontalSlides horizontalSlides = new HorizontalSlides();
    private Intake intake                     = new Intake();
    private VerticalSlides verticalSlides     = new VerticalSlides();
    private Claw claw                         = new Claw();
    private ScoringCombined scoring           = new ScoringCombined();

    private boolean onRedAlliance = true;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        mecanum.initialize(this);
        // TODO: Go to each subsystem file and input all names into driver hub before testing
        horizontalSlides.initialize(this);
        intake.initialize(this, onRedAlliance);
        verticalSlides.initialize(this);
        claw.initialize(this);
        scoring.initialize(this, horizontalSlides, intake, verticalSlides, claw);
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

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        mecanum.operateFieldCentric(); // press A button for slow mode
        scoring.operate();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        mecanum.shutdown();
        scoring.shutdown();
    }
}

