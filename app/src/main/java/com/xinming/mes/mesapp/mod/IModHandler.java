package com.xinming.mes.mesapp.mod;

import com.xinming.mes.mesapp.entity.ChartData;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;

/**
 * Created by Administrator on 2019/4/30.
 */

public interface IModHandler {

   void updateViewWithConfigData(RespiratorConfigDataVO viewData);

   void updateViewWithPackageData(RespiratorDataVO viewData,RespiratorConfigDataVO viewCfgData);

   void updateViewWithChartData(ChartData[] datas);
}

