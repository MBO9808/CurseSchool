package com.example.curseschool.CourseView;

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

import com.example.curseschool.Adapters.CourseViewAdapter;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.MainView.MainSite;
import com.example.curseschool.NewObjectsHandlers.NewCourseLanguageHandler;
import com.example.curseschool.Objects.Course;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserKind;
import com.example.curseschool.UserUtils.UserUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class CourseView extends AppCompatActivity {

    RecyclerView recyclerView;

    private CourseViewAdapter courseViewAdapter;
    private ArrayList<Course> courseArrayList;
    private Toolbar toolbar;
    private FloatingActionButton newCourse;
    private String MyPREFERENCES = "userData";
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        toolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Lista kursów");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseView.this, MainSite.class);
                intent.putExtra("Navigation", 1);
                startActivity(intent);
            }
        });
        currentUser = getCurrentUser();
        initDictionaryView();
        handleCoursesList();
        handleFloatingButton();
    }

    private User getCurrentUser() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        User user = UserUtils.getUserById(id);
        return user;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CourseView.this, MainSite.class);
        intent.putExtra("Navigation", 1);
        startActivity(intent);
    }

    private void initDictionaryView(){
        recyclerView = findViewById(R.id.coursesView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseArrayList = new ArrayList<>();
        courseViewAdapter = new CourseViewAdapter(this, courseArrayList, CourseView.this);
        recyclerView.setAdapter(courseViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleCoursesList(){
        courseArrayList = createCourseListData();
    }

    private void handleFloatingButton(){
        newCourse = findViewById(R.id.addNewCourse);
        if(currentUser.getType().equals(UserKind.admin.toString()))
            newCourse.setVisibility(View.VISIBLE);
        else
            newCourse.setVisibility(View.GONE);
        setListenerForNewCourse();
    }

    private ArrayList<Course> createCourseListData() {
        courseArrayList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from courses where archival = 0";
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
                    Date paymentDate = resultSet.getDate(9);
                    Float payment = resultSet.getFloat(10);
                    Date creationDate = resultSet.getDate(11);
                    boolean archival = resultSet.getBoolean(12);
                    Date signDate = resultSet.getDate(13);
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

    private ArrayList<Integer> getStudentsList(int courseId){
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

    private ArrayList<CourseDate> getCourseDatesList(int id){
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

    private void setListenerForNewCourse() {
        newCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newCourse.getId()) {
                    Intent intent = new Intent(CourseView.this, NewCourseMainForm.class);
                    startActivity(intent);
                }
            }
        });
    }
}