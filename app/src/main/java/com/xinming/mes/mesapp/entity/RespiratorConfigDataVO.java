package com.xinming.mes.mesapp.entity;

public class RespiratorConfigDataVO {
    String time = "";//时间
    String mode = "";//模式
    String flowCfg = "";//流量配置
    String pressureCfg = "";//压力配置
    String ipapCfg = "";//ipap测量值配置
    String epapCfg = "";//epap测量值配置
    String bmpCfg = "";//呼吸频率配置
    String mlCfg = "";//潮气量配置
    String fio2Cfg = "";//氧气浓度配置
    String spo2Cfg = "";//血氧配置
    String temperatureCfg = "";//温度配置
    String unit = "cmH2o";//单位
    String language = "中文";//语言

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

    public String getFlowCfg() {
        return flowCfg;
    }

    public void setFlowCfg(String flowCfg) {
        this.flowCfg = flowCfg;
    }

    public String getPressureCfg() {
        return pressureCfg;
    }

    public void setPressureCfg(String pressureCfg) {
        this.pressureCfg = pressureCfg;
    }

    public String getIpapCfg() {
        return ipapCfg;
    }

    public void setIpapCfg(String ipapCfg) {
        this.ipapCfg = ipapCfg;
    }

    public String getEpapCfg() {
        return epapCfg;
    }

    public void setEpapCfg(String epapCfg) {
        this.epapCfg = epapCfg;
    }

    public String getBmpCfg() {
        return bmpCfg;
    }

    public void setBmpCfg(String bmpCfg) {
        this.bmpCfg = bmpCfg;
    }

    public String getMlCfg() {
        return mlCfg;
    }

    public void setMlCfg(String mlCfg) {
        this.mlCfg = mlCfg;
    }

    public String getFio2Cfg() {
        return fio2Cfg;
    }

    public void setFio2Cfg(String fio2Cfg) {
        this.fio2Cfg = fio2Cfg;
    }

    public String getSpo2Cfg() {
        return spo2Cfg;
    }

    public void setSpo2Cfg(String spo2Cfg) {
        this.spo2Cfg = spo2Cfg;
    }

    public String getTemperatureCfg() {
        return temperatureCfg;
    }

    public void setTemperatureCfg(String temperatureCfg) {
        this.temperatureCfg = temperatureCfg;
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
}
