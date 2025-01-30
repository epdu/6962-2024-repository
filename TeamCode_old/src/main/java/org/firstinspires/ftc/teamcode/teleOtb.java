//Alan taught me a nice way to debug code, each time he will make a new file with mame-AutonomousCopyALan from Autonomous
//Blue left setup  use hardware init Red Alliance works, add blue still need to adjust hsv range, contours either too big or too small
//set the distance from front of robot to the block of game element
/*  Using the specs from the motor, you would need to find the encoder counts per revolution (of the output shaft).
     Then, you know that corresponds to 360 degrees of wheel rotation, which means the distance travelled is the circumference
      of the wheel (2 * pi * r_wheel). To figure out how many encoder ticks correspond to the distance you wanna go,
      just multiply the distance by the counts / distance you calculated above. Hope that helps!
// 11.87374348
//537 per revolution 11.87374348 inch
*/
package org.firstinspires.ftc.teamcode;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;


//HSV
//OpenCvVisionProcessor
//HardwarePowerpuffs
//works well except controlHubCam.stopStreaming(); check this 0225morning
@Autonomous(name = "TeleOtb")
public class teleOtb extends LinearOpMode {
    HardwareTeletubbies robot = new HardwareTeletubbies();   // Use a Powerpuffs's hardware
    //    public String allianceColor="red";// "null" for init set to be "red" or "blue" for each match
    public String allianceColor="blue";
    public String parkingSide="right";// "null" for init  set to be "left" or "right" for each match
    //    public String parkingSide="left";
    public double sleepingTime=0.0;// set to be any number if need to avoid collision with alliance
    public boolean autoParkingDone=false;
    //    private static final double[] redBlobColorThresholds = {20.0, 120.0, 180.0, 240.0, 90.0, 120.0};
//    private static final double[] blueBlobColorThresholds = {20.0, 250.0, 40.0, 250.0, 160.0, 240.0};

    //    public boolean targetFound = false;
    boolean targetFound     = false;    // Set to true when an AprilTag target is detected
    double  drive           = 0;        // Desired forward power/speed (-1 to +1)
    double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
    double  turn            = 0;        // Desired turning power/speed (-1 to +1)
    //    public boolean targetFound = false;
//    public double drive = 0;
//    public double turn = 0;
//    public double strafe = 0;
    DistanceSensor LeftSensor;
    DistanceSensor RightSensor;
    IMU imu;
    double ticksPerRotation;
    double initialFR;
    double initialFL;
    double initialBR;
    double initialBL;
    double positionFR;
    double positionFL;
    double positionBR;
    double positionBL;
    public String updates;
    public int i = 0;
    double targetheading;
    double heading;
    double previousHeading;
    double processedHeading;
    double  distanceInInch;
    double  distanceInInchDouble;
    double  distanceRFMotor;
    double  distanceRBMotor;
    double  distanceLFMotor;
    double  distanceLBMotor;
    private double wheelDiameterInInches = 3.77953;  // Adjust this based on your mecanum wheel diameter
    public String teamPropLocations;  //= "Left"
    public String PurplePixel;
    //    boolean
    public boolean found;
    public boolean  dropPurplePixelDone=false;
    public boolean  dropYellowPixelDone=false;
    double redVal;
    double blueVal;
    double greenVal;
    double liftInitial;
    double liftIdealPos;
    double liftIdealPower;
    int result;
    double cX = 0;
    double cY = 0;
    double width = 0;

    private OpenCvCamera controlHubCam;  // Use OpenCvCamera class from FTC SDK
    private static final int CAMERA_WIDTH = 1280; // width  of wanted camera resolution
    private static final int CAMERA_HEIGHT = 720; // height of wanted camera resolution
    /*
       1280 x 720 pixels Logitech Webcam C270 (1280 x 720 pixels)
       private static final int CAMERA_WIDTH = 640; // width  of wanted camera resolution
       private static final int CAMERA_HEIGHT = 360; // height of wanted camera resolution
    */
    // Calculate the distance using the formula
    public static final double objectWidthInRealWorldUnits = 3.9;  // Replace with the actual width of the object in real-world units
    //    public static final double objectWidthInRealWorldUnits = 3.75;  // original value Replace with the actual width of the object in real-world units
    public static final double focalLength = 922;  //Logitech C920 3.67mm pixel Size (µm): 3.98 pixel  C270  Replace with the focal length of the camera in pixels
//    public static final double focalLength = 728;  // Replace with the focal length of the camera in pixels

