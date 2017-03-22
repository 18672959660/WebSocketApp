package com.caojing.websocketapp.entity;

import com.caojing.websocketapp.utils.GsonUtils;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class LoginInfo {

    public static  String id;
    public static String name;
public static final  String LoginSucsess="com.caojing.websocketapp.entity.loginsucess";
    public static final String Message="com.caojing.websocketapp.entity.message";
    public static final String error="com.caojing.websocketapp.entity.error";
    public static String getJsonUntis(String act, String mage) {
        SendMessageInfo messageInfo = new SendMessageInfo();
        messageInfo.setAct(act);
        messageInfo.setUid(LoginInfo.id);
        messageInfo.setUsertype("2");
        messageInfo.setName(LoginInfo.name);
        messageInfo.setMsg(mage);
        return GsonUtils.toJson(messageInfo);
    }
}
