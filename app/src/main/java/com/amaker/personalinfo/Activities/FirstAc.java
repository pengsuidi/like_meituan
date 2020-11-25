package com.amaker.personalinfo.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.amaker.personalinfo.R;
import com.amaker.personalinfo.entity.Student;

import java.util.ArrayList;

public class FirstAc extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student stu = new Student();
                stu.setAge(108);
                stu.setName("s1");

                Student stu2 = new Student();
                stu2.setAge(109);
                stu2.setName("s2");


                Student stu3 = new Student();
                stu3.setAge(110);
                stu3.setName("s3");
                ArrayList<Student> list = new ArrayList<Student>();
                list.add(stu);
                list.add(stu2);
                list.add(stu3);

                Intent i = new Intent(FirstAc.this, StudentActivity.class);
                Bundle bundle = new Bundle();

                //传递对象
                bundle.putParcelable("student", stu);

                //传递List ,这里注意只能传ArrayList
                bundle.putParcelableArrayList("student_list", list);
                i.putExtras(bundle);
                FirstAc.this.startActivity(i);
            }
        });
    }
}