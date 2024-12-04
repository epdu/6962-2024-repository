package org.firstinspires.ftc.teamcode.Util;

import static org.firstinspires.ftc.teamcode.Util.ColorDetect.BLUE;

import android.graphics.Bitmap;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvCamera;

import java.util.concurrent.atomic.AtomicReference;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CameraCVPipeline extends OpenCvPipeline implements CameraStreamSource {

    BNO055IMU imu;
    double cX = 0;
    double cY = 0;
    double width = 0;

    public CameraCVPipeline() {}

    private RobotHardware rHardware = new RobotHardware();  /** MAKE SURE TO CHANGE THE FOV AND THE RESOLUTIONS ACCORDINGLY **/

    /**
     * AutoTune: Automatically tunes the color ranges at a competition to
     * ensure selection accuracy.
     */
    public static double TURN_FACTOR = 0.01;
    public static double TURN_FACTOR_D_GAIN = -0.01;
    public static int COLOR_AUTOTUNE_MODE = 0;
    static final int CAMERA_WIDTH = 640; // width  of wanted camera resolution
    final int CAMERA_HEIGHT = 360; // height of wanted camera resolution
    static final double FOV = 40;

    // Calculate the distance using the formula
    static final double objectWidthInRealWorldUnits = 3.75;  // Replace with the actual width of the object in real-world units
    static final double focalLength = 728;
    private final AtomicReference<Bitmap> lastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

    private ColorDetect detectionType = BLUE;
    private Supplier<Double> currentWristPosition = () -> 0.0;
    private double previousRotationAngle = 0.0;

    private double targetWristPosition = 0.0;
    private double sampleAngle = 0.0;

    private int  cameraMonitorViewID;
    private OpenCvCamera webcam1;


    public double getTargetWristPosition() {
        return targetWristPosition;
    }

    public double getSampleAngle() {
        return sampleAngle;
    }

    public void setDetectionType(ColorDetect sampleType) {
        this.detectionType = sampleType;
    }

    public void initialize() {
        lastFrame.set(Bitmap.createBitmap(680, 480, Bitmap.Config.RGB_565));
    }
    private Scalar detectedColorRangeMin = new Scalar(0, 0, 0);
    private Scalar detectedColorRangeMax = new Scalar(255, 255, 255);

    // Method to automatically determine the color range based on the detected sample
    private void calculateColorRange(Mat input, Rect boundingBox) {
        // Crop the image to the bounding box area
        Mat croppedSample = new Mat(input, boundingBox);

        // Convert the cropped sample to the HSV color space
        Mat hsvSample = new Mat();
        Imgproc.cvtColor(croppedSample, hsvSample, Imgproc.COLOR_RGB2HSV);

        // Calculate the average color in the cropped area
        Scalar averageColor = Core.mean(hsvSample);

        // Set the detected color range based on the average color
        detectedColorRangeMin = new Scalar(
                Math.max(averageColor.val[0] - 10, 0),
                Math.max(averageColor.val[1] - 50, 0),
                Math.max(averageColor.val[2] - 50, 0)
        );
        detectedColorRangeMax = new Scalar(
                Math.min(averageColor.val[0] + 10, 180),
                Math.min(averageColor.val[1] + 50, 255),
                Math.min(averageColor.val[2] + 50, 255)
        );

        // Release the cropped sample and HSV Mat
        croppedSample.release();
        hsvSample.release();
    }
    @Override
    public Mat processFrame(Mat input) {
        // Preprocess the frame to detect yellow regions
        Mat colourMask = preprocessFrame(input);

        // Find contours of the detected yellow regions
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(colourMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        // Find the largest yellow contour (blob)
        MatOfPoint largestContour = findLargestContour(contours);

        if (largestContour != null) {
            // Draw a red outline around the largest detected object
            Imgproc.drawContours(input, contours, contours.indexOf(largestContour), new Scalar(255, 0, 0), 2);

            // Calculate the width of the bounding box
            double width = calculateWidth(largestContour);
            double rotationAngle = getRotationAngle(largestContour);

            // Calculate the centroid of the largest contour
            Moments moments = Imgproc.moments(largestContour);
            cX = moments.get_m10() / moments.get_m00();
            cY = moments.get_m01() / moments.get_m00();

            // Display the width next to the label
            String widthLabel = "Width: " + (int) width + " pixels";
            String rotationLabel = "Angle to rotate: " + (int) rotationAngle + "degrees";
//            Imgproc.putText(input, widthLabel, new Point(cX + 10, cY + 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
            Imgproc.putText(input, rotationLabel, new Point(cX + 10, cY + 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
            //Display the Distance
            String distanceLabel = "Distance: " + String.format("%.2f", getDistance(width)) + " inches";
//            String angleLabel = "Angle: " + String.format("%.2f", getAngleTarget(width)) + "degrees";
//            Imgproc.putText(input, distanceLabel, new Point(cX + 10, cY + 60), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
//            Imgproc.putText(input, angleLabel, new Point(cX + 10, cY + 60), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
            String servoRotLabel = "Servo Rotation: " + String.format("%.2f", calculateServoPosition(0,getRotationAngle(largestContour))) + "ticks";
            targetWristPosition = calculateServoPosition(0.5, getRotationAngle(largestContour));
            sampleAngle = getRotationAngle(largestContour);
//            String angleLabel = "Angle: " + String.format("%.2f", getAngleTarget(width)) + "degrees";
            Imgproc.putText(input, servoRotLabel, new Point(cX + 10, cY + 60), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);



            // Draw a dot at the centroid
            String label = "(" + (int) cX + ", " + (int) cY + ")";
            Imgproc.putText(input, label, new Point(cX + 10, cY), Imgproc.FONT_HERSHEY_COMPLEX, 0.5, new Scalar(0, 255, 0), 2);
            Imgproc.circle(input, new Point(cX, cY), 5, new Scalar(0, 255, 0), -1);

        }
        Bitmap bitmap = Bitmap.createBitmap(input.width(), input.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(COLOR_AUTOTUNE_MODE == 1 ? input : colourMask, bitmap);

        // Update the last frame
        lastFrame.set(bitmap);

        return input;
    }

    private Mat preprocessFrame(Mat frame) {
        Mat hsvFrame = new Mat();
        Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_RGB2HSV);

        Mat colorMask = new Mat();

        Core.inRange(
                hsvFrame,
                detectionType.getColorRangeMinimum(),
                detectionType.getColorRangeMaximum(),
                colorMask
        );
        return colorMask;
    }

    private MatOfPoint findLargestContour(List<MatOfPoint> contours) {
        double maxArea = 0;
        MatOfPoint largestContour = null;
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                largestContour = contour;
            }
        }

        return largestContour;
    }
    private double calculateWidth(MatOfPoint contour) {
        Rect boundingRect = Imgproc.boundingRect(contour);
        return boundingRect.width;
    }

    private double getRotationAngle(MatOfPoint contour) {

        // Convert contour to MatOfPoint2f for the minAreaRect function
        MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

        // Get the minimum area bounding rectangle for the contour
        RotatedRect rotRect = Imgproc.minAreaRect(contour2f);

        // Get the angle of the bounding rectangle
        double angle = rotRect.angle;

        // If width < height, the object is closer to vertical and the angle needs to be adjusted to correct to vertical orientation
        if (rotRect.size.width > rotRect.size.height) {
            angle += 90.0;
        }

        // Normalize the angle to [-90, 90] to get the smallest rotation required
        if (angle > 90) {
            angle -= 180.0;
        }

        return angle;
    }
    private double calculateServoPosition(double current, double rotationAngle) {
        // Calculate the derivative (change) in rotation angle
//        double derivative = rotationAngle - previousRotationAngle;

//        double derivative = rotationAngle < 0 ? rotationAngle + previousRotationAngle : rotationAngle - previousRotationAngle;

        // Store the current angle for the next calculation
//        previousRotationAngle = rotationAngle;
        double servoRotationAngle = rotationAngle < 0.0 ? rotationAngle + 90.0 : rotationAngle - 90.0;

        // Calculate the servo adjustment based on the P and D terms
        double newServoPosition = servoRotationAngle < 0.0 ? -(servoRotationAngle / 180.0) : 0.87 - (servoRotationAngle / 180.0);
//        double servoAdjustment = (rotationAngle * TURN_FACTOR) + (derivative * TURN_FACTOR_D_GAIN);
//
//        // Apply the adjustment to the current servo position
//        double newServoPosition = current + servoAdjustment;

//         Ensure the servo position stays within valid bounds [0, 1]
//        if (newServoPosition > 1) {
//            newServoPosition = 1.0;
//        } else if (newServoPosition < 0.0) {
//            newServoPosition = 0.0;
//        }

        targetWristPosition = newServoPosition;
        return targetWristPosition;

    }

//}
//    private static double getAngleTarget(double objMidpoint){
//        double midpoint = -((objMidpoint - (CAMERA_WIDTH/2))*FOV)/CAMERA_WIDTH;
//        return midpoint;
//    }
    private static double getDistance(double width){
        double distance = (objectWidthInRealWorldUnits * focalLength) / width;
        return distance;
    }
//    public double PIDControl(double refrence, double state) {
//        double error = angleWrap(refrence - state);
//        telemetry.addData("Error: ", error);
//        integralSum += error * timer.seconds();
//        double derivative = (error - lastError) / (timer.seconds());
//        lastError = error;
//        timer.reset();
//        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki);
//        return output;
//    }
    public double angleWrap(double radians){
        while(radians > Math.PI){
            radians -= 2 * Math.PI;
        }
        while(radians < -Math.PI){
            radians += 2 * Math.PI;
        }
        return radians;
    }


    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> {
            // Pass the last frame (Bitmap) to the consumer
            bitmapConsumer.accept(lastFrame.get());
        });
    }

    public void closeCamera() {
        webcam1.stopStreaming();
    }

}