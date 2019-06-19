package com.xinming.mes.mesapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.charts.MesFPChartSpecialView;
import com.xinming.mes.mesapp.charts.MesFPChartView;
import com.xinming.mes.mesapp.entity.ChartData;
import com.xinming.mes.mesapp.exception.MesExceptionHandler;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pb = null;

    private View v1 = null;

    private  View  v2 =null;

    /**
     * 点击启动按钮，启动服务
     */
    public void test1(View v){
        Logger.d("test1 start !");
        MesFPChartSpecialView chart = (MesFPChartSpecialView)findViewById(R.id.chart1);
        ChartData d = new ChartData();
        d.setData(20);
        d.setColor(Color.RED);
        d.setTime(new Date());
        chart.addData(d);
        chart.invalidate();
        sleep(300);

        d = new ChartData();
        d.setData(20);
        d.setColor(Color.WHITE);
        d.setTime(new Date());
        chart.addData(d);
        chart.invalidate();
        sleep(300);

        d = new ChartData();
        d.setData(100);
        d.setColor(Color.WHITE);
        d.setTime(new Date());
        chart.addData(d);
        chart.invalidate();
        sleep(200);

        d = new ChartData();
        d.setData(-20);
        d.setColor(Color.GREEN);
        d.setTime(new Date());
        chart.addData(d);
        chart.invalidate();

        Logger.d("test1 end !");
    }

    public void test2(View v){

        Logger.d("test2 start !");
        MesFPChartView chart = (MesFPChartView)findViewById(R.id.chart2);
        ChartData d = new ChartData();
        d.setData(20);
        d.setColor(Color.RED);
        d.setTime(new Date());
        chart.addData(d);
        sleep(300);

        d = new ChartData();
        d.setData(20);
        d.setColor(Color.WHITE);
        d.setTime(new Date());
        chart.addData(d);
        sleep(300);

        d = new ChartData();
        d.setData(150);
        d.setColor(Color.WHITE);
        d.setTime(new Date());
        chart.addData(d);
        sleep(200);

        d = new ChartData();
        d.setData(-20);
        d.setColor(Color.GREEN);
        d.setTime(new Date());
        chart.addData(d);
        sleep(200);

        chart.invalidate();

        Logger.d("test2 end !");
    }

    private void sleep(long l){
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d("started  ---s");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();// 隐藏ActionBar
        MesExceptionHandler handler = new MesExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(handler);
        setContentView(R.layout.activity_mes);

        Logger.d("started");
//        pb = (ProgressBar)findViewById(R.id.start_progressBar);
//        v1 = View.inflate(this, R.layout.mes_mod1_main_land, null);
//        v2 = View.inflate(this, R.layout.mes_mod2_main_land, null);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                pb.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        pb.setVisibility(View.GONE);
//                    }
//                },5000);
//                int i = 0;
//                while(true){
//                    SystemClock.sleep(5000);
//                    if(i%2==0){
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                setContentView(v1);
//                                updateView1(v1);
//                            }
//                        });
//
//                    }else{
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                setContentView(v2);
//                                updateView2(v2);
//                            }
//                        });
//
//                    }
//                     i++  ;
//                }
//            }
//        }).start();


    }

    private void updateView1(final View v1){
        v1.post(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();//
                int mMinute = c.get(Calendar.MINUTE);//分
                int mSec = c.get(Calendar.SECOND);
                TextView tx = v1.findViewById(R.id.area2);
                tx.setText(mMinute+":"+mSec);
            }
        });

    }

    private void updateView2(final View v2){

        v2.post(new Runnable() {
            @Override
            public void run() {
                TextView tx = v2.findViewById(R.id.t2);
                tx.setText(""+Math.random()*100);
            }
        });
    }
}
