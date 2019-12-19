package com.example.recess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class CourseView extends AppCompatActivity {

    Account user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        ListView listView = findViewById(R.id.courseList);

        user = this.getIntent().getParcelableExtra("user");
        final Course[] courses;
        try {
            courses = APIFuncs.getCourses(user);
        } catch(AuthException e) {
            Toast.makeText(this, "Authentication error. Please login again.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CourseView.this, MainActivity.class);
            CourseView.this.startActivity(intent);
            return;
        }

        listView.setAdapter(new CourseArrayAdapter(this, courses));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseView.this, ProgressReport.class);
                intent.putExtra("periodID", courses[position].ID);
                CourseView.this.startActivity(intent);
            }
        });

    }
}
