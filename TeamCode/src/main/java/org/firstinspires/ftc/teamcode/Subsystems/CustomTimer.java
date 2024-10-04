package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CustomTimer {

    private boolean opModeStopped = false;
    public CustomTimer() {}
    public void initialize(OpMode opmode) {}
    public void operate() {}
    public void shutdown() { opModeStopped = true; }
    public void safeDelay(double delayInMillis){
        long start = System.currentTimeMillis();
        while((System.currentTimeMillis() - start < delayInMillis && !opModeStopped)){
            // wait, do nothing
        }
    }
}
