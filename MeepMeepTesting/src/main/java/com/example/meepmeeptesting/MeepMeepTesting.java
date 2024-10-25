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
                .setConstraints(45, 45, Math.toRadians(180), Math.toRadians(180), 17)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .lineToConstantHeading(new Vector2d(0, -33))
                                .lineToLinearHeading(new Pose2d(-48, -39, Math.toRadians(90.1)))
                                .lineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)))
                                .lineToLinearHeading(new Pose2d(-58, -39, Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)))
                                .lineToLinearHeading(new Pose2d(-55, -25, Math.toRadians(180)))
                                .lineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)))
                                .lineToLinearHeading(new Pose2d(-30, -12, Math.toRadians(90)))
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