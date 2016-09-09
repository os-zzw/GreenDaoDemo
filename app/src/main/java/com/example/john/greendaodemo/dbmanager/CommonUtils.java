package com.example.john.greendaodemo.dbmanager;

import android.content.Context;

import com.student.entity.Student;

import java.util.List;

/**
 * 完成对某一张表的具体操作, ORM 操作的是对象
 * Created by ZheWei on 2016/9/9.
 */
public class CommonUtils {
    private DaoManager mDaoManager;

    public CommonUtils(Context context) {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.init(context);
    }

    /**
     * 完成对数据库表的插入操作-->并且会检测数据库是否存在,不存在自己创建,
     */
    public boolean insertStudent(Student student) {
        boolean flag = false;
        flag = mDaoManager.getSession().insert(student) != -1;//不等于-1是true 否则是false
        return flag;
    }

    /**
     * 同时插入多条记录
     */
    public boolean insertMultStudent(final List<Student> students) {
        boolean flag = false;
        try {
            mDaoManager.getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Student s : students) {
                        mDaoManager.getSession().insertOrReplace(s);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改指定记录
     */
    public boolean uoDateStudent(Student student) {
        boolean flag = false;
        try {
            mDaoManager.getSession().update(student);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除指定记录
     */
    public boolean deleteStudent(Student student) {
        boolean flag = false;
        try {
            mDaoManager.getSession().delete(student);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有的记录
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            mDaoManager.getSession().deleteAll(Student.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
