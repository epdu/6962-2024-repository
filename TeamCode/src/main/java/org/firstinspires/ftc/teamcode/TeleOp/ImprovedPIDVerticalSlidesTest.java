package org.firstinspires.ftc.teamcode.TeleOp;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.NewVerticalSlides;

@TeleOp(name="PID Tuning Vertical Slides Test", group="Active TeleOps")
public class ImprovedPIDVerticalSlidesTest extends OpMode {
    // It has been brought to my attention that my current
    // vertical slides code is cooked, so I am redoing it all.
    // This time, I will tune it properly.
    private FtcDashboard dash = FtcDashboard.getInstance();
    private Telemetry dashboardTelemetry = dash.getTelemetry();

    private NewVerticalSlides verticalSlides = new NewVerticalSlides();

    @Override
    public void init() {
        verticalSlides.initialize(this);
    }

    @Override
    public void loop() {
        // Jayden will find time to tune this properly after first comp
        verticalSlides.operateTest();

        dashboardTelemetry.addData("Right Motor Encoder Pos: ", verticalSlides.telemetryMotorPos());
        dashboardTelemetry.addData("Target: ", verticalSlides.telemetryTarget());
        dashboardTelemetry.addData("PID Power R: ", verticalSlides.telemetryOutput());
        dashboardTelemetry.addData("Retracted Bool: ", verticalSlides.verticalSlidesRetracted);
        dashboardTelemetry.update();
    }
}
