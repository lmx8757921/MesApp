package com.xinming.mes.mesapp;

import android.app.Application;

import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.exception.MesExceptionHandler;
import com.xinming.mes.mesapp.mod.IModHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/16.
 */

public class MesApp extends Application {

    private Map<String,IModHandler> handlers = new HashMap<>();

    public void onCreate(){
        super.onCreate();
        Logger.addLogAdapter(new DiskLogAdapter());
        MesExceptionHandler handler = new MesExceptionHandler(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    public Map<String,IModHandler> getHandlers(){
        return handlers;
    }

}
