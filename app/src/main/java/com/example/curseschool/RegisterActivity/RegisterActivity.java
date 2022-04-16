package com.example.curseschool.RegisterActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.LoginActivity.LoginActivity;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserKind;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
    private Connection connect;
    private String connectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        handleAlreadyHaveAccount();
        handleRegister();
    }

    @Override
    public void onBackPressed() {
    }

    private void handleAlreadyHaveAccount() {
        alreadySignButton = findViewById(R.id.alreadySigned);
        goBackToLogin();
    }

    private void goBackToLogin() {
        alreadySignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
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
        boolean isAnyFieldEmpty = isEmptyField();
        if (!isAnyFieldEmpty)
            registerNewAccount();
    }

    private void registerNewAccount() {
        boolean emailExists = checkIfEmailExistInDatabase();
        if (emailExists) {
            showError(inputUserEmail, "Podany adres email jest już zajęty!");
        } else {
            addUserToDatabase();
            startLoginActivity();
        }
    }

    private void addUserToDatabase() {
        int userId = findMaxId();
        String userFirstName = inputUserFirstName.getText().toString();
        String userLastName = inputUserLastName.getText().toString();
        String userEmail = inputUserEmail.getText().toString();
        String userPassword = inputUserPassword.getText().toString();
        String userPhoneNumber = inputUserPhoneNumber.getText().toString();
        String userCity = inputUserCity.getText().toString();
        String userPostalCode = inputUserPostalCode.getText().toString();
        String userType = UserKind.student.toString();
        String userStreet = inputUserStreet.getText().toString();
        String idn = "Z00" + userId;
        String query = "INSERT INTO users (id, forename, surname, email, password, phone_number, city, adress, postal_code, user_type, archival, idn) "
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, userFirstName);
                preparedStatement.setString(3, userLastName);
                preparedStatement.setString(4, userEmail);
                preparedStatement.setString(5, userPassword);
                preparedStatement.setString(6, userPhoneNumber);
                preparedStatement.setString(7, userCity);
                preparedStatement.setString(8, userStreet);
                preparedStatement.setString(9, userPostalCode);
                preparedStatement.setString(10, userType);
                preparedStatement.setBoolean(11, false);
                preparedStatement.setString(12, idn);
                preparedStatement.execute();
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
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

    private boolean isEmptyField() {
        boolean isEmpty = false;
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
            isEmpty = true;
        } else if (userLastName.isEmpty()) {
            showError(inputUserLastName, "Nie podano nazwiska!");
            isEmpty = true;
        } else if (userEmail.isEmpty()) {
            showError(inputUserEmail, "Nie podano adresu email!");
            isEmpty = true;
        } else if (!userEmail.contains("@")) {
            showError(inputUserEmail, "Nieprawidłowy adres email!");
            isEmpty = true;
        } else if (userPassword.isEmpty()) {
            showError(inputUserPassword, "Nie podano hasła!");
            isEmpty = true;
        } else if (userPassword.length() < 7) {
            showError(inputUserPassword, "Hasło musi mieć więcej niż 7 znaków");
            isEmpty = true;
        } else if (userPhoneNumber.isEmpty()) {
            showError(inputUserPhoneNumber, "Nie podano numeru telefonu!");
            isEmpty = true;
        } else if (userPhoneNumber.length() <= 7) {
            showError(inputUserPhoneNumber, "Numer telefonu musi mieć więcej niż 9 cyfr");
            isEmpty = true;
        } else if (userCity.isEmpty()) {
            showError(inputUserCity, "Nie podano miasta!");
            isEmpty = true;
        } else if (userStreet.isEmpty()) {
            showError(inputUserStreet, "Nie podano ulicy!");
            isEmpty = true;
        } else if (userPostalCode.isEmpty()) {
            showError(inputUserPostalCode, "Nie podano kodu pocztowego!");
            isEmpty = true;
        } else if (!userPostalCode.contains("-")) {
            showError(inputUserPostalCode, "Nieprawidłowy kod pocztowy!");
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

    private boolean checkIfEmailExistInDatabase() {
        String userEmail = inputUserEmail.getText().toString();
        ResultSet resultSet = getResultSet(userEmail);
        if (resultSet != null)
            return true;

        return false;
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
                connect.close();
            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return resultSet;
    }

    private int findMaxId() {
        Integer id = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select MAX(id) from users";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        if (id == null)
            return 1;
        else
            return id + 1;
    }

    private void startLoginActivity() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

}