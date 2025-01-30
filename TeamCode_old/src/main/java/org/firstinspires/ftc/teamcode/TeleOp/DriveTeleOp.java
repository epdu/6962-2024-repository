package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;


@TeleOp(name="Only Mecanum Drive", group="Active TeleOps")
public class DriveTeleOp extends OpMode {
    // creating subsystems
    private final Mecanum mecanum = new Mecanum();
//    private OTOSManager otosManager = new OTOSManager();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        mecanum.initialize(this);
//        otosManager.initialize(this, true);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
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
        mecanum.operateFieldCentric(); // toggle Y button for slow mode
//        mecanum.operateRoboCentric();
//        otosManager.operate();
        telemetry.addData("Heading: ", mecanum.navxHeading());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {}
}