    //    private static final boolean USE_WEBCAM = true;
//    private OpenCvVisionProcessor redTeamPropOpenCv;
    //   private OpenCvVisionProcessor blueTeamPropOpenCv;
    //   final double DESIRED_DISTANCE = 6.0; //  orignal =6 this is how close the camera should get to the target (inches)
    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)
    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the approach speed to this max value (adjust for your robot)move
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)
    private static int DESIRED_TAG_ID = -1;
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag = null;
    private static final int POSITION_A_BOTTOM = 0; //Vertical  slides all the way in
    private static final int POSITION_Y_LOW = 800; // Vertical slides up //800 //1000 too high
    private static final int POSITION_Y_HIGH = 1300;//Vertical slides all the way up
    private static final int POSITION_Y_HIGHH = 1250;//Vertical slides all the way up
    private static final double SLIDE_POWER_H = 0.4; // Adjust as needed
    private static final double SLIDE_POWER_V = 0.70; // Adjust as needed
    private static final double SERVO_STEP = 0.01; // 每次调整的伺服步长
    double servoPosition = 0.5;
    private static final double SLIDE_POWER = 0.8; // Adjust as needed
    public float speedMultiplier = 0.5f;
    public float speedLimiter = 0.05f;
    boolean move = false;
    Gyro gyro = new Gyro(); // 创建 Gyro 类的对象
    SparkFunOTOS myOtos;
