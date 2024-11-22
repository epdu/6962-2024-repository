package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(90, 90, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-9, -63.5, Math.toRadians(-90)))
                        .strafeToConstantHeading(new Vector2d(-5, -34))
                        .waitSeconds(1)
                        .strafeToConstantHeading(new Vector2d(30,-34))
                        .splineToConstantHeading(new Vector2d(47,-10), Math.toRadians(0))
                        .strafeToConstantHeading(new Vector2d(47, -50))
                        .splineToConstantHeading(new Vector2d(57, -10), Math.toRadians(0))
                        .strafeToConstantHeading(new Vector2d(57, -50))
                        .splineToConstantHeading(new Vector2d(61, -10), Math.toRadians(0))
                        .strafeToConstantHeading(new Vector2d(61, -50))
                        .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
                        .strafeToConstantHeading(new Vector2d(26,-50))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(0, -34), Math.toRadians(-90))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
                        .strafeToConstantHeading(new Vector2d(26,-50))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(3, -34), Math.toRadians(-90))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
                        .strafeToConstantHeading(new Vector2d(26,-50))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(6, -34), Math.toRadians(-90))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
                        .strafeToConstantHeading(new Vector2d(26,-50))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(9, -34), Math.toRadians(-90))
                        .waitSeconds(1)
                        .strafeToConstantHeading(new Vector2d(40, -60))
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}