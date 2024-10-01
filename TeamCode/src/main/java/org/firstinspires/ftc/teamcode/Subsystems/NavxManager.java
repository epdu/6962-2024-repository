package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.hardware.GyroEx;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class NavxManager extends GyroEx
{
    /**
     * This was initially called 'NavxSubsystem' because it functions like a subsystem-ish, but renamed
     * because it isn't a subsystem per se (doesn't extend SubsystemBase and doesn't have the same init/periodic
     * loops). Anyway, it has all the Navx methods and info that you could need in here. I created this
     * by reverse engineering RevIMU from the FTClib library. -Josh
     * */
    //X (Pitch)	Left/Right	+ Tilt Backwards
    //Y (Roll)	Forward/Backward	+ Roll Left
    //Z (Yaw)	Up/Down	+ Clockwise/ – Counter-wise
    private final NavxMicroNavigationSensor gyro;
    double globalHeading;
    double relativeHeading;

    //Diff between global and relative heading
    double offset;

    public NavxManager(NavxMicroNavigationSensor navx)
    {
        gyro = navx;
        NavxMicroNavigationSensor.Parameters parameters = new NavxMicroNavigationSensor.Parameters();
        globalHeading = 0;
        relativeHeading = 0;
        offset = 0;
    }

    @Override
    public void init()
    {
        // do nothing...for now
    }

    public double getHeading()
    {
        return getAbsoluteHeading() - offset;
    }

    @Override
    public double getAbsoluteHeading() {
        return gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public double getPitch() {
        return gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).thirdAngle;
    }

    @Override
    public double[] getAngles() {
        Orientation orientation = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return new double[]{orientation.firstAngle, orientation.secondAngle, orientation.thirdAngle};
    }

    @Override
    public Rotation2d getRotation2d()
    {
        return null;
    }

    @Override
    public void reset()
    {
        offset += getHeading();
    }

    @Override
    public void disable()
    {
        gyro.close();
    }

    @Override
    public String getDeviceType() {
        return "Navx Micro v2";
    }

    public void rotateHeading(double clockwiseRotation)
    {
        // getHeading rotates clockwise by the offset, so this increments that
        offset += clockwiseRotation;
    }

    //Formats the angle to roadrunner. It's still in degrees here though.
    public double roadrunnerFormat()
    {
        return getHeading() - 90;
    }

//    public void gyroCheck(double targetAngle, int timeout)
//    {
//        navxCalibrationTimer.reset();
//        while (Math.abs(targetAngle - gyroManager.roadrunnerFormat()) > 3 && opModeIsActive())
//        {
//            schedule(mecanumDrive.roboCentric(0, 0, 1/(1-(targetAngle-gyroManager.roadrunnerFormat())) ));
//            if (navxCalibrationTimer.seconds() > timeout)
//                return;
//        }
//        Pose2d currentPos = roadrunnerMecanumDrive.getPoseEstimate();
//        roadrunnerMecanumDrive.setPoseEstimate(new Pose2d(currentPos.getX(), currentPos.getY(), Math.toRadians(targetAngle)));
//        telemetry.addLine("Gyrochecked!!");
//        telemetry.update();
//    }
}
