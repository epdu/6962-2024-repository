package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeArm;
import org.firstinspires.ftc.teamcode.Subsystems.ScoringArm;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalSlides;

@TeleOp(name = "Incremental Teleop", group = "Active TeleOps")
public class IncrementalTeleOp extends OpMode {
    private ScoringArm scoringArm = new ScoringArm();
    private IntakeArm intakeArm = new IntakeArm();
    private HorizontalSlides horizontalSlides = new HorizontalSlides();
    private VerticalSlides verticalSlides = new VerticalSlides();

    @Override
    public void init() {
        scoringArm.initialize(this);
        intakeArm.initialize(this);
        horizontalSlides.autoInitialize(this);
        verticalSlides.autoInitialize(this);

        telemetry.addLine("Gamepad 1: intake incrementals");
        telemetry.addLine("Left and Right Bumper - claw incremental");
        telemetry.addLine("D-Pad Up and Down - incremental arm");
        telemetry.addLine("D-Pad Left and Right - incremental rotate");
        telemetry.addLine("Left Joystick Y - incremental wrist flip");
        telemetry.addLine("Right Joystick Y - incremental horizontal slides");
        telemetry.addLine("A - whole arm hover");
        telemetry.addLine("B - whole arm transfer");
        telemetry.addLine("X - retract");
        telemetry.addLine("Y - extend");

        telemetry.addLine("\nGamepad 2: scoring incrementals");
        telemetry.addLine("Left and Right Bumper - claw incremental");
        telemetry.addLine("D-Pad Up and Down - incremental arm");
        telemetry.addLine("Left Joystick Y - incremental wrist turning");
        telemetry.addLine("Left Joystick X - incremental wrist rotation");
        telemetry.addLine("Right Joystick Y - incremental vertical slides");
        telemetry.addLine("A - whole arm score position");
        telemetry.addLine("B - whole arm transfer position");
        telemetry.addLine("X - rectract");
        telemetry.addLine("Y - extend bucket");
        telemetry.update();
    }

    @Override
    public void start() {
        telemetry.clear();
    }

    @Override
    public void loop() {
        scoringArm.operateIncremental();
        intakeArm.operateIncremental();
        horizontalSlides.operateIncremental();
        verticalSlides.operateIncremental();
        telemetry.update();

    }
}
