package io.github.wwqgtxx.soilqualitymonitor.bean;

/**
 * Created by Administrator on 2016/5/16.
 */
public class SettingBean {
    private int detectiontime;

    public int getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }

    private int updatetime;
//    private int lowertemperature;

    public int getDetectiontime() {
        return detectiontime;
    }

    public void setDetectiontime(int detectiontime) {
        this.detectiontime = detectiontime;
    }

//    public int getLowertemperature() {
//        return lowertemperature;
//    }
//
//    public void setLowertemperature(int lowertemperature) {
//        this.lowertemperature = lowertemperature;
//    }
}