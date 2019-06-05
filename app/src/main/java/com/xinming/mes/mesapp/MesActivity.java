package com.xinming.mes.mesapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.exception.MesExceptionHandler;
import com.xinming.mes.mesapp.mod.HLModHandler;
import com.xinming.mes.mesapp.mod.IModHandler;
import com.xinming.mes.mesapp.mod.OtherModHandler;
import com.xinming.mes.mesapp.mod.PAPModHandler;
import com.xinming.mes.mesapp.server.MesDataServer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

public class MesActivity extends AppCompatActivity {

    MesDataServer dataServer = null;

    private Handler mHandler = new Handler();

    private int mPort = 8888;//default port

    /**
     * 点击启动按钮，启动服务
     * @param view
     */
    public void startService(View view){
        EditText tPort = findViewById(R.id.tPort);
        mPort = Integer.parseInt(tPort.getText().toString());
        setContentView(R.layout.activity_main);
        startService();
    }

    /**
     * 点击启动按钮，启动服务
     */
    public void startService(){
        try {
            dataServer = new MesDataServer(this,mPort);
            dataServer.start();
        } catch (IOException e) {
            MesApp.showAlert(this,"网络监听异常","请确认端口号是否被占用或者网络是否开启！");
        }

    }

    public MesDataServer getDataServer() {
        return dataServer;
    }

    public void setDataServer(MesDataServer dataServer) {
        this.dataServer = dataServer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d("onCreate start !");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// 隐藏ActionBar
        MesExceptionHandler handler = new MesExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(handler);
        setContentView(R.layout.mes_port_setting);
        initModHandlers();
        initSettingLayout();
        Logger.d("onCreate end !");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dataServer != null){
            dataServer.stop();
        }
        Logger.d("onDestroy !");
    }

    /**
     * 初始化设置界面
     */
    private void initSettingLayout(){
        TextView tIp = findViewById(R.id.tIP);
        tIp.setText(getIpAddressString());
    }



    /**
     * 初始化各模式的handler
     */
    private void initModHandlers(){
        View v1 = View.inflate(this, R.layout.mes_mod1_other_land, null);
        View v2 = View.inflate(this, R.layout.mes_mod2_main_land, null);
        View v3 = View.inflate(this, R.layout.mes_mod3_main_land, null);
        IModHandler hlModHandler = new HLModHandler(this,v1,mHandler);
        IModHandler papModHandler = new PAPModHandler(this,v3,mHandler);
        IModHandler otherModHandler = new OtherModHandler(this,v3,mHandler);

        Map<String,IModHandler> handlers = ((MesApp)getApplication()).getHandlers();
        handlers.put("S/T",otherModHandler);
        handlers.put("T",otherModHandler);
        handlers.put("S",otherModHandler);
        handlers.put("CPAP",papModHandler);
        handlers.put("APAP",papModHandler);
        handlers.put("PCV",otherModHandler);
        handlers.put("AutoS",otherModHandler);
        handlers.put("MVAPS",otherModHandler);
        handlers.put("HFlow",hlModHandler);
        handlers.put("LFlow",hlModHandler);
    }

    /**
     * 获取手机IP地址
     * @return
     */
    private String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "无网络连接,请在设置中打开";
    }



}
