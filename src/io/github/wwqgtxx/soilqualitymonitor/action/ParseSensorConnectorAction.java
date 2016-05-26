package io.github.wwqgtxx.soilqualitymonitor.action;

import com.opensymphony.xwork2.ActionSupport;
import io.github.wwqgtxx.soilqualitymonitor.common.DataSave;
import io.github.wwqgtxx.soilqualitymonitor.sensor.SensorConnector;
import io.github.wwqgtxx.soilqualitymonitor.sensor.SensorDataUpdater;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ParseSensorConnectorAction extends ActionSupport{

    public String getScmode() {
        return scmode;
    }

    public void setScmode(String scmode) {
        this.scmode = scmode;
    }

    public String getSchost() {
        return schost;
    }

    public void setSchost(String schost) {
        this.schost = schost;
    }

    public int getScport() {
        return scport;
    }

    public void setScport(int scport) {
        this.scport = scport;
    }

    private String scmode;
    private String schost;
    private int scport;
    private Map<String,Object> dataMap= new HashMap<>();
    private SensorConnector sensorConnector = SensorConnector.getSensorConnector();
    private SensorDataUpdater sensorDataUpdater = SensorDataUpdater.getSensorDataUpdater();


    public String doSet() {
        if (scmode == null&&schost==null&&scport==0){
            dataMap.put("success", false);
            dataMap.put("info", "scmode == null&&schost==null&&scport==0");
            dataMap.put("timestamp", System.currentTimeMillis());
            return ERROR;
        }
        switch (scmode){
            case "server":{
                sensorConnector.initServerMode(scport);
                break;
            }
            case "client":{
                sensorConnector.initClientMode(schost,scport);
                break;
            }
            default:{
                dataMap.put("success", false);
                dataMap.put("info", "scmode is not support");
                dataMap.put("timestamp", System.currentTimeMillis());
                return ERROR;
            }
        }
        sensorDataUpdater.initUpdater();
        return doGet();

    }
    public String doGet() {
        dataMap.put("ready", sensorConnector.isInit());
        dataMap.put("success", true);
        dataMap.put("lastTimestamp", DataSave.getLastDataTimestamp());
        dataMap.put("timestamp", System.currentTimeMillis());
        return SUCCESS;

    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }



}
