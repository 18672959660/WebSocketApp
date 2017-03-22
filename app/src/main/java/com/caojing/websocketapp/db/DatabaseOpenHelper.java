package com.caojing.websocketapp.db;

import org.xutils.DbManager;

/**
 *  数据库帮助类
 * Created by Administrator on 2017/3/6 0006.
 */

public class DatabaseOpenHelper {
    private DbManager.DaoConfig daoConfig;
    public DatabaseOpenHelper(String name,int version) {
        daoConfig = new DbManager.DaoConfig()
                .setDbName(name)
                .setDbVersion(version)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                        //开启WAL, 对写入加速提升巨大(作者原话)
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //数据库升级操作
                    }
                });
    }
    public DbManager.DaoConfig getDaoConfig(){
        return daoConfig;
    }
}
