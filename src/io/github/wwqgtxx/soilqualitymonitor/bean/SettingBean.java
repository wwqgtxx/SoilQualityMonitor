package io.github.wwqgtxx.soilqualitymonitor.bean;

/**
 * Created by Administrator on 2016/5/16.
 */
public class SettingBean {
    private static int detectiontime;
    private static int lowertemperature;

    public static int getDetectiontime() {
        return detectiontime;
    }

    public static void setDetectiontime(int detectiontime) {
        SettingBean.detectiontime = detectiontime;
    }

    public static int getLowertemperature() {
        return lowertemperature;
    }

    public static void setLowertemperature(int lowertemperature) {
        SettingBean.lowertemperature = lowertemperature;
    }
}