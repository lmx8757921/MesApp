package com.xinming.mes.mesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.xinming.mes.mesapp.mod.IModHandler;
import com.xinming.mes.mesapp.mod.HLModHandler;
import com.xinming.mes.mesapp.mod.OtherModHandler;
import com.xinming.mes.mesapp.server.MesDataServer;

import java.util.Map;

public class MesActivity extends AppCompatActivity {

    MesDataServer dataServer = null;

    private ProgressBar pb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = (ProgressBar)findViewById(R.id.start_progressBar);
        pb.setVisibility(View.GONE);
        initModHandlers();
        dataServer = new MesDataServer(this);
        dataServer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataServer.stop();
    }

    private void initModHandlers(){
        View v1 = View.inflate(this, R.layout.mes_mod1_main_land, null);
        View v2 = View.inflate(this, R.layout.mes_mod2_main_land, null);
        View v3 = View.inflate(this, R.layout.mes_mod3_main_land, null);
        IModHandler hlModHandler = new HLModHandler(this,v1);
        IModHandler otherModHandler = new OtherModHandler(this,v3);
        Map<String,IModHandler> handlers = ((MesApp)getApplication()).getHandlers();
        handlers.put("S/T",otherModHandler);
        handlers.put("T",otherModHandler);
        handlers.put("S",otherModHandler);
        handlers.put("CPAP",otherModHandler);
        handlers.put("APAP",otherModHandler);
        handlers.put("PCV",otherModHandler);
        handlers.put("AutoS",otherModHandler);
        handlers.put("MVAPS",otherModHandler);
        handlers.put("HFlow",hlModHandler);
        handlers.put("LFlow",hlModHandler);
    }
}
