package com.xinming.mes.mesapp.entity;

import java.util.Date;

/**
 * Created by Administrator on 2019/6/6.
 */

public class ChartData {

    private String mode = null;

    private double data = 0;

    private float startX = 0;

    private float startY = 0;

    private Date time = null;

    private  int color = 0;

    private float stepX = 0;//x轴跨度，非常重要，数据越界时要往回退

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getStepX() {
        return stepX;
    }

    public void setStepX(float stepX) {
        this.stepX = stepX;
    }
}
