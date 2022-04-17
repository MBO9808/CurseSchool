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
import com.example.curseschool.DialogHandlers.CourseAdvancementDialogCloseHandler;
import com.example.curseschool.Objects.CourseAdvancement;
import com.example.curseschool.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class NewCourseAdvancementHandler extends BottomSheetDialogFragment {

    public static final String TAG = "NewCourseAdvancementDialog";
    private EditText courseAdvancementName;
    private Button saveButton;
    private String connectionResult = "";

    public static NewCourseAdvancementHandler newInstance() {
        return new NewCourseAdvancementHandler();
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
        View view = inflater.inflate(R.layout.add_new_course_advancement, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseAdvancementName = requireView().findViewById(R.id.textAddNewCourseAdvancement);
        saveButton = requireView().findViewById(R.id.saveNewCourseAdvancement);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            String name = bundle.getString("name");
            courseAdvancementName.setText(name);
            assert name != null;
            if (name.length() > 0)
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
        }

        courseAdvancementName.addTextChangedListener(new TextWatcher() {
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
                String name = courseAdvancementName.getText().toString();
                if (name != null && !name.equals("")) {
                    String text = "Poziom zaawansowania kursu o nazwie " + name + " już istnieje w słowniku.";
                    if (updated) {
                        int id = bundle.getInt("id");
                        CourseAdvancement courseAdvancement = new CourseAdvancement(id, name);
                        boolean classRoomAlreadyExists = isCourseAdvancementAlreadyExists(courseAdvancement);
                        if (classRoomAlreadyExists == false) {
                            updateCourseAdvancement(courseAdvancement);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        int id = findMaxId();
                        CourseAdvancement courseAdvancement = new CourseAdvancement(id, name);
                        boolean courseAdvancementAlreadyExists = isCourseAdvancementAlreadyExists(courseAdvancement);
                        if (courseAdvancementAlreadyExists == false) {
                            addNewCourseAdvancement(courseAdvancement);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Proszę wpisać nazwę poziomu zaawansowania", Toast.LENGTH_LONG).show();
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
                String query = "Select MAX(id) from course_advancement";
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
        if (activity instanceof CourseAdvancementDialogCloseHandler)
            ((CourseAdvancementDialogCloseHandler) activity).handleDialogClose(dialog);
    }

    private void updateCourseAdvancement(CourseAdvancement courseAdvancement) {
        int id = courseAdvancement.getId();
        String name = courseAdvancement.getName();
        String query = "UPDATE course_advancement SET name = '" + name + "' WHERE id = " + id;
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

    private void addNewCourseAdvancement(CourseAdvancement courseAdvancement) {
        int id = courseAdvancement.getId();
        String name = courseAdvancement.getName();
        String query = "INSERT INTO course_advancement (id, name) "
                + " VALUES(?,?)";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.execute();
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    private boolean isCourseAdvancementAlreadyExists(CourseAdvancement courseAdvancement) {
        int id = courseAdvancement.getId();
        String name = courseAdvancement.getName();
        CourseAdvancement courseAdvancementDb = getCourseAdvancementFromDb(name);
        if (courseAdvancementDb == null)
            return false;

        int courseAdvancementDbId = courseAdvancementDb.getId();
        String courseAdvancementDbName = courseAdvancementDb.getName();
        if (id != courseAdvancementDbId && name.equals(courseAdvancementDbName))
            return true;

        return false;

    }

    private CourseAdvancement getCourseAdvancementFromDb(String name) {
        CourseAdvancement courseAdvancement = null;
        String query = "SELECT * FROM course_advancement WHERE name = '" + name + "'";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next() != false) {
                    int id = resultSet.getInt(1);
                    String nameAdvancement = resultSet.getString(2);
                    courseAdvancement = new CourseAdvancement(id, nameAdvancement);
                }
                connect.close();
            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return courseAdvancement;
    }
}