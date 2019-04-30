package com.xinming.mes.mesapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pb = null;

    private View v1 = null;

    private  View  v2 =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = (ProgressBar)findViewById(R.id.start_progressBar);
        v1 = View.inflate(this, R.layout.mes_mod1_main_land, null);
        v2 = View.inflate(this, R.layout.mes_mod2_main_land, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pb.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                    }
                },5000);

                int i = 0;
                while(true){
                    SystemClock.sleep(5000);
                    if(i%2==0){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                setContentView(v1);
                                updateView1(v1);
                            }
                        });

                    }else{
                        runOnUiThread(new Runnable() {
                            public void run() {
                                setContentView(v2);
                                updateView2(v2);
                            }
                        });

                    }
                     i++  ;
                }
            }
        }).start();


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
