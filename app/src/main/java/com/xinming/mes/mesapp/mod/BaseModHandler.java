package com.xinming.mes.mesapp.mod;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseModHandler implements IModHandler {
    protected Context ctx = null;
    protected View v = null;
    public BaseModHandler(Context ctx,View v){
        this.ctx = ctx;
        this.v = v;
    }
    protected View getContentView(){
        View contentView = ((ViewGroup) (((Activity)ctx).getWindow().getDecorView().findViewById(android.R.id.content))).getChildAt(0);
        return  contentView;
    }
}
