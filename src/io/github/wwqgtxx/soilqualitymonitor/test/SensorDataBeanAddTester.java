package io.github.wwqgtxx.soilqualitymonitor.test;

import io.github.wwqgtxx.soilqualitymonitor.bean.SensorDataBean;
import io.github.wwqgtxx.soilqualitymonitor.common.DataBaseConnector;

import java.time.LocalDateTime;

/**
 * Created by wwq on 2016/5/29.
 */
public class SensorDataBeanAddTester{
    public static void main(String args[]) throws Exception{
        DataBaseConnector dataBaseConnector = DataBaseConnector.getDataBaseConnecter();
        dataBaseConnector.initSessionFactory();
        dataBaseConnector.saveOrUpdate((new SensorDataBean("1","1","1","1","1", LocalDateTime.now())),(new SensorDataBean("2","2","2","2","2", LocalDateTime.now())));
        for ( SensorDataBean sensorDataBean : dataBaseConnector.getAllByClass(SensorDataBean.class) ) {
            System.out.println( sensorDataBean.toString() );
        }
        SensorDataBean sensorData = dataBaseConnector.getById(SensorDataBean.class,12L);
        //sensorData.setSoilmoisture("5");
        //dataBaseConnector.saveOrUpdate(sensorData);
        //System.out.println(dataBaseConnector.getById(SensorDataBean.class,12L));
        dataBaseConnector.closeSessionFactory();

    }
}
