package io.github.wwqgtxx.soilqualitymonitor.sensor;

import io.github.wwqgtxx.soilqualitymonitor.common.DataSave;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wwq on 2016/5/26.
 */
public class SensorDataUpdater {
    private ScheduledFuture scheduledFuture = null;
    private ReentrantLock lock = new ReentrantLock();
    private SensorDataUpdater(){}

    public static SensorDataUpdater getSensorDataUpdater() {
        return sensorDataUpdater;
    }

    private static SensorDataUpdater sensorDataUpdater = new SensorDataUpdater();

    public void initUpdater(){
        initUpdater(1);
    }

    public void initUpdater(long period){
        initUpdater(period,TimeUnit.MINUTES);
    }

    public void initUpdater(long period, TimeUnit unit){
        initUpdater(0,period,unit);
    }

    public void initUpdater(long initialDelay, long period, TimeUnit unit){
        try{
            lock.lock();
            if (scheduledFuture!=null)
                scheduledFuture.cancel(false);
            ScheduledExecutorService service = Executors
                    .newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            scheduledFuture = service.scheduleAtFixedRate(()->{
                DataSave.setSensorData(SensorDataGetter.getSensorDataGetter().getSensorData());
            }, initialDelay, period, unit);
        }finally {
            lock.unlock();
        }

    }

}
