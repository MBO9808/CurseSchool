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
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserChangeEmail extends AppCompatActivity {

    private String MyPREFERENCES = "userData";
    private TextView oldEmail;
    private EditText newEmail;
    private Button save;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_email);
        newEmail = findViewById(R.id.textChangedEmail);
        save = findViewById(R.id.saveEmailChange);
        setToolbar();
        setOldEmail();
        handleSave();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Zmiana adresu email");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserChangeEmail.this, MainSite.class);
                intent.putExtra("Navigation", 3);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserChangeEmail.this, MainSite.class);
        intent.putExtra("Navigation", 3);
        startActivity(intent);
    }

    private void setOldEmail() {
        int currentUserId = getCurrentUserId();
        User currentUser = UserUtils.getUserById(currentUserId);
        oldEmail = findViewById(R.id.oldEmailText);
        oldEmail.setText(currentUser.getEmail());
    }

    private int getCurrentUserId() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        return id;
    }

    private void handleSave() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == save.getId()) {
                    String email = newEmail.getText().toString();
                    if (email.isEmpty()) {
                        Toast.makeText(UserChangeEmail.this, "Nie podano adresu email", Toast.LENGTH_LONG).show();
                        return;
                    } else if (!email.contains("@")) {
                        Toast.makeText(UserChangeEmail.this, "Nieprawidłowy adres email", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        updateUserEmail(email);
                        Intent intent = new Intent(UserChangeEmail.this, LoginActivity.class);
                        Toast.makeText(UserChangeEmail.this, "Ades email został zmieniony", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void updateUserEmail(String email) {
        int currentUserId = getCurrentUserId();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "UPDATE users SET email = '" + email + "' WHERE id = " + currentUserId;
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