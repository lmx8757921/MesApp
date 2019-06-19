package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.charts.MesFPChartView;
import com.xinming.mes.mesapp.entity.ChartData;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

import java.util.Locale;

public abstract class BaseModHandler implements IModHandler {

    protected Context ctx = null;
    protected Handler mHandler = null;
    protected String mode = null;
    protected Locale language = Locale.SIMPLIFIED_CHINESE;

    public BaseModHandler(Context ctx, Handler mHandler) {
        this.ctx = ctx;
        this.mHandler = mHandler;
    }


    @Override
    public void updateViewWithPackageData(final RespiratorDataVO data, RespiratorConfigDataVO viewCfgData) {
        //检查模式
        checkModeAndInit(viewCfgData.getMode());
        updatePackageData(data, viewCfgData);
    }

    @Override
    public void updateViewWithConfigData(final RespiratorConfigDataVO data) {

        //检查语言
        checkLanguage(data.getLanguage());
        //检查模式
        checkModeAndInit(data.getMode());
        //更新配置数据
        updateConfigData(data);

    }

    @Override
    public void updateViewWithChartData(final ChartData[] datas) {
        //子类去实现
        //更新单包数据
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.d("更新图表数据 start");
                //流量图表设置
                MesFPChartView chartFlow = ((Activity) ctx).findViewById(R.id.chart1);
                chartFlow.addData(datas[0]);
                chartFlow.invalidate();

                //压力图表设置
                MesFPChartView chartPressure = ((Activity) ctx).findViewById(R.id.chart2);
                chartPressure.addData(datas[1]);
                chartPressure.invalidate();
                Logger.d("更新图表数据 end");
            }
        });
    }

    protected abstract void updatePackageData(final RespiratorDataVO data, final RespiratorConfigDataVO cfgData);

    protected abstract void updateConfigData(final RespiratorConfigDataVO data);

    protected abstract void setContentView();


    protected void checkModeAndInit(String m) {
        if (!m.equals(mode)) {
            setContentView();
            mode = m;
            //init();
        }
    }

    protected void checkLanguage(Locale l) {
        if (language != l) {
            language = l;
            refreshViewLanguage(l);
            setContentView();
        }
    }

    private void refreshViewLanguage(final Locale l) {
        Resources resources = ctx.getResources();
        Configuration config = resources.getConfiguration();
        config.locale = l;
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(config, dm);
    }

}
