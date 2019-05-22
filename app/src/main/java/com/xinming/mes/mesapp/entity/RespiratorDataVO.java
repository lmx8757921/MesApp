package com.xinming.mes.mesapp.entity;

public class RespiratorDataVO {
    String time = "";//时间
    String mode = "";//模式
    int flow = 0;//流量
    int pressure = 0;//压力
    String ipap = "--";//ipap测量值
    String epap = "--";//epap测量值
    String cpap = "--";//cpap测量值
    String bmp = "--";//呼吸频率
    String ml = "--";//潮气量
    String fio2 = "--";//氧气浓度
    String ie = "--";//呼吸比
    String mv = "--";//分钟通气量
    String leak = "--";//漏气量
    String spo2 = "--";//血氧
    String temperature = "";//温度

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public String getIpap() {
        return ipap;
    }

    public void setIpap(String ipap) {
        this.ipap = ipap;
    }

    public String getEpap() {
        return epap;
    }

    public void setEpap(String epap) {
        this.epap = epap;
    }

    public String getCpap() {
        return cpap;
    }

    public void setCpap(String cpap) {
        this.cpap = cpap;
    }

    public String getBmp() {
        return bmp;
    }

    public void setBmp(String bmp) {
        this.bmp = bmp;
    }

    public String getMl() {
        return ml;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public String getFio2() {
        return fio2;
    }

    public void setFio2(String fio2) {
        this.fio2 = fio2;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getMv() {
        return mv;
    }

    public void setMv(String mv) {
        this.mv = mv;
    }

    public String getLeak() {
        return leak;
    }

    public void setLeak(String leak) {
        this.leak = leak;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

}
