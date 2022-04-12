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

public class ClassRoomDictionary extends AppCompatActivity implements ClassRoomDialogCloseHandler {

    RecyclerView recyclerView;

    private ClassRoomAdapter classRoomAdapter;
    private ArrayList<ClassRoom> classRooms;
    private Toolbar toolbar;
    private FloatingActionButton newClassRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room_dictionary);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Słownik sal zajęć");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassRoomDictionary.this, MainSite.class);
                intent.putExtra("Navigation", 3);
                startActivity(intent);
            }
        });
        initDictionaryView();
        handleSwipe();
        handleClassRoomList();
        handleFloatingButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClassRoomDictionary.this, MainSite.class);
        intent.putExtra("Navigation", 3);
        startActivity(intent);
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.classRoomDictionary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        classRooms = new ArrayList<>();
        classRoomAdapter = new ClassRoomAdapter(this, classRooms, ClassRoomDictionary.this);
        recyclerView.setAdapter(classRoomAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleClassRoomList() {
        classRooms = createClassRoomListData();
    }

    private void handleSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ClassRoomDictionaryHelper(classRoomAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void handleFloatingButton() {
        newClassRoom = findViewById(R.id.addNewClassRoom);
        setListenerForNewClassRoom();
    }

    private ArrayList<ClassRoom> createClassRoomListData() {
        classRooms.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from class_room order by number asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    int number = resultSet.getInt(2);
                    ClassRoom classRoom = new ClassRoom(id, number);
                    classRooms.add(classRoom);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return classRooms;
    }

    private void setListenerForNewClassRoom() {
        newClassRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newClassRoom.getId()) {
                    NewClassRoomHandler.newInstance().show(getSupportFragmentManager(), NewClassRoomHandler.TAG);
                }
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        classRooms = createClassRoomListData();
        classRoomAdapter.setClassRooms(classRooms);
        classRoomAdapter.notifyDataSetChanged();
    }
}