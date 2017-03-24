package com.caojing.websocketapp.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 最近联系人数据库实例
 * Created by Administrator on 2017/3/10 0010.
 */

@Table(name = "friend")
public class FriendDBInfo implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "userId")
    private String userId;  //联系人id
    @Column(name = "useName")
    private String userName;  //联系人姓名
    @Column(name = "msg")
    private String msg;   //收到的消息
    @Column(name = "newMsg")
    private String newMsg;  //新消息提醒
    @Column(name = "time")
    private String time;//时间

    public FriendDBInfo() {

    }

    public FriendDBInfo(String userId, String userName, String msg, String newMsg, String time) {
        this.userId = userId;
        this.userName = userName;
        this.msg = msg;
        this.newMsg = newMsg;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNewMsg() {
        return newMsg;
    }

    public void setNewMsg(String newMsg) {
        this.newMsg = newMsg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FriendDBInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", msg='" + msg + '\'' +
                ", newMsg=" + newMsg +
                '}';
    }
}
