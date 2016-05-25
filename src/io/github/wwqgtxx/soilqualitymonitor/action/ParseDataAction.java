package io.github.wwqgtxx.soilqualitymonitor.action;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import io.github.wwqgtxx.soilqualitymonitor.common.DataSave;
import io.github.wwqgtxx.soilqualitymonitor.bean.SensorDataBean;
import io.github.wwqgtxx.soilqualitymonitor.bean.SettingBean;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ParseDataAction extends ActionSupport{

    public SettingBean getSetting() {
        return setting;
    }

    public void setSetting(SettingBean setting) {
        this.setting = setting;
    }

    private SettingBean setting;
    private Map<String,Object> dataMap= new HashMap<>();
    private SensorDataBean sensorData = DataSave.getSensorData();


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