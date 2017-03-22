package com.caojing.websocketapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.caojing.websocketapp.MainActivity;
import com.caojing.websocketapp.R;
import com.caojing.websocketapp.adapter.FriendListAdapter;
import com.caojing.websocketapp.db.MessageDB;
import com.caojing.websocketapp.entity.FriendDBInfo;
import com.caojing.websocketapp.entity.FriendListInfo;
import com.caojing.websocketapp.service.MyService;
import com.caojing.websocketapp.service.MyServiceConnection;
import com.caojing.websocketapp.utils.MyUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.utils.JUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 最近联系好友列表
 */
public class FriendListActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener, View.OnClickListener {


    private EasyRecyclerView esfrecyclerView;
    private FriendListAdapter adapter;
    private List<FriendDBInfo> list;
    public static final String TAG = "com.caojing.websocketapp.activity.FriendListActivity";
    private TextView message_name;
    private MyBroadcastReceiver receiver;
    private TextView text_exit;
    private MyServiceConnection serviceConnection;

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            list = MessageDB.getIntance().loadFriend();
            adapter.clear();
            adapter.addAll(list);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        list = MessageDB.getIntance().loadFriend();
        serviceConnection=new MyServiceConnection();
        Intent  intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        message_name = (TextView) findViewById(R.id.message_name);
        message_name.setText("最近联系人");
        text_exit = (TextView) findViewById(R.id.text_exit);
        text_exit.setText("退出");
        text_exit.setOnClickListener(this);
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyUtils.UpdateFriend);
        registerReceiver(receiver, filter);
        initReCyclerView();
    }

    public void initReCyclerView() {
        esfrecyclerView = (EasyRecyclerView) findViewById(R.id.esfrecyclerView);
        esfrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, 1, 0, 0);
        itemDecoration.setDrawLastItem(false);//设置最后一行item是否有分割线,默认true.
        esfrecyclerView.addItemDecoration(itemDecoration);
        adapter = new FriendListAdapter(this);
        esfrecyclerView.setAdapterWithProgress(adapter);
        esfrecyclerView.setRefreshingColorResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        adapter.setOnItemClickListener(this);
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromuserName", list.get(position).getUserName());
        intent.putExtra("fromuserId", list.get(position).getUserId());
        startActivity(intent);
    }

    @Override
    public void BackAction(View view) {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unbindService(serviceConnection);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list = MessageDB.getIntance().loadFriend();
        adapter.clear();
        adapter.addAll(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_exit:
                serviceConnection.getBinder().closeConnect();
                finish();
                break;
        }
    }
}
