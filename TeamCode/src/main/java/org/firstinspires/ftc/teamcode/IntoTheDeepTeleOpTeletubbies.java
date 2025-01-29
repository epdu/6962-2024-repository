package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.HardwareTeletubbies;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.FieldCentricMecanumTeleOpTeletubbies.DriveTrains_ReducePOWER;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import static org.firstinspires.ftc.teamcode.Constants_CS.*;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_X_IN;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_B_EXTRUDE;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_B_EXTRUDETransfer;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_B_EXTRUDETransferC;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_B_EXTRUDE_MORE;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_A_BOTTOM;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_Y_LOW;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_Y_HIGH;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_Y_HIGHH;
import static org.firstinspires.ftc.teamcode.Constants_CS.POSITION_Y_HIGHHH;
import static org.firstinspires.ftc.teamcode.Constants_CS.SLIDE_POWER_H;
import static org.firstinspires.ftc.teamcode.Constants_CS.SLIDE_POWER_V;
import static org.firstinspires.ftc.teamcode.Constants_CS.IClawOpen;
import static org.firstinspires.ftc.teamcode.Constants_CS.IClawCloseLose;
import static org.firstinspires.ftc.teamcode.Constants_CS.IClawCloseTight;
import static org.firstinspires.ftc.teamcode.Constants_CS.OClawOpen;
import static org.firstinspires.ftc.teamcode.Constants_CS.OClawCloseLose;
import static org.firstinspires.ftc.teamcode.Constants_CS.OClawCloseTight;
import static org.firstinspires.ftc.teamcode.Constants_CS.OArmTransferPosition;
import static org.firstinspires.ftc.teamcode.Constants_CS.OArmRearSpecimenPick;
import static org.firstinspires.ftc.teamcode.Constants_CS.OClawSpecimenChambers;
import static org.firstinspires.ftc.teamcode.Constants_CS.SERVO_STEP;
import static org.firstinspires.ftc.teamcode.Constants_CS.SLIDE_POWER;
import static org.firstinspires.ftc.teamcode.Constants_CS.speedMultiplier;
import static org.firstinspires.ftc.teamcode.Constants_CS.speedLimiter1;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawLeft;
import static org.firstinspires.ftc.teamcode.Constants_CS.IArmLDown;
import static org.firstinspires.ftc.teamcode.Constants_CS.IArmRDown;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristxpitchDown;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristxpitchUp;
import static org.firstinspires.ftc.teamcode.Constants_CS.IArmLUp;
import static org.firstinspires.ftc.teamcode.Constants_CS.IArmRUp;
import static org.firstinspires.ftc.teamcode.Constants_CS.IArmLDownForPick;
import static org.firstinspires.ftc.teamcode.Constants_CS.IArmRDownForPick;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristxpitchIntermedia4PositionAdjust;
import static org.firstinspires.ftc.teamcode.Constants_CS.OClawCloseSuperTight;
import static org.firstinspires.ftc.teamcode.Constants_CS.speedLimiterFaster;
import static org.firstinspires.ftc.teamcode.Constants_CS.speedLimiterSlower;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
import static org.firstinspires.ftc.teamcode.Constants_CS.WristzyawRight;
@TeleOp(name = "AAAAA TeleOp 12252024 V1")
//V1 with pid but not  odo
public class IntoTheDeepTeleOpTeletubbies extends LinearOpMode {
    public float DriveTrains_ReducePOWER=0.75f;
    //   DriveTrains_ReducePOWER = 0.75f;
//    DriveTrains_ReducePOWER = speedLimiterSlower;//************************
    HardwareTeletubbies robot = new HardwareTeletubbies();
    public String fieldOrRobotCentric = "robot";
    boolean move = false;
    // 在类顶部声明PID控制器
    // 状态变量
    private boolean pidActive = false; // PID 控制是否激活
    private int pidTargetPosition = 0; // PID 控制目标位置
    private PIDController pidController = new PIDController(0.005, 0.0000005, 0.0002);// (0.005, 0.0000005, 0.0002) good for target 300 (1.9, 0.014, 4.9)
    // Tune these values  POSITION_B_EXTRUDETransfer = 600;//horizontal slides  out //600 is too much

    //    private PIDController pidControllerR = new PIDController(1.9, 0.014, 4.9); // Tune these values
//private PIDController pidControllerL = new PIDController(1.9, 0.014, 4.9); // Tune these values  POSITION_B_EXTRUDETransfer = 600;//horizontal slides  out //600 is too much
    int controlMode = 1;
    ButtonHandler dpadDownHandler = new ButtonHandler();
    ButtonHandler dpadUpHandler = new ButtonHandler();
    ButtonHandler dpadLeftHandler = new ButtonHandler();
    ButtonHandler dpadRightHandler = new ButtonHandler();
    ButtonHandler leftBumperHandler = new ButtonHandler();
    ButtonHandler rightBumperHandler = new ButtonHandler();
    ButtonHandler gamepad1XHandler = new ButtonHandler();
    ButtonHandler gamepad1BHandler = new ButtonHandler();
    ButtonHandler gamepad1YHandler = new ButtonHandler();
    ButtonHandler gamepad1AHandler = new ButtonHandler();
    ButtonHandler gamepad1BackHandler = new ButtonHandler();
    Gyro gyro = new Gyro(); // 创建 Gyro 类的对象
    private volatile boolean isRunning = true;
    ElapsedTime delayTimer = new ElapsedTime();
    /*
    package mypackage; // 与 Gyro 类的包名一致
            Gyro gyro = new Gyro(); // 创建 Gyro 类的对象
            gyro.turn();            // 调用 turn() 方法
     */
    public String armPositionCuzBorS ="NOLL"; //new variable for it and arm will go back of robo

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot.init(hardwareMap);
        gyro.robot.init(hardwareMap);
        Thread driveTrainThread = new Thread(this::runDriveTrain);
        Thread updateVSlidePIDControl = new Thread(this::runupdateVSlidePIDControl);
        Thread intakeThread = new Thread(this::runIntake);
        Thread outtakeThread = new Thread(this::runOuttake);

