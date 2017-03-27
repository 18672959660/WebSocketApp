package com.caojing.websocketapp.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import com.caojing.websocketapp.receiver.NetChangeReceiver;
import com.caojing.websocketapp.utils.MyUtils;
import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;


/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class MyApplication extends Application {
//    private NetChangeReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.initialize(this);
        JUtils.setDebug(true, "日志测试");
        EmojiManager.install(new IosEmojiProvider());
        org.xutils.x.Ext.init(this);
        org.xutils.x.Ext.setDebug(true); // 是否输出debug日志, 开启debug会影响性能.
        registerActivityLifecycleCallbacks(JActivityManager.getActivityLifecycleCallbacks());
        registerConnectivityNetworkMonitorForAPI21AndUp();

    }

    private void registerConnectivityNetworkMonitorForAPI21AndUp() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {
                    /**
                     * @param network
                     */
                    @Override
                    public void onAvailable(Network network) {
                        //网络恢复
                        sendBroadcast(
                                getConnectivityIntent(false)
                        );

                    }

                    /**
                     * @param network
                     */
                    @Override
                    public void onLost(Network network) {
                        //网络关闭
                        sendBroadcast(
                                getConnectivityIntent(true)
                        );

                    }
                }

        );

    }

    /**
     * @param noConnection
     * @return
     */
    private Intent getConnectivityIntent(boolean noConnection) {
        Intent intent = new Intent();
        intent.setAction(MyUtils.CONNECTIVITY_CHANGE);
        intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, noConnection);
        return intent;
    }
}
