package com.caojing.websocketapp.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 消息數據實例
 * Created by Administrator on 2017/3/6 0006.
 */
@Table(name = "message")
public class MessageDBInfo implements Serializable{
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "fromuid")
    private String fromuid;//发送者id
    @Column(name = "fromname")
    private String fromname;//发送者名称
    @Column(name = "msg")
    private String msg;//消息内容
    @Column(name = "msgtype")
    private String msgtype;//消息类型
    @Column(name = "touid")
    private String touid;//接收者id
    @Column(name = "toname")
    private String toname;//接收者名称
    @Column(name = "time")
    private String time;//时间

    public MessageDBInfo() {
    }

    public MessageDBInfo(String fromuid, String fromname, String msg, String msgtype, String touid, String toname, String time) {
        this.fromuid = fromuid;
        this.fromname = fromname;
        this.msg = msg;
        this.msgtype = msgtype;
        this.touid = touid;
        this.toname = toname;
        this.time = time;
    }

    public String getFromuid() {
        return fromuid;
    }

    public void setFromuid(String fromuid) {
        this.fromuid = fromuid;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MessageDBInfo{" +
                "fromuid='" + fromuid + '\'' +
                ", fromname='" + fromname + '\'' +
                ", msg='" + msg + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", touid='" + touid + '\'' +
                ", toname='" + toname + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
