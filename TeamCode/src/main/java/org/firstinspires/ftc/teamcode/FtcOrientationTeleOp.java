//package org.firstinspires.ftc.teamcode;
//
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Servo;
//
//
//@TeleOp
//public class FtcOrientationTeleOp extends LinearOpMode {
//
//
//    DcMotor LFMotor;
//    DcMotor LBMotor;
//    DcMotor RFMotor;
//    DcMotor RBMotor;
//
//
//    DcMotor liftMotorL;
//    DcMotor liftMotorR;
//
//
//    Servo Claw;
//    Servo ArmR;
//    Servo ArmL;
//    Servo Wirst;
//
//
//    boolean move = false;
//
//
//    @Override public void runOpMode() {
//        LFMotor = hardwareMap.dcMotor.get("LFMotor"); //control hub port 1
//        LBMotor = hardwareMap.dcMotor.get("LBMotor"); //control hub port 0
//        RFMotor = hardwareMap.dcMotor.get("RFMotor"); // expansion hub port 1
//        RBMotor = hardwareMap.dcMotor.get("RBMotor"); // expansion hub port 0
//
//
//        RFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//
//
//        liftMotorL = hardwareMap.get(DcMotor.class, "liftMotorL"); //control hub ort 2
//        liftMotorR = hardwareMap.get(DcMotor.class, "liftMotorR");
//
//
//        int positionL = liftMotorL.getCurrentPosition();
//        int positionR = liftMotorR.getCurrentPosition();
//
//
//        liftMotorR.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
//        liftMotorL.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
//
//
//        liftMotorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        liftMotorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//
//        liftMotorR.setDirection(DcMotorSimple.Direction.REVERSE);
//
//
//        Claw = hardwareMap.servo.get("Claw"); // expansion hub servo port 2
//        Claw.setPosition(0.72);
////        ArmR = hardwareMap.servo.get("ArmR"); // expansion hub servo port 0
//        ArmL = hardwareMap.servo.get("ArmL"); // expansion hub servo port 1
//        ArmL.setDirection(Servo.Direction.REVERSE);
//        ArmL.setPosition(0.927);
//        Wirst = hardwareMap.servo.get("Wirst"); // expansion hub servo port 3
//        Wirst.setPosition(1);
////        Wirst.setDirection(Servo.Direction.REVERSE);
//        waitForStart();
//
//
//        while (opModeIsActive()) {
//            liftArmHigh();
//            moveDriveTrain();
//            if (gamepad1.right_trigger > 0.3) { //open
//                Claw.setPosition(0.5);
//            }if (gamepad1.left_trigger > 0.3) { //close
//                Claw.setPosition(0.72);
//
//
//            }if (gamepad2.a  && !move) { //down
//                Wirst.setPosition(1);
//                ArmL.setPosition(0.927);
//            }
//            if (gamepad2.y && !move) { // up
//                ArmL.setPosition(0.1);
//                Wirst.setPosition(0);
//
//            }
//            if (gamepad2.b && !move) { //up
//                Wirst.setDirection(Servo.Direction.REVERSE);
//                Wirst.setPosition(0);
//            }
//            if (gamepad2.x && !move) { //down
//                Wirst.setPosition(1);
//            }
//            //for up
//
//
//
//
//            //for down
//        }
//    }
//    public void moveDriveTrain() {
//        double y = gamepad1.left_stick_y;
//        double x =- gamepad1.left_stick_x;
//        double rx = -gamepad1.right_stick_x;
//
//
//
//
//        double fl = y + x + rx;
//        double bl = y - x + rx;
//        double fr = y - x - rx;
//        double br = y + x - rx;
//
//
//        LFMotor.setPower(fl*0.5);
//        LBMotor.setPower(bl*0.5);
//        RFMotor.setPower(fr*0.5);
//        RBMotor.setPower(br*0.5);
//
//
//    }
//    public void liftArmHigh () {
//        double liftArm_y = -gamepad2.left_stick_y;
//        liftMotorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        liftMotorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        liftMotorL.setPower(liftArm_y*0.5);
//        liftMotorR.setPower(liftArm_y*0.5);
//        liftMotorR.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
//        liftMotorL.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
//        //up joystick makes the slides rotate clockwise on the out right side
//        //when looking at the robots right side from the outside wall the slide pulley spins clockwise/to the right when the joystick is pushed up
//
//
//    }
//}
//
//
//
//
