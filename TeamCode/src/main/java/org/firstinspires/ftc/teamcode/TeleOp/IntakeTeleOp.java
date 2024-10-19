package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;

@TeleOp(name="Only Intake Test", group="Active TeleOps")
public class IntakeTeleOp extends OpMode {
    // creating subsystems
    private final IntakeArm intake = new IntakeArm();

    private final boolean onRedAlliance = true;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        intake.initialize(this);
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
//        intake.operateTest();
        intake.operateTest();
        // Gamepad 2: tuning
        //

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {}
}


