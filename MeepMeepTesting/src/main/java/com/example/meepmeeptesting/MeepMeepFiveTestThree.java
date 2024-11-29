package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepFiveTestThree {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(80, 60, Math.toRadians(360), Math.toRadians(360), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(9, -62.5, Math.toRadians(-90)))
                        .strafeToConstantHeading(new Vector2d(9, -34))
                        .waitSeconds(0.5)
                        .splineToLinearHeading(new Pose2d(48,-49, Math.toRadians(90)), Math.toRadians(0))
                        .waitSeconds(0.8)
                        .strafeToConstantHeading(new Vector2d(58, -49))
                        .waitSeconds(0.8)
                        .turnTo(Math.toRadians(67))
                        .waitSeconds(0.8)
                        .strafeToLinearHeading(new Vector2d(36, -53), Math.toRadians(90))
                        .strafeToConstantHeading(new Vector2d(36,-57))
                        .splineToLinearHeading(new Pose2d(6, -34, Math.toRadians(-90)), Math.toRadians(90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(36, -53), Math.toRadians(90))
                        .strafeToConstantHeading(new Vector2d(36,-57))
                        .strafeToLinearHeading(new Vector2d(3, -34), Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(36, -53), Math.toRadians(90))
                        .strafeToConstantHeading(new Vector2d(36,-57))
                        .strafeToLinearHeading(new Vector2d(0, -34), Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(36, -53), Math.toRadians(90))
                        .strafeToConstantHeading(new Vector2d(36,-57))
                        .strafeToLinearHeading(new Vector2d(-5, -34), Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(-47.5, -48), Math.toRadians(90.1))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(-57, -57), Math.toRadians(45))
//                        .strafeToLinearHeading(new Vector2d(40, -60), Math.toRadians(90))
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}