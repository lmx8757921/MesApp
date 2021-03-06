package com.xinming.mes.mesapp.exception;


import android.content.Context;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.MesApp;

/**
 * Created by Administrator on 2019/5/5.
 */

public class MesExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context = null;

    public MesExceptionHandler(Context context){
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
       // Toast.makeText(this.context,"程序异常",Toast.LENGTH_LONG).show();
//        new Thread() {
//            @Override
//            public void run() {
//                //Looper.prepare();
//
//                //Toast.makeText(context,"程序异常",Toast.LENGTH_LONG).show();
//                //Looper.loop();
//                //Logger.e("System error",throwable.getMessage());
//
//            }
//        }.start();

        Logger.e("程序异常:"+throwable.getMessage());
        MesApp.showAlert(context,"程序异常",throwable.getMessage());
    }
}
