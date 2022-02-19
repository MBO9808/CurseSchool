package com.example.curseschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputUserFirstName;
    private EditText inputUserLastName;
    private EditText inputUserEmail;
    private EditText inputUserPassword;
    private EditText inputUserPhoneNumber;
    private EditText inputUserCity;
    private EditText inputUserStreet;
    private EditText inputUserPostalCode;
    private TextView signInButton;
    private TextView alreadySignButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        handleAlreadyHaveAccount();
        handleRegister();
    }

    private void handleAlreadyHaveAccount() {
        alreadySignButton = findViewById(R.id.alreadySigned);
        goBackToLogin();
    }

    private void goBackToLogin() {
        alreadySignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void handleRegister() {
        signInButton = findViewById(R.id.buttonRegisterUser);
        register();
    }

    private void register() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {
        initializeFields();
        validateFields();
    }

    private void initializeFields() {
        inputUserFirstName = findViewById(R.id.inputUserFirstName);
        inputUserLastName = findViewById(R.id.inputUserLastName);
        inputUserEmail = findViewById(R.id.inputUserEmail);
        inputUserPassword = findViewById(R.id.inputUserPassword);
        inputUserPhoneNumber = findViewById(R.id.inputUserPhoneNumber);
        inputUserCity = findViewById(R.id.inputUserCity);
        inputUserStreet = findViewById(R.id.inputUserStreet);
        inputUserPostalCode = findViewById(R.id.inputUserPostalCode);
    }

    private void validateFields() {
        String userFirstName = inputUserFirstName.getText().toString();
        String userLastName = inputUserLastName.getText().toString();
        String userEmail = inputUserEmail.getText().toString();
        String userPassword = inputUserPassword.getText().toString();
        String userPhoneNumber = inputUserPhoneNumber.getText().toString();
        String userCity = inputUserCity.getText().toString();
        String userPostalCode = inputUserPostalCode.getText().toString();
        String userStreet = inputUserStreet.getText().toString();

        if (userFirstName.isEmpty()) {
            showError(inputUserFirstName, "Nie podano imienia!");
        } else if (userLastName.isEmpty()) {
            showError(inputUserLastName, "Nie podano nazwiska!");
        } else if (userEmail.isEmpty()) {
            showError(inputUserEmail, "Nie podano adresu email!");
        } else if (!userEmail.contains("@")) {
            showError(inputUserEmail, "Nieprawidłowy adres email!");
        } else if (userPassword.isEmpty()) {
            showError(inputUserPassword, "Nie podano hasła!");
        } else if (userPassword.length() < 7) {
            showError(inputUserPassword, "Hasło musi mieć więcej niż 7 znaków");
        } else if (userPhoneNumber.isEmpty()) {
            showError(inputUserPhoneNumber, "Nie podano numeru telefonu!");
        } else if (userPhoneNumber.length() <= 7) {
            showError(inputUserPhoneNumber, "Numer telefonu musi mieć więcej niż 9 cyfr");
        } else if (userCity.isEmpty()) {
            showError(inputUserCity, "Nie podano miasta!");
        } else if (userStreet.isEmpty()) {
            showError(inputUserStreet, "Nie podano ulicy!");
        } else if (userPostalCode.isEmpty()) {
            showError(inputUserPostalCode, "Nie podano kodu pocztowego!");
        } else if (!userPostalCode.contains("-")) {
            showError(inputUserPostalCode, "Nieprawidłowy kod pocztowy!");
        } else {
            return;
        }
    }

    private void showError(EditText inputField, String errorMsg) {
        inputField.setError(errorMsg);
        inputField.requestFocus();
    }


}