package com.simon.appmanager_vip;


import android.app.Application;
import android.util.Log;

import org.xutils.x;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;

@SuppressWarnings("all")
public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = (MyApplication) this.getApplicationContext();

        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        JMessageClient.setDebugMode(true);
        // JMessageClient.init(this);
        // 是否支持漫游
        JMessageClient.init(this,true);
        // 注册接收事件
        JMessageClient.registerEventReceiver(this);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    /**
     * 接收通知栏点击事件
     *
     * @param event
     */
    public void onEvent(NotificationClickEvent event) {
        Message message = event.getMessage();
        TextContent textContent = (TextContent) message.getContent();
        Log.i("Simon", "接收通知栏点击事件：" + textContent.getText());
//        Intent notificationIntent = new Intent(mContext, ChatActivity.class);
//        startActivity(notificationIntent);//自定义跳转到指定页面
    }
}
