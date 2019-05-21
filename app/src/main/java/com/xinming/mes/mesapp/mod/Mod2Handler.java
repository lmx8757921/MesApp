package com.xinming.mes.mesapp.mod;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

public class Mod2Handler extends BaseModHandler {

    public Mod2Handler(Context ctx, View v2){
        super(ctx,v2);
    }
    @Override
    protected void updatePackageData(final RespiratorDataVO data) {
        v.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getSimpleName(),"run start");

            }
        });

    }

    @Override
    protected void updateConfigData(RespiratorConfigDataVO data) {
        //更新配置数据

    }
}
