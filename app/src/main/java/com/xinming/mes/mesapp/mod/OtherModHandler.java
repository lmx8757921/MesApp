package com.xinming.mes.mesapp.mod;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.charts.DynamicLineChartManager;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

public class OtherModHandler extends BaseModHandler {
    private DynamicLineChartManager flowChartManager;
    private DynamicLineChartManager pressureChartManager;

    public OtherModHandler(Context ctx, View v3){
        super(ctx,v3);
        LineChart mFlowChart = (LineChart) v.findViewById(R.id.chart1);
        LineChart mPreChart = (LineChart) v.findViewById(R.id.chart2);

        flowChartManager = new DynamicLineChartManager(mFlowChart, "流量", Color.CYAN);
        pressureChartManager = new DynamicLineChartManager(mPreChart, "压力", Color.GREEN);

        flowChartManager.setYAxis(120, -60, 10);
        pressureChartManager.setYAxis(100, 0, 10);
    }
    @Override
    protected void updatePackageData(final RespiratorDataVO data) {

        //更新单包数据
        v.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getSimpleName(),"更新单包数据 start");
                flowChartManager.addEntry(data.getFlow());
                pressureChartManager.addEntry(data.getPressure());
            }
        });
    }

    @Override
    protected void updateConfigData(RespiratorConfigDataVO data) {
        //更新配置数据
        v.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getSimpleName(),"更新配置数据 start");

            }
        });

    }
}