        driveTrainThread.start();
        updateVSlidePIDControl();
        intakeThread.start();
        outtakeThread.start();

        waitForStart();

        while (opModeIsActive()) {
//            telemetry.addData("Status", "All systems running...");
//            telemetry.update();
//            moveDriveTrain_RobotCentric(); // Select either RobotCentricDriveTrain() or FieldCentricDriveTrain() based on your requirements.
            if (gamepad1.right_stick_button) { //fix it later;
                DriveTrains_ReducePOWER = 0.25f;
                telemetry.addData("DriveTrains_ReducePOWER", DriveTrains_ReducePOWER);
                telemetry.update();
                // Non-blocking delay to prevent rapid mode switching
                delayTimer.reset();
                while (delayTimer.milliseconds() < 300 && opModeIsActive()) {
                    // Other tasks can be processed here
                } // 防止快速连击导致模式快速切换
            }
            if (gamepad1.left_stick_button) { //fix it later;
                DriveTrains_ReducePOWER = 0.80f;
                telemetry.addData("DriveTrains_ReducePOWER", DriveTrains_ReducePOWER);
                telemetry.update();
                // Non-blocking delay to prevent rapid mode switching
                delayTimer.reset();
                while (delayTimer.milliseconds() < 300 && opModeIsActive()) {
                    // Other tasks can be processed here
                } // 防止快速连击导致模式快速切换
            }

            if (gamepad1.start) { // 切换控制模式
                controlMode = (controlMode + 1) % 2; // 假设两种模式 0 和 1
                telemetry.addData("Control Mode", controlMode == 0 ? "Mode 0: Standard" : "Mode 1: Advanced");
                telemetry.update();
                // Non-blocking delay to prevent rapid mode switching
                delayTimer.reset();
                while (delayTimer.milliseconds() < 300 && opModeIsActive()) {
                    // Other tasks can be processed here
                } // 防止快速连击导致模式快速切换
            }

            moveDriveTrain_FieldCentric() ;
            updateVSlidePIDControl();
            intake();
            outtake();

//            moveDriveTrain(); //robot centric
//            liftVertSlidesHigh();
//            extrHoriSlidesLong();
//            servoGamepadControl();
////////////////////////////////////


        } //end of while loop
        // Stop all threads when op mode stops
        isRunning = false;
        try {
            driveTrainThread.join();
            intakeThread.join();
            outtakeThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    } //end of run mode

    // Thread for drive train
    private void runDriveTrain() {
        while (isRunning) {
            moveDriveTrain_FieldCentric();
//            sleep(50); // Add a short delay to prevent CPU overutilization
            while (delayTimer.milliseconds() < 50 && opModeIsActive()) {
                // Other tasks can be processed here
            }
        }
    }

    // Thread for drive train
    private void runupdateVSlidePIDControl() {
        while (isRunning) {
            updateVSlidePIDControl();
//            sleep(50); // Add a short delay to prevent CPU overutilization
            while (delayTimer.milliseconds() < 50 && opModeIsActive()) {
                // Other tasks can be processed here
            }
        }
    }

    // Thread for intake
    private void runIntake() {
        while (isRunning) {
            intake();
//            sleep(50); // Add a short delay to prevent CPU overutilization
            while (delayTimer.milliseconds() < 50 && opModeIsActive()) {
                // Other tasks can be processed here
            }
        }
    }

    // Thread for outtake
    private void runOuttake() {
        while (isRunning) {
            outtake();
//            sleep(50); // Add a short delay to prevent CPU overutilization
            while (delayTimer.milliseconds() < 50 && opModeIsActive()) {
                // Other tasks can be processed here
            }
        }
    }


