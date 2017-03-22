package com.caojing.websocketapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caojing.websocketapp.activity.BaseActivity;
import com.caojing.websocketapp.activity.LoginActivity;
import com.caojing.websocketapp.adapter.FriendListAdapter;
import com.caojing.websocketapp.adapter.MessageAdapter;
import com.caojing.websocketapp.db.MessageDB;
import com.caojing.websocketapp.entity.MessageDBInfo;
import com.caojing.websocketapp.service.MyService;
import com.caojing.websocketapp.service.MyServiceConnection;
import com.caojing.websocketapp.utils.MyUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.utils.JUtils;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;

import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String wsurl = "ws://192.168.1.220:29000";
    public static final String TAG = "com.caojing.websocketapp.MainActivity";
    private EmojiEditText emojiEditText;
    private Button mSend;

    private Intent intent;
    private String fromuserName, fromuserId;//消息发送人名稱，id
    private EasyRecyclerView esfrecyclerView;
    private MessageAdapter adapter;
    private List<MessageDBInfo> list;
    private MyBroadcastReceiver receiver;
    public static String AllfromuserId = "";
    private TextView message_name;
    private MyServiceConnection serviceConnection;
    private EmojiPopup emojiPopup;
    private ImageView btn_emoji;
    private LinearLayout root_view;

    /**
     * 收到消息廣播更新頁面
     */
    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MyUtils.UpdateMessage.equals(intent.getAction())) {
                MessageDBInfo dbInfo = (MessageDBInfo) intent.getSerializableExtra("MessageDBInfo");
                adapter.add(dbInfo);
                adapter.notifyDataSetChanged();
                esfrecyclerView.scrollToPosition(adapter.getCount() - 1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromuserName = getIntent().getStringExtra("fromuserName");
        fromuserId = getIntent().getStringExtra("fromuserId");
        AllfromuserId = fromuserId;
        receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyUtils.UpdateMessage);
        registerReceiver(receiver, intentFilter);
        intent = new Intent(this, MyService.class);
        serviceConnection = new MyServiceConnection();
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        bindObject();
        initReCyclerView();
    }

    /**
     * 绑定控件
     */
    private void bindObject() {
        message_name = (TextView) findViewById(R.id.message_name);
        message_name.setText(fromuserName);
        esfrecyclerView = (EasyRecyclerView) findViewById(R.id.esfrecyclerView);
        emojiEditText = (EmojiEditText) findViewById(R.id.emojiEditText);
        btn_emoji = (ImageView) findViewById(R.id.btn_emoji);
        btn_emoji.setOnClickListener(this);
        mSend = (Button) findViewById(R.id.btn_send);
        mSend.setOnClickListener(this);
        root_view = (LinearLayout) findViewById(R.id.root_view);
        emojiPopup = EmojiPopup.Builder.fromRootView(root_view).setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
            @Override
            public void onKeyboardClose() {
                if (emojiPopup.isShowing()) {
                    emojiPopup.dismiss();
                }
            }
        })
                .build(emojiEditText);
    }

    public void initReCyclerView() {
        esfrecyclerView = (EasyRecyclerView) findViewById(R.id.esfrecyclerView);
        esfrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, 0, 0, 0);
        itemDecoration.setDrawLastItem(false);//设置最后一行item是否有分割线,默认true.
        esfrecyclerView.addItemDecoration(itemDecoration);
        adapter = new MessageAdapter(this);
        esfrecyclerView.setAdapterWithProgress(adapter);
        esfrecyclerView.setRefreshingColorResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        list = MessageDB.getIntance().loadMessage(fromuserId);
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
        esfrecyclerView.scrollToPosition(adapter.getCount() - 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                serviceConnection.getBinder().sendTextMessage(emojiEditText.getText().toString(), fromuserName, fromuserId);
                emojiEditText.setText("");
                break;
            case R.id.btn_emoji:
                if (emojiPopup.isShowing()) {
                    emojiPopup.dismiss();
                } else {
                    emojiPopup.toggle();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        unregisterReceiver(receiver);
    }
}
