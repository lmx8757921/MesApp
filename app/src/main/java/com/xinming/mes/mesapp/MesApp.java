package com.xinming.mes.mesapp;

import android.app.Application;

import com.xinming.mes.mesapp.exception.MesExceptionHandler;

/**
 * Created by Administrator on 2019/5/16.
 */

public class MesApp extends Application {

    public void onCreate(){
        super.onCreate();
        MesExceptionHandler handler = new MesExceptionHandler(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

}
