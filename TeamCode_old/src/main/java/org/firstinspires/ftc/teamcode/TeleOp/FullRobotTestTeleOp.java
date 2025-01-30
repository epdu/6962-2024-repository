//package org.firstinspires.ftc.teamcode.TeleOp;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.Subsystems.CustomTimer;
//import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
//import org.firstinspires.ftc.teamcode.Subsystems.Intake;
//import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
//import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
//import org.firstinspires.ftc.teamcode.Subsystems.ScoringCombined;
//import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;
//
//@TeleOp(name="RED Whole Robot Test", group="Active TeleOps")
//public class FullRobotTestTeleOp extends OpMode {
//    // creating subsystems
//    private Mecanum mecanum                   = new Mecanum();
//    private HorizontalSlides horizontalSlides = new HorizontalSlides();
//    private VerticalSlides verticalSlides     = new VerticalSlides();
//    private Intake intake                     = new Intake();
//    private ScoringArm scoringArm             = new ScoringArm();
//    private ScoringCombined scoring           = new ScoringCombined();
//    private CustomTimer timer                 = new CustomTimer();
//
//    private boolean onRedAlliance = true;
//
//    /*
//     * Code to run ONCE when the driver hits INIT
//     */
//    @Override
//    public void init() {
//        mecanum.initialize(this);
//        horizontalSlides.initialize(this);
//        intake.initialize(this, timer, onRedAlliance);
//        verticalSlides.initialize(this);
//        scoringArm.initialize(this, timer);
//        scoring.initialize(this, horizontalSlides, intake, verticalSlides, scoringArm, timer);
//    }
//
//    @Override
//    public void init_loop() {}
//
//    @Override
//    public void start() {}
//
//    @Override
//    public void loop() {
//        mecanum.operateFieldCentricTest(); // press A button to toggle slow mode (refresh rate too high)
//        scoring.operateTest();
//        verticalSlides.operateTest();
//        scoringArm.operateTest();
//    }
//
//    @Override
//    public void stop() {}
//}
//
