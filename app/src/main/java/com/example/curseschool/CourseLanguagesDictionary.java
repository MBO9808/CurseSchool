package com.example.curseschool;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseLanguagesDictionary extends AppCompatActivity implements LanguageDialogCloseHandler {

    RecyclerView recyclerView;

    private CourseLanguageAdapter courseLanguageAdapter;
    private ArrayList<CourseLanguage> courseLanguageList;
    private Toolbar toolbar;
    private FloatingActionButton newCourseLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_languages_dictionary);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Słownik języków kursów");
        setSupportActionBar(toolbar);
        initDictionaryView();
        handleSwipe();
        handleLanguageList();
        handleFloatingButton();
    }

    private void initDictionaryView(){
        recyclerView = findViewById(R.id.languagesDictionary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseLanguageList = new ArrayList<>();
        courseLanguageAdapter = new CourseLanguageAdapter(this, courseLanguageList, CourseLanguagesDictionary.this);
        recyclerView.setAdapter(courseLanguageAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleLanguageList(){
        courseLanguageList = createLanguageListData();
    }

    private void handleSwipe(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new LanguageDictionaryHelper(courseLanguageAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void handleFloatingButton(){
        newCourseLanguage = findViewById(R.id.addNewLanguage);
        setListenerForNewLanguage();
    }

    private ArrayList<CourseLanguage> createLanguageListData() {
        courseLanguageList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from course_languages";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    CourseLanguage courseLanguage = new CourseLanguage(id, name);
                    courseLanguageList.add(courseLanguage);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return courseLanguageList;
    }

    private void setListenerForNewLanguage() {
        newCourseLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newCourseLanguage.getId()) {
                    NewCourseLanguageHandler.newInstance().show(getSupportFragmentManager(), NewCourseLanguageHandler.TAG);
                }
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        courseLanguageList = createLanguageListData();
        courseLanguageAdapter.setCourseLanguages(courseLanguageList);
        courseLanguageAdapter.notifyDataSetChanged();
    }
}