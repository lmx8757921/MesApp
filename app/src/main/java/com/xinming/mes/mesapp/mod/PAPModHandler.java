package com.xinming.mes.mesapp.mod;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;
import com.xinming.mes.mesapp.views.MesCircularView;

public class PAPModHandler extends BaseModHandler {

    boolean firstLoad = true;

    public PAPModHandler(Context ctx, View v1){
        super(ctx,v1);
        initChart();
    }

    @Override
    protected void updatePackageData(final RespiratorDataVO data, final RespiratorConfigDataVO cfgData) {
        v.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getSimpleName(),"更新单包数据 start");

                //流量图表
                flowChartManager.addEntry(data.getFlow());
                //压力图表
                pressureChartManager.addEntry(data.getPressure());

                //SpO2 value
//                TextView txSpO2Val = v.findViewById(R.id.spo2);
//                txSpO2Val.setText(data.getSpo2());

                //Cpap
                TextView txCpap = v.findViewById(R.id.t11);
                txCpap.setText(data.getCpap());
                //设置Cpap单位
                MesCircularView cView = v.findViewById(R.id.bta11);
                cView.setUnit(cfgData.getUnit());
                cView.invalidate();
                //APAP模式下,需要重新更新CPAP配置数据
                if("APAP".equals(data.getMode())){
                    //CPAP value
                    TextView txCpapVal = v.findViewById(R.id.t13);
                    txCpapVal.setText(cfgData.getCpap());
                }
                //首次加载更新如下数据,此数据是静态的,只刷新一次即可
                if(firstLoad){
                    //I:E value
//                    TextView txIeVal = v.findViewById(R.id.ie);
//                    txIeVal.setText("--");
//                    //MV value
//                    TextView txMvVal = v.findViewById(R.id.mv);
//                    txMvVal.setText("--");
//                    //Leak value
//                    TextView txLeakVal = v.findViewById(R.id.leak);
//                    txLeakVal.setText("--");

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
                Log.d(this.getClass().getSimpleName(),"更新单包数据 end");
            }
        });
    }

    @Override
    protected void updateConfigData(final RespiratorConfigDataVO data) {
        //更新配置数据
        v.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getSimpleName(),"更新配置数据 start");
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

                Log.d(this.getClass().getSimpleName(),"更新配置数据 end");
            }
        });
    }
}
