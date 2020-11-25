package com.amaker.personalinfo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Student;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    private TextView tv, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            Student student = (Student) b.get("student");
            if (student != null) {
                tv.setText(student.getName() + ":" + student.getAge());
            }

            //关键性代码，通过intent.getParcelableArrayListExtra方法获取list数据
            ArrayList<Student> list = intent.getParcelableArrayListExtra("student_list");
            if (list != null && list.size() > 0) {
                String str = "";
                for (Student s : list) {
                    str += s.getName() + " | ";
                }
                tv2.setText(str);
            }
        }
    }
}