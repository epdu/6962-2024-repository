package org.firstinspires.ftc.teamcode.Util;

import org.opencv.core.Scalar;

public enum ColorDetect {
    BLUE(
            /*
               RGB VALUE
               new Scalar(90,100,100),
               new Scalar(130,255,255)
             */
            new Scalar(94,80,2),
            new Scalar(126,255,255)
    ),
    YELLOW(
            new Scalar(20.0, 100.0, 100.0),
            new Scalar(30.0, 255.0, 255.0)
    ),
    RED(
            /*RGB VALUE*/
//            new Scalar(0.0, 120.0, 70.0),
//            new Scalar(10.0, 255.0, 255.0)
            new Scalar(161, 155,84),
            new Scalar(179,255,255)
    );
    private final Scalar colorRangeMinimum;
    private final Scalar colorRangeMaximum;

    ColorDetect(Scalar colorRangeMinimum, Scalar colorRangeMaximum) {
        this.colorRangeMinimum = colorRangeMinimum;
        this.colorRangeMaximum = colorRangeMaximum;
    }

    public Scalar getColorRangeMinimum() {
        return colorRangeMinimum;
    }

    public Scalar getColorRangeMaximum() {
        return colorRangeMaximum;
    }
}
