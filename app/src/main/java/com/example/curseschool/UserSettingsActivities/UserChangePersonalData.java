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

public class UserChangePersonalData extends AppCompatActivity {

    private String MyPREFERENCES = "userData";
    private Button save;
    private Toolbar toolbar;
    private EditText phoneNumber;
    private EditText city;
    private EditText street;
    private EditText postalCode;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_personal_data);
        currentUserId = getCurrentUserId();
        setToolbar();
        initFields();
        setFieldsValues();
        handleSave();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.mainToolBar);
        toolbar.setTitle("Zmiana danych");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserChangePersonalData.this, MainSite.class);
                intent.putExtra("Navigation", 3);
                startActivity(intent);
            }
        });
    }

    private void initFields() {
        phoneNumber = findViewById(R.id.changeUserPhoneNumber);
        city = findViewById(R.id.changeUserCity);
        street = findViewById(R.id.changeUserStreet);
        postalCode = findViewById(R.id.changeUserPostalCode);
        save = findViewById(R.id.saveUserPersonalDataChange);
    }

    private void setFieldsValues() {
        User currentUser = UserUtils.getUserById(currentUserId);
        phoneNumber.setText(currentUser.getPhoneNumber());
        city.setText(currentUser.getCity());
        street.setText(currentUser.getStreet());
        postalCode.setText(currentUser.getPostalCode());
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
                    save();
                }
            }
        });
    }

    private void save() {
        String userPhoneNumber = phoneNumber.getText().toString();
        String userCity = city.getText().toString();
        String userStreet = street.getText().toString();
        String userPostalCode = postalCode.getText().toString();
        if (userPhoneNumber.isEmpty()) {
            Toast.makeText(UserChangePersonalData.this, "Nie podano numeru telefonu", Toast.LENGTH_LONG).show();
            return;
        } else if (userPhoneNumber.length() != 9) {
            Toast.makeText(UserChangePersonalData.this, "Numer telefonu musi mieć 9 cyfr", Toast.LENGTH_LONG).show();
            return;
        } else if (userCity.isEmpty()) {
            Toast.makeText(UserChangePersonalData.this, "Nie podano miasta", Toast.LENGTH_LONG).show();
            return;
        } else if (userStreet.isEmpty()) {
            Toast.makeText(UserChangePersonalData.this, "Nie podano ulicy", Toast.LENGTH_LONG).show();
            return;
        } else if (userPostalCode.isEmpty()) {
            Toast.makeText(UserChangePersonalData.this, "Nie podano kodu pocztowego", Toast.LENGTH_LONG).show();
            return;
        } else if (!userPostalCode.contains("-")) {
            Toast.makeText(UserChangePersonalData.this, "Nieprawidłowy kod pocztowy", Toast.LENGTH_LONG).show();
            return;
        } else {
            updateUserData(userPhoneNumber, userCity, userStreet, userPostalCode);
            Intent intent = new Intent(UserChangePersonalData.this, MainSite.class);
            intent.putExtra("Navigation", 3);
            Toast.makeText(UserChangePersonalData.this, "Dane zostały zmienione", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }

    private void updateUserData(String phoneStr, String cityStr, String streetStr, String postalCodeStr) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "UPDATE users SET phone_number = '" + phoneStr + "', city = '" + cityStr + "', adress = '" + streetStr + "', postal_code = '" + postalCodeStr + "' WHERE id = " + currentUserId;
                Statement statement = connect.createStatement();
                statement.execute(query);
                connect.close();
            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }
}