package org.firstinspires.ftc.teamcode.Subsystems;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Util.CameraCVPipeline;
//import org.firstinspires.ftc.teamcode.Util.OpenCvPipeline;
import org.firstinspires.ftc.teamcode.Util.ColorDetect;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class CameraPortal {
    private OpMode opmode;
    private int  cameraMonitorViewID;
    private OpenCvCamera webcam1;

    private CameraCVPipeline pipeLine = new CameraCVPipeline();

    private final RobotHardware rHardware = new RobotHardware();

    public ColorDetect cameraColor = ColorDetect.YELLOW;




    public void init(OpMode opmode) {
        rHardware.init(opmode.hardwareMap);
        cameraMonitorViewID = rHardware.cameraMonitorViewId;
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(rHardware.webcam, cameraMonitorViewID);

        pipeLine.initialize(640, 480, webcam1);

        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                webcam1.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
//                TODO: Create Pipeline
                webcam1.setPipeline(pipeLine);
                FtcDashboard.getInstance().startCameraStream(
                        pipeLine,
                        30.0
                );

            }
            @Override
            public void onError(int errorCode) {opmode.telemetry.addLine("womp womp");}
        });
    }

    public void run(OpMode opmode) {


        if (opmode.gamepad1.a) {
            webcam1.stopStreaming();
        }
//        if (opmode.gamepad1.b) {
//            switch (cameraColor) {
//                case RED:
//                    cameraColor = cameraColor.BLUE;
//                    break;
//                case BLUE:
//                    cameraColor = cameraColor.YELLOW;
//                    break;
//                default:
//                    cameraColor = cameraColor.RED;
//            }
//        }
        opmode.telemetry.addData("Frame Count", webcam1.getFrameCount());
        opmode.telemetry.addData("FPS", String.format("%.2f", webcam1.getFps()));
        opmode.telemetry.addData("Total frame time ms", webcam1.getTotalFrameTimeMs());
        opmode.telemetry.addData("Pipeline time ms", webcam1.getPipelineTimeMs());
        opmode.telemetry.addData("Overhead time ms", webcam1.getOverheadTimeMs());
        opmode.telemetry.addData("Theoretical max FPS", webcam1.getCurrentPipelineMaxFps());
        opmode.telemetry.update();
    }
//    public Action getServoRotation() {
//        new InstantAction(() -> ())
//    }

    public double getRotation() {
        return pipeLine.getTargetWristPosition();
    }
}





