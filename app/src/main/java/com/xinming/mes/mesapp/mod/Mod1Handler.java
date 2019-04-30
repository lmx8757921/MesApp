package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinming.mes.mesapp.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2019/4/30.
 */

public class Mod1Handler implements IModHandler {
    private Context ctx = null;
    private View v1 = null;
    public Mod1Handler(Context ctx,View v1){
        this.ctx = ctx;
        this.v1 = v1;
    }
    @Override
    public void updateView() {
        ((Activity)ctx).runOnUiThread(new Runnable() {
            public void run() {
                if(getContentView() != v1){
                    Log.d("updateView","If first to setContentView");
                    ((Activity)ctx).setContentView(v1);
                }else{
                    Log.d("updateView","If second not set");
                }
            }
        });
        v1.post(new Runnable() {
            @Override
            public void run() {
                Log.d("Mod1Handler","run start");
                Calendar c = Calendar.getInstance();//
                int mMinute = c.get(Calendar.MINUTE);//分
                int mSec = c.get(Calendar.SECOND);
                TextView tx = v1.findViewById(R.id.area2);
                tx.setText(mMinute+":"+mSec);
            }
        });
    }

    private View getContentView(){
        View contentView = ((ViewGroup) (((Activity)ctx).getWindow().getDecorView().findViewById(android.R.id.content))).getChildAt(0);
        return  contentView;
    }
}
