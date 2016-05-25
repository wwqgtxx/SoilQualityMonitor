package io.github.wwqgtxx.soilqualitymonitor.bean;

/**
 * Created by Administrator on 2016/5/16.
 */
public class SensorDataBean {
    private String soiltemperature;

    public String getSoiltemperature() {
        return soiltemperature;
    }

    public void setSoiltemperature(String soiltemperature) {
        this.soiltemperature = soiltemperature;
    }

    public String getSoilmoisture() {
        return soilmoisture;
    }

    public void setSoilmoisture(String soilmoisture) {
        this.soilmoisture = soilmoisture;
    }

    public String getSoilfertility() {
        return soilfertility;
    }

    public void setSoilfertility(String soilfertility) {
        this.soilfertility = soilfertility;
    }

    public String getIndoortemperature() {
        return indoortemperature;
    }

    public void setIndoortemperature(String indoortemperature) {
        this.indoortemperature = indoortemperature;
    }

    public String getIndoormoisture() {
        return indoormoisture;
    }

    public void setIndoormoisture(String indoormoisture) {
        this.indoormoisture = indoormoisture;
    }

    private String soilmoisture;
    private String soilfertility;
    private String indoortemperature;
    private String indoormoisture;

}
