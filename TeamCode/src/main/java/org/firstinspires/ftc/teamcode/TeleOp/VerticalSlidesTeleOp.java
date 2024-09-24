package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.OTOSManager;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@TeleOp(name="Only Vertical Slides Test", group="Active TeleOps")
public class VerticalSlidesTeleOp extends OpMode {
    // creating subsystems
    private VerticalSlides verticalSlides = new VerticalSlides();

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
        verticalSlides.operateTest();
        // Gamepad 2:
        // Left Joystick Y - vertical slide manual control
        telemetry.addData("Vertical Slides Retracted: ", verticalSlides.verticalSlidesRetracted);
        telemetry.addData("Left Motor Encoder Pos: ", verticalSlides.telemetryLeftMotorPos());
        telemetry.addData("Right Motor Encoder Pos: ", verticalSlides.telemetryLeftMotorPos());

        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        verticalSlides.shutdown();
    }
}




