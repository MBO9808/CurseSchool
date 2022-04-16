package com.example.curseschool.TeacherGradesView;

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

import com.example.curseschool.Adapters.StudentGradesTeacherAdapter;
import com.example.curseschool.DialogHandlers.StudentGradeDialogCloseHandler;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Helpers.GradeHelper;
import com.example.curseschool.NewObjectsHandlers.NewGradeHandler;
import com.example.curseschool.Objects.Grade;
import com.example.curseschool.R;
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
    private int studentId;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_grades_teacher_view);
        studentId = getIntent().getIntExtra("studentId", 0);
        courseId = getIntent().getIntExtra("courseId", 0);
        String studentName = getStudentName(studentId);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Lista ocen studenta: " + studentName);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentGradesTeacherView.this, GradeCourseStudentsListView.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });
        initDictionaryView();
        handleSwipe();
        handleLanguageList();
        handleFloatingButton();
    }

    private String getStudentName(int studentId) {
        String studentName = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from users where id = " + studentId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String forename = resultSet.getString(2);
                    String surname = resultSet.getString(3);
                    studentName = forename + " " + surname;
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return studentName;
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
                String query = "Select * from grades where student_id = " + studentId + " and course_id = " + courseId;
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
                    NewGradeHandler.newInstance(studentId, courseId).show(getSupportFragmentManager(), NewGradeHandler.TAG);
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