//    SparkFunOTOSDrive drive;
    @Override
    public void runOpMode() throws InterruptedException {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        gyro.robot.init(hardwareMap);
//        myOtos.init(hardwareMap);

//        drive = new SparkFunOTOSDrive(hardwareMap, new Pose2d(0, 0, 0)); // 初始化SparkFunOTOSDrive
        myOtos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");
        telemetry.addData("Status", "OTOS sensor initialized!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // TODO: Need to do red or blue according to alliance color.
            moveForward(0.2, 25); //32
            sleep(100);
            moveVSlideToPosition(-POSITION_Y_LOW);
            sleep(600);
            moveForward(0.1, 4); //32
            sleep(100);
            moveVSlideToPosition(-POSITION_Y_HIGH);
            sleep(600);
            robot.OClaw.setPosition(0.32); //12122024
            sleep(100);
            moveBackward(0.3,9);
            sleep(100);
            moveVSlideToPosition(-POSITION_A_BOTTOM);
            strafeLeft(0.2, 50);//16
            moveBackward(0.3,10);
            moveBackward(0.3,5);
            sleep(600);

            //

//                moveForward(0.1, 4);
//            sleep(100);
//            moveBackward(0.3,2);
//                sleep(100);
//                moveVSlideToPosition(-POSITION_Y_LOW);
//                sleep(100);
//            moveForward(0.1, 4);
//                moveVSlideToPosition(-POSITION_Y_HIGH);
//                sleep(100);
//                robot.OClaw.setPosition(0.32); //12122024
//                sleep(100);
//                moveVSlideToPosition(POSITION_A_BOTTOM);
//                moveBackward(0.3,31);
//                strafeRight(0.3, 32);
//            sleep(100);


            //            while (found==false) {
//                if (allianceColor.equals("red")) {
//                    Point teamPropCentroid = redTeamPropOpenCv.getTeamPropCentroid();
//                    cX = teamPropCentroid.x;
//                    cY = teamPropCentroid.y;
//                    found = cX != 0.0 || cY != 0.0;
//                    telemetry.addData("line 149 first check point found or not ",found);
//                    telemetry.addData("allianceColor", allianceColor);
//                    telemetry.addData("Find team prop or not", found);
//                    telemetry.addData("Coordinate", "(" + (int) cX + ", " + (int) cY + ")");
//                    telemetry.addData("Distance in Inch", (getDistance(width)));
//                    telemetry.update();
//
//                } else if (allianceColor.equals("blue")) {
//                    Point teamPropCentroid = blueTeamPropOpenCv.getTeamPropCentroid();
//                    cX = teamPropCentroid.x;
//                    cY = teamPropCentroid.y;
//                    found = cX != 0.0 || cY != 0.0;
//                    telemetry.addData("allianceColor", allianceColor);
//                    telemetry.addData("Find team prop or not", found);
//                    telemetry.addData("Coordinate", "(" + (int) cX + ", " + (int) cY + ")");
//                    telemetry.addData("Distance in Inch", (getDistance(width)));
//                    telemetry.update();
//
//                } else {
//                    telemetry.addData("something wrong,allianceColor", allianceColor);
//                    telemetry.update();
//                }
//            }

//            findteamPropLocations();
//            dropPurplePixel();
//            aprilTagOmni();
//            dropYellowPixel();
//            isStopRequested();
//            autoParking();
//            if(autoParkingDone==true){
            break;
//            }
        }
//        dashboard.stopCameraStream();
//        controlHubCam.stopStreaming();

    }

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

        robot.VSMotorL.setPower(0);
        robot.VSMotorR.setPower(0);
        robot.VSMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.VSMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.VSMotorL.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
        robot.VSMotorR.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
        move = false;
    }
    public void moveRobot(double x, double y, double yaw) {
        telemetry.addData("x", x);
        telemetry.addData("y", y);
        telemetry.addData("yaw", yaw);
        telemetry.update();
        double leftFrontPower    =  x -y +yaw;
        double rightFrontPower   =  x +y -yaw;
        double leftBackPower     =  x +y +yaw;
        double rightBackPower    =  x -y -yaw;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        robot.LFMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.RFMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.LBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.RBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        robot.LFMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.RFMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.LBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.RBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addData("leftFrontPower", leftFrontPower);
        telemetry.addData("rightFrontPower", rightFrontPower);
        telemetry.addData("leftBackPower", leftBackPower);
        telemetry.addData("rightBackPower", rightBackPower);
        telemetry.update();
        robot.LFMotor.setPower(leftFrontPower);
        robot.RFMotor.setPower(rightFrontPower);
        robot.LBMotor.setPower(leftBackPower);
        robot.RBMotor.setPower(rightBackPower);

    }

    public void stopMotors(double p){
        robot.setAllPower(p);
    }
    public void moveForward(double power, double distanceInInch) {
        movement(power, -distanceInInch,-distanceInInch,-distanceInInch,-distanceInInch) ;
    }
    public void moveBackward(double power, double distanceInInch) {
        movement(power, +distanceInInch,+distanceInInch,+distanceInInch,+distanceInInch) ;
    }
    public void turnRight(double power, double distanceInInch) {
        movement(power, +distanceInInch,+distanceInInch,-distanceInInch,-distanceInInch);
    }
    public void turnLeft(double power, double distanceInInch) {
        movement(power, -distanceInInch,-distanceInInch,+distanceInInch,+distanceInInch);
    }
    public void strafeRight(double power, double distanceInInch) {
        movement(power, +distanceInInch,-distanceInInch,-distanceInInch,+distanceInInch);
    }
    public void strafeLeft(double power, double distanceInInch) {
        movement(power, -distanceInInch,+distanceInInch,+distanceInInch,-distanceInInch);
    }
    public void movement(double power, double distanceRF,double distanceRB,double distanceLF,double distanceLB) {
//input distance in inches, robot will finish movement "moveForward moveBackward ,turnRight turnLeft  strafeRight and strafeLeft"
        distanceRFMotor=(double)(distanceRF*537/(Math.PI * wheelDiameterInInches));
        distanceRBMotor=(double)(distanceRB*537/(Math.PI * wheelDiameterInInches));
        distanceLFMotor=(double)(distanceLF*537/(Math.PI * wheelDiameterInInches));
        distanceLBMotor=(double)(distanceLB*537/(Math.PI * wheelDiameterInInches));
        robot.RFMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.RBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.LFMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.LBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.RFMotor.setTargetPosition((int) distanceRFMotor);
        robot.RBMotor.setTargetPosition((int) distanceRBMotor);
        robot.LFMotor.setTargetPosition((int) distanceLFMotor);
        robot.LBMotor.setTargetPosition((int) distanceLBMotor);
        robot.RFMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.RBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.LFMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.LBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.RFMotor.setPower(+power);
        robot.RBMotor.setPower(+power);
        robot.LFMotor.setPower(+power);
        robot.LBMotor.setPower(+power);
        telemetry.addData("distanceRFMotor", distanceRFMotor);
        telemetry.addData("distanceRBMotor", distanceRBMotor);
        telemetry.addData("distanceLFMotor", distanceLFMotor);
        telemetry.addData("distanceLBMotor", distanceLBMotor);
        telemetry.update();
        while (robot.RFMotor.isBusy() || robot.RBMotor.isBusy() || robot.LFMotor.isBusy() || robot.LBMotor.isBusy() ||false) {}
        robot.RFMotor.setPower(0);
        robot.RBMotor.setPower(0);
        robot.LFMotor.setPower(0);
        robot.LBMotor.setPower(0);
    }






