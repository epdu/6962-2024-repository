//package org.firstinspires.ftc.teamcode.TeleOp;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.Subsystems.LinkageHorizontalSlides;
//
//@TeleOp(name="Only Linkage Horizontal Slides Test", group="Active TeleOps")
//public class LinkageHorizontalSlidesTeleOp extends OpMode {
//    // creating subsystems
//    private final LinkageHorizontalSlides horizontalSlides = new LinkageHorizontalSlides();
//
//    /*
//     * Code to run ONCE when the driver hits INIT
//     */
//    @Override
//    public void init() {
//
//        horizontalSlides.initialize(this);
//        telemetry.addLine("D-Pad Up and Down - incremental");
//    }
//
//    /*
//     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
//     */
//    @Override
//    public void init_loop() {
//    }
//
//    /*
//     * Code to run ONCE when the driver hits PLAY
//     */
//    @Override
//    public void start() {
//    }
//
//    /*
//     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
//     */
//    @Override
//    public void loop() {
//        horizontalSlides.operateTest();
//        horizontalSlides.operateVincent();
//        // Gamepad 2:
//        // Dpad Up and Down - Incremental extending and retracting
//    }
//
//    /*
//     * Code to run ONCE after the driver hits STOP
//     */
//    @Override
//    public void stop() {}
//}
//
//
//
//
