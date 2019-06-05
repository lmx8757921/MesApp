package com.xinming.mes.mesapp.entity;

public class RespiratorConfigData {
    double ipap = 0;//ipap测量值
    double epap = 0;//epap测量值
    double cpap = 0;//测量值
    int bmp = 0;//呼吸频率
    int ml = 0;//潮气量
    int fio2 = 0;//氧气浓度
    int temperature = 0;//温度
    int flow = 0;//流量
    String unit = "cmH2o";//单位
    String language = "中文";//语言
    String mode = null;//模式
    String dateTime ="";

    public double getIpap() {
        return ipap;
    }

    public void setIpap(double ipap) {
        this.ipap = ipap;
    }

    public double getEpap() {
        return epap;
    }

    public void setEpap(double epap) {
        this.epap = epap;
    }

    public double getCpap() {
        return cpap;
    }

    public void setCpap(double cpap) {
        this.cpap = cpap;
    }

    public int getBmp() {
        return bmp;
    }

    public void setBmp(int bmp) {
        this.bmp = bmp;
    }

    public int getMl() {
        return ml;
    }

    public void setMl(int ml) {
        this.ml = ml;
    }

    public int getFio2() {
        return fio2;
    }

    public void setFio2(int fio2) {
        this.fio2 = fio2;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
