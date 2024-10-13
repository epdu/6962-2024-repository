package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@TeleOp(name="Only Vertical Slides Test", group="Active TeleOps")
public class VerticalSlidesTeleOp extends OpMode {
    // creating subsystems
    private final VerticalSlides verticalSlides = new VerticalSlides();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        verticalSlides.initialize(this);
        telemetry.addLine("Left Joystick Y - vertical slide manual control");
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
        verticalSlides.operateTest();
//        verticalSlides.operateVincent();
        // Gamepad 1:
        // Y - high bucket PID
        // X - low bucket PID
        // Gamepad 2:
        // Left Joystick Y - vertical slide manual control
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {}
}