    //Begin Definition and Initialization of intake()
    public void intake() {

//Begin Definition and Initialization of gamepad
//        if (gamepad1.start) { // 切换控制模式
//            controlMode = (controlMode + 1) % 2; // 假设两种模式 0 和 1
//            telemetry.addData("Control Mode", controlMode == 0 ? "Mode 0: Standard" : "Mode 1: Advanced");
//            telemetry.update();
//            sleep(300); // 防止快速连击导致模式快速切换
//        }
// 根据不同模式定义按键功能
        switch (controlMode) {
            case 0:
                // intake
                dpadDownHandler.update(gamepad1.dpad_down);
                dpadUpHandler.update(gamepad1.dpad_up);
                dpadLeftHandler.update(gamepad1.dpad_left);
                dpadRightHandler.update(gamepad1.dpad_right);
                leftBumperHandler.update(gamepad1.left_bumper);
                rightBumperHandler.update(gamepad1.right_bumper);
                gamepad1XHandler.update(gamepad1.x);
                gamepad1BHandler.update(gamepad1.b);
                gamepad1YHandler.update(gamepad1.y);
                gamepad1AHandler.update(gamepad1.a);

                gamepad1BackHandler.update(gamepad1.back);
                //Begin  moveHSlideToPosition

                if (gamepad1.dpad_left) { //IN
                    moveHSlideToPosition(POSITION_X_IN);
//                    robot.Wristxpitch.setPosition(WristxpitchUp);
//                     startVSlidePIDControl(POSITION_Y_HIGHHH);// very high//specimen high for testing 01152025
                    gamepad1BHandler.reset();
                }
                if (gamepad1.dpad_down) { //EXTRUDE
                    moveHSlideToPosition(POSITION_B_EXTRUDE);
//                    robot.Wristxpitch.setPosition(WristxpitchIntermedia4PositionAdjust);
//                    robot.Wristxpitch.setPosition(WristxpitchDown);
//                   startVSlidePIDControl(POSITION_A_BOTTOM);// very high//specimen high for testing 01152025
                    gamepad1XHandler.reset();
                }
                if (gamepad1.dpad_up) { //EXTRUDE_MORE
                    moveHSlideToPosition(POSITION_B_EXTRUDETransferC);
                    gamepad1XHandler.reset();
                }
                if (gamepad1.dpad_right) { //EXTRUDE_MORE
                    moveHSlideToPosition(POSITION_B_EXTRUDE_MORE);
                    gamepad1XHandler.reset();
                }

 /*

                if (gamepad1BHandler.isShortPress()) { //IN
                    moveHSlideToPosition(POSITION_X_IN);
                    telemetry.addData("Status", "POSITION_X_IN");
                    telemetry.update();
                    gamepad1BHandler.reset();
                }
                if (gamepad1XHandler.isShortPress()) { //EXTRUDE
                    moveHSlideToPosition(POSITION_B_EXTRUDE);
                    telemetry.addData("Status", "POSITION_B_EXTRUDE");
                    telemetry.update();
//                    sleep(600);
//                    gyro.turn(90);// 调用 turn() 方法turn(90);
//                    telemetry.addData("Status", "gyro.turn");
//                    telemetry.update();
//                    sleep(1000);
                    gamepad1XHandler.reset();
                }
                if (gamepad1XHandler.isLongPress()) { //EXTRUDE_MORE
                    moveHSlideToPosition(POSITION_B_EXTRUDE_MORE);
                    gamepad1XHandler.reset();
                }

                */
                //End  moveHSlideToPosition

//Begin  open and close of intakeclaw 12122024 finetuned

                if (gamepad1.left_trigger > 0.3 && gamepad1.left_trigger <= 0.7) { // 轻按
                    robot.IClaw.setPosition(IClawOpen); //12122024
                }
                if (gamepad1.right_trigger > 0.3 && gamepad1.right_trigger <= 0.7) { // 轻按
                    robot.IClaw.setPosition(IClawCloseLose); //0.54 moveable 0.542 barely movable 0.543 hold
                }
                if (gamepad1.right_trigger > 0.7) { // 深按
                    robot.IClaw.setPosition(IClawCloseTight); //0.54 moveable 0.542 barely movable 0.543 hold
                }

//End open and close of intakeclaw

//Begin  Wristzyaw
                if (gamepad1.b) { //right
                    robot.Wristzyaw.setPosition(WristzyawRight); //Wristzyaw right 45 degree 12122024
                }
                if (gamepad1.x) { //left
                    robot.Wristzyaw.setPosition(WristzyawLeft); // Wristzyaw left 45 degree 12122024 // robot.Wristzyaw.setPosition(0.65); for left
                }

//one key ready for pick
                if (gamepad1.left_bumper) { //up if arm is Horizontal, the the wrist is vertical up and down
//                    robot.OArmL.setPosition(OArmRearSpecimenPick);
//                    robot.OArmR.setPosition(OArmRearSpecimenPick);
                    robot.OClaw.setPosition(OClawOpen); //
                    delayTimer.reset();
                    while (delayTimer.milliseconds() < 200 && opModeIsActive()) {
                        // Other tasks can be processed here
                    }
                    robot.Wristxpitch.setPosition(WristxpitchDown);
                    robot.IClaw.setPosition(IClawOpen);
                    robot.IArmL.setPosition(IArmLDown);
                    robot.IArmR.setPosition(IArmRDown);
                    //                    moveHSlideToPosition(POSITION_B_EXTRUDETransferC);
//                    sleep(500);
                    //                    robot.OClaw.setPosition(OClawOpen); //open
                }


/*

////one key ready for pick
//            if (gamepad1.right_bumper && !move) { //up if arm is Horizontal, the the wrist is vertical up and down
//                robot.Wristxpitch.setPosition(0.65);
//                sleep(200);
//                robot.IClaw.setPosition(0.32);
//                sleep(200);
//                robot.IArmL.setPosition(0.7);
//                robot.IArmR.setPosition(0.7);
//            }
//
////one key ready for pick up

*/
//one key ready for pick up

//one key ready for transfer
                if (gamepad1.right_bumper) { //
                    robot.OArmL.setPosition(OArmTransferPosition);//transfer position
                    robot.OArmR.setPosition(OArmTransferPosition);
                    robot.Wristxpitch.setPosition(WristxpitchIntermedia4PositionAdjust); // Wristxpitch
//                    sleep(600);
                    delayTimer.reset();
                    while (delayTimer.milliseconds() < 600 && opModeIsActive()) {
                        // Other tasks can be processed here
                    } // 防止快速连击导致模式快速切换
//                    robot.IClaw.setPosition(IClawCloseTight); //  0.543
                    robot.IClaw.setPosition(IClawCloseSuperTight); //  0.544
                    moveHSlideToPosition(30);
                    sleep(500);
                    robot.Wristxpitch.setPosition(0.05); // Wristxpitch
                    robot.IArmL.setPosition(0.65);
                    robot.IArmR.setPosition(0.65);
                    sleep(500);
                    robot.OClaw.setPosition(OClawCloseTight); // close 0.543 hold
                    sleep(500);
                    robot.IClaw.setPosition(IClawOpen); //open
                    sleep(300);
//                    moveHSlideToPosition(POSITION_B_EXTRUDETransferC);
//                    sleep(300);
                    robot.OArmL.setPosition(OArmRearSpecimenPick);
                    robot.OArmR.setPosition(OArmRearSpecimenPick);
//                    moveVSlideToPosition(-POSITION_Y_HIGH);// high
//                      startVSlidePIDControl(POSITION_Y_HIGHH);

                }

//one key ready for transfer

//******************Begin  IArm L and R****************

                if (gamepad1.y) { //up
                    robot.IArmL.setPosition(IArmLUp);  // always same as hardware IArmL.setPosition(0.6);
                    robot.IArmR.setPosition(IArmRUp);
                }
                if (gamepad1.a ) { //down
                    robot.IArmL.setPosition(IArmLDownForPick);
                    robot.IArmR.setPosition(IArmRDownForPick); //
                }

//******************end  IArm L and R*****************


                break;

            case 1:
                // out take
                dpadDownHandler.update(gamepad1.dpad_down);
                dpadUpHandler.update(gamepad1.dpad_up);
                dpadLeftHandler.update(gamepad1.dpad_left);
                dpadRightHandler.update(gamepad1.dpad_right);
                leftBumperHandler.update(gamepad1.left_bumper);
                rightBumperHandler.update(gamepad1.right_bumper);
                gamepad1XHandler.update(gamepad1.x);
                gamepad1BHandler.update(gamepad1.b);
                gamepad1YHandler.update(gamepad1.y);
                gamepad1AHandler.update(gamepad1.a);
                //Begin  moveVSlideToPosition
                // 左触发器双功能：轻按和深按
                if (gamepad1.dpad_left) { //IN
                    startVSlidePIDControl(POSITION_A_BOTTOM);
                    if (armPositionCuzBorS =="POSITION_Y_HIGHHH") {
                        robot.OArmL.setPosition(OArmRearSpecimenPick);//arm in back of robot
                        robot.OArmR.setPosition(OArmRearSpecimenPick);
                    }
                    if (armPositionCuzBorS =="POSITION_Y_HIGHH") {
                        robot.OArmL.setPosition(OArmTransferPosition);//arm in front of robot
                        robot.OArmR.setPosition(OArmTransferPosition);
                    }
                    gamepad1BHandler.reset();
                }
                if (gamepad1.dpad_down) { //EXTRUDE
                    startVSlidePIDControl(POSITION_Y_HIGHH);//bucket high
                    robot.OArmL.setPosition(OArmBucket);
                    robot.OArmR.setPosition(OArmBucket);
                    armPositionCuzBorS = "POSITION_Y_HIGHH"; //arm is going to go front of robo
//                    delayTimer.reset();
//                    while (delayTimer.milliseconds() < 600 && opModeIsActive()) {
//                        // Other tasks can be processed here
//                    }

                    gamepad1XHandler.reset();
                }
                if (gamepad1.dpad_up) { //EXTRUDE_MORE  //moveVSlideToPositionPID(POSITION_Y_HIGH);
                    startVSlidePIDControl(POSITION_Y_HIGH);
                    robot.OArmL.setPosition(OArmTransferPosition);
                    robot.OArmR.setPosition(OArmTransferPosition);
                    gamepad1XHandler.reset();
                }
                if (gamepad1.dpad_right) { //EXTRUDE_MORE
                    startVSlidePIDControl(POSITION_Y_HIGHHH);// very high//specimen high
                    armPositionCuzBorS = "POSITION_Y_HIGHHH"; //arm is going to go back of robo
                    gamepad1XHandler.reset();

                }
                /*
                // 左触发器双功能：轻按和深按
                if (gamepad1BHandler.isShortPress()) { //IN
//                    moveVSlideToPositionPID(POSITION_A_BOTTOM);// slides down
                    startVSlidePIDControl(POSITION_A_BOTTOM);
//                    moveVSlideToPosition(POSITION_A_BOTTOM);// slides down
                    gamepad1BHandler.reset();
                }
                if (gamepad1XHandler.isShortPress()) { //EXTRUDE
//                    moveVSlideToPositionPID(POSITION_Y_LOW);
                    startVSlidePIDControl(POSITION_Y_LOW);
//                    moveVSlideToPositionPID(POSITION_Y_LOWForTest);
//                moveVSlideToPosition(-POSITION_Y_LOW);// slides move to middle
                    gamepad1XHandler.reset();
                }
                if (gamepad1XHandler.isShortPress() && gamepad1BHandler.isShortPress()) { //EXTRUDE_MORE/                    moveVSlideToPositionPID(POSITION_Y_HIGH);
                    startVSlidePIDControl(POSITION_Y_HIGH);
//                moveVSlideToPosition(-POSITION_Y_HIGHHH);// high
//                    moveVSlideToPosition(-POSITION_Y_HIGH);// high
//                    moveVSlideToPosition(-POSITION_Y_HIGHH);// high
                    gamepad1XHandler.reset();
                }
//                    if (gamepad1XHandler.isLongPress()) { //EXTRUDE_MORE
//                        moveVSlideToPosition(-POSITION_Y_HIGHH);// very high
//                        gamepad1XHandler.reset();
//                    }
/////
*/

///////////////////////////////////////

                //************End  moveVSlideToPosition***************

//one key ready for pick
                if (gamepad1.left_bumper) { //up if arm is Horizontal, the the wrist is vertical up and down
//                    robot.OArmL.setPosition(OArmRearSpecimenPick);
//                    robot.OArmR.setPosition(OArmRearSpecimenPick);
                    robot.OClaw.setPosition(OClawOpen); //
                    delayTimer.reset();
                    while (delayTimer.milliseconds() < 200 && opModeIsActive()) {
                        // Other tasks can be processed here
                    }
                    robot.Wristxpitch.setPosition(WristxpitchDown);
                    robot.IClaw.setPosition(IClawOpen);
                    robot.IArmL.setPosition(IArmLDown);
                    robot.IArmR.setPosition(IArmRDown);
                    //                    moveHSlideToPosition(POSITION_B_EXTRUDETransferC);
//                    sleep(500);
                    //                    robot.OClaw.setPosition(OClawOpen); //open
                }

//one key ready for pick up

//one key ready for transfer
                if (gamepad1.right_bumper) { //
                    robot.OArmL.setPosition(OArmTransferPosition);//transfer position
                    robot.OArmR.setPosition(OArmTransferPosition);
                    robot.Wristxpitch.setPosition(WristxpitchIntermedia4PositionAdjust); // Wristxpitch
//                    sleep(600);
                    delayTimer.reset();
                    while (delayTimer.milliseconds() < 600 && opModeIsActive()) {
                        // Other tasks can be processed here
                    } // 防止快速连击导致模式快速切换
//                    robot.IClaw.setPosition(IClawCloseTight); //  0.54
                    robot.IClaw.setPosition(OClawCloseSuperTight);
                    moveHSlideToPosition(POSITION_X_IN);
                    sleep(500);
                    robot.Wristxpitch.setPosition(0.05); // Wristxpitch
                    robot.IArmL.setPosition(0.65);
                    robot.IArmR.setPosition(0.65);
                    sleep(500);
                    robot.OClaw.setPosition(OClawCloseTight); // close 0.543 hold
                    sleep(500);
                    robot.IClaw.setPosition(IClawOpen); //open
                    sleep(300);
//                    moveHSlideToPosition(POSITION_B_EXTRUDETransferC);
//                    sleep(300);
                    robot.OArmL.setPosition(OArmRearSpecimenPick);
                    robot.OArmR.setPosition(OArmRearSpecimenPick);
//                    sleep(600);
//                    moveVSlideToPosition(-POSITION_Y_HIGH);// high
//                    moveVSlideToPositionPID(-POSITION_Y_HIGH);// high

                }

//one key ready for transfer

//Begin  OArm L and R

                if (gamepad1.y) { //rear specimen    OArmTransferPosition   OArmRearSpecimenPick
                    robot.OArmL.setPosition(OArmRearSpecimenPick);
                    robot.OArmR.setPosition(OArmRearSpecimenPick);
                }
                if (gamepad1.a) { //front transfer
                    robot.OArmL.setPosition(OArmTransferPosition);
                    robot.OArmR.setPosition(OArmTransferPosition);
                }

//end  OArm L and R

//Begin  open and close of outtakeclaw 12122024 finetuned

                if (gamepad1.left_trigger > 0.3 && gamepad1.left_trigger <= 0.7) { // 轻按
                    robot.OClaw.setPosition(OClawOpen); //12122024
                }
                if (gamepad1.right_trigger > 0.3 && gamepad1.right_trigger <= 0.7) { // 轻按
                    robot.OClaw.setPosition(OClawCloseTight);
                }
                if (gamepad1.right_trigger > 0.7) { // 深按
                    robot.OClaw.setPosition(OClawCloseSuperTight); //
                }

//End open and close of outtakeclaw

//End Definition and Initialization of gamepad

                break;

            // 如果需要更多模式，可以继续添加 case。
        }



    }
//End Definition and Initialization of intake()


