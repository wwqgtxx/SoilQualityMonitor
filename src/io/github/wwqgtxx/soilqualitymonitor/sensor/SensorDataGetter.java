package io.github.wwqgtxx.soilqualitymonitor.sensor;

import io.github.wwqgtxx.soilqualitymonitor.bean.SensorDataBean;

/**
 * Created by Administrator on 2016/5/25.
 */
public class SensorDataGetter {
    private SensorDataGetter(){}

    public static SensorDataGetter getSensorDataGetter() {
        return sensorDataGetter;
    }

    private static final SensorDataGetter sensorDataGetter = new SensorDataGetter();
    private SensorConnector sensorConnector = SensorConnector.getSensorConnector();


    public String getSoilmoisture() {
        return sensorConnector.command("");
    }

    public String getSoilfertility() {
        return sensorConnector.command("");
    }

    public String getIndoortemperature() {
        return sensorConnector.command("");
    }

    public String getIndoormoisture() {
        return sensorConnector.command("");
    }

    public SensorDataBean getSensorData() {
        return new SensorDataBean(getSoilmoisture(),getSoilfertility(),getIndoortemperature(),getIndoormoisture());
    }

}
