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
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_mes_mod3_main_land);
        LineChart mChart1 = (LineChart) findViewById(R.id.chart1);
        //折线名字
        names.add("温度");
        names.add("压强");
        names.add("其他");
        //折线颜色
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);
        colour.add(Color.BLUE);

        dynamicLineChartManager1 = new DynamicLineChartManager(mChart1, names.get(0), colour.get(0));

        dynamicLineChartManager1.setYAxis(100, 0, 10);
        //死循环添加数据
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
                        }
                    });
                }
            }
        }).start();

    }
}
