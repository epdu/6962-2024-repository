package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MeepMeepAuto1 {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(750);

        Pose2d startPose = new Pose2d(7, -63.75, Math.toRadians(-90));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(45, 45, Math.toRadians(180), Math.toRadians(180), 17)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .lineToConstantHeading(new Vector2d(0, -33))
                                .waitSeconds(1)
                                //score preloaded specimen
                                .lineToLinearHeading(new Pose2d(62, -47, Math.toRadians(90)))
                                .lineToConstantHeading(new Vector2d(62, -62))
                                .waitSeconds(0.5)
                                .lineToLinearHeading(new Pose2d(8, -33, Math.toRadians(-90)))
                                .waitSeconds(1)
                                //.lineToConstantHeading(new Vector2d(38, -62))
                                .lineToLinearHeading(new Pose2d(-48, -43, Math.toRadians(90)))
                                .waitSeconds(1)
                                //run intake 1
                                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(45)))
                                .waitSeconds(0.5)
                                //score top
                                .lineToLinearHeading(new Pose2d(-57, -43, Math.toRadians(95)))
                                .waitSeconds(1)
                                //intake 2
                                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(45)))
                                .waitSeconds(0.5)
                                //score top
                                .lineToLinearHeading(new Pose2d(-57, -43, Math.toRadians(125)))
                                .waitSeconds(1)
                                //intake 3
                                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(45)))
                                .waitSeconds(0.5)
                                //score top
                                .lineToLinearHeading(new Pose2d(-36, -12, Math.toRadians(90)))
                                .lineToConstantHeading(new Vector2d(-23, -12))
                                //.lineToLinearHeading(new Pose2d(-48, -36, 0))
                                //.lineToConstantHeading(new Vector2d(62, -36))
                                .build());

//                .setDarkMode(true)
//                .setBackgroundAlpha(0.95f)
//                .addEntity(myBot)
//                .start();
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/Users/nwilliams25/Downloads/field-2024-juice-dark.png")); }
        catch (IOException e) {}

        meepMeep.setBackground(img)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}