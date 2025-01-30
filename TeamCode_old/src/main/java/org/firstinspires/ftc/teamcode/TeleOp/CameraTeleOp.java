package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Util.CameraCVPipeline.getAngleTarget;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Subsystems.CameraPortal;
import org.firstinspires.ftc.teamcode.Subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.Util.CameraCVPipeline;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvPipeline;

//import org.firstinspires.ftc.teamcode.Subsystems.CameraPortal;

@TeleOp(name="Only Camera Test", group="Active TeleOps")
public class CameraTeleOp extends OpMode {
    private CameraPortal cPortal = new CameraPortal();
    private CameraCVPipeline pipeline = new CameraCVPipeline();

    private Mecanum mecanum = new Mecanum();
    private RobotHardware rHardware = new RobotHardware();

//    private BNO055IMU imu1;
//    private BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    @Override
    public void init() {
        rHardware.init(this.hardwareMap);
        mecanum.initialize(this);
        cPortal.initialize(this);
//        imu1 = rHardware.imu;
//        parameters.mode = BNO055IMU.SensorMode.IMU;
//        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
//        imu1.initialize(parameters);
    }

    @Override
    public void init_loop() {
        if (gamepad1.b) {
            cPortal.changeColor();

        }
    }

    @Override
    public void loop() {
        cPortal.run(this);
//        double power = pipeline.PIDControl(Math.toRadians(0 + getAngleTarget(pipeline.cX)), imu1.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle);

//        mecanum.driveCamera(power);
        telemetry.addData("Midpoint offset: ", pipeline.centerOffset);
        telemetry.addData("Camera Color", cPortal.cameraColor);
    }
}
