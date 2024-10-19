package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;

@TeleOp(name="Only Horizontal Slides Test", group="Active TeleOps")
public class HorizontalSlidesTeleOp extends OpMode {
    // creating subsystems
    private final HorizontalSlides horizontalSlides = new HorizontalSlides();
//    private final LinkageHorizontalSlides horizontalSlides = new LinkageHorizontalSlides();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        horizontalSlides.initialize(this);
        telemetry.addLine("Dpad Up and Down - extendo incremental");
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
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        horizontalSlides.operateTest();
        horizontalSlides.operateVincent();
        // Gamepad 1: (linkage and spooled)
        // Right trigger - exponential mapping control (be careful)
        // Gamepad 2:
        // Left Joystick Y - manual control
        // Y - PID extend
        // X - PID retract
        // Gamepad 2: (when linkage)
        // D-pad Up and Down - incremental control
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {}
}




