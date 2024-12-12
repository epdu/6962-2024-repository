package org.firstinspires.ftc.teamcode.Util;

import org.opencv.core.Scalar;

public enum ColorDetect {
    BLUE(
            new Scalar(90.0, 100.0, 100.0),
            new Scalar(130.0, 255.0, 255.0)
    ),
    YELLOW(
            new Scalar(176, 130, 4),
            new Scalar(255,255,0)
    ),
    RED(
            new Scalar(0.0, 100.0, 100.0),
            new Scalar(10.0, 255.0, 255.0)
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
