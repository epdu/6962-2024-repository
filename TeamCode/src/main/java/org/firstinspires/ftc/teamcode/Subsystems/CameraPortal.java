package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Util.ColorDetect.BLUE;
import static org.firstinspires.ftc.teamcode.Util.ColorDetect.RED;
import static org.firstinspires.ftc.teamcode.Util.ColorDetect.YELLOW;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Util.CameraCVPipeline;
import org.firstinspires.ftc.teamcode.Util.ColorDetect;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class CameraPortal {
    private final CameraCVPipeline pipeLine = new CameraCVPipeline();

    private final IntakeArm intake = new IntakeArm();
    private final RobotHardware rHardware = new RobotHardware();

    private OpenCvCamera webcam1;

    public MultipleTelemetry dashTelemetry;
    public ColorDetect cameraColor;

    int cameraMonitorViewID;

    double cameraServoAngle;
    public double driveAngle;

    public void initialize(OpMode opMode) {
        cameraColor = BLUE;
        dashTelemetry = new MultipleTelemetry(opMode.telemetry, FtcDashboard.getInstance().getTelemetry());

        rHardware.init(opMode.hardwareMap);

        cameraMonitorViewID = rHardware.cameraMonitorViewId;
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(rHardware.webcam, cameraMonitorViewID);

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
            public void onError(int errorCode) {opMode.telemetry.addLine("womp womp");}
        });

        pipeLine.initialize();

        //uncomment if breaks
//        pipeLine.setDetectionType(pipeLine.detectionType);
        intake.initialize(opMode);
    }

    public void changeColor() {
            //toggle color detection pre init thru cycling w b button
        switch (cameraColor) {
            case RED:
                pipeLine.setDetectionType(BLUE);
                cameraColor = BLUE;
                break;
            default:
                pipeLine.setDetectionType(BLUE);
                cameraColor = RED;
        }
    }

    public void setRed() {
        cameraColor = RED;
    }

    public void setBlue() {
        cameraColor = BLUE;
    }

    // return servo position vs angle
    public double getServoRotation() {
        return pipeLine.getTargetWristPosition();
    }

    public double getSampleRotation() {
        return pipeLine.getSampleAngle();
    }

    public void setWristCamera() {
        intake.wrist.setWristCameraAngle(getServoRotation());
    }


    public void run(OpMode opMode) {
        cameraServoAngle = getServoRotation();
//        driveAngle = pipeLine.getMotorPower();
//        if (opMode.gamepad2.a) {intake.wrist.setWristCameraAngle(cameraServoAngle);}

//        opMode.telemetry.addData("Frame Count", webcam1.getFrameCount());
//        opMode.telemetry.addData("FP=S", String.format("%.2f", webcam1.getFps()));
//        opMode.telemetry.addData("Total frame time ms", webcam1.getTotalFrameTimeMs());
//        opMode.telemetry.addData("Pipeline time ms", webcam1.getPipelineTimeMs());
//        opMode.telemetry.addData("Overhead time ms", webcam1.getOverheadTimeMs());
//        opMode.telemetry.addData("Theoretical max FPS", webcam1.getCurrentPipelineMaxFps());
//        opMode.telemetry.addData("servo angle", getServoRotation());
//        dashTelemetry.update();
//        opMode.telemetry.update();
    }


    //camera claw action for auto
    public class ClawCamera implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            setWristCamera();
            return false;
        }
    }

    public Action ClawCamera() {
        return new ClawCamera();
    }
}






