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

public class MeepMeepAuto {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(750);

        Pose2d startPose = new Pose2d(19, -63.5, Math.toRadians(-90));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(45, 45, Math.toRadians(180), Math.toRadians(180), 17)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .lineToConstantHeading(new Vector2d(7, -34))
                                .waitSeconds(1)
                                .lineToConstantHeading(new Vector2d(30,-34))
                                .splineToConstantHeading(new Vector2d(47,-10), Math.toRadians(0))
                                .lineToConstantHeading(new Vector2d(47, -50))
                                .splineToConstantHeading(new Vector2d(57, -10), Math.toRadians(0))
                                .lineToConstantHeading(new Vector2d(57, -50))
                                .lineToLinearHeading(new Pose2d(24, -48, Math.toRadians(-45)))
                                .lineToLinearHeading(new Pose2d(7, -34, Math.toRadians(-90)))
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(24, -48, Math.toRadians(-45)))
                                .lineToLinearHeading(new Pose2d(7, -34, Math.toRadians(-90)))
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(24, -48, Math.toRadians(-45)))
                                .lineToLinearHeading(new Pose2d(7, -34, Math.toRadians(-90)))
                                .waitSeconds(1)
                                .lineToConstantHeading(new Vector2d(40, -60))
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