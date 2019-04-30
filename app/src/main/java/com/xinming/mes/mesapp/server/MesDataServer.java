package com.xinming.mes.mesapp.server;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.xinming.mes.mesapp.mod.IModHandler;
import com.xinming.mes.mesapp.mod.Mod1Handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Administrator on 2019/4/30.
 */

public class MesDataServer{

    private Context ctx = null;
    Map<String,View> views = null;
    ProgressBar pb = null;
    ServerSocket server = null;
    private boolean hasRun = false;
    public MesDataServer(Context cxt, Map<String,View> views,ProgressBar pb){
        this.ctx = cxt;
        this.views = views;
        this.pb = pb;
        try {
            server = new ServerSocket(8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        new Thread(new RecevieMsgTask()).start();
    }

    public void stop(){
        if(server !=null){
            try {
                server.close();
            } catch (IOException e) {
                Log.e("MesDataServer",e.getMessage());
            }
        }
    }

    private boolean isEnd(int a1,int a2,int a3){
        Log.d("",(char) a1 + ","+(char) a2 + ","+(char) a3);
        if(a1=='E' && a2 == 'N' && a3=='D'){
            return true;
        }else{
            return false;
        }
    }
    private IModHandler getModHandler(String mod){
        return new Mod1Handler(ctx,views.get(mod));
    }

    private class RecevieMsgTask implements Runnable{
        @Override
        public void run() {
            InputStream in = null;
            while(true){
                try  {
                    Socket client = server.accept();
                    Log.d("MesDataServer","connected");
                    if(hasRun){
                        continue;
                    }
                    hasRun = true;
                    while(true) {
                        in = client.getInputStream();
                        byte[] bts = new byte[255];
                        int a1,a2,a3;
                        a1=in.read();
                        a2=in.read();
                        while(true){
                            a3 = in.read();
                            if(isEnd(a1,a2,a3)){
                                getModHandler("").updateView();
                                break;
                            }
                            a1 =a2;
                            a2 =a3;
                            Log.d("MesDataServer","reading");
                        }
                    }

                }catch (IOException e){
                    hasRun = false;
                    throw new RuntimeException(e);
                }finally {
                    if(in != null){
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
