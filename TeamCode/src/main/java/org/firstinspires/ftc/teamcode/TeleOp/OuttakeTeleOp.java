package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Subsystems.OuttakeCombined;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@TeleOp(name="Only Outtake TeleOp (no intake, no extendo)", group="Active TeleOps")
public class OuttakeTeleOp extends OpMode {
    // creating subsystems
    private ScoringArm scoringArm = new ScoringArm();
    private Mecanum mecanumDrive = new Mecanum();
    private VerticalSlides vertSlides = new VerticalSlides();
    private CustomTimer timer = new CustomTimer();
    private OuttakeCombined outtake = new OuttakeCombined();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        mecanumDrive.initialize(this);
        scoringArm.initialize(this, timer);
        vertSlides.initialize(this);
        outtake.initialize(this, vertSlides, scoringArm, timer);

        telemetry.addLine("Right Bumper - toggle claw");
        telemetry.addLine("Both Joysticks - regular field centric mecanum driving");
        telemetry.addLine("Y - bucket score macro");
        telemetry.addLine("X - auto transfer macro");
        telemetry.update();
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
        // only macro-based control
        outtake.operateTest();
//        vertSlides.operateTest(); // using operateVincent is not a mistake, please leave here
        scoringArm.operateVincent();
        mecanumDrive.operateFieldCentricTest();

        // Gamepad 1:
        // Right Bumper - toggle claw
        // Both Joysticks - regular field centric mecanum driving
        // Y - bucket score macro
        // X - auto transfer macro

        // kill switch for the robot's safety if it starts going nuts
        if (gamepad1.a && gamepad1.b) {
            requestOpModeStop();
        }

        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {}
}
