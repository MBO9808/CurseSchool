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
import com.example.curseschool.DialogHandlers.GradeNameDialogCloseHandler;
import com.example.curseschool.Objects.GradeName;
import com.example.curseschool.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class NewGradeNameHandler extends BottomSheetDialogFragment {

    public static final String TAG = "NewGradeNameDialog";
    private EditText gradeName;
    private Button saveButton;
    private String connectionResult = "";

    public static NewGradeNameHandler newInstance() {
        return new NewGradeNameHandler();
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
        View view = inflater.inflate(R.layout.add_new_grade_name, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gradeName = requireView().findViewById(R.id.textAddNewGradeName);
        saveButton = requireView().findViewById(R.id.saveNewGradeName);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            String name = bundle.getString("name");
            gradeName.setText(name);
            assert name != null;
            if (name.length() > 0)
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
        }

        gradeName.addTextChangedListener(new TextWatcher() {
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
                String name = gradeName.getText().toString();
                if (name != null && !name.equals("")) {
                    String text = "Typ oceny o nazwie " + name + " już istnieje w słowniku.";
                    if (updated) {
                        int id = bundle.getInt("id");
                        GradeName gradeName = new GradeName(id, name);
                        boolean gradeNameAlreadyExists = isGradeNameAlreadyExists(gradeName);
                        if (gradeNameAlreadyExists == false) {
                            updateGradeName(gradeName);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        int id = findMaxId();
                        GradeName gradeName = new GradeName(id, name);
                        boolean gradeNameAlreadyExists = isGradeNameAlreadyExists(gradeName);
                        if (gradeNameAlreadyExists == false) {
                            addNewGradeName(gradeName);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Proszę wpisać nazwę typu oceny", Toast.LENGTH_LONG).show();
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
                String query = "Select MAX(id) from grade_type";
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
        if (activity instanceof GradeNameDialogCloseHandler)
            ((GradeNameDialogCloseHandler) activity).handleDialogClose(dialog);
    }

    private void updateGradeName(GradeName gradeName) {
        int id = gradeName.getId();
        String name = gradeName.getName();
        String query = "UPDATE grade_type SET name = '" + name + "' WHERE id = " + id;
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

    private void addNewGradeName(GradeName gradeName) {
        int id = gradeName.getId();
        String name = gradeName.getName();
        String query = "INSERT INTO grade_type (id, name, archival) "
                + " VALUES(?,?,?)";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setBoolean(3, false);
                preparedStatement.execute();
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    private boolean isGradeNameAlreadyExists(GradeName gradeName) {
        int id = gradeName.getId();
        String name = gradeName.getName();
        GradeName GradeNameDb = getGradeNameFromDb(name);
        if (GradeNameDb == null)
            return false;

        int GradeNameDbId = GradeNameDb.getId();
        String GradeNameDbName = GradeNameDb.getName();
        if (id != GradeNameDbId && name.equals(GradeNameDbName))
            return true;

        return false;

    }

    private GradeName getGradeNameFromDb(String name) {
        GradeName gradeName = null;
        String query = "SELECT * FROM grade_type WHERE archival = 0 and name = '" + name + "'";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next() != false) {
                    int id = resultSet.getInt(1);
                    String nameGrade = resultSet.getString(2);
                    gradeName = new GradeName(id, nameGrade);
                }
                connect.close();
            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return gradeName;
    }
}
