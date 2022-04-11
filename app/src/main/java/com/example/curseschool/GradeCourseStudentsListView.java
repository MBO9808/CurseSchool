package com.example.curseschool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GradeCourseStudentsListView extends AppCompatActivity {

    RecyclerView recyclerView;

    private GradeCourseStudentsListAdapter gradeCourseStudentsListAdapter;
    private ArrayList<Student> studentArrayList;
    private Toolbar toolbar;
    private String MyPREFERENCES = "userData";
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_course_students_list_view);
        toolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Lista studentów");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GradeCourseStudentsListView.this, GradeCourseTeacherView.class));
            }
        });
        courseId = getIntent().getIntExtra("courseId", 0);
        initDictionaryView();
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.gradeTeacherStudentListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentArrayList = new ArrayList<>();
        handleCoursesList();
        gradeCourseStudentsListAdapter = new GradeCourseStudentsListAdapter(this, studentArrayList, GradeCourseStudentsListView.this);
        recyclerView.setAdapter(gradeCourseStudentsListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleCoursesList() {
        studentArrayList = getStudentsList();
    }

    private ArrayList<Student> getStudentsList() {
        studentArrayList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select st.student_id, us.forename, us.surname, us.email, us.phone_number from course_students st " +
                        "join users us on us.id = st.student_id where st.course_id =  " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int studentId = resultSet.getInt(1);
                    String forename = resultSet.getString(2);
                    String surname = resultSet.getString(3);
                    String email = resultSet.getString(4);
                    String phoneNumber = resultSet.getString(5);
                    String name = forename + " " + surname;
                    Student student = new Student(studentId, courseId, name, email, phoneNumber);
                    studentArrayList.add(student);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return studentArrayList;
    }
}