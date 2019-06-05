package com.xinming.mes.mesapp.io;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/6/4.
 */

public class MesInputStream extends InputStream {
    private InputStream in = null;
    private List<Integer> datas = new ArrayList<>();
    public MesInputStream(InputStream in){
        this.in = in;
    }
    @Override
    public int read() throws IOException {
        int i = in.read();
        datas.add(i);
        return i;
    }

    public List<Integer> getDatas() {
        return datas;
    }

    public void logDatas(){
        Logger.d("查看MES数据开始*****************************");
        Logger.d(datas);
        datas.clear();
        Logger.d("查看MES数据结束*****************************");
    }
}
