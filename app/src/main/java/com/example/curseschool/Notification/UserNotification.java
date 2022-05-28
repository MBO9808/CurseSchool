package com.example.curseschool.Notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.curseschool.Adapters.NotificationAdapter;
import com.example.curseschool.DialogHandlers.LanguageDialogCloseHandler;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Helpers.NotificationHelper;
import com.example.curseschool.MainView.MainSite;
import com.example.curseschool.NewObjectsHandlers.NewCourseLanguageHandler;
import com.example.curseschool.NewObjectsHandlers.NewNotificationHandler;
import com.example.curseschool.Objects.Notification;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserKind;
import com.example.curseschool.UserUtils.UserUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class UserNotification extends AppCompatActivity implements LanguageDialogCloseHandler {

    RecyclerView recyclerView;

    private NotificationAdapter notificationAdapter;
    private ArrayList<Notification> notificationArrayList;
    private Toolbar toolbar;
    private FloatingActionButton newNotification;
    private String MyPREFERENCES = "userData";
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Ogłoszenia");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserNotification.this, MainSite.class);
                intent.putExtra("Navigation", 1);
                startActivity(intent);
            }
        });
        currentUser = getCurrentUser();
        initDictionaryView();
        handleSwipe();
        handleLanguageList();
        handleFloatingButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserNotification.this, MainSite.class);
        intent.putExtra("Navigation", 3);
        startActivity(intent);
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.notificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationArrayList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationArrayList, UserNotification.this);
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleLanguageList() {
        notificationArrayList = createNotificationListData();
    }

    private void handleSwipe() {
        if (currentUser.getType().equals(UserKind.student.toString()))
            return;

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotificationHelper(notificationAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private User getCurrentUser() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        User user = UserUtils.getUserById(id);
        return user;
    }

    private void handleFloatingButton() {
        newNotification = findViewById(R.id.addNewNotification);
        if (currentUser.getType().equals(UserKind.student.toString()))
            newNotification.setVisibility(View.GONE);
        else {
            newNotification.setVisibility(View.VISIBLE);
            setListenerForNewNotification();
        }
    }

    private ArrayList<Notification> createNotificationListData() {
        notificationArrayList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from notification where archival = 0";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    int user_id = resultSet.getInt(2);
                    String description = resultSet.getString(3);
                    Date date = resultSet.getDate(4);
                    boolean archival = resultSet.getBoolean(5);
                    Notification notification = new Notification(id, user_id, description, date, archival);
                    notificationArrayList.add(notification);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return notificationArrayList;
    }

    private void setListenerForNewNotification() {
        newNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newNotification.getId()) {
                    NewNotificationHandler.newInstance().show(getSupportFragmentManager(), NewNotificationHandler.TAG);
                }
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        notificationArrayList = createNotificationListData();
        notificationAdapter.setNotifications(notificationArrayList);
        notificationAdapter.notifyDataSetChanged();
    }
}