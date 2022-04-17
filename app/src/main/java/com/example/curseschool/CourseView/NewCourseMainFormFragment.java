package com.example.curseschool.CourseView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class NewCourseMainFormFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private EditText courseName;
    private Spinner teacherSpinner;
    private Spinner languageSpinner;
    private Spinner advancementSpinner;
    private EditText maxStudent;
    private TextView courseStartDate;
    private TextView courseEndDate;
    private Spinner classRoomSpinner;
    private TextView paymentDate;
    private EditText payment;
    private TextView signDate;
    private Button saveNewCourse;
    private DatePickerDialog.OnDateSetListener setListenerOnStartDate;
    private DatePickerDialog.OnDateSetListener setListenerOnEndDate;
    private DatePickerDialog.OnDateSetListener setListenerOnPaymentDate;
    private DatePickerDialog.OnDateSetListener setListenerOnSignDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_course_main_form, container, false);
        setToolbar();
        initFields();
        createDatePickers();
        createSpinners();
        setSaveListener();
        return view;
    }

    private void setToolbar(){
        toolbar = view.findViewById(R.id.include);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Nowy kurs");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CourseView.class);
                startActivity(intent);
            }
        });
    }

    private void initFields() {
        courseName = view.findViewById(R.id.newCourseFormName);
        teacherSpinner = view.findViewById(R.id.newCourseFormTeacher);
        languageSpinner = view.findViewById(R.id.newCourseFormLanguage);
        advancementSpinner = view.findViewById(R.id.newCourseFormAdvancement);
        maxStudent = view.findViewById(R.id.newCourseFormMaxStudents);
        maxStudent.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        courseStartDate = view.findViewById(R.id.newCourseFormStartDate);
        courseEndDate = view.findViewById(R.id.newCourseFormEndDate);
        classRoomSpinner = view.findViewById(R.id.newCourseFormClassRoom);
        paymentDate = view.findViewById(R.id.newCourseFormPaymentDateTo);
        payment = view.findViewById(R.id.newCourseFormPaymentValue);
        signDate = view.findViewById(R.id.newCourseFormEnroll);
        saveNewCourse = view.findViewById(R.id.saveNewCourse);
    }

    private void createDatePickers() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        createStartDatePicker(year, month, day);
        createEndDatePicker(year, month, day);
        createPaymentDatePicker(year, month, day);
        createSignDatePicker(year, month, day);
    }

    private void createStartDatePicker(int year, int month, int day) {
        courseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, setListenerOnStartDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListenerOnStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthTxt = String.valueOf(month);
                if(month < 10){
                    monthTxt = "0"+month;
                }

                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10){
                    dayOfMonthTxt = "0"+month;
                }

                String date = dayOfMonthTxt + "-" + monthTxt + "-" + year;
                courseStartDate.setText(date);
            }
        };
    }

    private void createEndDatePicker(int year, int month, int day) {
        courseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, setListenerOnEndDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListenerOnEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthTxt = String.valueOf(month);
                if(month < 10){
                    monthTxt = "0"+month;
                }

                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10){
                    dayOfMonthTxt = "0"+month;
                }

                String date = dayOfMonthTxt + "-" + monthTxt + "-" + year;
                courseEndDate.setText(date);
            }
        };
    }

    private void createPaymentDatePicker(int year, int month, int day) {
        paymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, setListenerOnPaymentDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListenerOnPaymentDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthTxt = String.valueOf(month);
                if(month < 10){
                    monthTxt = "0"+month;
                }

                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10){
                    dayOfMonthTxt = "0"+month;
                }

                String date = dayOfMonthTxt + "-" + monthTxt + "-" + year;
                paymentDate.setText(date);
            }
        };
    }

    private void createSignDatePicker(int year, int month, int day) {
        signDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, setListenerOnSignDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListenerOnSignDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthTxt = String.valueOf(month);
                if(month < 10){
                    monthTxt = "0"+month;
                }

                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10){
                    dayOfMonthTxt = "0"+month;
                }

                String date = dayOfMonthTxt + "-" + monthTxt + "-" + year;
                signDate.setText(date);
            }
        };
    }

    private void createSpinners() {
        createTeacherSpinner();
        createLanguageSpinner();
        createAdvancementSpinner();
        createClassRoomSpinner();
    }

    private void createTeacherSpinner() {
        ArrayList<String> teacherNameList = getTeachersList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, teacherNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSpinner.setAdapter(spinnerAdapter);
    }

    private void createLanguageSpinner() {
        ArrayList<String> languageNameList = getLanguageList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, languageNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(spinnerAdapter);
    }

    private void createAdvancementSpinner() {
        ArrayList<String> advancementNameList = getAdvancementList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, advancementNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        advancementSpinner.setAdapter(spinnerAdapter);
    }

    private void createClassRoomSpinner() {
        ArrayList<String> classRoomNameList = getClassRoomList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, classRoomNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classRoomSpinner.setAdapter(spinnerAdapter);
    }

    private ArrayList<String> getTeachersList() {
        ArrayList<String> teacherNameList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select forename, surname, idn from users where archival = 0 and user_type = 'admin' or user_type = 'teacher' order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String forename = resultSet.getString(1);
                    String surname = resultSet.getString(2);
                    String idn = resultSet.getString(3);
                    String name = forename + " " + surname + "/" + idn;
                    teacherNameList.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return teacherNameList;
    }

    private ArrayList<String> getLanguageList() {
        ArrayList<String> languageNameList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select name from course_languages order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    languageNameList.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return languageNameList;
    }

    private ArrayList<String> getAdvancementList() {
        ArrayList<String> advancementNameList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select name from course_advancement order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    advancementNameList.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return advancementNameList;
    }

    private ArrayList<String> getClassRoomList() {
        ArrayList<String> classRoomNameList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select number from class_room order by number asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = String.valueOf(resultSet.getInt(1));
                    classRoomNameList.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return classRoomNameList;
    }

    private void setSaveListener() {
        saveNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}