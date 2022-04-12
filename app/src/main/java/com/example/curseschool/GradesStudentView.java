package com.example.curseschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GradesStudentView extends AppCompatActivity {

    RecyclerView recyclerView;

    private GradesStudentAdapter gradesStudentAdapter;
    private ArrayList<Grade> gradeArrayList;
    private String MyPREFERENCES = "userData";
    private Toolbar toolbar;
    private int courseId;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_student_view);
        currentUserId = getCurrentUserId();
        courseId = getIntent().getIntExtra("courseId", 0);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Moje oceny");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GradesStudentView.this, GradesStudentCourseView.class);
                startActivity(intent);
            }
        });
        initDictionaryView();
        handleLanguageList();
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.gradesStudentView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gradeArrayList = new ArrayList<>();
        gradesStudentAdapter = new GradesStudentAdapter(this, gradeArrayList, GradesStudentView.this);
        recyclerView.setAdapter(gradesStudentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleLanguageList() {
        gradeArrayList = createGradeListData();
    }

    private ArrayList<Grade> createGradeListData() {
        gradeArrayList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from grades where student_id = " + currentUserId + " and course_id = " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    int studentId = resultSet.getInt(2);
                    int courseId = resultSet.getInt(3);
                    int gradeNameId = resultSet.getInt(4);
                    Float gradeValue = resultSet.getFloat(5);
                    Grade grade = new Grade(id, studentId, courseId, gradeNameId, gradeValue);
                    gradeArrayList.add(grade);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return gradeArrayList;
    }

    private int getCurrentUserId() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        return id;
    }
}