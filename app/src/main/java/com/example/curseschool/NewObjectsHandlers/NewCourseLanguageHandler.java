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
import com.example.curseschool.DialogHandlers.LanguageDialogCloseHandler;
import com.example.curseschool.Objects.CourseLanguage;
import com.example.curseschool.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class NewCourseLanguageHandler extends BottomSheetDialogFragment {

    public static final String TAG = "NewLanguageDialog";
    private EditText languageName;
    private Button saveButton;
    private String connectionResult = "";

    public static NewCourseLanguageHandler newInstance() {
        return new NewCourseLanguageHandler();
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
        View view = inflater.inflate(R.layout.add_new_language, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        languageName = requireView().findViewById(R.id.textAddNewLanguage);
        saveButton = requireView().findViewById(R.id.saveNewLanguage);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            String name = bundle.getString("language");
            languageName.setText(name);
            assert name != null;
            if (name.length() > 0)
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
        }

        languageName.addTextChangedListener(new TextWatcher() {
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
                String name = languageName.getText().toString();
                String text = "Język " + name + " już istnieje w słowniku.";
                if (updated) {
                    int id = bundle.getInt("id");
                    CourseLanguage courseLanguage = new CourseLanguage(id, name);
                    boolean languageAlreadyExists = isLanguageAlreadyExists(courseLanguage);
                    if(languageAlreadyExists == false) {
                        updateLanguage(courseLanguage);
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                    }
                } else {
                    int id = findMaxId();
                    CourseLanguage courseLanguage = new CourseLanguage(id, name);
                    boolean languageAlreadyExists = isLanguageAlreadyExists(courseLanguage);
                            if(languageAlreadyExists == false) {
                                addNewLanguage(courseLanguage);
                                dismiss();
                            } else {
                                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                            }
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
                String query = "Select MAX(id) from course_languages";
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

    private void updateLanguage(CourseLanguage courseLanguage) {
        int id = courseLanguage.getId();
        String name = courseLanguage.getLanguage();
        String query = "UPDATE course_languages SET name = '" + name + "' WHERE id = " + id;
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

    private void addNewLanguage(CourseLanguage courseLanguage) {
        int id = courseLanguage.getId();
        String name = courseLanguage.getLanguage();
        String query = "INSERT INTO course_languages (id, name) "
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

    private boolean isLanguageAlreadyExists(CourseLanguage courseLanguage){
        int id = courseLanguage.getId();
        String name = courseLanguage.getLanguage();
        CourseLanguage courseLanguageDb = getLanguageFromDb(name);
        if(courseLanguageDb == null)
            return false;

        int courseDbId = courseLanguageDb.getId();
        String courseDbName = courseLanguageDb.getLanguage();
        if(id != courseDbId && name.equals(courseDbName))
            return true;

        return false;

    }

    private CourseLanguage getLanguageFromDb(String name){
        CourseLanguage courseLanguage = null;
        String query = "SELECT * FROM course_languages WHERE name = '" + name + "'";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if(resultSet.next() != false){
                    int id = resultSet.getInt(1);
                    String language = resultSet.getString(2);
                    courseLanguage = new CourseLanguage(id, language);
                }
                connect.close();
            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return courseLanguage;
    }

}
