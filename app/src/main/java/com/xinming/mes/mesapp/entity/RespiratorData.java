package com.xinming.mes.mesapp.entity;

public class RespiratorData {
    String time = null;//时间
    String mode = null;//模式
    int flow = 0;//流量
    int pressure = 0;//压力
    double ipap = 0;//ipap测量值
    double epap = 0;//epap测量值
    double cpap = 0;//测量值
    int bmp = 0;//呼吸频率
    int ml = 0;//潮气量
    int fio2 = 0;//氧气浓度
    double ie = 0;//呼吸比
    double mv = 0;//分钟通气量
    int leak = 0;//漏气量
    int spo2 = 0;//血氧
    int temperature = 0;//温度
    int event = 0;//呼吸事件
    int status = 0;//机器状态
    int alarm = 0;//报警
    int workTime=0;//当前工作时间 分钟



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

    public double getIe() {
        return ie;
    }

    public void setIe(double ie) {
        this.ie = ie;
    }

    public double getMv() {
        return mv;
    }

    public void setMv(double mv) {
        this.mv = mv;
    }

    public int getLeak() {
        return leak;
    }

    public void setLeak(int leak) {
        this.leak = leak;
    }

    public int getSpo2() {
        return spo2;
    }

    public void setSpo2(int spo2) {
        this.spo2 = spo2;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }
}
