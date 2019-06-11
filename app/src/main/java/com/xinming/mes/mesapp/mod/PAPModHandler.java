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

public class PAPModHandler extends BaseModHandler {

    boolean firstLoad = true;

    public PAPModHandler(Context ctx, View v1,Handler mHandler){
        super(ctx,v1,mHandler);
        initChart();
    }

    @Override
    protected void updatePackageData(final RespiratorDataVO data, final RespiratorConfigDataVO cfgData) {
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

                //SpO2 value
                TextView txSpO2Val = v.findViewById(R.id.spo2);
                txSpO2Val.setText(data.getSpo2());

                //Cpap
                TextView txCpap = v.findViewById(R.id.t11);
                txCpap.setText(data.getCpap());

                //APAP模式下,需要重新更新CPAP配置数据
                if("APAP".equals(data.getMode())){
                    //CPAP value
                    TextView txCpapVal = v.findViewById(R.id.t13);
                    txCpapVal.setText(cfgData.getCpap());
                }
                //首次加载更新如下数据,此数据是静态的,只刷新一次即可
                if(firstLoad){
                    //I:E value
                    TextView txIeVal = v.findViewById(R.id.ie);
                    txIeVal.setText("--");
                    //MV value
                    TextView txMvVal = v.findViewById(R.id.mv);
                    txMvVal.setText("--");
                    //Leak value
                    TextView txLeakVal = v.findViewById(R.id.leak);
                    txLeakVal.setText("--");

                    //Epap
                    TextView txEpap = v.findViewById(R.id.t21);
                    txEpap.setText("--");
                    //呼吸频率
                    TextView txBpm = v.findViewById(R.id.t31);
                    txBpm.setText("--");
                    //潮气量
                    TextView txMl = v.findViewById(R.id.t41);
                    txMl.setText("--");

                    firstLoad = false;
                }

                //潮气量
                TextView txFio2 = v.findViewById(R.id.t51);
                txFio2.setText(data.getFio2());

                // 报警
                TextView txAlarm = v.findViewById(R.id.area2);
                txAlarm.setText( data.getAlarm() );
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
                txMode.setText( "模式     " + data.getMode());

                //CPAP mode
                TextView txCpapName = v.findViewById(R.id.t12);
                txCpapName.setText(data.getMode());
                //CPAP value
                TextView txCpapVal = v.findViewById(R.id.t13);
                txCpapVal.setText(data.getCpap());

                TextView txEpapName = v.findViewById(R.id.t22);
                //EPAP mode
                txEpapName.setText("");
                //EPAP value
                TextView txEpapVal = v.findViewById(R.id.t23);
                txEpapVal.setText("");

                //呼吸频率 mode
                TextView txBmpName = v.findViewById(R.id.t32);
                txBmpName.setText("");
                //呼吸频率 value
                TextView txBmpVal = v.findViewById(R.id.t33);
                txBmpVal.setText("");

                //潮气量 mode
                TextView txMlName = v.findViewById(R.id.t42);
                txMlName.setText("");
                //潮气量 value
                TextView txMlVal = v.findViewById(R.id.t43);
                txMlVal.setText("");

                //FiO2 mode
                TextView txFio2Name = v.findViewById(R.id.t52);
                txFio2Name.setText("FiO2");
                //FiO2 value
                TextView txFio2Val = v.findViewById(R.id.t53);
                txFio2Val.setText(data.getFio2());

                //设置Cpap单位
                MesCircularView cView = v.findViewById(R.id.bta11);
                cView.setUnit(data.getUnit());
                cView.invalidate();

                //图表设置
                MesFPChartView chartFlow = v.findViewById(R.id.chart1);
                chartFlow.setUnit(data.getUnit());
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
