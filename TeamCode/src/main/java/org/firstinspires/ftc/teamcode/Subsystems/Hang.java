package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Util.HangStates.DEPLOYED;
import static org.firstinspires.ftc.teamcode.Util.HangStates.EXTEND;
import static org.firstinspires.ftc.teamcode.Util.HangStates.GROUND;
import static org.firstinspires.ftc.teamcode.Util.HangStates.PTO;
import static org.firstinspires.ftc.teamcode.Util.HangStates.RETRACT;
import static org.firstinspires.ftc.teamcode.Util.HangStates.STAGETWO;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Util.HangStates;
import org.firstinspires.ftc.teamcode.Util.RobotHardware;

public class Hang {
    private final RobotHardware rHardware = new RobotHardware();
    private CRServo hangServoL, hangServoR;

    private Servo ptoServo;
    private DcMotorEx vRslideMotor, vLslideMotor, dcBackLeftMotor, dcFrontLeftMotor;
    private OpMode opmode;
    private NavxManager navxManager;

    private static final double CR_SERVO_POWER = 1.0;
    private static final double CR_SERVO_REVERSE_POWER = -1.0;
    private static final double SLIDE_POWER = 1.0;
    private static final double SLIDE_RETRACTION_POWER = -1.0;

    private static final double MOTOR_RETRACTION_POWER = -1.0;
    private static final double SERVO_RUN_TIME = 2.0; // adjust as necessary TODO: TUNE

//    private static final double SERVO_REVERSE_TIME = x to tune if auto doesnt work TODO: TUNE
    private static final double SLIDE_EXTENSION_TIME = 1.5; // adjust as necessary TODO: TUNE

    private static final double MOTOR_RUN_TIME = 1.5; // TODO: TUNE
    private static final double SLIDE_RETRACTION_TIME = 1.5; // adjust as necessary TODO: TUNE
    private static final double TARGET_PITCH = -10.0; // Desired pitch angle (adjust as necessary) TODO: TUNE

    private static final double RELEASE_POSITION = 0.4;
    private static final double SET_PTO_POS = 0.0061;

    public double servoPos;

    private boolean deployed = false; // this is for starting the hang (stage 1 hang)
    private boolean stageTwoActivated = false; // this is for the stage 2 part of the hang

    public HangStates hangState;

    public Hang() {}
    public void initialize(OpMode opmode) {
        rHardware.init(opmode.hardwareMap);
        this.opmode = opmode;
        this.hangServoL = rHardware.hangServoL;
        this.hangServoR = rHardware.hangServoR;
        this.vRslideMotor = rHardware.vRslideMotor;
        this.vLslideMotor = rHardware.vLslideMotor;
        this.dcBackLeftMotor = rHardware.DcLeftBackMotor;
        this.dcFrontLeftMotor = rHardware.DcLeftBackMotor;
        this.ptoServo = rHardware.ptoActivationServo;
        this.navxManager = new NavxManager(rHardware.navx);
        this.hangState = GROUND;

        vRslideMotor.setDirection(DcMotorEx.Direction.REVERSE);
        dcBackLeftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        dcFrontLeftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        vRslideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        vLslideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        vRslideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        vLslideMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        hangServoL.setDirection(DcMotorSimple.Direction.REVERSE);
//        setPtoServo(SET_PTO_POS);
    }

    public void operateTest() {
        servoPos = ptoServo.getPosition();

        hangServoL.setPower(-opmode.gamepad2.left_stick_y);
        hangServoR.setPower(-opmode.gamepad2.left_stick_y);

        if (opmode.gamepad2.left_bumper) {
            setPtoServo(servoPos - 0.001);
        } else if (opmode.gamepad2.right_bumper) {
            setPtoServo(servoPos + 0.001);
        }

        //TODO: test if reversed or not
        vLslideMotor.setPower(-opmode.gamepad2.right_stick_y);
        vRslideMotor.setPower(-opmode.gamepad2.right_stick_y);

        dcBackLeftMotor.setPower(-opmode.gamepad2.right_stick_y);
        dcFrontLeftMotor.setPower(-opmode.gamepad2.right_stick_y);

        opmode.telemetry.addData("activation micro servo pos", servoPos);
        opmode.telemetry.addData("front left current draw", dcFrontLeftMotor.getCurrent(CurrentUnit.AMPS));
        opmode.telemetry.addData("back left current draw", dcBackLeftMotor.getCurrent(CurrentUnit.AMPS));
        opmode.telemetry.addData("left slide current draw", vLslideMotor.getCurrent(CurrentUnit.AMPS));
        opmode.telemetry.addData("right slide current draw", vRslideMotor.getCurrent(CurrentUnit.AMPS));
        opmode.telemetry.addData("navx pitch", navxManager.getPitch());
    }

