package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.charts.DynamicLineChartManager;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseModHandler implements IModHandler {
    protected Context ctx = null;
    protected View v = null;
    protected Handler mHandler = null;
    protected DynamicLineChartManager flowChartManager;
    protected DynamicLineChartManager pressureChartManager;

    private String mode = null;
    private String modeType = null;
    private Map<String,String> types = new HashMap<>();

    public BaseModHandler(Context ctx,View v,Handler mHandler){
        this.ctx = ctx;
        this.v = v;
        this.mHandler = mHandler;

        types.put("S/T","mode3");
        types.put("T","mode3");
        types.put("S","mode3");
        types.put("CPAP","mode3");
        types.put("APAP","mode3");
        types.put("PCV","mode3");
        types.put("AutoS","mode3");
        types.put("MVAPS","mode3");
        types.put("HFlow","mode1");
        types.put("LFlow","mode1");
    }


    @Override
    public void updateViewWithPackageData(final RespiratorDataVO data,RespiratorConfigDataVO viewCfgData) {
        updatePackageData(data,viewCfgData);
    }

    @Override
    public void updateViewWithConfigData(final RespiratorConfigDataVO data) {

        //更新配置数据
        checkModeAndInit(data);
        mode = data.getMode();
        modeType = getModeType(mode);
        updateConfigData(data);

    }

    protected abstract void updatePackageData(final RespiratorDataVO data,final RespiratorConfigDataVO cfgData);

    protected abstract void updateConfigData(final RespiratorConfigDataVO data);

    protected abstract  void init();


    /**
     * c初始化图表控件
     */
    protected void initChart(){
        Logger.d("initChart start");
//        LineChart mFlowChart = (LineChart) v.findViewById(R.id.chart1);
//        LineChart mPreChart = (LineChart) v.findViewById(R.id.chart2);
//
//        flowChartManager = new DynamicLineChartManager(mFlowChart, "流量", Color.CYAN);
//        pressureChartManager = new DynamicLineChartManager(mPreChart, "压力", Color.GREEN);
//
//        flowChartManager.setYAxis(120, -60, 10);
//        pressureChartManager.setYAxis(100, 0, 10);
        Logger.d("initChart end");
    }

    protected void checkModeAndInit(final RespiratorConfigDataVO data){
        String m = data.getMode();
        if(!m.equals(mode)){
            if(!getModeType(m).equals(modeType)){
                setContentView(data);
                init();
            }
        }
    }

    private String getModeType(String mode){
        return types.get(mode);
    }

    protected  void  setContentView(final RespiratorConfigDataVO data){
        Logger.d("setContentView start");
        ((Activity)ctx).runOnUiThread(new Runnable() {
            public void run() {
                ((Activity) ctx).setContentView(v);
            }
        });
        Logger.d("setContentView end");
    }



}
