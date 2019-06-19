package com.xinming.mes.mesapp.server;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.BuildConfig;
import com.xinming.mes.mesapp.MesApp;
import com.xinming.mes.mesapp.exception.MesDataException;
import com.xinming.mes.mesapp.io.MesInputStream;
import com.xinming.mes.mesapp.service.DataExecuterService;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2019/4/30.
 */

public class MesDataServer {

    //activity 上下文
    private Context ctx = null;
    //TCP server
    ServerSocket server = null;

    public MesDataServer(Context cxt,int port) throws IOException{
        this.ctx = cxt;
        server = new ServerSocket(port);

    }

    public void start() {
        new Thread(new RecevieMsgTask()).start();
    }

    public void stop() {
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                Logger.e( e.getMessage());
            }
        }
    }

    /**
     * 接收呼吸机数据任务类
     */
    private class RecevieMsgTask implements Runnable {

        /**
         * 核心处理数据方法
         */
        @Override
        public void run() {
            InputStream in = null;
            DataExecuterService service = new DataExecuterService( ((MesApp)ctx.getApplicationContext()).getHandlers());
            try {
                while (true) {
                        Socket client = server.accept();
                        Logger.d("connected");
                        in = client.getInputStream();
                        if(BuildConfig.DEBUG){
                            in = new MesInputStream(in);
                        }

    //                    int  a = 0;
    //                    while(true){
    //                        a = in.read();
    //                        Log.d("RecevieMsgTask",String.valueOf(a));
    //                        if(a == -1){
    //                            break;
    //                        }
    //                    }
                        //接收数据并显示到View
                        service.execRespiratorData(in);
    //                    in.close();
                       // client.close();
                }
            } catch (IOException ie) {
                //throw new RuntimeException(e);
                Logger.e("网络异常："+ie.getMessage());
                MesApp.showAlert(ctx,"网络异常：",ie.getMessage());
                Logger.d("网络异常日志结束******");
            } catch(MesDataException me){
                Logger.e("MES数据解析错误："+me.getMessage());
                MesApp.showAlert(ctx,"MES数据解析错误：",me.getMessage());
                Logger.d("MES数据解析错误日志结束######");
            }
            catch (Exception e){
                Logger.e("未预期错误："+e.getMessage());
                MesApp.showAlert(ctx,"未预期错误：","请联系管理员!");
            } finally{
                close(in);
            }
        }

        private void close(InputStream in){
            if (in != null) {
                try {
                    in.close();
                    stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
