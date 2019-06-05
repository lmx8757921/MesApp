package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.charts.DynamicLineChartManager;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

public abstract class BaseModHandler implements IModHandler {
    protected Context ctx = null;
    protected View v = null;
    protected Handler mHandler = null;
    protected DynamicLineChartManager flowChartManager;
    protected DynamicLineChartManager pressureChartManager;
    public BaseModHandler(Context ctx,View v,Handler mHandler){
        this.ctx = ctx;
        this.v = v;
        this.mHandler = mHandler;
    }


    @Override
    public void updateViewWithPackageData(final RespiratorDataVO data,RespiratorConfigDataVO viewCfgData) {
        setContentView();
        updatePackageData(data,viewCfgData);
    }

    @Override
    public void updateViewWithConfigData(final RespiratorConfigDataVO data) {
        //更新配置数据
        setContentView();
        updateConfigData(data);
    }

    protected abstract void updatePackageData(final RespiratorDataVO data,final RespiratorConfigDataVO cfgData);

    protected abstract void updateConfigData(final RespiratorConfigDataVO data);

    /**
     * c初始化图表控件
     */
    protected void initChart(){
        Logger.d("initChart start");
        LineChart mFlowChart = (LineChart) v.findViewById(R.id.chart1);
        LineChart mPreChart = (LineChart) v.findViewById(R.id.chart2);

        flowChartManager = new DynamicLineChartManager(mFlowChart, "流量", Color.CYAN);
        pressureChartManager = new DynamicLineChartManager(mPreChart, "压力", Color.GREEN);

        flowChartManager.setYAxis(120, -60, 10);
        pressureChartManager.setYAxis(100, 0, 10);
        Logger.d("initChart end");
    }

    protected View getContentView(){
        View contentView = ((ViewGroup) (((Activity)ctx).getWindow().getDecorView().findViewById(android.R.id.content))).getChildAt(0);
        return  contentView;
    }

    protected  void  setContentView(){
        Logger.d("setContentView start");
        ((Activity)ctx).runOnUiThread(new Runnable() {
            public void run() {
                if(getContentView() != v){
                    Logger.d("If first to setContentView");
                    ((Activity)ctx).setContentView(v);
                }else{
                    Logger.d("If second not set");
                }
            }
        });
        Logger.d("setContentView end");
    }

}
