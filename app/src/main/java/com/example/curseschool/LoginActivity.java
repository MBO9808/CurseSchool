package com.example.curseschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView registerButton;
    private TextView loginButton;
    private EditText inputUserLogin;
    private EditText inputUserPassword;

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
                validateFields();
            }
        });
    }

    private void validateFields() {
        initializeFields();
        checkIfFieldsEmpty();
    }

    private void initializeFields() {
        inputUserLogin = findViewById(R.id.inputUserLogin);
        inputUserPassword = findViewById(R.id.inputUserLoginPassword);
    }

    private void checkIfFieldsEmpty() {
        String userEmail = inputUserLogin.getText().toString();
        String userPassword = inputUserPassword.getText().toString();
        if (userEmail.isEmpty()) {
            showError(inputUserLogin, "Nie podano adresu email!");
        } else if (!userEmail.contains("@")) {
            showError(inputUserLogin, "Nieprawidłowy adres email!");
        } else if (userPassword.isEmpty()) {
            showError(inputUserPassword, "Nie podano hasła!");
        } else if (userPassword.length() <= 7) {
            showError(inputUserPassword, "Hasło musi mieć więcej niż 7 znaków");
        } else {
            return;
        }
    }

    private void showError(EditText inputField, String errorMsg) {
        inputField.setError(errorMsg);
        inputField.requestFocus();
    }


}