//    public void lookfortag(int tag){

///////////////////////
//        while (true) {//////////while (opModeIsActive()
//            //while ((targetFound=true)&&(abs(rangeError)>0.05||abs(headingError)>0.05||abs(yawError)>0.05))
//            DESIRED_TAG_ID = tag;
//            desiredTag = null;
//
//            // Step through the list of detected tags and look for a matching tag
//            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
//            for (AprilTagDetection detection : currentDetections) {
//                // Look to see if we have size info on this tag.
//                if (detection.metadata != null) {
//                    //  Check to see if we want to track towards this tag.
//                    if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
//                        // Yes, we want to use this tag.
//                        targetFound = true;
//                        desiredTag = detection;
//                        telemetry.addData("test", targetFound);
//                        telemetry.update();
//
//                        break;  // don't look any further.
//                    } else {
//                        // This tag is in the library, but we do not want to track it right now.
//                        telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
//                        telemetry.update();
//
//                    }
//                } else {
//                    // This tag is NOT in the library, so we don't have enough information to track to it.
//                    telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
//                    telemetry.update();
//
//                }
//            }
//
//            if (targetFound==true) {
//                double rangeError = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
//                double headingError = desiredTag.ftcPose.bearing;
//                double yawError = desiredTag.ftcPose.yaw;
//                // Use the speed and turn "gains" to calculate how we want the robot to move.
//                drive = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
//                turn = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
//                strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);
//
//                telemetry.addData("\n>", "HOLD Left-Bumper to Drive to Target\n");
//                telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
//                telemetry.addData("DESIRED_DISTANCE", DESIRED_DISTANCE);
//                telemetry.addData("Range", "%5.1f inches", desiredTag.ftcPose.range);
//                telemetry.addData("Bearing", "%3.0f degrees", desiredTag.ftcPose.bearing);
//                telemetry.addData("Yaw", "%3.0f degrees", desiredTag.ftcPose.yaw);
//                telemetry.addData("drive ", drive);
//                telemetry.addData("turn ", turn);
//                telemetry.addData("strafe", strafe);
//                telemetry.update();
//
//            }
//            moveRobot(drive, strafe, turn);
//            sleep(10);
//            if( targetFound==true && abs(drive)<0.1 && abs(strafe)<0.05 && abs(turn)<0.05 ){
//                telemetry.addData("if for break drive", drive);
//                telemetry.update();
////                sleep(5000);
////                isStopRequested();
//                break;}  // don't look any further
//        }
//////////////////////
//    }
//    private static double getDistance(double width){
//        double distance = (objectWidthInRealWorldUnits * focalLength) / width;
//        return distance;
//    }
//    public String  findteamPropLocations(){
//        telemetry.addData("cX", cX);
//        telemetry.addData("cY", cY);
//        telemetry.addData("teamPropLocations", teamPropLocations);
//        telemetry.update();
//
//        if(cX > 0 && cX < 184 && cY <400 && cY > 100 ){// if(cX > 0 && cX < 365 )0 183   230-410 407-640365-320 640
//            teamPropLocations="Left";
//            found=true;
//            telemetry.addData("Left", cX);
//            telemetry.addData("teamPropLocations", teamPropLocations);
//            telemetry.update();
//
//        } else if ( cX > 184 && cX < 457  && cY <400 && cY > 100) {//    cX > 230 && cX < 410 work, cX > 460 && cX < 820
//            teamPropLocations = "Center";
//            found=true;
//            telemetry.addData("Center", cX);
//            telemetry.addData("teamPropLocations", teamPropLocations);
//            telemetry.update();
//
//        } else if( cX > 457 && cX < 640 && cY <400 && cY > 100) {// cX > 915 && cX < 1280
//            teamPropLocations = "Right";
//            found=true;
//            telemetry.addData("Right",cX);
//            telemetry.addData("teamPropLocations", teamPropLocations);
//            telemetry.update();
//
//        }
//        telemetry.addData("teamPropLocations", teamPropLocations);
//        telemetry.update();
//        return teamPropLocations;
//    }
//    public boolean  dropPurplePixel() {
//        if(dropPurplePixelDone == false){
//            if ( teamPropLocations.equals("Left")) {
//                telemetry.addData("teamPropLocations", teamPropLocations);
//                telemetry.update();
//                strafeRight(0.3,3);
//
////                moveBackward(0.3, 40);
//////                robot.Wrist.setPosition(0.08);//down
////                strafeRight(0.3, 12);
//////                robot.ClawL.setPosition(0.2);//open
////                sleep(20);
//////                robot.Wrist.setPosition(0.5);//up
//////                robot.ClawL.setPosition(0.505);//close
//////                robot.Wrist.setPosition(0.8);//up
////                turnLeft(0.3, 14.5);
////                moveBackward(0.3, 8);
////                strafeRight(0.3, 24);
//
//                dropPurplePixelDone = true;
//
//            } else if (teamPropLocations.equals("Right")) {
//                telemetry.addData("teamPropLocations", teamPropLocations);
//                telemetry.update();
//                strafeLeft(0.3,3);
//
////                moveBackward(0.3, 28);
//////                robot.Wrist.setPosition(0.08);//down
////                turnLeft(0.3, 14.5);
//////                robot.ClawL.setPosition(0.2);//open
////                sleep(20);
//////                robot.Wrist.setPosition(0.5);//up
//////                robot.ClawL.setPosition(0.505);//close
//////                robot.Wrist.setPosition(0.8);//up
////                moveBackward(0.3, 8);
////                strafeLeft(0.3, 4);
//
//                dropPurplePixelDone = true;
//            } else if (teamPropLocations.equals("Center")) {
//                telemetry.addData("teamPropLocations", teamPropLocations);
//                telemetry.update();
//                //drives to the backdrop
//                moveBackward(0.3,31);
//                sleep(100);
//                moveForward(0.3, 3);
//                sleep(100);
//                turnRight(0.4, 16.5);
//                sleep(100);
//                moveForward(0.3,24);
//                sleep(100);
//                turnRight(0.3, 16.5);
//                sleep(100);
//                moveForward(0.3, 48);
//                sleep(100);
//                turnRight(0.3, 16.5);
//
//
////                moveBackward(0.3, 46);
//////                robot.Wrist.setPosition(0.08);//down
//////                robot.ClawL.setPosition(0.2);//open
//////                sleep(20);
//////                robot.Wrist.setPosition(0.5);//up
//////                robot.ClawL.setPosition(0.505);//close
//////                robot.Wrist.setPosition(0.8);//up
////
////                turnLeft(0.3, 14.5);
////                moveBackward(0.3, 20);
////                strafeRight(0.3, 22);
////                moveForward(0.3, 10);
//
//                dropPurplePixelDone = true;
//            }
//        }
//        telemetry.addData("teamPropLocations", teamPropLocations);
//        telemetry.update();
//        return dropPurplePixelDone;
//    }

