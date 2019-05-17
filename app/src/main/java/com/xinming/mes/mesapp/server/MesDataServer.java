package com.xinming.mes.mesapp.server;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.xinming.mes.mesapp.entity.RespiratorData;
import com.xinming.mes.mesapp.mod.IModHandler;
import com.xinming.mes.mesapp.mod.Mod1Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Administrator on 2019/4/30.
 */

public class MesDataServer {

    private Context ctx = null;
    Map<String, View> views = null;
    ServerSocket server = null;

    public MesDataServer(Context cxt, Map<String, View> views) {
        this.ctx = cxt;
        this.views = views;
        try {
            server = new ServerSocket(8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        new Thread(new RecevieMsgTask()).start();
    }

    public void stop() {
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                Log.e("MesDataServer", e.getMessage());
            }
        }
    }

    private boolean isEnd(int a1, int a2, int a3) {
        Log.d("", (char) a1 + "," + (char) a2 + "," + (char) a3);
        if (a1 == 'E' && a2 == 'N' && a3 == 'D') {
            return true;
        } else {
            return false;
        }
    }

    private IModHandler getModHandler(String mod) {
        return new Mod1Handler(ctx, views.get(mod));
    }

    private class RecevieMsgTask implements Runnable {
        @Override
        public void run() {
            InputStream in = null;
            while (true) {
                try {
                    Socket client = server.accept();
                    Log.d("MesDataServer", "connected");
                    while (true) {
                        in = client.getInputStream();
                        //获取呼吸机数据
                        RespiratorData data = getRespiratorData(in);
                        //更新显示数据
                        getModHandler(data.getMode()).updateView(data);
                        //读到结尾
                        readEnd(in);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }  finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private RespiratorData getRespiratorData(InputStream in) throws IOException {
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
                return getRespiratorDataConfig(in);
            }else{
                return getRespiratorDataPackage(in);
            }
        }

        /**
         * 获取呼吸机配置数据
         * @param in
         * @return
         * @throws IOException
         */
        private RespiratorData getRespiratorDataConfig(InputStream in) throws IOException{
            //  1	当前模式
            int mode = in.read();
            RespiratorData data = new RespiratorData();
            String modeName = getMode(mode);
            data.setConfig(true);
            data.setMode(modeName);
            if("HFlow".equals(modeName) || "LFlow".equals(modeName)){
                //流量
                int flow = in.read();
                data.setFlow(flow);
                //温度
                int temperature = in.read();
                data.setTemperature(temperature);
                //氧气浓度
                int fio2 = in.read();
                data.setFio2(fio2);
            }else if("S/T".equals(modeName)){

            }else if("T".equals(modeName)){

            }else if("S".equals(modeName)){

            }else if("CPAP".equals(modeName)){

            }else if("APAP".equals(modeName)){

            }else if("PC".equals(modeName)){

            }else if("AutoS".equals(modeName)){

            }else if("MVAPS".equals(modeName)){

            }
            return data;
        }

        /**
         * 获取呼吸机数据
         * @param in
         * @return
         * @throws IOException
         */
        private RespiratorData getRespiratorDataPackage(InputStream in) throws IOException{
            //  1	当前模式
            int mode = in.read();
            RespiratorData data = new RespiratorData();
            String modeName = getMode(mode);
            data.setConfig(false);
            data.setMode(modeName);
            if("HFlow".equals(modeName) || "LFlow".equals(modeName)){
//                0--2	时、分、秒
                in.read(new byte[3]);
//                3--4	流量(-200 - +200) L/min
                byte[] flowBytes = new byte[2] ;
                in.read(flowBytes);
                data.setFlow(getShort(flowBytes));
//                5	温度
                int temperature = in.read();
                data.setTemperature(temperature);
//                6	氧气浓度0-100
                int fio2 = in.read();
                data.setFio2(fio2);
//                7	血氧0-100
                in.read(new byte[1]);
//                8	脉率0-255
                in.read(new byte[1]);

            }else if("CPAP".equals(modeName) || "APAP".equals(modeName)){
//                0--2	时、分、秒
                in.read(new byte[3]);
//                3--4	目标压力
                in.read(new byte[2]);
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
                in.read(new byte[1]);
//                15	脉率0-255
                in.read(new byte[1]);
//                16	氧气浓度0-100
                in.read(new byte[1]);
//                17	CPAP(40-400)4-40cmH2O测量值
                in.read(new byte[1]);
            }else{
//                0--2	时、分、秒
                in.read(new byte[3]);
//                3--4	压力(0-350)0-35cmH2O
                in.read(new byte[2]);
//                5--6	流量(-200 - +200) L/min
                in.read(new byte[2]);
//                7--8	潮气量(0-2500)mL
                in.read(new byte[2]);
//                9	漏气量(0-99) L/min
                in.read(new byte[1]);
//                10	分钟通气量(0-30)L/min
                in.read(new byte[1]);
//                11	呼吸频率(0-60)min
                in.read(new byte[1]);
//                12	吸呼比(1-99),1:0.1-9.9
                in.read(new byte[1]);
//                13	IPAP(40-250)4-25cmH2O 目标值
                in.read(new byte[1]);
//                14	EPAP(40-200)4-20cmH2O目标值
                in.read(new byte[1]);
//                15	呼吸事件对128求余后： 0，无；1低通气，2，呼吸暂停 4，鼾声5.中枢
////                主动/被动 ：主动>=128,被动<128;
                in.read(new byte[1]);
//                16	血氧0-100  0，无；1低通气，2，呼吸暂停 4，鼾声5.中枢
//                主动/被动 ：主动>=128,被动<128;
                in.read(new byte[1]);

//                17	脉率0-255
                in.read(new byte[1]);
//                18	氧气浓度0-100
                in.read(new byte[1]);
//                19--20	IPAP(40-400)4-40cmH2O测量值
                in.read(new byte[2]);
//                21	EPAP(40-200)4-20cmH2O
                in.read(new byte[1]);
            }
            return data;
        }

        /**
         * 读数据到结尾
         * @param in
         * @throws IOException
         */
        private void readEnd(InputStream in) throws IOException {
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
                default : throw new RuntimeException("模式数据错误,请确认数据传解析的输正确性");
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


        public short getShort(byte[] b) {
            return (short) (((b[1] << 8) | b[0] & 0xff));
        }



    }

}