    //Begin Definition and Initialization of outtake()
    public void outtake() {
    }
//End Definition and Initialization of outtake()

    //Begin Definition and Initialization of steptestservo()
    public void servoGamepadControl() {
        //Begin debugging with a step increment of 0.05  SGC - servoGamepadControl

/**
 * This code snippet controls the position of a servo motor using the gamepad triggers.
 *
 * **Purpose**:
 * - The left trigger (`gamepad1.left_trigger`) increases the servo's position by a fixed step (`SERVO_STEP`).
 * - The right trigger (`gamepad1.right_trigger`) decreases the servo's position by a fixed step (`SERVO_STEP`).
 * - The servo position is constrained between 0.01 (minimum) and 0.99 (maximum) to prevent invalid values.
 * - The current servo position is displayed on the telemetry for real-time monitoring.
 *
 * **Usage Instructions**:
 * 1. Press the **left trigger** (`gamepad1.left_trigger`) to move the servo incrementally towards its maximum position.
 * 2. Press the **right trigger** (`gamepad1.right_trigger`) to move the servo incrementally towards its minimum position.
 * 3. The servo's position is updated with a small delay (`sleep(200)` milliseconds) to prevent rapid changes from multiple trigger presses.
 * 4. Adjust `SERVO_STEP` as needed to control the increment size for finer or coarser adjustments.
 *
 * **Setup**:
 * - Ensure the servo is connected to the correct port and initialized in the `robot.TServo` variable.
 * - Configure the `SERVO_STEP` variable to determine how much the position changes with each trigger press.
 * - Calibrate the servo movement range (e.g., 0.01 to 0.99) based on your servo's physical limits to avoid damage.
 */


//            if (gamepad1.left_trigger > 0.3) {
//                servoPosition = servoPosition + SERVO_STEP;
//                if (servoPosition >= 1.0) {
//                    servoPosition = 0.99; // 限制最大值
//                }
//                robot.TServo.setPosition(servoPosition);
//                telemetry.addData("Servo Position", servoPosition);
//                telemetry.update();
//                sleep(200);
//            }
//            if (gamepad1.right_trigger > 0.3) {
//                servoPosition = servoPosition - SERVO_STEP;
//                if (servoPosition <= 0.0) {
//                    servoPosition = 0.01; // 限制最小值
//                }
//                robot.TServo.setPosition(servoPosition);
//                telemetry.addData("Servo Position", servoPosition);
//                telemetry.update();
//                sleep(200);
//            }

//End debugging with a step increment of 0.05

    }
//End Definition and Initialization of steptestservo()

//Temp ******************************
//**************************

////Begin  open and close of outtakeclaw 12122024 finetuned
//
//                    if (gamepad2.left_trigger > 0.3) { //open
//                        robot.OClaw.setPosition(0.32); //12122024
//                    }
//                    if (gamepad2.right_trigger > 0.3) { //close
//                        robot.OClaw.setPosition(0.548); // 0.543 hold
//                    }
//
////End open and close of outtakeclaw

//End Definition and Initialization of gamepad