//    public void  dropYellowPixel(){
//        if(dropYellowPixelDone == false){
////            robot.Wrist.setPosition(0.08);//down
////            robot.ArmL.setPosition(0.5);
////            robot.ArmR.setPosition(0.5);
////            robot.Wrist.setPosition(0.95);//up
////            robot.ArmL.setPosition(0.95);
////            robot.ArmR.setPosition(0.95);
////            robot.ClawR.setPosition(0.2);//open
////            sleep(20);
////            moveForward(0.3, 4);
////            robot.ClawR.setPosition(0.505);//close
////            robot.ArmL.setPosition(0.5);
////            robot.ArmR.setPosition(0.5);
////            robot.Wrist.setPosition(0.8);//up
////            robot.ArmL.setPosition(0);
////            robot.ArmR.setPosition(0);
//            strafeLeft(0.3, 4);
//            dropYellowPixelDone=true;
//        }else if (dropYellowPixelDone==true){
//            //it`s done
//        }// move arms and then open claw
//    }
//    public boolean autoParking(){
//        if(allianceColor.equals("red")) {
//            if (parkingSide.equals("left")) {
//                if (teamPropLocations.equals("Left")) {
//                    telemetry.addData("parkingSide", allianceColor,parkingSide,teamPropLocations);
//                    telemetry.update();moveForward(0.3, 5);
//                    strafeRight(0.3, 12);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                } else if (teamPropLocations.equals("Right")) {
//                    moveForward(0.3, 5);
//                    strafeRight(0.3, 40);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                } else if (teamPropLocations.equals("Center")) {
//                    moveForward(0.3, 5);
//                    strafeRight(0.3, 30);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                }
//            } else if (parkingSide.equals("right")) {
//                if (teamPropLocations.equals("Left")) {
//                    moveForward(0.3, 5);
//                    strafeLeft(0.3, 40);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                } else if (teamPropLocations.equals("Right")) {
//                    moveForward(0.3, 5);
//                    strafeLeft(0.3, 12);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                } else if (teamPropLocations.equals("Center")) {
//                    moveForward(0.3, 5);
//                    strafeLeft(0.3, 30);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                }
//            } else {
//                telemetry.addData("something wrong,parkingSide", parkingSide);
//                telemetry.update();
//            }
//
//        }else if (allianceColor.equals("blue")){
//            if (parkingSide.equals("left")) {
//                if (teamPropLocations.equals("Left")) {
//                    moveForward(0.3, 5);
//                    strafeRight(0.3, 12);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                } else if (teamPropLocations.equals("Right")) {
//                    moveForward(0.3, 5);
//                    strafeRight(0.3, 40);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                } else if (teamPropLocations.equals("Center")) {
//                    moveForward(0.3, 5);
//                    strafeRight(0.3, 30);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                }
//            } else if (parkingSide.equals("right")) {
//                if (teamPropLocations.equals("Left")) {
//                    moveForward(0.3, 5);
//                    strafeLeft(0.3, 40);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                } else if (teamPropLocations.equals("Right")) {
//                    moveForward(0.3, 5);
//                    strafeLeft(0.3, 12);
//                    moveBackward(0.3, 10);
//                    autoParkingDone = true;
//
//                } else if (teamPropLocations.equals("Center")) {
//                    moveForward(0.3, 5);
//                    strafeLeft(0.3, 30);
//                    moveBackward(0.3, 20);
//                    autoParkingDone = true;
//
//                }
//            }else {
//                telemetry.addData("something wrong,parkingSide", parkingSide);
//                telemetry.update();
//            }
//        }else {
//            telemetry.addData("something wrong,allianceColor", allianceColor);
//            telemetry.update();
//        }
//        return autoParkingDone;
//    }
    //work here
