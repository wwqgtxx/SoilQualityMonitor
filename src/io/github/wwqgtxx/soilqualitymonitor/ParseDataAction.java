package io.github.wwqgtxx.soilqualitymonitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ParseDataAction extends ActionSupport{

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    private Setting setting;
    private Map<String,Object> dataMap= new HashMap<>();
    private SensorData sensorData = DataSave.getSensorData();


    public String doSet() {
        if (setting == null){
            dataMap.put("success", false);
            dataMap.put("info", "sensorData == null!");
            dataMap.put("timestamp", System.currentTimeMillis());
            return ERROR;
        }
        DataSave.setSetting(setting);
        return doGet();

    }
    public String doGet() {
        dataMap.put("dataList", sensorData);
        dataMap.put("success", true);
        dataMap.put("lastTimestamp",DataSave.getLastDataTimestamp());
        dataMap.put("timestamp", System.currentTimeMillis());
        return SUCCESS;

    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }



}
