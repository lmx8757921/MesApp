package com.xinming.mes.mesapp.service;

import android.util.Log;

import com.xinming.mes.mesapp.MesApp;
import com.xinming.mes.mesapp.entity.RespiratorConfigData;
import com.xinming.mes.mesapp.entity.RespiratorConfigDataVO;
import com.xinming.mes.mesapp.entity.RespiratorData;
import com.xinming.mes.mesapp.entity.RespiratorDataVO;
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
    public void execRespiratorData(InputStream in) throws IOException {
//            6	同步字
        in.read(new byte[6]);
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
//            1	呼吸机型号
        in.read(new byte[1]);
//            6	同步字
        in.read(new byte[6]);
//            2	信息字数
        in.read(new byte[2]);
//            1	目标地址
        in.read(new byte[1]);
//            1	源地址
        in.read(new byte[1]);
//            2	命令码
        in.read(new byte[2]);
//            2	命令码参数
        in.read(new byte[2]);
//            9	呼吸机序列号
        in.read(new byte[9]);
        //配置数据下多读以下几个数据
        if(isConfig){
            //            1	呼吸机型号
            in.read(new byte[1]);
            //            11	呼吸机软件版本
            in.read(new byte[11]);
            //            7	当前时间
            in.read(new byte[7]);
            //            6	累计运行时间
            in.read(new byte[6]);
            //获取呼吸机数据
            RespiratorConfigDataVO cfgVo = new RespiratorConfigDataVO();
            setRespiratorDataConfig(in,cfgVo);
            //更新显示配置数据
            getModHandler(configData.getMode()).updateViewWithConfigData(cfgVo);
        }else{
            RespiratorData data = new RespiratorData();
            RespiratorConfigDataVO cfgVo = new RespiratorConfigDataVO();
            RespiratorDataVO vo = new RespiratorDataVO();
            setRespiratorDataPackage(in,data,vo,cfgVo);
            //更新显示数据
            getModHandler(data.getMode()).updateViewWithPackageData(vo,cfgVo);
        }

        //读到流的结尾
        readToEnd(in);
    }


    /**
     * 获取呼吸机配置数据,分别设置到VO和原生实体
     * @param in
     * @return
     * @throws IOException
     */
    private  void setRespiratorDataConfig(InputStream in,RespiratorConfigDataVO vo) throws IOException{
        //  1	当前模式
        int mode = in.read();
        configData = new RespiratorConfigData();
        String modeName = getMode(mode);
        configData.setMode(modeName);
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
            double ipap =  getValueWithUnit(getShort(bIpaps),configData.getUnit());
            configData.setIpap(ipap);
            vo.setIpap(String.valueOf(ipap));
        }

        if(bEpaps != null){
            double epap =  getValueWithUnit(getShort(bEpaps),configData.getUnit());
            configData.setEpap(epap);
            vo.setEpap(String.valueOf(epap));
        }

        if(bCpaps != null){
            double cpap =  getValueWithUnit(getShort(bCpaps),configData.getUnit());
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
    private void setConfigData(InputStream in,RespiratorConfigDataVO vo) throws IOException{

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
    private  void setRespiratorDataPackage(InputStream in,RespiratorData data,RespiratorDataVO vo,RespiratorConfigDataVO cfgVo) throws IOException{
        //  1	当前模式
        int mode = in.read();
        String modeName = getMode(mode);
        data.setMode(modeName);
        if("HFlow".equals(modeName) || "LFlow".equals(modeName)){
//                0--2	时、分、秒
            in.read(new byte[3]);
//                3--4	流量(-200 - +200) L/min
            byte[] flowBytes = new byte[2] ;
            in.read(flowBytes);
            data.setFlow(getShort(flowBytes));
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

        }else if("CPAP".equals(modeName) || "APAP".equals(modeName)){
//                0--2	时、分、秒
            in.read(new byte[3]);
//                3--4	目标压力
           byte[] bDps = new byte[2];
            in.read(bDps);
            if("APAP".equals(modeName)){
                double dps =  getValueWithUnit(getShort(bDps),configData.getUnit());
                configData.setCpap(dps);
                cfgVo.setCpap(String.valueOf(dps));
            }
//                5--6	当前压力
            in.read(new byte[2]);
//                7--8	当前流量
            in.read(new byte[2]);
//                9	呼吸事件0，无；1低通气，2，呼吸暂停 4，鼾声5.中枢
            in.read(new byte[1]);
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

        }else{
//                0--2	时、分、秒
            in.read(new byte[3]);
//                3--4	压力(0-350)0-35cmH2O
            in.read(new byte[2]);
//                5--6	流量(-200 - +200) L/min
            in.read(new byte[2]);
//                7--8	潮气量(0-2500)mL
            byte[] bmls = new byte[2];
            in.read(bmls);
            data.setMl(getShort(bmls));
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
            double ipap =  getValueWithUnit(getShort(bIpaps),configData.getUnit());
            data.setIpap(ipap);
            vo.setIpap(String.valueOf(ipap));
//            EPAP(2)
//                21	EPAP(40-200)4-20cmH2O
            byte[] bEpaps = new byte[2];
            in.read(bEpaps);
            double epap =  getValueWithUnit(getShort(bEpaps),configData.getUnit());
            data.setEpap(epap);
            vo.setEpap(String.valueOf(epap));

        }
    }

    /**
     * 获取不同模式下的业务处理Handler
     * @param modeName
     * @return
     */
    private IModHandler getModHandler(String modeName) {
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
                break;
            }
            a1 = a2;
            a2 = a3;
            Log.d("MesDataServer", "reading");
        }
    }

    /**
     * 获取呼吸机工作模式
     * @param m
     * @return
     */
    private String getMode(int m){
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
            default : throw new RuntimeException("getMode模式数据错误,请确认数据传解析的输正确性!");
        }
    }

    /**
     * 判断是否是配置数据 true:是 false:单包数据
     * @param cmds
     * @return
     */
    private  boolean  isConfig(byte[] cmds){
        if(cmds[0] == 0x01 && cmds[01] ==0x01){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 双字节转成short型
     * @param b
     * @return
     */
    private short getShort(byte[] b) {
        return (short) (((b[1] << 8) | b[0] & 0xff));
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


    private String getUnit(int u){
        if(u == 0x00){
            return "kpa";
        }else if(u == 0x01){
            return "cmH2o";
        }else if(u == 0x02){
            return "hpa";
        }else{
            throw new RuntimeException("getUnit模式数据错误,请确认数据传解析的输正确性!");
        }
    }

    private String getLanguage(int l){
        if(l == 0x00){
            return "中文";
        }else if(l == 0x01){
            return "英文";
        }else{
            throw new RuntimeException("getLanguage模式数据错误,请确认数据传解析的输正确性!");
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
        Log.d("", (char) a1 + "," + (char) a2 + "," + (char) a3);
        if (a1 == 'E' && a2 == 'N' && a3 == 'D') {
            return true;
        } else {
            return false;
        }
    }
}