    public Action getHangSequence() {
        return new SequentialAction(
                new InstantAction(() -> runServos(CR_SERVO_POWER)),
                new SleepAction(SERVO_RUN_TIME),
                new InstantAction(this::stopServos)
//                new InstantAction(() -> switchHangState(hangState))
        );
    }

    public Action engagePTO() {
        if (hangState == GROUND) {
            return new InstantAction(() -> setPtoServo(SET_PTO_POS));
        } else if (hangState == EXTEND) {
            return new ParallelAction(
//                new InstantAction(() -> switchHangState(hangState)),
                new InstantAction(() -> setPtoServo(RELEASE_POSITION))
            );
        }
        return new InstantAction(() -> setPtoServo(SET_PTO_POS));
    }



    public Action getHangSequenceTwo() {
        // First action: Monitor pitch and stop servos when target pitch is reached
        return new SequentialAction(
                new InstantAction(() -> runServos(CR_SERVO_REVERSE_POWER)),
                new SleepAction(2),
                new InstantAction(() -> stopServos())
//                new Action() {
//                    @Override
//                    public boolean run(TelemetryPacket packet) {
//                        if (navxManager.getPitch() <= TARGET_PITCH) {
//                            stopServos(); // Stop servos when the target pitch is achieved
////                            switchHangState(hangState);
//                            return false; // Action complete
//                        } else {
//                            return true; // Continue running
//                        }
//                    }
//                }
                // Second action: Extend and retract slides (seperated into diff action)
        );
    }
    public Action getHangExtend() {
        return new SequentialAction(
                new ParallelAction(
                    new InstantAction(() -> setVerticalSlidePower(SLIDE_POWER)),
                    new SleepAction(SLIDE_EXTENSION_TIME),
                    new InstantAction(() -> setVerticalSlidePower(0))
                )
//                new InstantAction(() -> switchHangState(hangState))
        );
    }

    public Action getHangRetract() {
        return new ParallelAction(
            //engage backleft
            new InstantAction(() -> runBackLeftMotor(MOTOR_RETRACTION_POWER)),
            new SleepAction(MOTOR_RUN_TIME),

            //engage vert
            new InstantAction(() -> setVerticalSlidePower(SLIDE_RETRACTION_POWER)),
            new SleepAction(SLIDE_RETRACTION_TIME)
//            new InstantAction(() -> switchHangState(hangState))
        );
    }

    public Action reverseHangSequence() {
//        this.hangState = GROUND;
        return new SequentialAction(
                new InstantAction(() -> reverseServos(CR_SERVO_POWER)),
                new SleepAction(SERVO_RUN_TIME),
                new InstantAction(this::stopServos)
        );
    }

    // Helper methods to control servos and slide motor
    public void runServos(double power) {
        hangServoR.setPower(power);
        hangServoL.setPower(power);
    }

    public void stopServos() {
        hangServoR.setPower(0);
        hangServoL.setPower(0);
    }

    public void reverseServos(double power) {
        hangServoR.setPower(-power);
        hangServoL.setPower(-power);
    }

    // set power for both vertical slide motors
    private void setVerticalSlidePower(double power) {
        vRslideMotor.setPower(power);
        vLslideMotor.setPower(power);
    }

    private void setPtoServo(double position) {
        ptoServo.setPosition(position);
    }

    public void ptoServoSustain() {setPtoServo(SET_PTO_POS);}
    public void ptoServoRelease() {setPtoServo(RELEASE_POSITION);}

    private void runBackLeftMotor(double power) {
        dcBackLeftMotor.setPower(power);
    }

    //used for switching states in actions
    public void switchHangState() {
        switch (hangState) {
            case GROUND:
                hangState = DEPLOYED;
                break;
            case DEPLOYED:
                hangState = STAGETWO;
                break;
            case STAGETWO:
                hangState = EXTEND;
                break;
            case EXTEND:
                hangState = PTO;
                break;
            case PTO:
                hangState = RETRACT;
                break;
            default:
                this.hangState = GROUND;
        }
    }
}
