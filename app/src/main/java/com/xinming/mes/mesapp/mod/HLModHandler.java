package com.xinming.mes.mesapp.mod;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

/**
 * Created by Administrator on 2019/4/30.
 */

public class HLModHandler extends BaseModHandler {

    public HLModHandler(Context ctx, View v1){
        super(ctx,v1);
    }
    @Override
    protected void updatePackageData(final RespiratorDataVO data) {
        v.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getSimpleName(),"run start");
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

    @Override
    protected void updateConfigData(RespiratorConfigDataVO data) {
        //更新配置数据

    }


}
