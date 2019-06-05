package com.xinming.mes.mesapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
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
//        Logger.addLogAdapter(new DiskLogAdapter());
//        Logger.addLogAdapter(new AndroidLogAdapter() {
//            @Override public boolean isLoggable(int priority, String tag) {
//                return BuildConfig.DEBUG;
//            }
//        });
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)      //（可选）是否显示线程信息。 默认值为true
                .methodCount(12)               // （可选）要显示的方法行数。 默认2
                .methodOffset(0)               // （可选）设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
                //.logStrategy()  //（可选）更改要打印的日志策略。 默认LogCat
                .tag("MES-LOG")                  //（可选）每个日志的全局标记。 默认PRETTY_LOGGER（如上图）
                .build();
        //Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
       Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));

    }

    public Map<String,IModHandler> getHandlers(){
        return handlers;
    }


    public static void showAlert(final Context ctx, final String title,final String msg){

        ((MesActivity)ctx).runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Logger.d("showAlert start");
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                        .setTitle(title)//标题
                        .setMessage(msg)//内容
                        .setIcon(R.mipmap.ic_launcher);//图标

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(ctx instanceof MesActivity){
                            MesActivity act = (MesActivity)ctx;
                            act.startService();
                        }
                    }
                });

                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                });
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();

                dialog.show();
                Logger.d("showAlert end");
            }
        });

    }

}