    //Begin  Wristxpitch do not use it any more
//            if (gamepad1.dpad_up && !move) { //up if arm is Horizontal, the the wrist is vertical up and down
//                robot.Wristxpitch.setPosition(0.05); // Wristxpitch  12122024
//            }
//            if (gamepad1.dpad_down && !move) { //down
//                robot.Wristxpitch.setPosition(0.65); // Wristxpitch 12122024 0.65
//            }
//End   Wristxpitch
//            if (gamepad2.a && !move ) { //down
//                moveVSlideToPosition(POSITION_A_BOTTOM);
//            }
//            if (gamepad2.y && !move) { //up prepare forchameber
//                moveVSlideToPosition(-POSITION_Y_LOW);
//            }
//            if (gamepad2.right_bumper && !move){ //upforchameber
//                moveVSlideToPosition(-POSITION_Y_HIGH);
//            }

    //      HAND SPECIALIST   48444442243  JULIA MAYBERRY
    //for up
    //for down

    // ...（其他原始代码逻辑保持不变）
//
//
    //Begin  Wristxpitch do not use it any more
//            if (gamepad1.dpad_up && !move) { //up if arm is Horizontal, the the wrist is vertical up and down
//                robot.Wristxpitch.setPosition(0.05); // Wristxpitch  12122024
//            }
//            if (gamepad1.dpad_down && !move) { //down
//                robot.Wristxpitch.setPosition(0.65); // Wristxpitch 12122024 0.65
//            }
//End   Wristxpitch
//            if (gamepad2.a && !move ) { //down
//                moveVSlideToPosition(POSITION_A_BOTTOM);
//            }
//            if (gamepad2.y && !move) { //up prepare forchameber
//                moveVSlideToPosition(-POSITION_Y_LOW);
//            }
//            if (gamepad2.right_bumper && !move){ //upforchameber
//                moveVSlideToPosition(-POSITION_Y_HIGH);
//            }

