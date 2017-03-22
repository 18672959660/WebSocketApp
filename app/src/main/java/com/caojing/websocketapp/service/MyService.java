package com.caojing.websocketapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.caojing.websocketapp.MainActivity;
import com.caojing.websocketapp.R;
import com.caojing.websocketapp.activity.FriendListActivity;
import com.caojing.websocketapp.db.MessageDB;
import com.caojing.websocketapp.entity.FriendDBInfo;
import com.caojing.websocketapp.entity.FriendListInfo;
import com.caojing.websocketapp.entity.LoginInfo;
import com.caojing.websocketapp.entity.MessageDBInfo;
import com.caojing.websocketapp.entity.SendMessageInfo;
import com.caojing.websocketapp.http.HttpBase;
import com.caojing.websocketapp.utils.GsonUtils;
import com.caojing.websocketapp.utils.MyUtils;
import com.google.gson.Gson;
import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MyService extends Service {
    private static final String TAG = "MyService";
    private WebSocketConnection mConnect = new WebSocketConnection();
    private int num;
    private boolean isExit;//true 退出操作

    public MyService() {
    }

    public class MyBinder extends Binder {
        public void sendTextMessage(String text, String toname, String toid) {
            if (mConnect.isConnected()) {
                Log.i(TAG, "發送消息");
                Log.i(TAG, LoginInfo.getJsonUntis("touser_" + toid + "_" + toname, text));
                mConnect.sendTextMessage(LoginInfo.getJsonUntis("touser_" + toid + "_" + toname, text));
            }
        }

        public void closeConnect() {
            if (mConnect.isConnected()) {
                isExit=true;
                mConnect.disconnect();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "绑定服务...");
        return new MyBinder();
    }

    /**
     * websocket连接，接收服务器消息
     */
    private void connect() {
        Log.i(TAG, "ws connect....");
        try {
            if (!mConnect.isConnected()) {
                mConnect.connect(HttpBase.httpurl, new WebSocketHandler() {
                    @Override
                    public void onOpen() {
//                        Toast.makeText(getApplicationContext(), "連接成功" + HttpBase.httpurl, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Status:Connect to " + HttpBase.httpurl);
                        mConnect.sendTextMessage(LoginInfo.getJsonUntis("sign", "测试"));

                    }

                    @Override
                    public void onTextMessage(String payload) {
                        Log.i(TAG, payload);
                        FriendListInfo info = GsonUtils.toBean(payload, FriendListInfo.class);
                        if (info.getCode() == 1) {
                            for (int i = 0; i < info.getUsers_list().size(); i++) {
                                FriendDBInfo dbInfo = new FriendDBInfo(info.getUsers_list().get(i).getUid(), info.getUsers_list().get(i).getNickname(), "", "0");
                                MessageDB.getIntance().svaeFriend(dbInfo);
                            }
                            //返回最近联系人，登陆成功,发送广播给登录页跳转到首页
                            Intent intent = new Intent(LoginInfo.LoginSucsess);
                            intent.putExtra("users_list", (Serializable) info.getUsers_list());
                            sendBroadcast(intent);
                        } else if (info.getCode() == 7) {
                            //返回收到的消息
                            num++;
                            if (!LoginInfo.id.equals(info.getFromuser().getUid())) {
                                //收到其他人发送的消息
                                FriendDBInfo dbInfo = new FriendDBInfo(info.getFromuser().getUid(), info.getFromuser().getNickname(), info.getMsg().getContent(), "0");
                                MessageDB.getIntance().svaeFriend(dbInfo);
                                if (JUtils.isBackground()) {
                                    //程序在后台的时候弹出通知提示
                                    processCustomMessage(info);
                                } else {
                                    if (FriendListActivity.TAG.equals(JActivityManager.getCurrentActivityName())) {
                                        //最近联系人页面在前台的时候
                                        Intent intent = new Intent(MyUtils.UpdateFriend);
                                        intent.putExtra("fromuid", info.getFromuser().getUid());
                                        sendBroadcast(intent);
                                    } else if (MainActivity.TAG.equals(JActivityManager.getCurrentActivityName())) {
                                        //聊天界面在前台的时候

                                    } else {
                                        Toast.makeText(getApplicationContext(), "收到消息 " + payload, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                //自己发送的消息
                                FriendDBInfo dbInfo = new FriendDBInfo(info.getTouser().getUid(), info.getTouser().getNickname(), info.getMsg().getContent(), "0");
                                MessageDB.getIntance().svaeFriend(dbInfo);
                            }
                            MessageDBInfo dbInfo = new MessageDBInfo(info.getFromuser().getUid(),
                                    info.getFromuser().getNickname(), info.getMsg().getContent(),
                                    "text",
                                    info.getTouser().getUid(), info.getTouser().getNickname(),
                                    info.getMsg().getTime());
                            MessageDB.getIntance().savePerson(dbInfo);
                            //如果收到的消息和當前聊天的客戶id相同，或者和當前發送消息的客戶id相同就發送廣播更新聊天UI
                            if (MainActivity.AllfromuserId.equals(info.getFromuser().getUid()) || MainActivity.AllfromuserId.equals(info.getTouser().getUid())) {
                                Intent intent = new Intent(MyUtils.UpdateMessage);
                                intent.putExtra("MessageDBInfo", (Serializable) dbInfo);
                                sendBroadcast(intent);
                            }
                        } else {
                            //错误信息，登录失败
                            Intent intent = new Intent(LoginInfo.error);
                            intent.putExtra("content", info.getContent());
                            sendBroadcast(intent);
                        }
                    }


                    @Override
                    public void onClose(int code, String reason) {
                        Log.i(TAG, "Connection lost..");
                        if (!isExit){
                            connect();
                        }else{
                            isExit=false;
                        }
                        Intent intent = new Intent(LoginInfo.error);
                        intent.putExtra("content", "连接失败，原因" + reason);
                        sendBroadcast(intent);
                    }
                });
            }

        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws JSONException
     */
    private void processCustomMessage(FriendListInfo info) {
        Log.e("test", "来一个自定义消息");
        String title = info.getFromuser().getNickname();
        String message = info.getMsg().getContent();
        NotificationManager nm = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        builder.setDefaults(Notification.DEFAULT_ALL);
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), MainActivity.class);
        intent.putExtra("fromuserName", info.getFromuser().getNickname());
        intent.putExtra("fromuserId", info.getFromuser().getUid());
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        nm.notify(num, builder.build());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mConnect.disconnect();
    }
}
