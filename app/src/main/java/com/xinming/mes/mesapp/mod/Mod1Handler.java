package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.entity.RespiratorData;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Administrator on 2019/4/30.
 */

public class Mod1Handler extends BaseModHandler {
//    private Context ctx = null;
//    private View v1 = null;
    public Mod1Handler(Context ctx,View v1){
        super(ctx,v1);
//        this.ctx = ctx;
//        this.v1 = v1;
    }
    @Override
    public void updateView(final RespiratorData data) {
        ((Activity)ctx).runOnUiThread(new Runnable() {
            public void run() {
                if(getContentView() != v){
                    Log.d("Mod1Handler updateView","If first to setContentView");
                    ((Activity)ctx).setContentView(v);
                }else{
                    Log.d("Mod1Handler updateView","If second not set");
                }
            }
        });
        if(data.isConfig()){//更新配置数据

        }else{//更新单包数据
            v.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Mod1Handler","run start");
                    TextView txTime = v.findViewById(R.id.area2);
                    //时间
                    txTime.setText(data.getTime());

                    TextView txFlow = v.findViewById(R.id.t3);
                    //流量
                    txFlow.setText(data.getFlow());

                    TextView txFio2 = v.findViewById(R.id.t4);
                    //氧浓度
                    txFio2.setText(data.getFio2());

                    TextView txTempture = v.findViewById(R.id.t5);
                    //温度
                    txFio2.setText(String.valueOf(data.getTemperature()));

                }
            });
        }

    }


}