//    public void  aprilTagOmni(){
//        if (teamPropLocations.equals("Left"))
//        {
//            DESIRED_TAG_ID = 1;
//            lookfortag(DESIRED_TAG_ID);
//            telemetry.addData("aprilTagOmni, DESIRED_TAG_ID", DESIRED_TAG_ID);
//            telemetry.update();
//        } else if (teamPropLocations.equals("Center")) {
//            DESIRED_TAG_ID = 2;
//            lookfortag(DESIRED_TAG_ID);
//            telemetry.addData("aprilTagOmni, DESIRED_TAG_ID", DESIRED_TAG_ID);
//            telemetry.update();
//        } else if (teamPropLocations.equals("Right")) {
//            DESIRED_TAG_ID = 3;
//            lookfortag(DESIRED_TAG_ID);
//            telemetry.addData("aprilTagOmni, DESIRED_TAG_ID", DESIRED_TAG_ID);
//            telemetry.update();
//        }
//    }




//    private void initVisionPortal() {
//        aprilTag = new AprilTagProcessor.Builder().build();
//        //Below isYCrCb That means for RED, lowColorThresholds should be new Scalar(20, 180, 90) and highColorThresholds should be new Scalar(120, 240, 120)
//        //For BLUE, low should be new Scalar(20, 40, 160) and high should be new Scalar(250, 250, 240)
//        //below is HSV
//        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(1, 98, 34), new Scalar(30, 255, 255) );
//        blueTeamPropOpenCv= new OpenCvVisionProcessor("Blue", new Scalar(93,70,25), new Scalar(130, 255, 255) );
///*
//        //346/2=173 -+10 -> 163,180 54 56
//        //207/2=103-+10=93 113
////        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(140,25,35), new Scalar(179, 255, 255) );
////        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(140,25,35), new Scalar(179, 255, 255) );
//        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(1, 98, 34), new Scalar(30, 255, 255) );//good
//        blueTeamPropOpenCv= new OpenCvVisionProcessor("Blue", new Scalar(93,70,25), new Scalar(130, 255, 255) );//good
//That means for RED, lowColorThresholds should be new Scalar(20, 180, 90) and highColorThresholds should be new Scalar(120, 240, 120)
//For BLUE, low should be new Scalar(20, 40, 160) and high should be new Scalar(250, 250, 240)
//
//
//private static final double[] redBlobColorThresholds = {20.0, 120.0, 180.0, 240.0, 90.0, 120.0};
//    private static final double[] blueBlobColorThresholds = {20.0, 250.0, 40.0, 250.0, 160.0, 240.0};
//        //346/2=173 -+10 -> 163,180 54 56
//        //207/2=103-+10=93 113
//        red cube 346 54 56 blue tape 204 85 59 blue cube 207 90 39
//        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(160,40,50), new Scalar(180, 255, 255) );
//        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(0,40,50), new Scalar(30, 255, 255) );
//        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(1, 98, 34), new Scalar(30, 255, 255) );
//        blueTeamPropOpenCv= new OpenCvVisionProcessor("Blue", new Scalar(180, 8, 24), new Scalar(230, 255, 255));
//        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(0, 10, 120), new Scalar(100, 255, 255) );
//        redTeamPropOpenCv= new OpenCvVisionProcessor("Red", new Scalar(125, 120, 50), new Scalar(190, 255, 255) );
//        blueTeamPropOpenCv= new OpenCvVisionProcessor("Blue", new Scalar(160, 200,120), new Scalar(100, 255, 255) );
//        blueTeamPropOpenCv= new OpenCvVisionProcessor("Blue", new Scalar(130, 120, 50), new Scalar(130, 255, 255) );
//*/
//        // Adjust Image Decimation to trade-off detection-range for detection-rate.
//        // eg: Some typical detection data using a Logitech C920 WebCam
//        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
//        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
//        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
//        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
//        // Note: Decimation can be changed on-the-fly to adapt during a match.
//        aprilTag.setDecimation(2);
//
//        // Create the vision portal by using a builder.
//        visionPortal = new VisionPortal.Builder()
//                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
//                .addProcessors(aprilTag)
//                .addProcessor(redTeamPropOpenCv)
//                .addProcessor(blueTeamPropOpenCv)
//                .build();
//        setManualExposure(6, 250);
//        telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
//        telemetry.update();
//    }
//    private void    setManualExposure(int exposureMS, int gain) {
//        // Wait for the camera to be open, then use the controls
//
//        if (visionPortal == null) {
//            return;
//        }
//
//        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
//            telemetry.addData("Camera", "Waiting");
//            telemetry.update();
//            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
//                sleep(20);
//            }
//            telemetry.addData("Camera", "Ready");
//            telemetry.update();
//        }
//
//        if (!isStopRequested())
//        {
//            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
//            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
//                exposureControl.setMode(ExposureControl.Mode.Manual);
//                sleep(50);
//            }
//            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
//            sleep(20);
//            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
//            gainControl.setGain(gain);
//            sleep(20);
//        }
//    }

}