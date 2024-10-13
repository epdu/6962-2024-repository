package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CustomTimer {
    public CustomTimer() {}
    public void initialize() {}
    public void operate() {}
    public void safeDelay(double delayInMillis){
        long start = System.currentTimeMillis();
        while((System.currentTimeMillis() - start < delayInMillis)){
            // wait, do nothing
        }
    }
}
