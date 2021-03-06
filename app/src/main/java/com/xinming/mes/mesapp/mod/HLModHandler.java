package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.entity.ChartData;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

/**
 * Created by Administrator on 2019/4/30.
 */

public class HLModHandler extends BaseModHandler {

    public HLModHandler(Context ctx, Handler mHandler){
        super(ctx,mHandler);
    }

    @Override
    protected void updatePackageData(final RespiratorDataVO data,final RespiratorConfigDataVO cfgData) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.d("更新单包数据 start");
                Activity v = (Activity) ctx;
                //SPO2
                TextView txSpO2 = v.findViewById(R.id.t1);
                txSpO2.setText(data.getSpo2());

                TextView txTime = v.findViewById(R.id.workingtime);
                //工作时间
                txTime.setText(data.getWorkTime());

                //报警
                TextView txAlarm = v.findViewById(R.id.alarm);
                txAlarm.setText(data.getAlarm());

                //状态
                TextView txStatus = v.findViewById(R.id.status);
                txStatus.setText(data.getStatus());

                TextView txFlow = v.findViewById(R.id.t3);
                //流量
                txFlow.setText(String.valueOf(data.getFlow()));

                TextView txFio2 = v.findViewById(R.id.t2);
                //氧浓度
                txFio2.setText(data.getFio2());

                TextView txTempture = v.findViewById(R.id.t4);
                //温度
                txTempture.setText(String.valueOf(data.getTemperature()));
                Logger.d("更新单包数据 end");
            }
        });


    }

    @Override
    protected void updateConfigData(final RespiratorConfigDataVO data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.d("更新配置数据 start");
                Activity v = (Activity) ctx;
                //氧浓度
                TextView txFiO2 = v.findViewById(R.id.t21);
                txFiO2.setText(data.getFio2());

                TextView txFlow = v.findViewById(R.id.t31);
                //流量
                txFlow.setText(data.getFlow());

                TextView txTempture = v.findViewById(R.id.t41);
                //温度
                txTempture.setText(String.valueOf(data.getTemperature()));
                Logger.d("更新配置数据 end");

            }
        });
    }

    @Override
    public void updateViewWithChartData(final ChartData[] datas) {
        //DO NOTHING
    }

    protected  void  setContentView(){
        ((Activity)ctx).runOnUiThread(new Runnable() {
            public void run() {
                ((Activity) ctx).setContentView(R.layout.mes_mod2_main_land);
            }
        });
    }

}
