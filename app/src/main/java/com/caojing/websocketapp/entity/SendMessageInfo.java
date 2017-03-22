package com.caojing.websocketapp.entity;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class SendMessageInfo {


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * id : 1_1
     * name : 小明
     * act : sign
     * msg : 测试
     */

    private String uid;
    private String name;
    private String act;
    private String msg;

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    private String usertype;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
