package com.example.curseschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GradeNameDictionary extends AppCompatActivity implements GradeNameDialogCloseHandler {

    RecyclerView recyclerView;

    private GradeNameAdapter gradeNameAdapter;
    private ArrayList<GradeName> gradeNames;
    private Toolbar toolbar;
    private FloatingActionButton newGradeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_name_dictionary);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Słownik typów ocen");
        setSupportActionBar(toolbar);
        initDictionaryView();
        handleSwipe();
        handleClassRoomList();
        handleFloatingButton();
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.gradeNameDictionary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gradeNames = new ArrayList<>();
        gradeNameAdapter = new GradeNameAdapter(this, gradeNames, GradeNameDictionary.this);
        recyclerView.setAdapter(gradeNameAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleClassRoomList() {
        gradeNames = createGradeNameListData();
    }

    private void handleSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new GradeNameDictionaryHelper(gradeNameAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void handleFloatingButton() {
        newGradeName = findViewById(R.id.addNewGradeName);
        setListenerForNewCourseAdvancement();
    }

    private ArrayList<GradeName> createGradeNameListData() {
        gradeNames.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from grade_type order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    GradeName gradeName = new GradeName(id, name);
                    gradeNames.add(gradeName);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return gradeNames;
    }

    private void setListenerForNewCourseAdvancement() {
        newGradeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newGradeName.getId()) {
                    NewGradeNameHandler.newInstance().show(getSupportFragmentManager(), NewGradeNameHandler.TAG);
                }
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        gradeNames = createGradeNameListData();
        gradeNameAdapter.setGradeNames(gradeNames);
        gradeNameAdapter.notifyDataSetChanged();
    }
}