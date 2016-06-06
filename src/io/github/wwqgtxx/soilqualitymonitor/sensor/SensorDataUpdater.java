package io.github.wwqgtxx.soilqualitymonitor.sensor;

import io.github.wwqgtxx.soilqualitymonitor.bean.SensorDataBean;
import io.github.wwqgtxx.soilqualitymonitor.common.DataBaseConnector;
import io.github.wwqgtxx.soilqualitymonitor.common.DataSave;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wwq on 2016/5/26.
 */
public class SensorDataUpdater {
    private static Logger logger = LogManager.getLogger(SensorDataUpdater.class);
    private SensorDataGetter sensorDataGetter;
    private static final DataBaseConnector dataBaseConnector = DataBaseConnector.getDataBaseConnecter();
    private ScheduledExecutorService service = Executors
            .newSingleThreadScheduledExecutor();
    private ScheduledFuture scheduledFuture = null;
    private ReentrantLock lock = new ReentrantLock();

    private SensorDataUpdater(){}

    public static SensorDataUpdater getSensorDataUpdater() {
        return sensorDataUpdater;
    }

    private static SensorDataUpdater sensorDataUpdater = new SensorDataUpdater();

    public void initUpdater(SensorDataGetter sensorDataGetter){
        initUpdater(sensorDataGetter,10);
    }

    public void initUpdater(SensorDataGetter sensorDataGetter,long period){
        initUpdater(sensorDataGetter,period,TimeUnit.SECONDS);
    }

    public void initUpdater(SensorDataGetter sensorDataGetter,long period, TimeUnit unit){
        initUpdater(sensorDataGetter,0,period,unit);
    }

    public void initUpdater(SensorDataGetter sensorDataGetter,long initialDelay, long period, TimeUnit unit){
        try{
            lock.lock();
            logger.info("SensorDataUpdater start init");
            if (scheduledFuture!=null)
                scheduledFuture.cancel(false);

            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            scheduledFuture = service.scheduleAtFixedRate(()-> {
                SensorDataBean sensorData = sensorDataGetter.getSensorData();
                dataBaseConnector.saveOrUpdate(sensorData);
                DataSave.setSensorData(sensorData);
                DataSave.setLastDataTimestamp(System.currentTimeMillis());
            }
                    , initialDelay, period, unit);
            logger.info("SensorDataUpdater finish init");
        }finally {
            lock.unlock();
        }

    }

    public void changeUpdateTime(long period, TimeUnit unit){
        initUpdater(sensorDataGetter,period,unit);
    }

}
