package com.example.meepmeeptesting;

import java.awt.image.BufferedImage;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(750);

        Pose2d startPose = new Pose2d(7, -63.75, Math.toRadians(-90));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(25, 15, Math.toRadians(120), Math.toRadians(120), 17)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .lineToConstantHeading(new Vector2d(-9, -33))
                                //score preloaded specimen
                                .lineToSplineHeading(new Pose2d(-48, -43, Math.toRadians(90)))
                                //run intake 1
                                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(45)))
                                //score top
                                .lineToLinearHeading(new Pose2d(-57, -43, Math.toRadians(90)))
                                //intake 2
                                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(45)))
                                //score top
                                .lineToLinearHeading(new Pose2d(-57, -43, Math.toRadians(125)))
                                //intake 3
                                .lineToLinearHeading(new Pose2d(-57, -57, Math.toRadians(45)))
                                //score top
                                .lineToLinearHeading(new Pose2d(-48, -36, 0))
                                .lineToConstantHeading(new Vector2d(20, -36))
                                .lineToLinearHeading(new Pose2d(38, -68, Math.toRadians(-90)))
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