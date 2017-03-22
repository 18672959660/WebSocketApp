package com.caojing.websocketapp.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.caojing.websocketapp.MainActivity;
import com.caojing.websocketapp.R;
import com.caojing.websocketapp.entity.FriendListInfo;
import com.caojing.websocketapp.entity.LoginInfo;
import com.caojing.websocketapp.http.HttpBase;
import com.caojing.websocketapp.service.MyService;
import com.jude.utils.JUtils;

import java.io.Serializable;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText email;
    private EditText password;
    private Button email_sign_in_button;
    private RadioGroup raidgroup_id;
    private RadioButton radiobutton1, radiobutton2;
    private MyService.MyBinder binder;
    private MyBroadcastReceiver receiver;
    private List<FriendListInfo.UsersListBean> users_list;
    /**
     * 接收登录返回的广播
     */
    class MyBroadcastReceiver extends  BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (LoginInfo.LoginSucsess.equals(intent.getAction())){
            //登录成功，返回最近联系人
            email_sign_in_button.setText("登录");
            email_sign_in_button.setEnabled(true);
            users_list= (List<FriendListInfo.UsersListBean>) intent.getSerializableExtra("users_list");
            Intent intent1 = new Intent(getBaseContext(), FriendListActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("users_list", (Serializable) users_list);
            startActivity(intent1);
            finish();
        }else if(LoginInfo.error.equals(intent.getAction())){
            //登录失败，返回错误信息
            email_sign_in_button.setText("登录");
            email_sign_in_button.setEnabled(true);
            JUtils.Toast(intent.getStringExtra("content"));
        }
    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        receiver=new MyBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(LoginInfo.LoginSucsess);
        intentFilter.addAction(LoginInfo.error);
        registerReceiver(receiver,intentFilter);
        initView();
    }

    private void initView() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
        email_sign_in_button.setOnClickListener(this);
        raidgroup_id = (RadioGroup) findViewById(R.id.raidgroup_id);
        radiobutton1 = (RadioButton) findViewById(R.id.radiobutton1);
        radiobutton2 = (RadioButton) findViewById(R.id.radiobutton2);
        raidgroup_id.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                PermissionGen.needPermission(LoginActivity.this, 100,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        });
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.raidgroup_id:
                if (checkedId == R.id.radiobutton1) {
                    HttpBase.httpurl = HttpBase.wsurl;
                } else if (checkedId == R.id.radiobutton2) {
                    HttpBase.httpurl = HttpBase.Testwsurl;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 权限请求成功
     */
    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        LoginInfo.id=email.getText().toString();
        LoginInfo.name=password.getText().toString();

        email_sign_in_button.setText("登录中...");
        email_sign_in_button.setEnabled(false);
        Intent intent = new Intent(LoginActivity.this, MyService.class);
        startService(intent);
    }

    /**
     * 权限请求失败
     */
    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        JUtils.Toast("获取权限失败,请在权限管理中手动打开电话或读写权限");
    }
}
