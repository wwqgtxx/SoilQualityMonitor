package io.github.wwqgtxx.soilqualitymonitor.sensor;

import io.github.wwqgtxx.soilqualitymonitor.bean.SensorDataBean;
import io.github.wwqgtxx.soilqualitymonitor.common.DataBaseConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static Logger logger = LogManager.getLogger(SensorDataGetter.class);


    public String getSoilmoisture() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = sensorConnector.command("F!!!").substring(0,2);
        logger.debug(result);
        return result;
    }

    public String getSoilfertility() {return "0";}

    public String getIndoortemperature() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = sensorConnector.command("F@@@").substring(0,2);
        logger.debug(result);
        return result;
    }

    public String getIndoormoisture() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = sensorConnector.command("F@@@").substring(2,4);
        logger.debug(result);
        return result;
    }

    public String getSoiltemperature() {
        return "0";
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
