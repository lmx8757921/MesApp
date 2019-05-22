package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

public abstract class BaseModHandler implements IModHandler {
    protected Context ctx = null;
    protected View v = null;
    public BaseModHandler(Context ctx,View v){
        this.ctx = ctx;
        this.v = v;
    }
    @Override
    public void updateViewWithPackageData(final RespiratorDataVO data,RespiratorConfigDataVO viewCfgData) {
        setContentView();
        updatePackageData(data,viewCfgData);
    }

    @Override
    public void updateViewWithConfigData(final RespiratorConfigDataVO data) {
        //更新配置数据
        setContentView();
        updateConfigData(data);
    }

    protected abstract void updatePackageData(final RespiratorDataVO data,final RespiratorConfigDataVO cfgData);

    protected abstract void updateConfigData(final RespiratorConfigDataVO data);

    protected View getContentView(){
        View contentView = ((ViewGroup) (((Activity)ctx).getWindow().getDecorView().findViewById(android.R.id.content))).getChildAt(0);
        return  contentView;
    }

    protected  void  setContentView(){
        ((Activity)ctx).runOnUiThread(new Runnable() {
            public void run() {
                if(getContentView() != v){
                    Log.d(this.getClass().getSimpleName(),"If first to setContentView");
                    ((Activity)ctx).setContentView(v);
                }else{
                    Log.d(this.getClass().getSimpleName(),"If second not set");
                }
            }
        });
    }




}
