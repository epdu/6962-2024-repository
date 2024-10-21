package org.firstinspires.ftc.teamcode.Util;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

public class CameraPortal {
    private OpMode opmode;
    private int  cameraMonitorViewID;
    private OpenCvCamera webcam1;

    private OpenCVPipeline pipeLine = new OpenCVPipeline();
     
    private final RobotHardware rHardware = new RobotHardware();

    public void init() {
        cameraMonitorViewID = rHardware.cameraMonitorViewId;
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(rHardware.webcam, cameraMonitorViewID);

        pipeLine.initialize(webcam1);

        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                webcam1.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
//                TODO: Create Pipeline
                webcam1.setPipeline(pipeLine);
            }
            @Override
            public void onError(int errorCode) {opmode.telemetry.addLine("womp womp");}
        });
    }

    public void run() {
        opmode.telemetry.addData("Frame Count", webcam1.getFrameCount());
        opmode.telemetry.addData("FPS", String.format("%.2f", webcam1.getFps()));
        opmode.telemetry.addData("Total frame time ms", webcam1.getTotalFrameTimeMs());
        opmode.telemetry.addData("Pipeline time ms", webcam1.getPipelineTimeMs());
        opmode.telemetry.addData("Overhead time ms", webcam1.getOverheadTimeMs());
        opmode.telemetry.addData("Theoretical max FPS", webcam1.getCurrentPipelineMaxFps());
        opmode.telemetry.update();

        if (opmode.gamepad1.a) {
            webcam1.stopStreaming();
        }
    }
}



