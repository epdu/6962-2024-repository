package org.firstinspires.ftc.teamcode.Util;

import org.opencv.core.Scalar;

public enum ColorDetect {
    BLUE(
            new Scalar(2,146,148),
            new Scalar(141,191,225)
    ),
    YELLOW(
            new Scalar(224, 153, 0),
            new Scalar(250,183,0)
    ),
    RED(
            new Scalar(64,1,1),
            new Scalar(247,156,156)
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
