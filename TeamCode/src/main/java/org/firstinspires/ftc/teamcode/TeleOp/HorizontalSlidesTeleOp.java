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
import org.firstinspires.ftc.teamcode.Subsystems.ScoringCombined;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@TeleOp(name="Only Horizontal Slides Test", group="Active TeleOps")
public class HorizontalSlidesTeleOp extends OpMode {
    // creating subsystems
    private HorizontalSlides horizontalSlides = new HorizontalSlides();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        horizontalSlides.initialize(this);
        telemetry.addLine("Right Joystick Y - horizontal slide manual control");
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
        horizontalSlides.operateTest();
        // Gamepad 2:
        // Right Joystick Y - horizontal slide manual control
        telemetry.addData("Horizontal Slides Retracted: ", horizontalSlides.horizontalSlidesRetracted);
        telemetry.addData("Encoder Pos: ", horizontalSlides.telemetryMotorPos());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        horizontalSlides.shutdown();
    }
}




