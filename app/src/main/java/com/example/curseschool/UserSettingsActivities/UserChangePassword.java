package com.example.curseschool.UserSettingsActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.LoginActivity.LoginActivity;
import com.example.curseschool.MainView.MainSite;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserChangePassword extends AppCompatActivity {

    private String MyPREFERENCES = "userData";
    private EditText password;
    private Button save;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);
        setToolbar();
        password = findViewById(R.id.userChangePassword);
        save = findViewById(R.id.savePasswordChange);
        handleSave();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Zmiana hasła");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserChangePassword.this, MainSite.class);
                intent.putExtra("Navigation", 3);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserChangePassword.this, MainSite.class);
        intent.putExtra("Navigation", 3);
        startActivity(intent);
    }

    private int getCurrentUserId() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        return id;
    }

    private void handleSave(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == save.getId()){
                    save();
                }
            }
        });
    }

    private void save(){
        String pass = password.getText().toString();
        if(pass.equals("")){
            Toast.makeText(UserChangePassword.this, "Proszę wpisać nowe hasło", Toast.LENGTH_LONG).show();
        } else if(pass.length() < 7){
            Toast.makeText(UserChangePassword.this, "Hasło musi mieć przynajmniej 7 znaków", Toast.LENGTH_LONG).show();
        } else {
            updateUserPassword(pass);
            Intent intent = new Intent(UserChangePassword.this, LoginActivity.class);
            Toast.makeText(UserChangePassword.this, "Hasło zostało zmienione", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }

    private void updateUserPassword(String password) {
        int currentUserId = getCurrentUserId();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "UPDATE users SET password = '" + password + "' WHERE id = " + currentUserId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                connect.close();
            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }
}