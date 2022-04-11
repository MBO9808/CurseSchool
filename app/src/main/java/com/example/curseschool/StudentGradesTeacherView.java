package com.example.curseschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentGradesTeacherView extends AppCompatActivity implements StudentGradeDialogCloseHandler {

    RecyclerView recyclerView;

    private StudentGradesTeacherAdapter studentGradesTeacherAdapter;
    private ArrayList<Grade> gradeArrayList;
    private Toolbar toolbar;
    private FloatingActionButton newGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_grades_teacher_view);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Lista ocen studenta");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentGradesTeacherView.this, GradeCourseStudentsListView.class));
            }
        });
        initDictionaryView();
        handleSwipe();
        handleLanguageList();
        handleFloatingButton();
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.studentGradesView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gradeArrayList = new ArrayList<>();
        studentGradesTeacherAdapter = new StudentGradesTeacherAdapter(this, gradeArrayList, StudentGradesTeacherView.this);
        recyclerView.setAdapter(studentGradesTeacherAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleLanguageList() {
        gradeArrayList = createGradeListData();
    }

    private void handleSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new GradeHelper(studentGradesTeacherAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void handleFloatingButton() {
        newGrade = findViewById(R.id.addNewGrade);
        setListenerForNewGrade();
    }

    private ArrayList<Grade> createGradeListData() {
        gradeArrayList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from grades";
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

    private void setListenerForNewGrade() {
        newGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newGrade.getId()) {
                    NewGradeHandler.newInstance().show(getSupportFragmentManager(), NewGradeHandler.TAG);
                }
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        gradeArrayList = createGradeListData();
        studentGradesTeacherAdapter.setGrades(gradeArrayList);
        studentGradesTeacherAdapter.notifyDataSetChanged();
    }
}