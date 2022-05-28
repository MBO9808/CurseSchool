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
import android.widget.Toast;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.Course;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private TextView paymentDate;
    private EditText payment;
    private TextView signDate;
    private ExtendedFloatingActionButton saveNewCourse;
    private DatePickerDialog.OnDateSetListener setListenerOnStartDate;
    private DatePickerDialog.OnDateSetListener setListenerOnEndDate;
    private DatePickerDialog.OnDateSetListener setListenerOnPaymentDate;
    private DatePickerDialog.OnDateSetListener setListenerOnSignDate;
    private ArrayList<String> teacherNameList;
    private ArrayList<String> languageNameList;
    private ArrayList<String> advancementNameList;

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

    private void setToolbar() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth, setListenerOnStartDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListenerOnStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month += 1;
                String monthTxt = String.valueOf(month);
                if (month < 10)
                    monthTxt = "0" + month;
                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10)
                    dayOfMonthTxt = "0" + dayOfMonthTxt;
                String date = year + "-" + monthTxt + "-" + dayOfMonthTxt;
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
                month += 1;
                String monthTxt = String.valueOf(month);
                if (month < 10) {
                    monthTxt = "0" + month;
                }

                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    dayOfMonthTxt = "0" + dayOfMonthTxt;
                }

                String date = year + "-" + monthTxt + "-" + dayOfMonthTxt;
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
                month += 1;
                String monthTxt = String.valueOf(month);
                if (month < 10) {
                    monthTxt = "0" + month;
                }

                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    dayOfMonthTxt = "0" + dayOfMonthTxt;
                }

                String date = year + "-" + monthTxt + "-" + dayOfMonthTxt;
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
                month += 1;
                String monthTxt = String.valueOf(month);
                if (month < 10) {
                    monthTxt = "0" + month;
                }

                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    dayOfMonthTxt = "0" + dayOfMonth;
                }

                String date = year + "-" + monthTxt + "-" + dayOfMonthTxt;
                signDate.setText(date);
            }
        };
    }

    private void createSpinners() {
        createTeacherSpinner();
        createLanguageSpinner();
        createAdvancementSpinner();
    }

    private void createTeacherSpinner() {
        teacherNameList = getTeachersList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, teacherNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSpinner.setAdapter(spinnerAdapter);
    }

    private void createLanguageSpinner() {
        languageNameList = getLanguageList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, languageNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(spinnerAdapter);
    }

    private void createAdvancementSpinner() {
        advancementNameList = getAdvancementList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, advancementNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        advancementSpinner.setAdapter(spinnerAdapter);
    }

    private ArrayList<String> getTeachersList() {
        ArrayList<String> teacherList = new ArrayList<>();
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
                    teacherList.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return teacherList;
    }

    private ArrayList<String> getLanguageList() {
        ArrayList<String> languageList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select name from course_languages where archival = 0 order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    languageList.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return languageList;
    }

    private ArrayList<String> getAdvancementList() {
        ArrayList<String> advancementList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select name from course_advancement where archival = 0 order by id asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    advancementList.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return advancementList;
    }

    private ArrayList<String> getClassRoomList() {
        ArrayList<String> classRoomList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select number from class_room where archival = 0 order by number asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = String.valueOf(resultSet.getInt(1));
                    classRoomList.add(name);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return classRoomList;
    }

    private void setSaveListener() {
        saveNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == saveNewCourse.getId()) {
                    saveNewCourse();
                }
            }
        });
    }

    private void saveNewCourse() {
        String name = courseName.getText().toString();
        if (name == null || name.equals("")) {
            Toast.makeText(getContext(), "Proszę uzupełnić nazwę kursu", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedTeacherPosition = teacherSpinner.getSelectedItemPosition();
        String teacherName = teacherNameList.get(selectedTeacherPosition);
        int teacherId = getTeacherId(teacherName);

        int selectedLanguagePosition = languageSpinner.getSelectedItemPosition();
        String languageName = languageNameList.get(selectedLanguagePosition);
        int languageId = getLanguageId(languageName);

        int selectedAdvancementPosition = advancementSpinner.getSelectedItemPosition();
        String advancementName = advancementNameList.get(selectedAdvancementPosition);
        int advancementId = getAdvancementId(advancementName);

        String studentLimit = maxStudent.getText().toString();
        if (studentLimit == null || studentLimit.equals("")) {
            Toast.makeText(getContext(), "Proszę uzupełnić liczbe studentów", Toast.LENGTH_SHORT).show();
            return;
        }

        String startDateTxt = courseStartDate.getText().toString();
        if (startDateTxt == null || startDateTxt.equals("")) {
            Toast.makeText(getContext(), "Proszę uzupełnić datę rozpoczęcia kursu", Toast.LENGTH_SHORT).show();
            return;
        }
        java.sql.Date startDate = java.sql.Date.valueOf(startDateTxt);

        String endDateTxt = courseEndDate.getText().toString();
        if (endDateTxt == null || endDateTxt.equals("")) {
            Toast.makeText(getContext(), "Proszę uzupełnić datę zakończenia kursu", Toast.LENGTH_SHORT).show();
            return;
        }
        java.sql.Date endDate = java.sql.Date.valueOf(endDateTxt);

        if(startDate.after(endDate)){
            Toast.makeText(getContext(), "Data rozpoczęcia kursu nie może być późniejsza niż data zakończenia kursu",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String dateOfPaymentTxt = paymentDate.getText().toString();
        if (dateOfPaymentTxt == null || dateOfPaymentTxt.equals("")) {
            Toast.makeText(getContext(), "Proszę wybrać termin płatności", Toast.LENGTH_SHORT).show();
            return;
        }
        java.sql.Date dateOfPayment = java.sql.Date.valueOf(dateOfPaymentTxt);

        String cost = payment.getText().toString();
        if (cost == null || cost.equals("")) {
            Toast.makeText(getContext(), "Proszę uzupełnić koszt kursu", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateOfSignTxt = signDate.getText().toString();
        if (dateOfSignTxt == null || dateOfSignTxt.equals("")) {
            Toast.makeText(getContext(), "Proszę uzupełnić ostateczną datę zapisów", Toast.LENGTH_SHORT).show();
            return;
        }
        java.sql.Date dateOfSign = java.sql.Date.valueOf(dateOfSignTxt);
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateStr = simpleDateFormat.format(currentDate);
        java.sql.Date creationDate = java.sql.Date.valueOf(currentDateStr);

        String query = "INSERT INTO courses (id, course_name, teacher_id, language_id, course_advancement_id, " +
                "max_students_number, start_date, end_date, payment_date, payment, creation_date, archival, sign_date) "
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            int courseId = findMaxId();
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, courseId);
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3, teacherId);
                preparedStatement.setInt(4, languageId);
                preparedStatement.setInt(5, advancementId);
                preparedStatement.setInt(6, Integer.valueOf(studentLimit));
                preparedStatement.setDate(7, startDate);
                preparedStatement.setDate(8, endDate);
                preparedStatement.setDate(9, dateOfPayment);
                preparedStatement.setFloat(10, Float.valueOf(cost));
                preparedStatement.setDate(11, creationDate);
                preparedStatement.setBoolean(12, false);
                preparedStatement.setDate(13, dateOfSign);
                preparedStatement.execute();
                connect.close();

            } else {
                Toast.makeText(getContext(), "Wystąpił problem z połączeniem z internetem", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        Intent intent = new Intent(getContext(), CourseView.class);
        Toast.makeText(getContext(), "Zapisano nowy kurs", Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }

    private int getTeacherId(String teacherName) {
        int teacherId = 0;
        String parts[] = teacherName.split("/");
        String teacherIdn = parts[1];

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select id from users where idn = '" + teacherIdn + "'";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    teacherId = resultSet.getInt(1);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return teacherId;
    }

    private int getLanguageId(String languageName) {
        int languageId = 0;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select id from course_languages where archival = 0 and name = '" + languageName + "'";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    languageId = resultSet.getInt(1);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return languageId;
    }

    private int getAdvancementId(String advancementName) {
        int advancementId = 0;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select id from course_advancement where archival = 0 and name = '" + advancementName + "'";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    advancementId = resultSet.getInt(1);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return advancementId;
    }

    private int findMaxId() {
        Integer id = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select MAX(id) from courses";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                connect.close();

            } else {
                Toast.makeText(getContext(), "Wystąpił problem z połączeniem z internetem", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        if (id == null)
            return 1;
        else
            return id + 1;
    }

}