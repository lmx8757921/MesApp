package com.xinming.mes.mesapp.entity;

import java.util.Locale;

public class RespiratorConfigDataVO {
    String ipap = "";//ipap测量值
    String epap = "";//epap测量值
    String cpap = "";//cpap测量值
    String bmp = "";//呼吸频率
    String ml = "";//潮气量
    String fio2 = "";//氧气浓度
    String temperature = "";//温度
    String flow = "";//流量
    String unit = "cmH2o";//单位
    Locale language = Locale.SIMPLIFIED_CHINESE;//语言
    String mode = "";//模式
    String dateTime ="";

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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
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
