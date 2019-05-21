package com.xinming.mes.mesapp.server;

import android.content.Context;
import android.util.Log;

import com.xinming.mes.mesapp.MesApp;
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

    public MesDataServer(Context cxt) {
        this.ctx = cxt;
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
            while (true) {
                try {
                    Socket client = server.accept();
                    Log.d("MesDataServer", "connected");
                    in = client.getInputStream();
                    //接收数据并显示到View
                    service.execRespiratorData(in);

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

    }

}
