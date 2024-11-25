package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepFiveTestTwo {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(80, 80, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        RoadRunnerBotEntity secondBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(80, 80, Math.toRadians(180), Math.toRadians(180), 18)
                .setColorScheme(new ColorSchemeBlueDark())
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(9, -63.5, Math.toRadians(-90)))
                        .strafeToConstantHeading(new Vector2d(9, -34))
                        .waitSeconds(0.5)
                        .splineToLinearHeading(new Pose2d(25,-36, Math.toRadians(40)), Math.toRadians(0))
                        .turnTo(Math.toRadians(-40))
                        .strafeToLinearHeading(new Vector2d(35, -36), Math.toRadians(40))
                        .turnTo(Math.toRadians(-40))
                        .strafeToLinearHeading(new Vector2d(45, -36), Math.toRadians(40))
                        .turnTo(Math.toRadians(-40))
                        .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
//                        .strafeToConstantHeading(new Vector2d(26,-50))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(6, -34), Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
//                        .strafeToConstantHeading(new Vector2d(26,-50))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(3, -34), Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
//                        .strafeToConstantHeading(new Vector2d(26,-50))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(0, -34), Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(24, -48), Math.toRadians(-45))
//                        .strafeToConstantHeading(new Vector2d(26,-50))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(-5, -34), Math.toRadians(-90))
                        .waitSeconds(0.5)
//                        .strafeToConstantHeading(new Vector2d(-30, -36))
                        .strafeToLinearHeading(new Vector2d(-47, -48), Math.toRadians(90.1))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(-57, -57), Math.toRadians(45))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(-57, -48), Math.toRadians(90))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(-57, -57), Math.toRadians(45))
                        .waitSeconds(1)
                        .turn(Math.toRadians(10))
                        .build());

        secondBot.runAction(secondBot.getDrive().actionBuilder(new Pose2d(-15, -63, Math.toRadians(90)))
                        .turn(Math.toRadians(360))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .addEntity(secondBot)
                .start();
    }
}