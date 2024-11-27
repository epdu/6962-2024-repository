package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.CameraPortal;

//import org.firstinspires.ftc.teamcode.Subsystems.CameraPortal;

@TeleOp(name="Only Camera Test", group="Active TeleOps")
public class CameraTeleOp extends OpMode {
    public CameraPortal cPortal = new CameraPortal();

    @Override
    public void init() {
        cPortal.initialize(this);
    }

    @Override
    public void loop() {
        cPortal.run(this);
    }
}
