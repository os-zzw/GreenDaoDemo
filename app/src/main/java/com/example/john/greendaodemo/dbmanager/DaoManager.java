package com.example.john.greendaodemo.dbmanager;


import android.content.Context;

import com.student.dao.DaoMaster;
import com.student.dao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 1.创建数据库
 * 2.创建表
 * 3.增删改查
 * 4.对数据库的升级
 * Created by ZheWei on 2016/9/9.
 */
public class DaoManager {
    private static final String TAG = "DaoManager";
    private static final String DB_NAME = "mydb.sqlite";//数据库名称

    private volatile static DaoManager manager;//多线程访问声明为单例模式

    private static DaoMaster.DevOpenHelper helper;
    private static DaoMaster master;
    private static DaoSession session;
    private Context mContext;

    private DaoManager() {

    }

    public void init(Context context) {
        this.mContext = context;
    }

    public static DaoManager getInstance() {
        if (manager == null) {
            synchronized (DaoManager.class) {
                if (manager == null) {
                    manager = new DaoManager();
                }
            }
        }
        return manager;
    }

    /**
     * 判断是否存在数据库,如果没有则创建数据库
     */
    public DaoMaster getMaster() {
        if (master == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
            master = new DaoMaster(helper.getWritableDatabase());
        }
        return master;
    }

    /**
     * 完成对数据库的 增删改查 ,这里仅仅是一个接口
     */
    public DaoSession getSession() {
        if (session == null) {
            if (master == null) {
                master = getMaster();
            }
            session = master.newSession();
        }
        return session;
    }

    /**
     * 打开输出日志的操作,默认的是关闭的
     */
    public void setDeBug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭所有的连接
     */
    public void closeConnections() {
        closeHelper();
        closeSession();
    }

    private void closeSession() {
        if (session != null) {
            session.clear();
            session = null;
        }
    }

    private void closeHelper() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

}

