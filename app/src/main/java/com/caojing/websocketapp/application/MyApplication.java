package com.caojing.websocketapp.application;

import android.app.Application;

import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;


/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.initialize(this);
        JUtils.setDebug(true, "日志测试");
        EmojiManager.install(new IosEmojiProvider());
        org.xutils.x.Ext.init(this);
        org.xutils.x.Ext.setDebug(true); // 是否输出debug日志, 开启debug会影响性能.
        registerActivityLifecycleCallbacks(JActivityManager.getActivityLifecycleCallbacks());
    }
}
