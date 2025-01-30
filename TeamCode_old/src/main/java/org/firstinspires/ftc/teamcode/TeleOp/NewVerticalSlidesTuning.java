package org.firstinspires.ftc.teamcode.TeleOp;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.NewVerticalSlides;

@TeleOp(name="PID Tuning Vertical Slides Test", group="Active TeleOps")
public class NewVerticalSlidesTuning extends OpMode {
    private FtcDashboard dash = FtcDashboard.getInstance();
    private Telemetry dashboardTelemetry = dash.getTelemetry();
    private ElapsedTime elapsedtime;


    private NewVerticalSlides verticalSlides = new NewVerticalSlides();

    @Override
    public void init() {
        verticalSlides.autoInitialize(this);
        elapsedtime = new ElapsedTime();

    }

    @Override
    public void start() {
        elapsedtime.reset();
    }

    @Override
    public void loop() {
        verticalSlides.operateTuning();

        dashboardTelemetry.addData("Right Motor Encoder Pos: ", verticalSlides.telemetryMotorPos());
        dashboardTelemetry.addData("Target: ", verticalSlides.telemetryTarget());
        dashboardTelemetry.addData("PID Power R: ", verticalSlides.telemetryOutput());
        dashboardTelemetry.addData("elapsed time (loop time)", elapsedtime.milliseconds());
        dashboardTelemetry.update();

        telemetry.addData("loop time", elapsedtime.milliseconds());
        telemetry.update();
        elapsedtime.reset();
    }
}
