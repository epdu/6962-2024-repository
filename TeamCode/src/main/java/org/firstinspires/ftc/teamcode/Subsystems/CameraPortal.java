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
//import org.firstinspires.ftc.teamcode.Util.OpenCvPipeline;
import org.firstinspires.ftc.teamcode.Util.ColorDetect;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

public class CameraPortal {
    private final CameraCVPipeline pipeLine = new CameraCVPipeline();

    private final IntakeArm intake = new IntakeArm();
    private final RobotHardware rHardware = new RobotHardware();

    private OpenCvCamera webcam1;

    public MultipleTelemetry dashTelemetry;
    public ColorDetect cameraColor = BLUE;

    public void initialize(OpMode opMode) {
        dashTelemetry = new MultipleTelemetry(opMode.telemetry, FtcDashboard.getInstance().getTelemetry());

        rHardware.init(opMode.hardwareMap);
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(rHardware.webcam, rHardware.cameraMonitorViewId);
        pipeLine.initialize(opMode, webcam1);
        pipeLine.setDetectionType(cameraColor);
        intake.initialize(opMode);
    }

    public void changeColor() {
            //toggle color detection pre init thru cycling w b button
        switch (cameraColor) {
            case RED:
                pipeLine.setDetectionType(BLUE);
                cameraColor = BLUE;
                break;
            case BLUE:
                pipeLine.setDetectionType(YELLOW);
                cameraColor = YELLOW;
                break;
            default:
                pipeLine.setDetectionType(RED);
                cameraColor = RED;
        }
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
        if (opMode.gamepad2.a) {setWristCamera();}

        opMode.telemetry.addData("Frame Count", webcam1.getFrameCount());
        opMode.telemetry.addData("FP=S", String.format("%.2f", webcam1.getFps()));
        opMode.telemetry.addData("Total frame time ms", webcam1.getTotalFrameTimeMs());
        opMode.telemetry.addData("Pipeline time ms", webcam1.getPipelineTimeMs());
        opMode.telemetry.addData("Overhead time ms", webcam1.getOverheadTimeMs());
        opMode.telemetry.addData("Theoretical max FPS", webcam1.getCurrentPipelineMaxFps());
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






