package com.example.john.greendaodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.john.greendaodemo.dbmanager.CommonUtils;
import com.student.entity.Student;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CommonUtils mCommonUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mCommonUtils = new CommonUtils(this);
    }


    @OnClick({R.id.btn_add, R.id.btn_addMore, R.id.btn_delete, R.id.btn_deleteMore, R.id.btn_upDate, R.id.btn_queryAll, R.id.btn_queryOne, R.id.btn_queryBuilder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Student student = new Student();
                student.setName("john");
                student.setAddress("address");
                student.setAge(20);
                mCommonUtils.insertStudent(student);
                break;
            case R.id.btn_addMore:
                List<Student> students = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    Student student1 = new Student();
                    student1.setName("john");
                    student1.setAddress("address");
                    student1.setAge(20);
                    students.add(student1);
                }
                mCommonUtils.insertMultStudent(students);
                break;
            case R.id.btn_delete:
                Student student1 = new Student();
                student1.setId(1L);
                mCommonUtils.deleteStudent(student1);
                break;
            case R.id.btn_deleteMore:
                mCommonUtils.deleteAll();
                break;
            case R.id.btn_upDate:
                Student student2 = new Student();
                student2.setId(1L);
                student2.setName("upDateName");
                student2.setAddress("沈阳");
                mCommonUtils.uoDateStudent(student2);
                break;
            case R.id.btn_queryAll:
                List<Student> students1 = mCommonUtils.listAll();
                for (int i = 0; i < students1.size(); i++) {
                    Student o = students1.get(i);
                    Log.i(TAG, "list: " + o.getId() + o.getName() + o.getAddress());
                }
                break;
            case R.id.btn_queryOne:
                Student student3 = new Student();
                student3.setId(1L);
                Student student4 = mCommonUtils.listOneStudent(1L);
                Log.i(TAG, "onClick: query_one" + student4.getName() + student4.getId());
                break;
            case R.id.btn_queryBuilder://使用复合条件进行查询
                //                mCommonUtils.queryBySql();
                mCommonUtils.queryByBuilder();
                break;
        }
    }
}
