package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.NewHorizontalSlides;

@TeleOp(name="PID Tuning Horizontal Slides Test", group="Active TeleOps")
public class NewHorizontalSlidesTuning extends OpMode {
    private FtcDashboard dash = FtcDashboard.getInstance();
    private Telemetry dashboardTelemetry = dash.getTelemetry();

    private NewHorizontalSlides horizontalSlides = new NewHorizontalSlides();

    @Override
    public void init() {
        horizontalSlides.initialize(this);
    }

    @Override
    public void loop() {
        horizontalSlides.operateTuning();

        dashboardTelemetry.addData("Right Motor Encoder Pos: ", horizontalSlides.telemetryMotorPos());
        dashboardTelemetry.addData("Target: ", horizontalSlides.telemetryTarget());
        dashboardTelemetry.addData("PID Power R: ", horizontalSlides.telemetryOutput());
        dashboardTelemetry.update();
    }
}
