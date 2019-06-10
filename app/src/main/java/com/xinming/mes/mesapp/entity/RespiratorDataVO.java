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
    String eventLow = "";//呼吸事件低6位
    String eventHigh = "";//呼吸事件高2位：0xC0      主动/被动/待机
    String eventHighColor = "";//绿色/红色/白色0xC0/ 0x40//0x00： 11B/ 01B //00B:
    String status = "";//机器状态 0待机 1预热中 2工作中 3干燥管路 4转运中
    String alarm = "";//报警 1内部故障	2管道脱落   3窒息报警 	4超温报警	5高吸气压力	6高呼吸频率 	7低呼吸频率        	8低通气量	9呼吸管路	10漏气报警	11阻塞报警	12氧气浓度低	13氧气浓度高	14无法达到目标流量	15检查水量	16无法达到目标温度	17检查工作条件	18切换患者界面
    String workTime = "";//当前工作时间 分钟


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

    public String getEventLow() {
        return eventLow;
    }

    public void setEventLow(String eventLow) {
        this.eventLow = eventLow;
    }

    public String getEventHigh() {
        return eventHigh;
    }

    public void setEventHigh(String eventHigh) {
        this.eventHigh = eventHigh;
    }

    public String getEventHighColor() {
        return eventHighColor;
    }

    public void setEventHighColor(String eventHighColor) {
        this.eventHighColor = eventHighColor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }
}
