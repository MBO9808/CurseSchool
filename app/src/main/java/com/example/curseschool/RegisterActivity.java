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
        alreadySignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void handleRegister() {
        signInButton = findViewById(R.id.buttonRegisterUser);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {
        checkFirstName();
        checkLastName();
        checkEmail();
        checkPassword();
        checkPhoneNumber();
        checkCity();
        checkStreet();
        checkPostalCode();
    }

    private void checkFirstName() {
        inputUserFirstName = findViewById(R.id.inputUserFirstName);
        String userFirstName = inputUserFirstName.getText().toString();
        if (userFirstName.isEmpty())
            showError(inputUserFirstName, "Nie podano imienia!");
    }

    private void checkLastName() {
        inputUserLastName = findViewById(R.id.inputUserLastName);
        String userFirstName = inputUserLastName.getText().toString();
        if (userFirstName.isEmpty())
            showError(inputUserLastName, "Nie podano nazwiska!");
    }

    private void checkEmail() {
        inputUserEmail = findViewById(R.id.inputUserEmail);
        String userEmail = inputUserEmail.getText().toString();
        if (userEmail.isEmpty())
            showError(inputUserEmail, "Nie podano adresu email!");

        if (!userEmail.contains("@"))
            showError(inputUserEmail, "Nieprawidłowy adres email!");
    }

    private void checkPassword() {
        inputUserPassword = findViewById(R.id.inputUserPassword);
        String userPassword = inputUserPassword.getText().toString();
        if (userPassword.isEmpty())
            showError(inputUserPassword, "Nie podano hasła!");

        if (userPassword.length() < 7)
            showError(inputUserPassword, "Hasło musi mieć więcej niż 7 znaków");
    }

    private void checkPhoneNumber() {
        inputUserPhoneNumber = findViewById(R.id.inputUserPhoneNumber);
        String userPhoneNumber = inputUserPhoneNumber.getText().toString();
        if (userPhoneNumber.isEmpty())
            showError(inputUserPhoneNumber, "Nie podano numeru telefonu!");

        if (userPhoneNumber.length() < 7)
            showError(inputUserPhoneNumber, "Numer telefonu musi mieć więcej niż 9 cyfr");
    }

    private void checkCity() {
        inputUserCity = findViewById(R.id.inputUserCity);
        String userCity = inputUserCity.getText().toString();
        if (userCity.isEmpty())
            showError(inputUserCity, "Nie podano miasta!");
    }

    private void checkStreet() {
        inputUserStreet = findViewById(R.id.inputUserStreet);
        String userStreet = inputUserStreet.getText().toString();
        if (userStreet.isEmpty())
            showError(inputUserStreet, "Nie podano ulicy!");
    }

    private void checkPostalCode() {
        inputUserPostalCode = findViewById(R.id.inputUserPostalCode);
        String userPostalCode = inputUserPostalCode.getText().toString();
        if (userPostalCode.isEmpty())
            showError(inputUserPostalCode, "Nie podano kodu pocztowego!");

        if (!userPostalCode.contains("-"))
            showError(inputUserPostalCode, "Nieprawidłowy kod pocztowy!");
    }

    private void showError(EditText inputUserFirstName, String errorMsg) {
        inputUserFirstName.setError(errorMsg);
    }


}