package com.example.curseschool.NewObjectsHandlers;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.curseschool.DialogHandlers.GradeNameDialogCloseHandler;
import com.example.curseschool.DialogHandlers.UserDialogCloseHandler;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserKind;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class NewUserHandler extends BottomSheetDialogFragment {

    public static final String TAG = "NewUserDialog";
    private EditText userForename;
    private EditText userSurname;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userPhoneNumber;
    private EditText userCity;
    private EditText userStreet;
    private EditText userPostalCode;
    private Spinner userType;
    private Button saveButton;
    private String connectionResult = "";
    private ArrayList<String> userTypes;

    public static NewUserHandler newInstance() {
        return new NewUserHandler();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.NewLanguageDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_user, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userForename = requireView().findViewById(R.id.textAddNewUserForename);
        userSurname = requireView().findViewById(R.id.textAddNewUserSurname);
        userEmail = requireView().findViewById(R.id.textAddNewUserEmail);
        userPassword = requireView().findViewById(R.id.textAddNewUserPassword);
        userPhoneNumber = requireView().findViewById(R.id.textAddNewUserPhoneNumber);
        userCity = requireView().findViewById(R.id.textAddNewUserCity);
        userStreet = requireView().findViewById(R.id.textAddNewUserStreet);
        userPostalCode = requireView().findViewById(R.id.textAddNewUserPostalCode);
        userType = requireView().findViewById(R.id.textAddNewUserType);
        saveButton = requireView().findViewById(R.id.saveNewGradeName);
        createUserTypeSpinner();
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            String name = bundle.getString("forename");
            String surname = bundle.getString("surname");
            String email = bundle.getString("email");
            String password = bundle.getString("password");
            String phoneNumber = bundle.getString("phoneNumber");
            String city = bundle.getString("city");
            String street = bundle.getString("street");
            String postalCode = bundle.getString("postalCode");
            String type = bundle.getString("type");
            userForename.setText(name);
            userSurname.setText(surname);
            userEmail.setText(email);
            userPassword.setText(password);
            userPhoneNumber.setText(phoneNumber);
            userCity.setText(city);
            userStreet.setText(street);
            userPostalCode.setText(postalCode);
            setSpinner(type);
            assert name != null;
            if (name.length() > 0)
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
        }

        userForename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean updated = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userForename.getText().toString();
                String surname = userSurname.getText().toString();
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();
                String phoneNumber = userPhoneNumber.getText().toString();
                String city = userCity.getText().toString();
                String street = userStreet.getText().toString();
                String postalCode = userPostalCode.getText().toString();
                int selectedTeacherPosition = userType.getSelectedItemPosition();
                String type = getUserKind(userTypes.get(selectedTeacherPosition));
                if (!name.equals("") && !surname.equals("") && !email.equals("") && !password.equals("") && !phoneNumber.equals("") && !city.equals("") && !street.equals("") && !postalCode.equals("")) {
                    String text = "Użytkownik o adresie email: " + email + " już istnieje.";
                    if (updated) {
                        int id = bundle.getInt("id");
                        boolean emailAlreadyExists = isEmailAlreadyExists(email);
                        if (emailAlreadyExists == false) {
                            updateUser(id, name, surname, email, password, phoneNumber, city, street, postalCode, type);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        int id = findMaxId();
                        boolean emailAlreadyExists = isEmailAlreadyExists(email);
                        if (emailAlreadyExists == false) {
                            addNewUser(id, name, surname, email, password, phoneNumber, city, street, postalCode, type);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Proszę uzupełnić wszystkie pola", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private int findMaxId() {
        Integer id = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof UserDialogCloseHandler)
            ((UserDialogCloseHandler) activity).handleDialogClose(dialog);
    }

    private void updateUser(int id, String name, String surname, String email, String password, String phoneNumber, String city, String street, String postalCode, String type) {
        String query = "UPDATE users SET forename = '" + name + "', surname = '" + surname + "', email = '" + email + "', password = '" + password + "',phone_number = '" + phoneNumber + "', city = '" + city + "', adress = '" + street + "', postal_code = '" + postalCode + "', user_type = '" + type + "'  WHERE id = " + id;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                Statement statement = connect.createStatement();
                statement.executeUpdate(query);
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    private void addNewUser(int id, String name, String surname, String email, String password, String phoneNumber, String city, String street, String postalCode, String type) {
        String query = "INSERT INTO users (id, forename, surname, email, password, phone_number, city, adress, postal_code, user_type, archival, idn) "
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, surname);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, password);
                preparedStatement.setString(6, phoneNumber);
                preparedStatement.setString(7, city);
                preparedStatement.setString(8, street);
                preparedStatement.setString(9, postalCode);
                preparedStatement.setString(10, type);
                preparedStatement.setBoolean(11, false);
                preparedStatement.setString(12, "Z00" + id);
                preparedStatement.execute();
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    private boolean isEmailAlreadyExists(String email) {
        String userEmailFromDb = getUserEmailFromDb(email);
        if (userEmailFromDb == "")
            return false;

        if (email.equals(userEmailFromDb))
            return true;

        return false;

    }

    private String getUserEmailFromDb(String userEmail) {
        String email = "";
        String query = "SELECT email FROM users WHERE archival = 0 and email = '" + userEmail + "'";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next() != false) {
                    email = resultSet.getString(1);
                }
                connect.close();
            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return email;
    }

    private void createUserTypeSpinner(){
        userTypes = new ArrayList<>();
        userTypes.add("Administrator");
        userTypes.add("Student");
        userTypes.add("Nauczyciel");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, userTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(spinnerAdapter);
    }

    public String getName(UserKind userKind) {
        if (userKind.equals(UserKind.admin)) {
            return String.valueOf(R.string.admin);
        } else if (userKind.equals(UserKind.teacher)) {
            return String.valueOf(R.string.teacher);
        } else if (userKind.equals(UserKind.student)) {
            return String.valueOf(R.string.student);
        } else {
            return "";
        }
    }

    public String getUserKind(String type) {
        if (type.equals("Administrator")) {
            return UserKind.admin.toString();
        } else if (type.equals(R.string.student)) {
            return UserKind.student.toString();
        } else if (type.equals(R.string.teacher)) {
            return UserKind.teacher.toString();
        } else {
            return UserKind.student.toString();
        }
    }

    private void setSpinner(String type){
        if(type.equals(UserKind.admin.toString())){
            userType.setSelection(0);
        } else if (type.equals(UserKind.student.toString())){
            userType.setSelection(1);
        } else if (type.equals(UserKind.teacher.toString())){
            userType.setSelection(2);
        }
    }
}