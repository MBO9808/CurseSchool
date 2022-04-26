package com.example.curseschool.StudentGradesView;

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

import com.example.curseschool.Adapters.GradesStudentCourseAdapter;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.MainView.MainSite;
import com.example.curseschool.Objects.Course;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class GradesStudentCourseView extends AppCompatActivity {

    RecyclerView recyclerView;

    private GradesStudentCourseAdapter gradesStudentCourseAdapter;
    private ArrayList<Course> courseArrayList;
    private Toolbar toolbar;
    private String MyPREFERENCES = "userData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_student_course_view);
        toolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Lista moich kursów");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GradesStudentCourseView.this, MainSite.class));
            }
        });
        initDictionaryView();
        handleCoursesList();
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.gradesStudentCourseView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseArrayList = new ArrayList<>();
        gradesStudentCourseAdapter = new GradesStudentCourseAdapter(this, courseArrayList, GradesStudentCourseView.this);
        recyclerView.setAdapter(gradesStudentCourseAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleCoursesList() {
        courseArrayList = createCourseListData();
    }


    private ArrayList<Course> createCourseListData() {
        int currentUserId = getCurrentUserId();
        courseArrayList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from courses c  join course_students s on s.course_id = c.id where c.archival = 0 and s.student_id = " + currentUserId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String courseName = resultSet.getString(2);
                    int teacherId = resultSet.getInt(3);
                    int languageId = resultSet.getInt(4);
                    int advancementId = resultSet.getInt(5);
                    int maxStudents = resultSet.getInt(6);
                    Date startDate = resultSet.getDate(7);
                    Date endDate = resultSet.getDate(8);
                    Date paymentDate = resultSet.getDate(10);
                    Float payment = resultSet.getFloat(11);
                    Date creationDate = resultSet.getDate(12);
                    boolean archival = resultSet.getBoolean(13);
                    Date signDate = resultSet.getDate(14);
                    ArrayList<Integer> studentsList = getStudentsList(id);
                    ArrayList<CourseDate> courseDatesList = getCourseDatesList(id);
                    Course course = new Course(id, courseName, teacherId, languageId, advancementId, maxStudents, startDate, endDate, paymentDate, payment, creationDate, archival, signDate, studentsList, courseDatesList);
                    courseArrayList.add(course);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return courseArrayList;
    }

    private int getCurrentUserId() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        return id;
    }

    private ArrayList<Integer> getStudentsList(int courseId) {
        ArrayList<Integer> studentsList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select student_id from course_students where course_id = " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int studentId = resultSet.getInt(1);
                    studentsList.add(studentId);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return studentsList;
    }

    private ArrayList<CourseDate> getCourseDatesList(int id) {
        ArrayList<CourseDate> courseDates = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from course_dates where course_id = " + id;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int dateId = resultSet.getInt(1);
                    int courseId = resultSet.getInt(2);
                    Date date = resultSet.getDate(3);
                    Time courseTimeStart = resultSet.getTime(4);
                    Time courseTimeEnd = resultSet.getTime(5);
                    int classRoomId = resultSet.getInt(6);
                    CourseDate courseDate = new CourseDate(dateId, courseId, date, courseTimeStart, courseTimeEnd, classRoomId);
                    courseDates.add(courseDate);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return courseDates;
    }
}