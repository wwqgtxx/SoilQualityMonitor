package io.github.wwqgtxx.soilqualitymonitor;

/**
 * Created by Administrator on 2016/5/16.
 */
public class Setting {
    private static int detectiontime;
    private static int lowertemperature;

    public static int getDetectiontime() {
        return detectiontime;
    }

    public static void setDetectiontime(int detectiontime) {
        Setting.detectiontime = detectiontime;
    }

    public static int getLowertemperature() {
        return lowertemperature;
    }

    public static void setLowertemperature(int lowertemperature) {
        Setting.lowertemperature = lowertemperature;
    }
}