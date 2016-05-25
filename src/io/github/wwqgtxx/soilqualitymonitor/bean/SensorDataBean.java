package io.github.wwqgtxx.soilqualitymonitor.bean;


/**
 * Created by Administrator on 2016/5/16.
 */
public class SensorDataBean {
    public SensorDataBean(){}

    public SensorDataBean(String soilmoisture,String soilfertility,String indoortemperature,String indoormoisture){
        this.soilmoisture = soilmoisture;
        this.soilfertility = soilfertility;
        this.indoortemperature = indoortemperature;
        this.indoormoisture = indoormoisture;
    }

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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(super.toString());
        sb.append("[");
        sb.append("soilmoisture");
        sb.append("=");
        sb.append("\"");
        sb.append(soilmoisture);
        sb.append("\"");
        sb.append(",");
        sb.append("soilfertility");
        sb.append("=");
        sb.append("\"");
        sb.append(soilfertility);
        sb.append("\"");
        sb.append(",");
        sb.append("indoortemperature");
        sb.append("=");
        sb.append("\"");
        sb.append(indoortemperature);
        sb.append("\"");
        sb.append(",");
        sb.append("indoormoisture");
        sb.append("=");
        sb.append("\"");
        sb.append(indoormoisture);
        sb.append("\"");
        sb.append("]");
        sb.append(">");
        return  sb.toString();
    }

}
