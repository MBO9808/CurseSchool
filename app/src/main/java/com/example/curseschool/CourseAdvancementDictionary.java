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

public class CourseAdvancementDictionary extends AppCompatActivity implements CourseAdvancementDialogCloseHandler {

    RecyclerView recyclerView;

    private CourseAdvancementAdapter courseAdvancementAdapter;
    private ArrayList<CourseAdvancement> courseAdvancements;
    private Toolbar toolbar;
    private FloatingActionButton newCourseAdvancement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_advancement_dictionary);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Słownik poziomów zaawansownia kursu");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CourseAdvancementDictionary.this, MainSite.class));
            }
        });
        initDictionaryView();
        handleSwipe();
        handleClassRoomList();
        handleFloatingButton();
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.courseAdvancementDictionary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdvancements = new ArrayList<>();
        courseAdvancementAdapter = new CourseAdvancementAdapter(this, courseAdvancements, CourseAdvancementDictionary.this);
        recyclerView.setAdapter(courseAdvancementAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleClassRoomList() {
        courseAdvancements = createCourseAdvancementListData();
    }

    private void handleSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CourseAdvancementHelper(courseAdvancementAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void handleFloatingButton() {
        newCourseAdvancement = findViewById(R.id.addNewCourseAdvancement);
        setListenerForNewCourseAdvancement();
    }

    private ArrayList<CourseAdvancement> createCourseAdvancementListData() {
        courseAdvancements.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from course_advancement order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    CourseAdvancement courseAdvancement = new CourseAdvancement(id, name);
                    courseAdvancements.add(courseAdvancement);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return courseAdvancements;
    }

    private void setListenerForNewCourseAdvancement() {
        newCourseAdvancement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newCourseAdvancement.getId()) {
                    NewCourseAdvancementHandler.newInstance().show(getSupportFragmentManager(), NewCourseAdvancementHandler.TAG);
                }
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        courseAdvancements = createCourseAdvancementListData();
        courseAdvancementAdapter.setCourseAdvancements(courseAdvancements);
        courseAdvancementAdapter.notifyDataSetChanged();
    }
}