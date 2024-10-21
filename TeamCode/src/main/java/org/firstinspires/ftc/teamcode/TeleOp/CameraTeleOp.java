package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystems.CameraPortal;

public class CameraTeleOp extends OpMode {
    public CameraPortal cPortal = new CameraPortal();

    @Override
    public void init() {
        cPortal.init();
    }

    @Override
    public void loop() {
        cPortal.run();
    }
}