    //      HAND SPECIALIST   48444442243  JULIA MAYBERRY
    //for up
    //for down
//****************************************************************************************
//


    //Temp *************************
    public void moveDriveTrain() {
        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = (gamepad1.right_stick_x*0.5);
        double fl = y - x - rx;
        double bl = y + x - rx;
        double fr = y + x + rx;
        double br = y - x + rx;
        robot.LFMotor.setPower(fl*DriveTrains_ReducePOWER);
        robot.LBMotor.setPower(bl*DriveTrains_ReducePOWER);
        robot.RFMotor.setPower(fr*DriveTrains_ReducePOWER);
        robot.RBMotor.setPower(br*DriveTrains_ReducePOWER);
    }

//Begin Definition and Initialization of Vertical Slides by gamepad2.left_stick_y

    public void liftVertSlidesHigh () {
        double liftVertSlides_y = -gamepad2.left_stick_y;
        robot.VSMotorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.VSMotorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.VSMotorL.setPower(liftVertSlides_y*0.45);
        robot.VSMotorR.setPower(liftVertSlides_y*0.45);
        robot.VSMotorL.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
        robot.VSMotorR.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));

        //up joystick makes the slides rotate clockwise on the out right side
        //when looking at the robots right side from the outside wall the slide pulley spins clockwise/to the right when the joystick is pushed up
    }

//End Definition and Initialization of Vertical Slides by gamepad2.left_stick_y

//Begin Definition and Initialization of Horizontal Slides by gamepad2.left_stick_x  extrude slides long

    public void extrHoriSlidesLong() {
        double liftVertSlides_y = gamepad2.left_stick_x;
        robot.HSMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.HSMotor.setPower(liftVertSlides_y*0.45);
        robot.HSMotor.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));

        //up joystick makes the slides rotate clockwise on the out right side
        //when looking at the robots right side from the outside wall the slide pulley spins clockwise/to the right when the joystick is pushed up
    }

