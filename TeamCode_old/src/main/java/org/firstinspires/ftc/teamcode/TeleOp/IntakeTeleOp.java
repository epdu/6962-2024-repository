package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import org.firstinspires.ftc.teamcode.Subsystems.CameraPortal;
import org.firstinspires.ftc.teamcode.Subsystems.CameraPortal;
import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Util.CameraCVPipeline;

@TeleOp(name="Only Intake Test", group="Active TeleOps")
public class IntakeTeleOp extends OpMode {
    // creating subsystems
    private final IntakeArm intake = new IntakeArm();
//    private CameraPortal cPortal = new CameraPortal();
//    private CameraCVPipeline pipeline = new CameraCVPipeline();


    private final boolean onRedAlliance = true;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        intake.initialize(this);
//        cPortal.initialize(this);
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
        intake.operateTest(this);
//        cPortal.run(this);
//        if (gamepad1.b) {
//            cPortal.changeColor();
//        }
        // Gamepad 2: tuning
        //

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {}
}


