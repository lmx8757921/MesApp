package com.xinming.mes.mesapp.service;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.entity.RespiratorConfigData;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorData;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;
import com.xinming.mes.mesapp.exception.MesDataException;
import com.xinming.mes.mesapp.io.MesInputStream;
import com.xinming.mes.mesapp.mod.IModHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class DataExecuterService {

    RespiratorConfigData configData = new RespiratorConfigData();

    Map<String,IModHandler> handlers = null;

    public DataExecuterService(Map<String,IModHandler> handlers){
        this.handlers = handlers;
    }

    /**
     * 获取呼吸机传来的数据
     * @param in
     * @return
     * @throws IOException
     */
    public void execRespiratorData(InputStream in) throws IOException, MesDataException {
        Logger.d("start *****************");
        if (in == null){
            return;
        }
        while(true){
            //            6	同步字
            int isEnd = in.read();
            if(isEnd == -1){
                break;
            }
            in.read(new byte[5]);
//            2	信息字数
            in.read(new byte[2]);
//            1	目标地址
            in.read(new byte[1]);
//            1	源地址
            in.read(new byte[1]);
//            2	命令码
            byte[] cmds = new byte[2];
            in.read(cmds);
            boolean isConfig = isConfig(cmds);
//            2	命令码参数
            in.read(new byte[2]);
//            9	呼吸机序列号
            in.read(new byte[9]);
            //配置数据下多读以下几个数据
            if(isConfig){
                //获取呼吸机数据
                RespiratorConfigDataVO cfgVo = new RespiratorConfigDataVO();
                //            1	呼吸机型号
                in.read(new byte[1]);
                //            11	呼吸机软件版本
                in.read(new byte[11]);
                //            7	当前时间
                byte[] bDatestimes = new byte[7];
                in.read(bDatestimes);
                String date = getDate(bDatestimes[0],bDatestimes[1],bDatestimes[2],bDatestimes[3]);
                configData.setDateTime(date);
                cfgVo.setDateTime(date);
                //            6	累计运行时间
                in.read(new byte[6]);
                setRespiratorDataConfig(in,cfgVo);
                //更新显示配置数据
                getModHandler(configData.getMode()).updateViewWithConfigData(cfgVo);
            }else{
                RespiratorData data = new RespiratorData();
                RespiratorConfigDataVO cfgVo = new RespiratorConfigDataVO();
                RespiratorDataVO vo = new RespiratorDataVO();
                setRespiratorDataPackage(in,data,vo,cfgVo);
                //更新显示数据
                getModHandler(configData.getMode()).updateViewWithPackageData(vo,cfgVo);
            }

            //读到流的结尾
            readToEnd(in);

        }
        Logger.d("end *****************");
    }


    /**
     * 获取呼吸机配置数据,分别设置到VO和原生实体
     * @param in
     * @return
     * @throws IOException
     */
    private  void setRespiratorDataConfig(InputStream in,RespiratorConfigDataVO vo) throws IOException, MesDataException {
        //  1	当前模式
        int mode = in.read();
        configData = new RespiratorConfigData();
        String modeName = getMode(mode);
        Logger.d("modeName="+modeName);
        configData.setMode(modeName);
        vo.setMode(modeName);
        byte[] bIpaps = null;
        byte[] bEpaps = null;
        byte[] bCpaps = null;
        if("HFlow".equals(modeName) || "LFlow".equals(modeName)){
            //流量
            int flow = in.read();
            configData.setFlow(flow);
            vo.setFlow(String.valueOf(flow));
            //温度
            int temperature = in.read();
            configData.setTemperature(temperature);
            vo.setTemperature(getTemperature(temperature));
            //氧气浓度
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(getFiO2(fio2));
        }else if("S/T".equals(modeName)){
//            IPAP(2)
            bIpaps = new byte[2];
            in.read(bIpaps);
//            EPAP(2)
            bEpaps = new byte[2];
            in.read(bEpaps);
//            上升时间(1)
            in.read(new byte[1]);
//            呼吸频率(1)
            int bmp = in.read();
            configData.setBmp(bmp);
            vo.setBmp(String.valueOf(bmp));
//            吸气时间(1)
            in.read(new byte[1]);
//            吸气灵敏度(1)
            in.read(new byte[1]);
//            呼气灵敏度(1)
            in.read(new byte[1]);
//            延时升压(1)
            in.read(new byte[1]);
//            氧浓度(1)
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(getFiO2(fio2));
//            温度(1)
            in.read(new byte[1]);

        }else if("T".equals(modeName)){
//            IPAP(2)
            bIpaps = new byte[2];
            in.read(bIpaps);
//            EPAP(2)
            bEpaps = new byte[2];
            in.read(bEpaps);
//            上升时间(1)
            in.read(new byte[1]);
//            呼吸频率(1)
            int bmp = in.read();
            configData.setBmp(bmp);
            vo.setBmp(String.valueOf(bmp));
//            吸气时间(1)
            in.read(new byte[1]);
//            呼气时间(1)
            in.read(new byte[1]);
//            延时升压(1)
            in.read(new byte[1]);
//            氧浓度(1)
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//            温度(1)
            in.read(new byte[1]);

        }else if("S".equals(modeName)){
//           IPAP(2)
            bIpaps = new byte[2];
            in.read(bIpaps);
//            EPAP(2)
            bEpaps = new byte[2];
            in.read(bEpaps);
//            上升时间(1)
            in.read(new byte[1]);
//           吸气灵敏度(1)
            in.read(new byte[1]);
//           呼气灵敏度(1)
            in.read(new byte[1]);
//            延时升压(1)
            in.read(new byte[1]);
//            氧浓度(1)
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//            温度(1)
            in.read(new byte[1]);
        }else if("CPAP".equals(modeName)){
//            CPAP(2)
            bCpaps = new byte[2];
            in.read(bCpaps);
//            压力延迟(1)
            in.read(new byte[1]);
//            舒适程度(1)
            in.read(new byte[1]);
//            氧浓度(1)
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//            温度(1)
            in.read(new byte[1]);

        }else if("APAP".equals(modeName)){
//            CPAP Start(2)
            in.read(new byte[2]);
//            CPAP Max (2)
            in.read(new byte[2]);
//            CPAP Min (2)
            in.read(new byte[2]);
//            延时升压(1)
            in.read(new byte[1]);
//            舒适程度(1)
            in.read(new byte[1]);
//            氧浓度(1)
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//            温度(1)
            in.read(new byte[1]);
        }else if("PC".equals(modeName)){
//           IPAP(2)
            bIpaps = new byte[2];
            in.read(bIpaps);
//            EPAP(2)
            bEpaps = new byte[2];
            in.read(bEpaps);
//            上升时间(1)
            in.read(new byte[1]);
//            呼吸频率(1)
            int bmp = in.read();
            configData.setBmp(bmp);
            vo.setBmp(String.valueOf(bmp));
//            吸气时间(1)
            in.read(new byte[1]);
//            吸气灵敏度(1)
            in.read(new byte[1]);
//            延时升压(1)
            in.read(new byte[1]);
//            氧浓度(1)
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//            温度(1)
            in.read(new byte[1]);

        }else if("AutoS".equals(modeName)){
//            IPAP Max (2)
            in.read(new byte[2]);
//            EPAP Min (2)
            in.read(new byte[2]);
//            PS Max (2)
            in.read(new byte[2]);
//            PS Min (2)
            in.read(new byte[2]);
//            上升时间(1)
            in.read(new byte[1]);
//            吸气灵敏度(1)
            in.read(new byte[1]);
//            呼气灵敏度(1)
            in.read(new byte[1]);
//            延时升压(1)
            in.read(new byte[1]);
//            氧浓度(1)
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//            温度(1)
            in.read(new byte[1]);

        }else if("MVAPS".equals(modeName)){
//            Vt (2)
            in.read(new byte[2]);
//            IPAP Max (2)
            in.read(new byte[2]);
//            IPAP Min (2)
            in.read(new byte[2]);
//            EPAP (2)
            bEpaps = new byte[2];
            in.read(bEpaps);
//            上升时间(1)
            in.read(new byte[1]);
//            呼吸频率(1)
            int bmp = in.read();
            configData.setBmp(bmp);
            vo.setBmp(String.valueOf(bmp));
//            吸气时间(1)
            in.read(new byte[1]);
//            吸气灵敏度(1)
            in.read(new byte[1]);
//            呼气灵敏度(1)
            in.read(new byte[1]);
//            延时升压(1)
            in.read(new byte[1]);
//            氧浓度(1)
            int fio2 = in.read();
            configData.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//            温度(1)
            in.read(new byte[1]);
        }

        //呼吸报警：S/T、T、S、CPAP、APAP、PC、AutoS、MVAPS
        readRespiratorAlarmData(in,modeName);
        //配置
        setConfigData(in,vo);

        //IPAP EPAP CPAP与单位相关的,重新计算
        if(bIpaps != null){
            double ipap =  getValueWithUnit(getInt(bIpaps),configData.getUnit());
            configData.setIpap(ipap);
            vo.setIpap(String.valueOf(ipap));
        }

        if(bEpaps != null){
            double epap =  getValueWithUnit(getInt(bEpaps),configData.getUnit());
            configData.setEpap(epap);
            vo.setEpap(String.valueOf(epap));
        }

        if(bCpaps != null){
            double cpap =  getValueWithUnit(getInt(bCpaps),configData.getUnit());
            configData.setCpap(cpap);
            vo.setCpap(String.valueOf(cpap));
        }
    }

    /**
     * 读取报警数据
     * 呼吸报警：S/T、T、S、CPAP、APAP、PC、AutoS、MVAPS	湿化报警： HFlow、LFlow
     * @param in
     * @param modeName
     * @throws IOException
     */
    private void readRespiratorAlarmData(InputStream in, String modeName) throws IOException{
        if("HFlow".equals(modeName) || "LFlow".equals(modeName)){
//            高氧浓度	0x28	0x00：关
//            其他：如0x28：40%
//                    低氧浓度	0x17	0x00：关
//            其他：如0x17：23%
//                    环境温度报警声音	0x00	0x00：关
//            0x01：开
//            阻塞报警	0x00	0x00：关
//            0x01：开
//            报警--备用	0x00	备用
//            报警--备用	0x00	备用
//            报警--备用	0x00	备用
//            报警--备用	0x00	备用
//            报警--备用	0x00	备用
//            报警--备用	0x00	备用
//            报警--备用	0x00	备用
            in.read(new byte[12]);
        }else{
//            管道脱落	0x00	0x00：关
//            0x01：开
//            窒息	0x0A	0x00：关
//            其他：如0x0A ：10 S
//            高呼吸频率	0x16	0x00：关
//            其他：如0x16：22 bpm
//            低呼吸频率	0x02	0：关闭
//            其他：如0x02：2 bpm
//            低通气量	0x02	0x00：关
//            其他：如0x02：2 L/min
//            高氧浓度	0x28	0x00：关
//            其他：如0x28：40%
//                    低氧浓度	0x17	0x00：关
//            其他：如0x17：23%
//                    高吸气压力	0x01	0x00：关
//            0x01：开
//            报警--备用	0x00	备用
//            报警--备用	0x00	备用
//            报警--备用	0x00	备用
            in.read(new byte[12]);
        }
    }

    /**
     * 设置配置数据的配置项到VO
     * @param in
     * @param vo
     * @throws IOException
     */
    private void setConfigData(InputStream in,RespiratorConfigDataVO vo) throws IOException, MesDataException {

//        配置--临床设置	0x00	0x00：关
//        0x01：开
        in.read(new byte[1]);
//        配置—背光模式	0x01	0x00：长亮
//        0x01：渐暗
        in.read(new byte[1]);
//        配置--数据存储	0x01	0x00：关
//        0x01：开
        in.read(new byte[1]);
//        配置--智能启动	0x01	0x00：关
//        0x01：开
        in.read(new byte[1]);
//        配置--压力单位	0x00	0x00：cmh2o
//        0x01： kpa
//        0x02： hpa
        int unit = in.read();
        configData.setUnit(getUnit(unit));
        vo.setUnit(configData.getUnit());
//        配置--语言	0x00	0x00：中文
//        0x01：英文
        int language = in.read();
        configData.setLanguage(getLanguage(language));
        vo.setLanguage(configData.getLanguage());
    }
    /**
     * 获取呼吸机数据,分别设置到VO和原生实体
     * @param in
     * @return
     * @throws IOException
     */
    private  void setRespiratorDataPackage(InputStream in,RespiratorData data,RespiratorDataVO vo,RespiratorConfigDataVO cfgVo) throws IOException ,MesDataException{

        String modeName = configData.getMode();
        data.setMode(modeName);
        vo.setMode(modeName);
        if("HFlow".equals(modeName) || "LFlow".equals(modeName)){
//                0--2	时、分、秒
            byte[] timeBts = new byte[3];
            in.read(timeBts);
            data.setTime(getRespTime(timeBts));
            vo.setTime(data.getTime());
//                3--4	流量(-200 - +200) L/min
            byte[] flowBytes = new byte[2] ;
            in.read(flowBytes);
            data.setFlow(getSignedInt(flowBytes));
            vo.setFlow(data.getFlow());
//                5	温度
            int temperature = in.read();
            data.setTemperature(temperature);
            vo.setTemperature(getTemperature(temperature));
//                6	氧气浓度0-100
            int fio2 = in.read();
            data.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//                7	血氧0-100
            in.read(new byte[1]);
//                8	脉率0-255
            in.read(new byte[1]);
//            当前工作时间 分钟
            int t0 = in.read();
            int t1 = in.read();
            int t2 = in.read();
            int t3 = in.read();
            int t = t0 *256*256*256 + t1*256*256 + t2 *256 + t3;
            data.setWorkTime(t);
            vo.setWorkTime(getWorkTime(t));
            //读9个无用字节
            in.read(new byte[9]);

        }else if("CPAP".equals(modeName) || "APAP".equals(modeName)){
//                0--2	时、分、秒
            byte[] timeBts = new byte[3];
            in.read(timeBts);
            data.setTime(getRespTime(timeBts));
            vo.setTime(data.getTime());
//                3--4	目标压力
            byte[] bDps = new byte[2];
            in.read(bDps);
            if("APAP".equals(modeName)){
                double dps =  getValueWithUnit(getInt(bDps),configData.getUnit());
                configData.setCpap(dps);
                cfgVo.setCpap(String.valueOf(dps));
            }
//                5--6	当前压力
            byte[] bCps = new byte[2];
            in.read(bCps);
            double cps =  getValueWithUnit(getInt(bCps),configData.getUnit());
            data.setCpap(cps);
            vo.setCpap(String.valueOf(cps));
//                7--8	当前流量
            byte[] flowBytes = new byte[2] ;
            in.read(flowBytes);
            data.setFlow(getSignedInt(flowBytes));
            vo.setFlow(data.getFlow());
//                9	呼吸事件0，无；1低通气，2，呼吸暂停 4，鼾声5.中枢
            int event = in.read();
            setEvent(data,vo,event);
//                10-11	潮气量(0-2500)mL
            in.read(new byte[2]);
//                12	漏气量(0-99) L/min
            in.read(new byte[1]);
//                13	分钟通气量(0-30)L/min
            in.read(new byte[1]);
//                14	血氧0-100
            int spo2 =  in.read();
            data.setSpo2(spo2);
            vo.setSpo2(String.valueOf(spo2));
//                15	脉率0-255
            in.read(new byte[1]);
//                16	氧气浓度0-100
            int fio2 = in.read();
            data.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//                17	CPAP(40-400)4-40cmH2O测量值
            int iCpap = in.read();
            double cpap =  getValueWithUnit(iCpap,configData.getUnit());
            data.setCpap(cpap);
            vo.setCpap(String.valueOf(cpap));

            //读3个无用字节
            in.read(new byte[3]);

        }else{
//                0--2	时、分、秒
            byte[] timeBts = new byte[3];
            in.read(timeBts);
            data.setTime(getRespTime(timeBts));
            vo.setTime(data.getTime());
//                3--4	压力(0-350)0-35cmH2O
            byte[] bPs = new byte[2];
            in.read(bPs);
            double cp =  getValueWithUnit(getInt(bPs),configData.getUnit());
            data.setCpap(cp);
            vo.setCpap(String.valueOf(cp));

//                5--6	流量(-200 - +200) L/min
            byte[] flowBytes = new byte[2] ;
            in.read(flowBytes);
            data.setFlow(getSignedInt(flowBytes));
            vo.setFlow(data.getFlow());
//                7--8	潮气量(0-2500)mL
            byte[] bmls = new byte[2];
            in.read(bmls);
            data.setMl(getInt(bmls));
            vo.setMl(String.valueOf(data.getMl()));
//                9	漏气量(0-99) L/min
            int leak = in.read();
            data.setLeak(leak);
            vo.setLeak(String.valueOf(leak));
//                10	分钟通气量(0-30)L/min
            int mv = in.read();
            double dMv =  getValueWithUnit(mv,configData.getUnit());
            data.setMv(dMv);
            vo.setMv(String.valueOf(dMv));
//                11	呼吸频率(0-60)min
            int bmp = in.read();
            data.setBmp(bmp);
            vo.setBmp(String.valueOf(bmp));
//                12	吸呼比(1-99),1:0.1-9.9
            int ie = in.read();
            data.setIe(((double)ie)/10);
            vo.setIe("1:"+(data.getIe()));
//                13	IPAP(40-250)4-25cmH2O 目标值
            in.read(new byte[1]);
//                14	EPAP(40-200)4-20cmH2O目标值
            in.read(new byte[1]);
//                15	呼吸事件对128求余后： 0，无；1低通气，2，呼吸暂停 4，鼾声5.中枢
////                主动/被动 ：主动>=128,被动<128;
            in.read(new byte[1]);
//                16	血氧0-100  0，无；1低通气，2，呼吸暂停 4，鼾声5.中枢
//                主动/被动 ：主动>=128,被动<128;
            int spo2 = in.read();
            data.setSpo2(spo2);
            vo.setSpo2(String.valueOf(spo2));
//                17	脉率0-255
            in.read(new byte[1]);
//                18	氧气浓度0-100
            int fio2 = in.read();
            data.setFio2(fio2);
            vo.setFio2(String.valueOf(fio2));
//                19--20	IPAP(40-400)4-40cmH2O测量值
            byte[] bIpaps = new byte[2];
            in.read(bIpaps);
            double ipap =  getValueWithUnit(getInt(bIpaps),configData.getUnit());
            data.setIpap(ipap);
            vo.setIpap(String.valueOf(ipap));
//            EPAP(2)
//                21	EPAP(40-200)4-20cmH2O
            int e = in.read();
            double epap =  getValueWithUnit(e,configData.getUnit());
            data.setEpap(epap);
            vo.setEpap(String.valueOf(epap));

        }

        //机器状态
        int status = in.read();
        data.setStatus(status);
        vo.setStatus(getStatus(status));
        //报警
        int alarm = in.read();
        data.setAlarm(alarm);
        vo.setAlarm(getAlarm(alarm));
    }


    /**
     * 获取不同模式下的业务处理Handler
     * @param modeName
     * @return
     */
    private IModHandler getModHandler(String modeName) throws MesDataException{
        if(!handlers.containsKey(modeName)){
            throw new MesDataException("模式数据错误,请确认模式数据"+modeName+"的正确性!");
        }
        return handlers.get(modeName);
    }

    /**
     * 读数据到结尾
     * @param in
     * @throws IOException
     */
    private void readToEnd(InputStream in) throws IOException {
        int a1, a2, a3;
        a1 = in.read();
        a2 = in.read();
        while (true) {
            a3 = in.read();
            if (isEnd(a1, a2, a3)) {
                if(in instanceof MesInputStream){
                    ((MesInputStream) in).logDatas();
                }
                break;
            }
            a1 = a2;
            a2 = a3;
        }
    }

    /**
     * 设置呼吸事件
     * @param data
     * @param vo
     * @param event
     * @throws MesDataException
     */
    private void setEvent(RespiratorData data,RespiratorDataVO vo,int event)throws MesDataException{
        int low6 = event & 0x3F;
        data.setEvent(event);
        vo.setEventLow(getEventLow (low6));
        vo.setEventHigh(getEventHigh(event));
        vo.setEventHighColor(getEventHighColor(event));

    }

    private String getWorkTime(int t){
        StringBuilder sb = new StringBuilder();
        int tMod = t % 60;
        if(t> 60){
            int h = t/60;
            int hMod = h%24;
            if(h > 24) {
                int day = h/24;
                sb.append(day).append("天");
            }
            sb.append(hMod).append("小时");
        }
        sb.append(tMod).append("分");

        return sb.toString();
    }

    private String getEventLow(int low6){
        String el = "";
        //0，无；1低通气，2，呼吸暂停 4，鼾声5.中枢
        if(low6 == 0){
            el = "无";
        }else if(low6 == 1){
            el = "低通气";
        }else if(low6 == 2){
            el = "呼吸暂停";
        }else if(low6 == 4){
            el = "鼾声";
        }else if(low6 == 5){
            el = "中枢";
        }
        return el;
    }

    private String getEventHigh(int h) throws MesDataException{
        String eh = "";
        //主动/被动/待机 ：0xC0/ 0x40//0x00
        int h8 = h & 0xc0;
        if(h8 == 0xC0){
            eh = "主动";
        }else if(h8 == 0x40){
            eh = "被动";
        }else if(h8 == 0x00){
            eh = "待机";
        }else{
            throw new MesDataException("getEventHigh数据错误,请确认呼吸事件高2位"+h8+"的正确性!");
        }
        return eh;
    }

    private String getEventHighColor(int h) throws MesDataException{
        String eh = "";
        //绿色/红色/白色：11B/ 01B //00B:
        int h2 = h >>> 6;
        if(h2 == 3){
            eh = "绿色";
        }else if(h2 == 1){
            eh = "红色";
        }else if(h2 == 0){
            eh = "白色";
        }else{
            throw new MesDataException("getEventHighColor数据错误,请确认呼吸事件高2位"+h2+"的正确性!");
        }
        return eh;
    }

    private String getStatus(int status) throws  MesDataException{
        //0待机 1预热中 2工作中 3干燥管路 4转运中
        if(status == 0){
            return "待机";
        }else if(status == 1){
            return "预热中";
        }else if(status == 2){
            return "工作中";
        }else if(status == 3){
            return "干燥管路";
        }else if(status == 4){
            return "转运中";
        }else{
            throw new MesDataException("状态数据错误,请确认状态数据"+status+"的正确性!");
        }
    }

    private String getAlarm(int alarm) throws  MesDataException{
        //1内部故障	2管道脱落   3窒息报警 	4超温报警	5高吸气压力	6高呼吸频率 	7低呼吸频率        	8低通气量	9呼吸管路	10漏气报警	11阻塞报警	12氧气浓度低	13氧气浓度高	14无法达到目标流量	15检查水量	16无法达到目标温度	17检查工作条件	18切换患者界面
        switch (alarm){
            case 0:
                return "";
            case 1:
                return "内部故障";
            case 2:
                return "管道脱落";
            case 3:
                return "窒息报警";
            case 4:
                return "超温报警";
            case 5:
                return "高吸气压力";
            case 6:
                return "高呼吸频率";
            case 7:
                return "低呼吸频率";
            case 8:
                return "低通气量";
            case 9:
                return "呼吸管路";
            case 10:
                return "漏气报警";
            case 11:
                return "阻塞报警";
            case 12:
                return "氧气浓度低";
            case 13:
                return "氧气浓度高";
            case 14:
                return "无法达到目标流量";
            case 15:
                return "检查水量";
            case 16:
                return "无法达到目标温度";
            case 17:
                return "检查工作条件";
            case 18:
                return "切换患者界面";
            default:
                throw new MesDataException("报警数据错误,请确认报警数据"+alarm+"的正确性!");
        }

    }

    private String getDate(byte byh,byte byl,byte bm,byte bd){
        StringBuffer sb = new StringBuffer ();
        sb.append(bcdToString(byh) );
        sb.append(bcdToString(byl) );
        sb.append("年");
        sb.append(bcdToString(bm));
        sb.append("月");
        sb.append(bcdToString(bd));
        sb.append("日");
        return sb.toString();
    }

    private String getRespTime(byte[] bts){
        StringBuffer sb = new StringBuffer();
        sb.append(bcdToString(bts[0]));
        sb.append(":");
        sb.append(bcdToString(bts[1]));
        sb.append(":");
        sb.append(bcdToString(bts[2]));
        return sb.toString();
    }

    /**
     * 获取呼吸机工作模式
     * @param m
     * @return
     */
    private String getMode(int m) throws MesDataException{
        switch(m){
            case 0x00:return "S/T" ;
            case 0x01:return "T" ;
            case 0x02:return "S" ;
            case 0x03:return "CPAP" ;
            case 0x04:return "APAP" ;
            case 0x05:return "PCV" ;
            case 0x06:return "AutoS" ;
            case 0x07:return "MVAPS" ;
            case 0x08:return "HFlow" ;
            case 0x09:return "LFlow" ;
            default : throw new MesDataException("getMode模式数据错误,请确认模式数据"+m+"的正确性!");
        }
    }

    /**
     * 判断是否是配置数据 true:是 false:单包数据
     * @param cmds
     * @return
     */
    private  boolean  isConfig(byte[] cmds) throws MesDataException{
        if(cmds[0] == 0x01 && cmds[01] ==0x01){
            return true;
        }else if(cmds[0] == 0x01 && cmds[01] ==0x02){
            return false;
        }else{
            throw new MesDataException("配置数据命令,请确认数据"+cmds[0] + "," +cmds[1] +"的正确性!");
        }
    }

    /**
     * 双字节转成int型
     * @param b
     * @return
     */
    private int getInt(byte[] b) {
        return (b[0] & 0xff)* 256 + (b[1]& 0xff);
    }

    /**
     * 双字节转成有符号int型
     * @param b
     * @return
     */
    private int getSignedInt(byte[] b){
        int i = ((b[0]&0x7f)*256 + (0xff & b[1]));
        if(b[0] < 0){
            return -i;
        }
        return i;


    }
    /**
     * 根据单位取压力值
     * @param val
     * @param unit
     * @return
     */
    private double getValueWithUnit(int val,String unit){
        double i = val;
        if("kpa".equals(unit)){
            i = i /100;
        }else{
            //0x00：cmh2o//0x02： hpa
            i = i /10;
        }
        return  i;
    }


    private String getUnit(int u) throws MesDataException{
        if(u == 0x00){
            return "cmh2o";
        }else if(u == 0x01){
            return "kpa";
        }else if(u == 0x02){
            return "hpa";
        }else{
            throw new RuntimeException("getUnit单位数据错误,请确认单位数据"+u+"的正确性!");
        }
    }

    private String getLanguage(int l) throws MesDataException{
        if(l == 0x00){
            return "中文";
        }else if(l == 0x01){
            return "英文";
        }else{
            throw new RuntimeException("getLanguage语言数据错误,请确认语言数据"+l+"的正确性!");
        }
    }

    private String getTemperature(int t){
        //0x00，关；其他：如0x25，37℃
        if(t == 0x00){
            return "关";
        }else{
            return String.valueOf(t);
        }
    }

    private  String getFiO2(int f){
        //0x00，关；其他：如0x32， 50%
        if(f == 0x00){
            return "关";
        }else{
            return String.valueOf(f);
        }
    }

    /**
     * 判断数据是否结束
     * @param a1
     * @param a2
     * @param a3
     * @return
     */
    private boolean isEnd(int a1, int a2, int a3) {
        //Logger.d("", (char) a1 + "," + (char) a2 + "," + (char) a3);
        if (a1 == 'E' && a2 == 'N' && a3 == 'D') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * BCD转成字符串
     * @param b
     * @return
     */
    private String bcdToString(byte b){
        StringBuffer sb = new StringBuffer();
        int h = ((b & 0xff) >> 4) + 48;
        sb.append ((char) h);
        int l = (b & 0x0f) + 48;
        sb.append ((char) l);
        return sb.toString () ;
    }
}
