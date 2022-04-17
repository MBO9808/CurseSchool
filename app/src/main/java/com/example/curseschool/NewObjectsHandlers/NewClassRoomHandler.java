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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.DialogHandlers.ClassRoomDialogCloseHandler;
import com.example.curseschool.Objects.ClassRoom;
import com.example.curseschool.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class NewClassRoomHandler extends BottomSheetDialogFragment {

    public static final String TAG = "NewClassRoomDialog";
    private EditText classRoomNumber;
    private Button saveButton;
    private String connectionResult = "";

    public static NewClassRoomHandler newInstance() {
        return new NewClassRoomHandler();
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
        View view = inflater.inflate(R.layout.add_new_class_room, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classRoomNumber = requireView().findViewById(R.id.textAddNewClassRoom);
        saveButton = requireView().findViewById(R.id.saveNewClassRoom);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            Integer number = bundle.getInt("number");
            classRoomNumber.setText(String.valueOf(number));
            if (number != null)
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
        }

        classRoomNumber.addTextChangedListener(new TextWatcher() {
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
                String numberTxt = classRoomNumber.getText().toString();
                if (numberTxt != null && !numberTxt.equals("")) {
                    Integer number = Integer.valueOf(classRoomNumber.getText().toString());
                    String text = "Sale o numerze " + number + " już istnieje w słowniku.";
                    if (updated) {
                        int id = bundle.getInt("id");
                        ClassRoom classRoom = new ClassRoom(id, number);
                        boolean classRoomAlreadyExists = isClassRoomAlreadyExists(classRoom);
                        if (classRoomAlreadyExists == false) {
                            updateClassRoom(classRoom);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        int id = findMaxId();
                        ClassRoom classRoom = new ClassRoom(id, number);
                        boolean classRoomAlreadyExists = isClassRoomAlreadyExists(classRoom);
                        if (classRoomAlreadyExists == false) {
                            addNewClassRoom(classRoom);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Proszę wpisać numer sali", Toast.LENGTH_LONG).show();
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
                String query = "Select MAX(id) from class_room";
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
        if (activity instanceof ClassRoomDialogCloseHandler)
            ((ClassRoomDialogCloseHandler) activity).handleDialogClose(dialog);
    }

    private void updateClassRoom(ClassRoom classRoom) {
        int id = classRoom.getId();
        int number = classRoom.getClassRoom();
        String query = "UPDATE class_room SET number = '" + number + "' WHERE id = " + id;
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

    private void addNewClassRoom(ClassRoom classRoom) {
        int id = classRoom.getId();
        int number = classRoom.getClassRoom();
        String query = "INSERT INTO class_room (id, number) "
                + " VALUES(?,?)";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, number);
                preparedStatement.execute();
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    private boolean isClassRoomAlreadyExists(ClassRoom classRoom) {
        int id = classRoom.getId();
        int number = classRoom.getClassRoom();
        ClassRoom classRoomDb = getClassRoomFromDb(number);
        if (classRoomDb == null)
            return false;

        int classRoomDbId = classRoomDb.getId();
        int classRoomDbName = classRoomDb.getClassRoom();
        if (id != classRoomDbId && number == classRoomDbName)
            return true;

        return false;

    }

    private ClassRoom getClassRoomFromDb(int number) {
        ClassRoom classRoom = null;
        String query = "SELECT * FROM class_room WHERE number = '" + number + "'";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next() != false) {
                    int id = resultSet.getInt(1);
                    int classRoomNo = resultSet.getInt(2);
                    classRoom = new ClassRoom(id, classRoomNo);
                }
                connect.close();
            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return classRoom;
    }
}
