package io.github.wwqgtxx.soilqualitymonitor.bean;


import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Administrator on 2016/5/16.
 */
@Entity
@Table(name = "SensorData")
public class SensorDataBean {
    @Id @GeneratedValue @Column(name="id") private long id;
    private LocalDateTime dateTime;
    private String soiltemperature;
    private String soilmoisture;
    private String soilfertility;
    private String indoortemperature;
    private String indoormoisture;


    public SensorDataBean(){}

    public SensorDataBean(String soiltemperature,String soilmoisture,String soilfertility,String indoortemperature,String indoormoisture,LocalDateTime dateTime){
        this.soiltemperature = soiltemperature;
        this.soilmoisture = soilmoisture;
        this.soilfertility = soilfertility;
        this.indoortemperature = indoortemperature;
        this.indoormoisture = indoormoisture;
        this.dateTime = dateTime;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }


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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


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
        sb.append(",");
        sb.append("dateTime");
        sb.append("=");
        sb.append("\"");
        sb.append(dateTime);
        sb.append("\"");
        sb.append(",");
        sb.append("id");
        sb.append("=");
        sb.append("\"");
        sb.append(id);
        sb.append("\"");
        sb.append("]");
        sb.append(">");
        return  sb.toString();
    }

}
