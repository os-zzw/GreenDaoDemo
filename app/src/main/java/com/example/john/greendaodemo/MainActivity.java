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

    @OnClick({R.id.btn_add, R.id.btn_addMore, R.id.btn_delete, R.id.btn_upDate, R.id.btn_query})
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
                Log.i(TAG, "onClick: addMore");
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
                Student student2 = new Student();
                student2.setId(1001L);
                mCommonUtils.deleteStudent(student2);
                break;
            case R.id.btn_upDate:
                Student student1 = new Student();
                student1.setName("john");
                student1.setId(1001L);
                student1.setAddress("address改变了");
                student1.setAge(20544);
                mCommonUtils.uoDateStudent(student1);
                break;
            case R.id.btn_query:
                List<Student> students1 = mCommonUtils.listAll();
                for (int i = 0; i < students1.size(); i++) {
                    Student o = students1.get(i);
                    Log.i(TAG, "list: " + o.getId()+o.getName()+o.getAddress());
                }
                break;
        }
    }
}
