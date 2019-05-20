package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.charts.DynamicLineChartManager;
import com.xinming.mes.mesapp.entity.RespiratorData;

import java.util.ArrayList;
import java.util.List;

public class Mod3Handler extends BaseModHandler {
    private DynamicLineChartManager flowChartManager;
    private DynamicLineChartManager pressureChartManager;

    public Mod3Handler(Context ctx, View v3){
        super(ctx,v3);
        LineChart mFlowChart = (LineChart) v.findViewById(R.id.chart1);
        LineChart mPreChart = (LineChart) v.findViewById(R.id.chart2);

        flowChartManager = new DynamicLineChartManager(mFlowChart, "流量", Color.CYAN);
        pressureChartManager = new DynamicLineChartManager(mPreChart, "压力", Color.GREEN);

        flowChartManager.setYAxis(120, -60, 10);
        pressureChartManager.setYAxis(100, 0, 10);
    }
    @Override
    public void updateView(final RespiratorData data) {


        ((Activity)ctx).runOnUiThread(new Runnable() {
            public void run() {
                if(getContentView() != v){
                    Log.d("Mod3Handler updateView","If first to setContentView");
                    ((Activity)ctx).setContentView(v);
                }else{
                    Log.d("Mod3Handler updateView","If second not set");
                }
            }
        });

        if(data.isConfig()){//更新配置数据
            v.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Mod3Handler","更新配置数据 start");

                }
            });
        }else{//更新单包数据
            v.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Mod3Handler","更新单包数据 start");
                    flowChartManager.addEntry(data.getFlow());
                    pressureChartManager.addEntry(data.getPressure());
                }
            });
        }
    }
}
