package com.example.john.greendaodemo.dbmanager;

import android.content.Context;
import android.util.Log;

import com.student.dao.StudentDao;
import com.student.entity.Student;

import org.greenrobot.greendao.query.QueryBuilder;

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
        Log.i("MainActivity", "insertStudent: " + flag);
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
        Log.d("MainActivity", "insertMultStudent: " + flag);
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
        Log.i("MainActivity", "uoDateStudent: " + flag);
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
        Log.i("MainActivity", "deleteStudent: " + flag);
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
        Log.i("aaa", "deleteAll: " + flag);
        return flag;
    }

    /**
     * 查询 某一个表 的 所有记录
     */
    public List<Student> listAll() {
        return mDaoManager.getSession().loadAll(Student.class);
    }

    /**
     * 按照主键查询某一个 表 中 的单行记录
     */
    public Student listOneStudent(long key) {
        return mDaoManager.getSession().load(Student.class, key);
    }

    /**
     * 按照sql语句进行查询
     */
    public void queryBySql() {
        List<Student> list = mDaoManager.getSession().queryRaw(Student.class, "where name like ? and _id<=?", new String[]{"%jo%", "4"});
        for (Student s : list) {
            Log.i("MainActivity", s.getId() + "");
        }
    }

    /**
     * 使用查询构建器进行查询
     */
    public void queryByBuilder() {
        //使用查询构建器
        QueryBuilder<Student> queryBuilder = mDaoManager.getSession().queryBuilder(Student.class);
        //这些条件是 逻辑与
        queryBuilder.where(StudentDao.Properties.Name.like("john"));
        List<Student> list = queryBuilder.where(StudentDao.Properties.Id.le(4)).list();
        for (Student s : list) {
            Log.i("MainActivity", s.getId() + "");
        }
    }

}
