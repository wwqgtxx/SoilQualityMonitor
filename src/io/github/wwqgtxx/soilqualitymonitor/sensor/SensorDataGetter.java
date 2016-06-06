package io.github.wwqgtxx.soilqualitymonitor.sensor;

import io.github.wwqgtxx.soilqualitymonitor.bean.SensorDataBean;
import io.github.wwqgtxx.soilqualitymonitor.common.DataBaseConnector;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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
    private DataBaseConnector dataBaseConnector = DataBaseConnector.getDataBaseConnecter();


    public String getSoilmoisture() {return sensorConnector.command("");}

    public String getSoilfertility() {
        return sensorConnector.command("");
    }

    public String getIndoortemperature() {
        return sensorConnector.command("");
    }

    public String getIndoormoisture() {
        return sensorConnector.command("");
    }

    public String getSoiltemperature() {
        return sensorConnector.command("");
    }

    public SensorDataBean getSensorData() {
        return new SensorDataBean(getSoiltemperature(),getSoilmoisture(),getSoilfertility(),getIndoortemperature(),getIndoormoisture(), LocalDateTime.now());
    }

    public List<SensorDataBean> getSensorDataList(){
        return dataBaseConnector.getAllByClass(SensorDataBean.class);
    }


    public static class RandomSensorDataGetter extends SensorDataGetter {
        Random ra =new Random();

        public static RandomSensorDataGetter getRandomSensorDataGetter() {
            return  randomSensorDataGetter;
        }

        private static final RandomSensorDataGetter randomSensorDataGetter = new RandomSensorDataGetter();
        @Override
        public String getSoilmoisture() {return Integer.toString(ra.nextInt(100));}
        @Override
        public String getSoilfertility() {return Integer.toString(ra.nextInt(100));}
        @Override
        public String getIndoortemperature(){return Integer.toString(ra.nextInt(100));}
        @Override
        public String getIndoormoisture(){return Integer.toString(ra.nextInt(100));}
        @Override
        public String getSoiltemperature(){return Integer.toString(ra.nextInt(100));}
    }

}
