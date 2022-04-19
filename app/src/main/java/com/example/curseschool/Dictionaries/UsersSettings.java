package com.example.curseschool.Dictionaries;

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

import com.example.curseschool.Adapters.UsersAdapter;
import com.example.curseschool.DialogHandlers.GradeNameDialogCloseHandler;
import com.example.curseschool.DialogHandlers.UserDialogCloseHandler;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Helpers.GradeNameDictionaryHelper;
import com.example.curseschool.Helpers.UsersHelper;
import com.example.curseschool.MainView.MainSite;
import com.example.curseschool.NewObjectsHandlers.NewUserHandler;
import com.example.curseschool.Objects.GradeName;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UsersSettings extends AppCompatActivity implements UserDialogCloseHandler {

    RecyclerView recyclerView;

    private UsersAdapter usersAdapter;
    private ArrayList<User> users;
    private Toolbar toolbar;
    private FloatingActionButton newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_settings);
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Użytkownicy");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersSettings.this, MainSite.class);
                intent.putExtra("Navigation", 3);
                startActivity(intent);
            }
        });
        initDictionaryView();
        handleSwipe();
        handleUsersList();
        handleFloatingButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UsersSettings.this, MainSite.class);
        intent.putExtra("Navigation", 3);
        startActivity(intent);
    }

    private void initDictionaryView() {
        recyclerView = findViewById(R.id.userSettingView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, users, UsersSettings.this);
        recyclerView.setAdapter(usersAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void handleUsersList() {
        users = createUsersListData();
    }

    private void handleSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new UsersHelper(usersAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void handleFloatingButton() {
        newUser = findViewById(R.id.addNewUser);
        setListenerForNewUser();
    }

    private ArrayList<User> createUsersListData() {
        users.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from users where archival = 0 order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt(1));
                    user.setFirstName(resultSet.getString(2));
                    user.setLastName(resultSet.getString(3));
                    user.setEmail(resultSet.getString(4));
                    user.setPhoneNumber(resultSet.getString(6));
                    user.setCity(resultSet.getString(7));
                    user.setStreet(resultSet.getString(8));
                    user.setType(resultSet.getString(9));
                    user.setArchival(resultSet.getBoolean(10));
                    user.setPostalCode(resultSet.getString(11));
                    user.setIdn(resultSet.getString(12));
                    users.add(user);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return users;
    }

    private void setListenerForNewUser() {
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == newUser.getId()) {
                    NewUserHandler.newInstance().show(getSupportFragmentManager(), NewUserHandler.TAG);
                }
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        users = createUsersListData();
        usersAdapter.setUsers(users);
        usersAdapter.notifyDataSetChanged();
    }
}