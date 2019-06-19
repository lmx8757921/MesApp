package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.charts.MesFPChartView;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;
import com.xinming.mes.mesapp.views.MesCircularView;

public class OtherModHandler extends BaseModHandler {

    public OtherModHandler(Context ctx, Handler mHandler){
        super(ctx,mHandler);
    }
    @Override
    protected void updatePackageData(final RespiratorDataVO data,final RespiratorConfigDataVO cfgData) {

        //更新单包数据
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.d("更新单包数据 start");
                Activity v = (Activity) ctx;
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
                if(mode.equals(v.getString(R.string.mode_mvaps))){
                    TextView txIpapVal = v.findViewById(R.id.t13);
                    txIpapVal.setText(cfgData.getIpap());
                }else if(mode.equals(v.getString(R.string.mode_autos))){
                    //IPAP config
                    TextView txIpapVal = v.findViewById(R.id.t13);
                    txIpapVal.setText(cfgData.getIpap());

                    //EPAP config
                    TextView txEpapVal = v.findViewById(R.id.t23);
                    txEpapVal.setText(cfgData.getEpap());
                }

                //设置Epap单位
                MesCircularView epapView = v.findViewById(R.id.bta21);
                epapView.setUnit(cfgData.getUnit());
                epapView.invalidate();
                //Epap
                TextView txEpap = v.findViewById(R.id.t21);
                txEpap.setText(data.getEpap());
                //设置呼吸频率单位
                MesCircularView bpmView = v.findViewById(R.id.bta31);
                bpmView.setUnit(v.getString(R.string.bpm));
                bpmView.invalidate();
                //呼吸频率
                TextView txBpm = v.findViewById(R.id.t31);
                txBpm.setText(data.getBmp());

                //设置呼吸频率单位
                MesCircularView mlView = v.findViewById(R.id.bta41);
                mlView.setUnit(v.getString(R.string.ml));
                mlView.invalidate();

                //潮气量 value
                TextView txMlVal = v.findViewById(R.id.t41);
                txMlVal.setText(data.getMl());

                //氧气浓度
                TextView txFio2 = v.findViewById(R.id.t51);
                txFio2.setText(data.getFio2());

                // 报警
                TextView txAlarm = v.findViewById(R.id.area2);
                String alarmVal = data.getAlarm();
                if(alarmVal != null && !alarmVal.isEmpty()){
                    txAlarm.setText( String.format("%s%s",v.getString(R.string.two_exclamatory_mark),data.getAlarm() ));
                }else{
                    txAlarm.setText("");
                }

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
                Activity v = (Activity) ctx;
                //  mode title
                TextView txMode = v.findViewById(R.id.area1);
                txMode.setText(String.format("%s%s%s",v.getString(R.string.mode) ,"     ",data.getMode()));

                //IPAP mode
                TextView txIpapName = v.findViewById(R.id.t12);
                txIpapName.setText(v.getString(R.string.tv_ipap));
                //IPAP value
                TextView txIpapVal = v.findViewById(R.id.t13);
                txIpapVal.setText(data.getIpap());

                TextView txEpapName = v.findViewById(R.id.t22);
                //EPAP mode
                txEpapName.setText(v.getString(R.string.tv_epap));
                //EPAP value
                TextView txEpapVal = v.findViewById(R.id.t23);
                txEpapVal.setText(data.getEpap());

                //呼吸频率 mode
                TextView txBmpName = v.findViewById(R.id.t32);
                txBmpName.setText(v.getString(R.string.tv_respiratory_rate));
                //呼吸频率 value
                TextView txBmpVal = v.findViewById(R.id.t33);
                txBmpVal.setText(data.getBmp());

                //潮气量 mode
                TextView txMlName = v.findViewById(R.id.t42);
                txMlName.setText(v.getString(R.string.tv_tidal_volume));
                //潮气量 value
                TextView txMlVal = v.findViewById(R.id.t43);
                txMlVal.setText(data.getMl());

                //FiO2 mode
                TextView txFio2Name = v.findViewById(R.id.t52);
                txFio2Name.setText(v.getString(R.string.tv_fio2));
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


    protected  void  setContentView(){
        ((Activity)ctx).runOnUiThread(new Runnable() {
            public void run() {
                ((Activity) ctx).setContentView(R.layout.mes_mod3_main_land);
            }
        });
    }
}
