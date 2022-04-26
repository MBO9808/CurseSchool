package com.example.curseschool.NewObjectsHandlers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.curseschool.DialogHandlers.LanguageDialogCloseHandler;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNotificationHandler extends BottomSheetDialogFragment {

    public static final String TAG = "NewNotificationDialog";
    private EditText notificationDescription;
    private String MyPREFERENCES = "userData";
    private Button saveButton;
    private String connectionResult = "";

    public static NewNotificationHandler newInstance() {
        return new NewNotificationHandler();
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
        View view = inflater.inflate(R.layout.add_new_notification, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationDescription = requireView().findViewById(R.id.textAddNewNotification);
        saveButton = requireView().findViewById(R.id.saveNewNotification);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            String description = bundle.getString("description");
            notificationDescription.setText(description);
            assert description != null;
            if (description.length() > 0)
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
        }

        notificationDescription.addTextChangedListener(new TextWatcher() {
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
                String description = notificationDescription.getText().toString();
                if (description != null && !description.equals("")) {
                    if (updated) {
                        int id = bundle.getInt("id");
                            updateNotification(id, description);
                            dismiss();
                    } else {
                            addNewNotification(description);
                            dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "Proszę wpisać język", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private int getCurrentUserId() {
        SharedPreferences sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        return id;
    }

    private int findMaxId() {
        Integer id = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select MAX(id) from notification";
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
        if (activity instanceof LanguageDialogCloseHandler)
            ((LanguageDialogCloseHandler) activity).handleDialogClose(dialog);
    }

    private void updateNotification(int id, String description) {
        int userId = getCurrentUserId();
        String query = "UPDATE notification SET description = '" + description + "', user_id = " + userId + " WHERE id = " + id;
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

    private void addNewNotification(String description) {
        int id = findMaxId();
        int userId = getCurrentUserId();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String dateStr = dateFormat.format(date);
        String query = "INSERT INTO notification (id, user_id, description, created_date, archival) "
                + " VALUES(?,?,?,?,?)";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(3, description);
                preparedStatement.setString(4,dateStr);
                preparedStatement.setBoolean(5,false);
                preparedStatement.execute();
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

}