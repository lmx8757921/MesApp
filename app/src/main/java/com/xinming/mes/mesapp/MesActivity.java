package com.xinming.mes.mesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.xinming.mes.mesapp.mod.IModHandler;
import com.xinming.mes.mesapp.mod.Mod1Handler;
import com.xinming.mes.mesapp.mod.Mod2Handler;
import com.xinming.mes.mesapp.mod.Mod3Handler;
import com.xinming.mes.mesapp.server.MesDataServer;

import java.util.HashMap;
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
        IModHandler modHandler1 = new Mod1Handler(this,v1);
        IModHandler modHandler2 = new Mod2Handler(this,v2);
        IModHandler modHandler3 = new Mod3Handler(this,v3);
        Map<String,IModHandler> handlers = ((MesApp)getApplication()).getHandlers();
        handlers.put("S/T",modHandler3);
        handlers.put("T",modHandler3);
        handlers.put("S",modHandler3);
        handlers.put("CPAP",modHandler3);
        handlers.put("APAP",modHandler3);
        handlers.put("PCV",modHandler3);
        handlers.put("AutoS",modHandler3);
        handlers.put("MVAPS",modHandler3);
        handlers.put("HFlow",modHandler1);
        handlers.put("LFlow",modHandler1);
    }
}
