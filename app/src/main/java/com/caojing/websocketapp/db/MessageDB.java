package com.caojing.websocketapp.db;

import android.text.TextUtils;
import android.util.Log;

import com.caojing.websocketapp.entity.FriendDBInfo;
import com.caojing.websocketapp.entity.FriendListInfo;
import com.caojing.websocketapp.entity.LoginInfo;
import com.caojing.websocketapp.entity.MessageDBInfo;
import com.jude.utils.JUtils;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 * Created by Administrator on 2017/3/6 0006.
 */
public class MessageDB {
    public static final String DB_NAME = "message.db";
    //数据库名
    public static final int VERSION = 1;
    //数据库版本号
    private static MessageDB personDB;
    private static DbManager db;
    private static String TAG = "MessageDB";

    //接收构造方法初始化的DbManager对象
    private MessageDB() {
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(DB_NAME, VERSION);
        db = x.getDb(databaseOpenHelper.getDaoConfig());
    }

    //构造方法私有化,拿到DbManager对象
    public synchronized static MessageDB getIntance() {
        if (personDB == null) {
            personDB = new MessageDB();
        }
        return personDB;
    }
    //获取PersonDB实例

    /****************************************************************************************/
    //写两个测试方法，也就是常用的数据库操作
    //将Person实例存进数据库
    public void savePerson(MessageDBInfo person) {
        try {
            db.save(person);
            Log.d("xyz", "save succeed!");
        } catch (DbException e) {
            Log.d("xyz", e.toString());
        }
    }

    /**
     * 保存好友信息到数据库中
     *
     * @param info
     */
    public void svaeFriend(FriendDBInfo info) {
        try {
            List<FriendDBInfo> list = db.selector(FriendDBInfo.class).expr("userId" + " =  '" + info.getUserId() + "'").findAll();
            if (list != null && list.size() > 0) {
                //如果数据库中存在这个用户，就修改这个用户对应的最后一条消息
                if (!TextUtils.isEmpty(info.getMsg())) {
                    info.setId(list.get(0).getId());
                    db.update(info);
                    JUtils.Log(TAG, "修改msg成功"+info.getMsg());
                }
            } else {
                //如果数据库中不存在这个用户，就插入这个用户到数据库中
                db.save(info);
                Log.d(TAG, "新增好友信息成功");
            }
        } catch (DbException e) {
            e.printStackTrace();
            JUtils.Log(TAG, "修改msg失败"+e);
        }
    }

    /**
     * 加载最近联系人列表
     *
     * @return
     */
    public List<FriendDBInfo> loadFriend() {
        List<FriendDBInfo> list = null;
        try {
            list = db.selector(FriendDBInfo.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    //读取所有message信息
    public List<MessageDBInfo> loadPerson() {
        List<MessageDBInfo> list = null;
        try {
            list = db.selector(MessageDBInfo.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 加載消息歷史
     *
     * @param uid
     * @return
     */
    public List<MessageDBInfo> loadMessage(String uid) {
        List<MessageDBInfo> list = null;
        try {
//         db.findDbModelAll(new SqlInfo("select * from message where uid" + "=" + uid+ "or" + "touid" + "=" + uid));
            list = db.selector(MessageDBInfo.class).expr("(fromuid" + " =  '" + LoginInfo.id + "' and " + "  touid  " + " =  '" + uid + "')"
                    + "  or  "
                    + "(fromuid" + " =  '" + uid + "' and " + "  touid  " + " =  '" + LoginInfo.id + "')").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

}