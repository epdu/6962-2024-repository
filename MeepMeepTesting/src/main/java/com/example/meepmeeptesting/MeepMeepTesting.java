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

        Pose2d startPose = new Pose2d(11.25, -62.5, Math.toRadians(0));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(25, 15, Math.toRadians(120), Math.toRadians(120), 17)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .lineToLinearHeading(new Pose2d(11.25, -32, Math.toRadians(90)))
                                //score preloaded specimen
                                .lineToLinearHeading(new Pose2d(38, -30, Math.toRadians(0)))
                                //run intake
                                .lineToSplineHeading(new Pose2d(-50, -66, Math.toRadians(180)))
                                //score top basket
                                .lineToLinearHeading(new Pose2d(-50,-35, Math.toRadians(90)))
                                //intake
                                .lineToSplineHeading(new Pose2d(-50, -66, Math.toRadians(180)))
                                .lineToLinearHeading(new Pose2d(-53,-35, Math.toRadians(90)))
                                .lineToSplineHeading(new Pose2d(-50, -66, Math.toRadians(180)))
                                .lineToLinearHeading(new Pose2d(-55,-35, Math.toRadians(90)))

                                .build());

//                .setDarkMode(true)
//                .setBackgroundAlpha(0.95f)
//                .addEntity(myBot)
//                .start();
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/Users/dlin26/Downloads/field-2024-juice-dark.png")); }
        catch (IOException e) {}

        meepMeep.setBackground(img)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}