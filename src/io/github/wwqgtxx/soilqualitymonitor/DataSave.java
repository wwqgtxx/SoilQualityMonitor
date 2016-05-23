package io.github.wwqgtxx.soilqualitymonitor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/5/16.
 */
public class DataSave {
    private static long lastDataTimestamp;

    public static long getLastDataTimestamp() {
        return lastDataTimestamp;
    }

    public static void setLastDataTimestamp(long lastDataTimestamp) {
        DataSave.lastDataTimestamp = lastDataTimestamp;
    }

    public static SensorData getSensorData() {
        return sensorData;
    }

    public static void setSensorData(SensorData sensorData) {
        DataSave.sensorData = sensorData;
    }

    private static SensorData sensorData;

    public static Setting getSetting() {
        return setting;
    }

    public static void setSetting(Setting setting) {
        DataSave.setting = setting;
    }

    private static Setting setting;

}
