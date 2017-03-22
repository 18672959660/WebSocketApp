package com.caojing.websocketapp.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class MyServiceConnection implements ServiceConnection {

    private MyService.MyBinder binder;

    public MyService.MyBinder getBinder() {
        return binder;
    }

    public void setBinder(MyService.MyBinder binder) {
        this.binder = binder;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (MyService.MyBinder) service;   //该binder,需要在activity中声明。
        Log.d("learnservice", "绑定服务conn...");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d("learnservice", "解除绑定服务conn...");
    }
}
