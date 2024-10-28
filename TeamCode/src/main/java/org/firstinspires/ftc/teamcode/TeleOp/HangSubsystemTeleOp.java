//package org.firstinspires.ftc.teamcode.TeleOp;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import org.firstinspires.ftc.teamcode.Subsystems.HangSubsystem;
//
//@TeleOp(name="HangSubsystem Test", group="Active TeleOps")
//public class HangSubsystemTeleOp extends OpMode {
//    private final HangSubsystem hangSubsystem = new HangSubsystem();
//
//    @Override
//    public void init() {
//        hangSubsystem.initialize(this);
//        telemetry.addLine("Press Y to deploy, D-Pad Left to activate stage two.");
//        telemetry.update();
//    }
//
//    @Override
//    public void loop() {
//        if (gamepad1.y && !hangSubsystem.isDeployed()) {
//            hangSubsystem.deploy();
//        }
//
//        if (gamepad1.dpad_left && hangSubsystem.isDeployed() && !hangSubsystem.isStageTwoActivated()) {
//            hangSubsystem.activateStageTwo();
//        }
//
//        hangSubsystem.operate();
//    }
//    @Override
//    public void stop() {}
//}
