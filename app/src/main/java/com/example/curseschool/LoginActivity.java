package com.example.curseschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    private TextView registerButton;
    private TextView loginButton;
    private EditText inputUserLogin;
    private EditText inputUserPassword;
    private Connection connect;
    private String connectionResult = "";
    private String MyPREFERENCES = "userData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        goToRegister();
        login();
    }

    private void goToRegister() {
        registerButton = findViewById(R.id.noAccountTextView);
        startRegisterActivity();
    }

    private void startRegisterActivity() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void login() {
        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validateFields();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    private void validateFields() throws SQLException {
        startActivity(new Intent(LoginActivity.this, MainSite.class));
        initializeFields();
        boolean isEmpty = checkIfFieldsEmpty();
        if (!isEmpty) {
            boolean login = validateLogin();
            if (login) {
                startUserSession();
                startActivity(new Intent(LoginActivity.this, MainSite.class));
            }
        }
    }

    private void startUserSession() {
        String userEmail = inputUserLogin.getText().toString();
        User user = UserUtils.getUserFromEmail(userEmail);
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("type", user.getType());
        editor.commit();
        singleToneClass.getInstance().setData(user.getEmail());
    }

    private void initializeFields() {
        inputUserLogin = findViewById(R.id.inputUserLogin);
        inputUserPassword = findViewById(R.id.inputUserLoginPassword);
    }

    private boolean checkIfFieldsEmpty() {
        boolean isEmpty = false;
        String userEmail = inputUserLogin.getText().toString();
        String userPassword = inputUserPassword.getText().toString();
        if (userEmail.isEmpty()) {
            showError(inputUserLogin, "Nie podano adresu email!");
            isEmpty = true;
        } else if (!userEmail.contains("@")) {
            showError(inputUserLogin, "Nieprawidłowy adres email!");
            isEmpty = true;
        } else if (userPassword.isEmpty()) {
            showError(inputUserPassword, "Nie podano hasła!");
            isEmpty = true;
        } else if (userPassword.length() <= 7) {
            showError(inputUserPassword, "Hasło musi mieć więcej niż 7 znaków");
            isEmpty = true;
        } else {
            isEmpty = false;
        }

        return isEmpty;
    }

    private void showError(EditText inputField, String errorMsg) {
        inputField.setError(errorMsg);
        inputField.requestFocus();
    }

    private boolean validateLogin() throws SQLException {
        boolean loginSuccess = false;
        String userEmail = inputUserLogin.getText().toString();
        String userPassword = inputUserPassword.getText().toString();
        ResultSet resultSet = getResultSet(userEmail);
        if (resultSet.next() == false) {
            showError(inputUserLogin, "Nieprawidłowy adres email!");
        } else {
            do {
                String password = resultSet.getString(5);
                if (!userPassword.equals(password))
                    showError(inputUserPassword, "Nieprawidłowe hasło!");
                else
                    loginSuccess = true;
            } while (resultSet.next());
        }
        return loginSuccess;
    }

    private ResultSet getResultSet(String userEmail) {
        ResultSet resultSet = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from users where email = '" + userEmail + "'";
                Statement statement = connect.createStatement();
                resultSet = statement.executeQuery(query);
            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return resultSet;
    }


}