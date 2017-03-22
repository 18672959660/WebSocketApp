package com.caojing.websocketapp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class FriendListInfo implements Serializable{


    /**
     * code : 1
     * users_list : [{"id":"b54f53ae255b43cf8458a81e97da46b2_1","nickname":"游客B54F"},{"id":"bf20f8e4b59747d89647335cbb7216d5_1","nickname":"游客BF20"}]
     * fromuser : {"id":"5690_2","nickname":"朱登辉"}
     * touser : {"id":"b54f53ae255b43cf8458a81e97da46b2_1","nickname":"游客B54F"}
     * msg : {"content":"55","time":"2017/3/1 16:58:49","msgtype":"0"}
     */

    private int code;
    private FromuserBean fromuser;
    private TouserBean touser;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    private MsgBean msg;
    private List<UsersListBean> users_list;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public FromuserBean getFromuser() {
        return fromuser;
    }

    public void setFromuser(FromuserBean fromuser) {
        this.fromuser = fromuser;
    }

    public TouserBean getTouser() {
        return touser;
    }

    public void setTouser(TouserBean touser) {
        this.touser = touser;
    }

    public List<UsersListBean> getUsers_list() {
        return users_list;
    }

    public void setUsers_list(List<UsersListBean> users_list) {
        this.users_list = users_list;
    }

    public static class FromuserBean implements Serializable{
        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        /**
         * id : 5690_2
         * nickname : 朱登辉
         * content : 55
         * time : 2017/3/1 16:58:49
         */

        private String uid;
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }


    }

    public static class TouserBean implements Serializable{
        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        /**
         * id : b54f53ae255b43cf8458a81e97da46b2_1
         * nickname : 游客B54F
         */

        private String uid;
        private String nickname;


        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    public static class MsgBean implements Serializable{
        private String content;
        private String time;
        private String msgtype;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }
    }

    public static class UsersListBean implements Serializable {
        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        /**
         * id : b54f53ae255b43cf8458a81e97da46b2_1
         * nickname : 游客B54F
         */

        private String uid;
        private String nickname;
        private int tip;


        public int getTip() {
            return tip;
        }

        public void setTip(int tip) {
            this.tip = tip;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
