package com.xinming.mes.mesapp.entity;

import java.util.Date;

/**
 * Created by Administrator on 2019/6/6.
 */

public class ChartData {

    private int data = 0;

    private float startX = 0;

    private float startY = 0;

    private Date time = null;

    private  String color = null;

    private float stepX = 0;//x轴跨度，非常重要，数据越界时要往回退

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getStepX() {
        return stepX;
    }

    public void setStepX(float stepX) {
        this.stepX = stepX;
    }
}
