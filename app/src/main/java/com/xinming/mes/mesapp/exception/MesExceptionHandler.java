package com.xinming.mes.mesapp.exception;


import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by Administrator on 2019/5/5.
 */

public class MesExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context = null;

    public MesExceptionHandler(Context context){
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
       // Toast.makeText(this.context,"程序异常",Toast.LENGTH_LONG).show();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context,"程序异常",Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
    }
}
