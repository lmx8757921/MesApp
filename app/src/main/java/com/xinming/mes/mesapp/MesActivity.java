package com.xinming.mes.mesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.xinming.mes.mesapp.server.MesDataServer;

import java.util.HashMap;
import java.util.Map;

public class MesActivity extends AppCompatActivity {

    MesDataServer dataServer = null;

    private ProgressBar pb = null;

    Map<String,View> views = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = (ProgressBar)findViewById(R.id.start_progressBar);
        initViews();
        dataServer = new MesDataServer(this,views,pb );
        dataServer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataServer.stop();
    }

    private void initViews(){
        View v1 = View.inflate(this, R.layout.mes_mod1_main_land, null);
        View v2 = View.inflate(this, R.layout.mes_mod2_main_land, null);
        View v3 = View.inflate(this, R.layout.mes_mod3_main_land, null);
        //TODO
        views.put("",v1);
        views.put("S/T",v1);
        views.put("T",v1);
        views.put("S",v1);
        views.put("CPAP",v1);
        views.put("APAP",v1);
        views.put("PCV",v1);
        views.put("AutoS",v1);
        views.put("MVAPS",v1);
        views.put("HFlow",v1);
        views.put("LFlow",v1);
    }
}