//End Definition and Initialization of Horizontal Slides by gamepad2.left_stick_x

    //Begin Definition and Initialization of Vertical Slides
    private void moveVSlideToPosition ( int targetPosition){
        robot.VSMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.VSMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("targetPosition", targetPosition);
        telemetry.addData("liftMotorL.getCurrentPosition()",robot.VSMotorL.getCurrentPosition());
        telemetry.addData("liftMotorR.getCurrentPosition()",robot.VSMotorR.getCurrentPosition());
        telemetry.update();
        robot.VSMotorL.setTargetPosition(-targetPosition);
        robot.VSMotorR.setTargetPosition(-targetPosition);
        robot.VSMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.VSMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.VSMotorL.setPower(+SLIDE_POWER_V);
        robot.VSMotorR.setPower(+SLIDE_POWER_V);
        move = true;
        while (robot.VSMotorL.isBusy() && robot.VSMotorR.isBusy() && move) {
            // Wait until the motor reaches the target position
        }
//        while (robot.VSMotorR.isBusy() && move) {
        //           // Wait until the motor reaches the target position
        //       }
        telemetry.addData("targetPosition", targetPosition);
        telemetry.addData("after while liftMotorL.getCurrentPosition()",robot.VSMotorL.getCurrentPosition());
        telemetry.addData("after while liftMotorR.getCurrentPosition()",robot.VSMotorR.getCurrentPosition());
        telemetry.update();

//        robot.VSMotorL.setPower(0.2);
//        robot.VSMotorR.setPower(0.2);
//        robot.VSMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        robot.VSMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        robot.VSMotorL.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
//        robot.VSMotorR.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
// Fine-tune the position using a PID-like approach
//        holdSlidePosition(targetPosition);
        move = false;
    }
    //////////////////////////
    private void moveVSlideToPositionPID(int targetPosition) {
        // Set motors to use encoders
        robot.VSMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.VSMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Initialize PID Controllers
        pidController.reset();
        pidController.enable();
//        pidControllerL.reset();
//        pidControllerR.reset();
        pidController.setSetpoint(targetPosition);
//        pidControllerL.setSetpoint(targetPosition);
//        pidControllerR.setSetpoint(targetPosition);
        pidController.setTolerance(50);
//        pidControllerL.setTolerance(10); // Allowable position error
//        pidControllerR.setTolerance(10);

        move = true;

        // Main control loop
        while (move && opModeIsActive()) {
            int currentPositionL = robot.VSMotorL.getCurrentPosition();
//            int currentPositionL = robot.VSMotorL.getCurrentPosition();
//            int currentPositionR = robot.VSMotorR.getCurrentPosition();
            // Calculate PID outputs
            double powerL = pidController.performPID(currentPositionL);
            double powerR =  powerL;
//            double powerL = pidControllerL.performPID(currentPositionL);
//            double powerR = pidControllerR.performPID(currentPositionR);
            // Set motor power based on PID outputs
            robot.VSMotorL.setPower(powerL);
            robot.VSMotorR.setPower(powerR);
            // Telemetry for debugging
            telemetry.addData("Target Position", targetPosition);
            telemetry.addData("Current Position L", currentPositionL);
            telemetry.addData("Power L", powerL);
            telemetry.addData("Power R", powerR);
            telemetry.update();

            // Check if both motors are on target  (pidController.onTarget()   Math.abs(currentPositionL - targetPosition) < 50
            if (Math.abs(currentPositionL - targetPosition)<10) {
                move = false;
                telemetry.addData("move", move);
                telemetry.update();
            }
//            telemetry.addData("Target Position", targetPosition);
//            telemetry.addData("Current Position L", currentPositionL);
//            telemetry.addData("Power L", powerL);
//            telemetry.addData("Power R", powerR);
//            telemetry.update();
        }

        // Stop motors and set braking behavior
        robot.VSMotorL.setPower(0);
        robot.VSMotorR.setPower(0);
        robot.VSMotorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.VSMotorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    //////////////////////////

    /// 初始化 PID 控制器
    private void startVSlidePIDControl(int targetPosition) {
        robot.VSMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.VSMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pidController.reset();
        pidController.enable();
        pidController.setSetpoint(targetPosition);
        pidController.setTolerance(10); // 允许误差范围
        pidTargetPosition = targetPosition;
        pidActive = true; // 激活 PID 控制
    }
    // 在主循环中调用的非阻塞 PID 控制逻辑
    private void updateVSlidePIDControl() {
        if (!pidActive) return; // 如果 PID 未激活，直接返回

        int currentPositionL = robot.VSMotorL.getCurrentPosition();

        // 计算 PID 输出
        double powerL = pidController.performPID(currentPositionL);
        robot.VSMotorL.setPower(powerL*0.4);
        robot.VSMotorR.setPower(powerL*0.4);

        // 输出 Telemetry 信息
        telemetry.addData("PID Target", pidTargetPosition);
        telemetry.addData("Current Position L", currentPositionL);
        telemetry.addData("Power L", powerL);
        telemetry.update();

//        // 如果达到目标位置，停止滑轨运动，但保持抗重力功率
//        if (pidController.onTarget()) {
//            robot.VSMotorL.setPower(0.1); // 保持位置的最小功率
//            robot.VSMotorR.setPower(0.1);
//            pidActive = false; // 停止 PID 控制
//        }
        // 在 updateVSlidePIDControl 中加入抗重力逻辑
        if (!pidActive && Math.abs(robot.VSMotorL.getCurrentPosition() - pidTargetPosition) > 10) {
            double holdPower = pidController.performPID(robot.VSMotorL.getCurrentPosition());
            robot.VSMotorL.setPower(holdPower);
            robot.VSMotorR.setPower(holdPower);
            pidActive = false; // 停止 PID 控制
        }

    }


///////////////////////////

    /*****
     // 状态变量
     private boolean pidActive = false; // PID 控制是否激活
     private int pidTargetPosition = 0; // PID 控制目标位置

     // 初始化 PID 控制器
     private void startVSlidePIDControl(int targetPosition) {
     pidController.reset();
     pidController.enable();
     pidController.setSetpoint(targetPosition);
     pidController.setTolerance(10); // 允许误差范围
     pidTargetPosition = targetPosition;
     pidActive = true; // 激活 PID 控制
     }

     // 在主循环中调用的非阻塞 PID 控制逻辑
     private void updateVSlidePIDControl() {
     if (!pidActive) return; // 如果 PID 未激活，直接返回

     int currentPositionL = robot.VSMotorL.getCurrentPosition();

     // 计算 PID 输出
     double powerL = pidController.performPID(currentPositionL);
     robot.VSMotorL.setPower(powerL);
     robot.VSMotorR.setPower(powerL);

     // 输出 Telemetry 信息
     telemetry.addData("PID Target", pidTargetPosition);
     telemetry.addData("Current Position L", currentPositionL);
     telemetry.addData("Power L", powerL);
     telemetry.update();

     // 如果达到目标位置，停止滑轨运动，但保持抗重力功率
     if (pidController.onTarget()) {
     robot.VSMotorL.setPower(0.1); // 保持位置的最小功率
     robot.VSMotorR.setPower(0.1);
     pidActive = false; // 停止 PID 控制
     }
     }


     ///////***************************************

     @Override
     public void runOpMode() {
     robot.init(hardwareMap);

     waitForStart();

     while (opModeIsActive()) {
     // 定期更新滑轨的 PID 控制状态
     updateVSlidePIDControl();

     // 其他操作逻辑，例如按键控制机器人
     if (gamepad1.a) {
     startVSlidePIDControl(POSITION_Y_LOW); // 按下 A 键，移动滑轨到低位
     }

     if (gamepad1.b) {
     startVSlidePIDControl(POSITION_Y_HIGH); // 按下 B 键，移动滑轨到高位
     }

     // 继续处理其他操作
     moveDriveTrain_FieldCentric();
     intake();
     outtake();
     }
     }
     //***************************
     // 在 updateVSlidePIDControl 中加入抗重力逻辑
     if (!pidActive && Math.abs(robot.VSMotorL.getCurrentPosition() - pidTargetPosition) > 10) {
     double holdPower = pidController.performPID(robot.VSMotorL.getCurrentPosition());
     robot.VSMotorL.setPower(holdPower);
     robot.VSMotorR.setPower(holdPower);
     }




     */


///////////////////////////
    private void holdSlidePosition(int targetPosition) {
        final double HOLD_POWER = 0.1; // Minimal power to hold the position
        final int POSITION_TOLERANCE = 10; // Allowable deviation from the target

        while (true) {
            int currentPositionL = robot.VSMotorL.getCurrentPosition();
            int currentPositionR = robot.VSMotorR.getCurrentPosition();

            // Check if the slide is within the tolerance
            if (Math.abs(currentPositionL + targetPosition) <= POSITION_TOLERANCE &&
                    Math.abs(currentPositionR + targetPosition) <= POSITION_TOLERANCE) {
                robot.VSMotorL.setPower(0);
                robot.VSMotorR.setPower(0);
            } else {
                // Apply minimal power to correct the position
                robot.VSMotorL.setPower(HOLD_POWER);
                robot.VSMotorR.setPower(HOLD_POWER);
            }

            // Optionally break the loop based on a condition or timer
            // Example: break if a stop flag is set
            if (!move) {
                break;
            }

            // Add telemetry to monitor holding behavior
            telemetry.addData("Holding Position L", currentPositionL);
            telemetry.addData("Holding Position R", currentPositionR);
            telemetry.update();
        }
    }
//End Definition and Initialization of Vertical Slides

    //Begin Definition and Initialization of Horizontal Slides
    private void moveHSlideToPosition ( int targetPosition){
        robot.HSMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("targetPosition", targetPosition);
        telemetry.addData("robot.HSMotor.getCurrentPosition()",robot.HSMotor.getCurrentPosition());
        telemetry.update();
        robot.HSMotor.setTargetPosition(-targetPosition);
        robot.HSMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.HSMotor.setPower(+SLIDE_POWER_H);
        move = true;
        while (robot.HSMotor.isBusy()  && move) {
            // Wait until the motor reaches the target position
        }
//        while (robot.VSMotorR.isBusy() && move) {
        //           // Wait until the motor reaches the target position
        //       }
        telemetry.addData("targetPosition", targetPosition);
        telemetry.addData("after while HSMotor.getCurrentPosition()",robot.HSMotor.getCurrentPosition());
        telemetry.update();
//        robot.HSMotor.setPower(.15);
//        robot.HSMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        robot.HSMotor.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));

        move = false;
    }

//End Definition and Initialization of Horizontal Slides



    public void moveDriveTrain_FieldCentric() {
        double y = gamepad1.left_stick_y * (1); // Remember, Y stick value is reversed
        double x = -gamepad1.left_stick_x * (1);
        double rx = -gamepad1.right_stick_x * (1); //*(0.5) is fine

        // This button choice was made so that it is hard to hit on accident,
        // it can be freely changed based on preference.
        // The equivalent button is start on Xbox-style controllers.
        //******************************************temp
//        if (gamepad1.back) {
//            robot.imu.resetYaw();
//        }
//******************************************temp

        double botHeading = robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        robot.LFMotor.setPower(frontLeftPower * DriveTrains_ReducePOWER);
        robot.LBMotor.setPower(backLeftPower * DriveTrains_ReducePOWER);
        robot.RFMotor.setPower(frontRightPower * DriveTrains_ReducePOWER);
        robot.RBMotor.setPower(backRightPower * DriveTrains_ReducePOWER);
    }

    public void moveDriveTrain_RobotCentric() {
        double robot_y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double robot_x = gamepad1.left_stick_x;
        double robot_rx = gamepad1.right_stick_x*0.5; // If a smooth turn is required 0.5

        double fl = robot_y - robot_x - robot_rx;
        double bl = robot_y + robot_x - robot_rx;
        double fr = robot_y + robot_x + robot_rx;
        double br = robot_y - robot_x + robot_rx;

        robot.LFMotor.setPower(fl * speedMultiplier);
        robot.LBMotor.setPower(bl * speedMultiplier);
        robot.RFMotor.setPower(fr * speedMultiplier);
        robot.RBMotor.setPower(br * speedMultiplier);

    }
    public void RobotCentricDriveTrain () {
        double robot_y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double robot_x = gamepad1.left_stick_x;
        double robot_rx = gamepad1.right_stick_x*0.5; // If a smooth turn is required 0.5

        double fl = robot_y - robot_x - robot_rx;
        double bl = robot_y + robot_x - robot_rx;
        double fr = robot_y + robot_x + robot_rx;
        double br = robot_y - robot_x + robot_rx;

        robot.LFMotor.setPower(fl * speedMultiplier);
        robot.LBMotor.setPower(bl * speedMultiplier);
        robot.RFMotor.setPower(fr * speedMultiplier);
        robot.RBMotor.setPower(br * speedMultiplier);

    }

//Begin Definition and Initialization of Horizontal Slides


//    private void moveHSlideToPosition ( int targetPosition){
//
//        robot.HSMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        telemetry.addData("targetPosition", targetPosition);
//        telemetry.addData("liftMotorR.getCurrentPosition()",robot.HSMotor.getCurrentPosition());
//        telemetry.update();
//        robot.HSMotor.setTargetPosition(-targetPosition);
//        robot.HSMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        robot.HSMotor.setPower(+SLIDE_POWER_H);
//
//        move = true;
//
//        while (robot.HSMotor.isBusy() &&  move) {
//            // Wait until the motor reaches the target position
//        }
//
//        robot.HSMotor.setPower(0);
//        robot.HSMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        robot.HSMotor.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
//        move = false;
//    }


//End Definition and Initialization of Horizontal Slides




}








