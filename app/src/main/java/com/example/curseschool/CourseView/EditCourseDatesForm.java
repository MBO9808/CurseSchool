package com.example.curseschool.CourseView;

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

import com.example.curseschool.Adapters.CourseDatesAdapter;
import com.example.curseschool.DialogHandlers.CourseDateDialogCloseHandler;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Helpers.CourseDateHelper;
import com.example.curseschool.NewObjectsHandlers.NewClassRoomHandler;
import com.example.curseschool.NewObjectsHandlers.NewCourseDateHandler;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class EditCourseDatesForm extends AppCompatActivity implements CourseDateDialogCloseHandler {

    RecyclerView recyclerView;

    private CourseDatesAdapter courseDatesAdapter;
    private ArrayList<CourseDate> courseDates;
    private Toolbar toolbar;
    private FloatingActionButton newDate;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_dates_form);
        courseId = getIntent().getIntExtra("courseId", 0);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Terminy zajęć");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditCourseDatesForm.this, CourseFormView.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });
        initDictionaryView();
        handleSwipe();
        handleCourseDatesList();
        handleFloatingButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditCourseDatesForm.this, CourseFormView.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.courseDatesRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseDates = new ArrayList<>();
        courseDatesAdapter = new CourseDatesAdapter(this, courseDates, EditCourseDatesForm.this);
        recyclerView.setAdapter(courseDatesAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleCourseDatesList() {
        courseDates = getCourseDatesList();
    }

    private void handleSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CourseDateHelper(courseDatesAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void handleFloatingButton() {
        newDate = findViewById(R.id.addNewCourseDate);
        setListenerForNewCourseDate();
    }

    private ArrayList<CourseDate> getCourseDatesList() {
        courseDates.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from course_dates where course_id = " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int dateId = resultSet.getInt(1);
                    int courseId = resultSet.getInt(2);
                    Date date = resultSet.getDate(3);
                    Time courseTimeStart = resultSet.getTime(4);
                    Time courseTimeEnd = resultSet.getTime(5);
                    CourseDate courseDate = new CourseDate(dateId, courseId, date, courseTimeStart, courseTimeEnd);
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

    private void setListenerForNewCourseDate() {
        newDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newDate.getId()) {
                    NewCourseDateHandler.newInstance().show(getSupportFragmentManager(), NewCourseDateHandler.TAG);
                }
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        courseDates = getCourseDatesList();
        courseDatesAdapter.setCourseDates(courseDates);
        courseDatesAdapter.notifyDataSetChanged();
    }
}