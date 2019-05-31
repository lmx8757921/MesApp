package com.xinming.mes.mesapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.xinming.mes.mesapp.charts.DynamicLineChartManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivityTest extends AppCompatActivity {
    private DynamicLineChartManager dynamicLineChartManager1;
    private DynamicLineChartManager dynamicLineChartManager2;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mes_mod3_main_land);
        LineChart mChart1 = (LineChart) findViewById(R.id.chart1);
        LineChart mChart2 = (LineChart) findViewById(R.id.chart2);
        //折线名字
        names.add("温度");
        names.add("压强");
        names.add("其他");
        //折线颜色
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);
        colour.add(Color.BLUE);

        dynamicLineChartManager1 = new DynamicLineChartManager(mChart1, names.get(0), colour.get(0));
        dynamicLineChartManager2 = new DynamicLineChartManager(mChart2, names.get(1), colour.get(1));
        dynamicLineChartManager1.setYAxis(120, -60, 10);
        dynamicLineChartManager2.setYAxis(100, 0, 10);
//        死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dynamicLineChartManager1.addEntry((int) (Math.random() * 100));
                            dynamicLineChartManager2.addEntry((int) (Math.random() * 100));
                        }
                    });
                }
            }
        }).start();

    }
}
