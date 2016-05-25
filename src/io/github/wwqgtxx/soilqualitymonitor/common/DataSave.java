package io.github.wwqgtxx.soilqualitymonitor.common;

import io.github.wwqgtxx.soilqualitymonitor.bean.SensorDataBean;
import io.github.wwqgtxx.soilqualitymonitor.bean.SettingBean;

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

    public static SensorDataBean getSensorData() {
        return sensorData;
    }

    public static void setSensorData(SensorDataBean sensorData) {
        DataSave.sensorData = sensorData;
    }

    private static SensorDataBean sensorData;

    public static SettingBean getSetting() {
        return setting;
    }

    public static void setSetting(SettingBean setting) {
        DataSave.setting = setting;
    }

    private static SettingBean setting;

}
