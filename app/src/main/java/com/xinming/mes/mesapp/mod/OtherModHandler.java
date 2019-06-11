package com.xinming.mes.mesapp.mod;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.charts.MesFPChartView;
import com.xinming.mes.mesapp.entity.ChartData;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;
import com.xinming.mes.mesapp.views.MesCircularView;

import java.util.Date;

public class OtherModHandler extends BaseModHandler {

    public OtherModHandler(Context ctx, View v3,Handler mHandler){
        super(ctx,v3,mHandler);
        initChart();
    }
    @Override
    protected void updatePackageData(final RespiratorDataVO data,final RespiratorConfigDataVO cfgData) {

        //更新单包数据
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.d("更新单包数据 start");
                //流量图表设置
                ChartData fData = new ChartData();
                fData.setTime(new Date());
                fData.setData(data.getFlow());
                fData.setColor(data.getEventHighColor());
                MesFPChartView chartFlow = v.findViewById(R.id.chart1);
                chartFlow.addData(fData);
                chartFlow.invalidate();
                //压力图表设置
                ChartData pData = new ChartData();
                pData.setTime(new Date());
                pData.setData(data.getPressure());
                pData.setColor(data.getEventHighColor());
                MesFPChartView chartPressure = v.findViewById(R.id.chart2);
                chartPressure.addData(pData);
                chartPressure.invalidate();

                //I:E value
                TextView txIeVal = v.findViewById(R.id.ie);
                txIeVal.setText(data.getIe());
                //MV value
                TextView txMvVal = v.findViewById(R.id.mv);
                txMvVal.setText(data.getMv());
                //Leak value
                TextView txLeakVal = v.findViewById(R.id.leak);
                txLeakVal.setText(data.getLeak());
                //SpO2 value
                TextView txSpO2Val = v.findViewById(R.id.spo2);
                txSpO2Val.setText(data.getSpo2());

                //设置Ipap单位
                MesCircularView ipapView = v.findViewById(R.id.bta11);
                ipapView.setUnit(cfgData.getUnit());
                ipapView.invalidate();
                //Ipap
                TextView txIpap = v.findViewById(R.id.t11);
                txIpap.setText(data.getIpap());
                //设置Epap单位
                MesCircularView epapView = v.findViewById(R.id.bta21);
                epapView.setUnit(cfgData.getUnit());
                epapView.invalidate();
                //Epap
                TextView txEpap = v.findViewById(R.id.t21);
                txEpap.setText(data.getEpap());
                //呼吸频率
                TextView txBpm = v.findViewById(R.id.t31);
                txBpm.setText(data.getBmp());
                //潮气量
                TextView txMl = v.findViewById(R.id.t41);
                txMl.setText(data.getMl());
                //潮气量
                TextView txFio2 = v.findViewById(R.id.t51);
                txFio2.setText(data.getFio2());

                // 报警
                TextView txAlarm = v.findViewById(R.id.area2);
                txAlarm.setText( "!!" + data.getAlarm() );
                Logger.d("更新单包数据 end");
            }
        });

    }

    @Override
    protected void updateConfigData(final RespiratorConfigDataVO data) {

        //更新配置数据
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.d("更新配置数据 start");
                //  mode title
                TextView txMode = v.findViewById(R.id.area1);
                txMode.setText( "模式     " + data.getMode() );

                //IPAP mode
                TextView txIpapName = v.findViewById(R.id.t12);
                txIpapName.setText("IPAP");
                //IPAP value
                TextView txIpapVal = v.findViewById(R.id.t13);
                txIpapVal.setText(data.getIpap());

                TextView txEpapName = v.findViewById(R.id.t22);
                //EPAP mode
                txEpapName.setText("EPAP");
                //EPAP value
                TextView txEpapVal = v.findViewById(R.id.t23);
                txEpapVal.setText(data.getEpap());

                //呼吸频率 mode
                TextView txBmpName = v.findViewById(R.id.t32);
                txBmpName.setText("呼吸频率");
                //呼吸频率 value
                TextView txBmpVal = v.findViewById(R.id.t33);
                txBmpVal.setText(data.getBmp());

                //潮气量 mode
                TextView txMlName = v.findViewById(R.id.t42);
                txMlName.setText("潮气量");
                //潮气量 value
                TextView txMlVal = v.findViewById(R.id.t43);
                txMlVal.setText(data.getMl());

                //FiO2 mode
                TextView txFio2Name = v.findViewById(R.id.t52);
                txFio2Name.setText("FiO2");
                //FiO2 value
                TextView txFio2Val = v.findViewById(R.id.t53);
                txFio2Val.setText(data.getFio2());

                //图表设置
                MesFPChartView chartFlow = v.findViewById(R.id.chart1);
                chartFlow.invalidate();

                MesFPChartView chartPressure = v.findViewById(R.id.chart2);
                chartPressure.setUnit(data.getUnit());
                chartPressure.invalidate();
                Logger.d("更新配置数据 end");
            }
        });
    }

    protected void init(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //图表初始化
                MesFPChartView chartFlow = v.findViewById(R.id.chart1);
                chartFlow.resetData();

                MesFPChartView chartPressure = v.findViewById(R.id.chart2);
                chartPressure.resetData();

                //I:E value
                TextView txIeVal = v.findViewById(R.id.ie);
                txIeVal.setText("--");
                //MV value
                TextView txMvVal = v.findViewById(R.id.mv);
                txMvVal.setText("--");
                //Leak value
                TextView txLeakVal = v.findViewById(R.id.leak);
                txLeakVal.setText("--");
                //SpO2 value
                TextView txSpO2Val = v.findViewById(R.id.spo2);
                txSpO2Val.setText("--");

                //设置Ipap单位
                MesCircularView ipapView = v.findViewById(R.id.bta11);
                ipapView.setUnit("");
                ipapView.invalidate();
                //Ipap
                TextView txIpap = v.findViewById(R.id.t11);
                txIpap.setText("--");
                //设置Epap单位
                MesCircularView epapView = v.findViewById(R.id.bta21);
                epapView.setUnit("");
                epapView.invalidate();
                //Epap
                TextView txEpap = v.findViewById(R.id.t21);
                txEpap.setText("--");
                //呼吸频率
                TextView txBpm = v.findViewById(R.id.t31);
                txBpm.setText("--");
                //潮气量
                TextView txMl = v.findViewById(R.id.t41);
                txMl.setText("--");
                //潮气量
                TextView txFio2 = v.findViewById(R.id.t51);
                txFio2.setText("--");
            }
        });
    }
}
