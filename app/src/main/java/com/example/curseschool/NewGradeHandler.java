package com.example.curseschool;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class NewGradeHandler extends BottomSheetDialogFragment {

    public static final String TAG = "NewGradeDialog";
    private EditText gradeText;
    private Spinner gradeType;
    private Button saveButton;
    private String connectionResult = "";
    private ArrayList<String> gradeNames = new ArrayList<>();
    private int studentId;
    private int courseId;

    public NewGradeHandler(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public static NewGradeHandler newInstance(int studentId, int courseId) {
        return new NewGradeHandler(studentId, courseId);
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
        View view = inflater.inflate(R.layout.add_new_grade, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gradeText = requireView().findViewById(R.id.grade);
        gradeType = requireView().findViewById(R.id.gradeSpinner);
        gradeNames = createGradeNameListData();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, gradeNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeType.setAdapter(spinnerAdapter);
        saveButton = requireView().findViewById(R.id.saveNewGrade);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            Float gradeValue = bundle.getFloat("gradeValue");
            Integer gradeTypeId = bundle.getInt("gradeTypeId");
            String gradeName = getGradeName(gradeTypeId);
            gradeType.setSelection(spinnerAdapter.getPosition(gradeName));
            gradeText.setText(String.valueOf(gradeValue));
            assert gradeValue != null;
            if (gradeValue != null && gradeTypeId != null)
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
        }

        gradeText.addTextChangedListener(new TextWatcher() {
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
                Float value = Float.valueOf(gradeText.getText().toString());
                int gradePosition = gradeType.getSelectedItemPosition();
                String name = gradeNames.get(gradePosition);
                int gradeId = getGradeNameId(name);
                if (updated) {
                    int id = bundle.getInt("id");
                    updateGrade(id, gradeId, value);
                    dismiss();
                } else {
                    int id = findMaxId();
                    int gradePos = gradeType.getSelectedItemPosition();
                    String gradeN = gradeNames.get(gradePos);
                    int gradeNameId = getGradeNameId(gradeN);
                    Float valueGrade = Float.valueOf(gradeText.getText().toString());
                    Grade grade = new Grade(id, studentId, courseId, gradeNameId, valueGrade);
                    addNewGrade(grade);
                    dismiss();
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
                String query = "Select MAX(id) from grades";
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
        if (activity instanceof StudentGradeDialogCloseHandler)
            ((StudentGradeDialogCloseHandler) activity).handleDialogClose(dialog);
    }

    private void updateGrade(int id, int gradeId, Float value) {
        String query = "UPDATE grades SET grade = '" + value + "', grade_name_id = " + gradeId + "  WHERE id = " + id;
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

    private void addNewGrade(Grade grade) {
        String query = "INSERT INTO grades (id, student_id, course_id, grade_name_id, grade) "
                + " VALUES(?,?,?,?,?)";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, grade.getId());
                preparedStatement.setInt(2, grade.getStudentId());
                preparedStatement.setInt(3, grade.getCourseId());
                preparedStatement.setInt(4, grade.getGradeNameId());
                preparedStatement.setFloat(5, grade.getGrade());
                preparedStatement.execute();
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    private ArrayList<String> createGradeNameListData() {
        ArrayList<String> gradeNames = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from grade_type order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    gradeNames.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return gradeNames;
    }

    private String getGradeName(Integer gradeNameId) {
        ArrayList<GradeName> gradeNamesList = getFullGradeNameListData();
        String name = "";
        for (GradeName grade : gradeNamesList) {
            int gradeId = grade.getId();
            if (gradeId == gradeNameId)
                name = grade.getName();
        }
        return name;
    }

    private int getGradeNameId(String gradeName) {
        ArrayList<GradeName> gradeNamesList = getFullGradeNameListData();
        int gradeId = 0;
        for (GradeName grade : gradeNamesList) {
            String gradeN = grade.getName();
            if (gradeN.equals(gradeName))
                gradeId = grade.getId();
        }
        return gradeId;
    }

    private ArrayList<GradeName> getFullGradeNameListData() {
        ArrayList<GradeName> gradeNames = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from grade_type order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    GradeName gradeName = new GradeName(id, name);
                    gradeNames.add(gradeName);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return gradeNames;
    }
}