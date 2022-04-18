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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EditCourseFormFragment extends Fragment {

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
    private ExtendedFloatingActionButton saveEditCourse;
    private DatePickerDialog.OnDateSetListener setListenerOnStartDate;
    private DatePickerDialog.OnDateSetListener setListenerOnEndDate;
    private DatePickerDialog.OnDateSetListener setListenerOnPaymentDate;
    private DatePickerDialog.OnDateSetListener setListenerOnSignDate;
    private ArrayList<String> teacherNameList;
    private ArrayList<String> languageNameList;
    private ArrayList<String> advancementNameList;
    private ArrayList<String> classRoomNameList;
    private int courseId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_course_form, container, false);
        courseId = getActivity().getIntent().getIntExtra("courseId", 0);
        setToolbar();
        initFields();
        createDatePickers();
        createSpinners();
        setCourseValues();
        setSaveListener();
        return view;
    }

    private void setToolbar() {
        toolbar = view.findViewById(R.id.includeToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Edycja kursu");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CourseView.class);
                startActivity(intent);
            }
        });
    }

    private void initFields() {
        courseName = view.findViewById(R.id.editCourseFormName);
        teacherSpinner = view.findViewById(R.id.editCourseFormTeacher);
        languageSpinner = view.findViewById(R.id.editCourseFormLanguage);
        advancementSpinner = view.findViewById(R.id.editCourseFormAdvancement);
        maxStudent = view.findViewById(R.id.editCourseFormMaxStudents);
        maxStudent.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        courseStartDate = view.findViewById(R.id.editCourseFormStartDate);
        courseEndDate = view.findViewById(R.id.editCourseFormEndDate);
        classRoomSpinner = view.findViewById(R.id.editCourseFormClassRoom);
        paymentDate = view.findViewById(R.id.editCourseFormPaymentDateTo);
        payment = view.findViewById(R.id.editCourseFormPaymentValue);
        signDate = view.findViewById(R.id.editCourseFormEnroll);
        saveEditCourse = view.findViewById(R.id.saveEditCourse);
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
        createClassRoomSpinner();
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

    private void createClassRoomSpinner() {
        classRoomNameList = getClassRoomList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, classRoomNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classRoomSpinner.setAdapter(spinnerAdapter);
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

    private void setCourseValues() {
        Course course = getCourse();
        courseName.setText(course.getCourseName());

        int teacherId = course.getTeacherId();
        int teacherPositionOnList = getTeacherPosition(teacherId);
        teacherSpinner.setSelection(teacherPositionOnList);

        int languageId = course.getLanguageId();
        int languagePositionOnList = getLanguagePosition(languageId);
        languageSpinner.setSelection(languagePositionOnList);

        int advancementId = course.getCourseAdvancementId();
        int advancementPositionOnList = getAdvancementPosition(advancementId);
        advancementSpinner.setSelection(advancementPositionOnList);

        maxStudent.setText(String.valueOf(course.getMaxStudents()));

        Date startDate = course.getStartDate();
        courseStartDate.setText(startDate.toString());

        Date endDate = course.getEndDate();
        courseEndDate.setText(endDate.toString());

        int classRoomId = course.getClassRoomId();
        int classRoomPositionOnList = getClassRoomPosition(classRoomId);
        classRoomSpinner.setSelection(classRoomPositionOnList);

        Date paymentDateForCourse = course.getPaymentDate();
        paymentDate.setText(paymentDateForCourse.toString());

        Float cost = course.getPayment();
        payment.setText(String.valueOf(cost));

        Date signDateForCourse = course.getSignDate();
        signDate.setText(signDateForCourse.toString());
    }

    private int getTeacherPosition(int teacherId) {
        int position = 0;
        String teacherName = getTeacherName(teacherId);
        for (int i = 0; i < teacherNameList.size(); i++) {
            if (teacherName.equals(teacherNameList.get(i))) {
                position = i;
            }
        }

        return position;
    }

    private String getTeacherName(int teacherId) {
        String name = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select forename, surname, idn from users where id = " + teacherId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String forename = resultSet.getString(1);
                    String surname = resultSet.getString(2);
                    String idn = resultSet.getString(3);
                    name = forename + " " + surname + "/" + idn;
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return name;
    }

    private int getLanguagePosition(int languageId) {
        int position = 0;
        String languageName = getLanguageName(languageId);
        for (int i = 0; i < languageNameList.size(); i++) {
            if (languageName.equals(languageNameList.get(i))) {
                position = i;
            }
        }

        return position;
    }

    private String getLanguageName(int languageId) {
        String name = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select name from course_languages where id = " + languageId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    name = resultSet.getString(1);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return name;
    }

    private int getAdvancementPosition(int advancementId) {
        int position = 0;
        String advancementName = getAdvancementName(advancementId);
        for (int i = 0; i < advancementNameList.size(); i++) {
            if (advancementName.equals(advancementNameList.get(i))) {
                position = i;
            }
        }

        return position;
    }

    private String getAdvancementName(int advancementId) {
        String name = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select name from course_advancement where id = " + advancementId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    name = resultSet.getString(1);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return name;
    }

    private int getClassRoomPosition(int classRoomId) {
        int position = 0;
        String classRoomName = getClassRoomName(classRoomId);
        for (int i = 0; i < classRoomNameList.size(); i++) {
            if (classRoomName.equals(classRoomNameList.get(i))) {
                position = i;
            }
        }

        return position;
    }

    private String getClassRoomName(int classRoomId) {
        String name = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select number from class_room where id = " + classRoomId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    name = String.valueOf(resultSet.getInt(1));
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return name;
    }

    private void setSaveListener() {
        saveEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == saveEditCourse.getId()) {
                    saveEditedCourse();
                }
            }
        });
    }

    private Course getCourse() {
        Course course = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from courses where archival = 0 and id = " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String courseName = resultSet.getString(2);
                    int teacherId = resultSet.getInt(3);
                    int languageId = resultSet.getInt(4);
                    int advancementId = resultSet.getInt(5);
                    int maxStudents = resultSet.getInt(6);
                    Date startDate = resultSet.getDate(7);
                    Date endDate = resultSet.getDate(8);
                    int classRoomId = resultSet.getInt(9);
                    Date paymentDate = resultSet.getDate(10);
                    Float payment = resultSet.getFloat(11);
                    Date creationDate = resultSet.getDate(12);
                    boolean archival = resultSet.getBoolean(13);
                    Date signDate = resultSet.getDate(14);
                    ArrayList<Integer> studentsList = getStudentsList();
                    ArrayList<CourseDate> courseDatesList = getCourseDatesList();
                    course = new Course(id, courseName, teacherId, languageId, advancementId, maxStudents, startDate, endDate, classRoomId, paymentDate, payment, creationDate, archival, signDate, studentsList, courseDatesList);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return course;
    }

    private ArrayList<Integer> getStudentsList() {
        ArrayList<Integer> studentsList = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select student_id from course_students where course_id = " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int studentId = resultSet.getInt(1);
                    studentsList.add(studentId);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return studentsList;
    }

    private ArrayList<CourseDate> getCourseDatesList() {
        ArrayList<CourseDate> courseDates = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from course_dates where course_id = " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int dateId = resultSet.getInt(1);
                    int courseId = resultSet.getInt(2);
                    Date date = resultSet.getDate(3);
                    Time courseTimeStart = resultSet.getTime(4);
                    Time courseTimeEnd = resultSet.getTime(5);
                    CourseDate courseDate = new CourseDate(dateId, courseId, date, courseTimeStart, courseTimeEnd);
                    courseDates.add(courseDate);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return courseDates;
    }

    private void saveEditedCourse() {
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

        String endDateTxt = courseEndDate.getText().toString();
        if (endDateTxt == null || endDateTxt.equals("")) {
            Toast.makeText(getContext(), "Proszę uzupełnić datę zakończenia kursu", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedClassRoomPosition = classRoomSpinner.getSelectedItemPosition();
        String classRoomName = classRoomNameList.get(selectedClassRoomPosition);
        int classRoomId = getClassRoomId(classRoomName);

        String dateOfPaymentTxt = paymentDate.getText().toString();
        if (dateOfPaymentTxt == null || dateOfPaymentTxt.equals("")) {
            Toast.makeText(getContext(), "Proszę wybrać termin płatności", Toast.LENGTH_SHORT).show();
            return;
        }

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

        String query = "UPDATE courses SET course_name = '" + name + "', teacher_id = " + teacherId + ", language_id = " + languageId + ", course_advancement_id = " + advancementId + ", max_students_number = " + Integer.valueOf(studentLimit) +
                ", start_date = '" + startDateTxt + "', end_date = '" + endDateTxt + "', class_room_id = " + classRoomId + ", payment_date = '" + dateOfPaymentTxt + "', payment = " + Float.valueOf(cost)
                + ", sign_date = '" + dateOfSignTxt + "' where id = " + courseId;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                Statement statement = connect.createStatement();
                statement.executeUpdate(query);
                connect.close();

            } else {
                Toast.makeText(getContext(), "Wystąpił problem z połączeniem z internetem", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        Intent intent = new Intent(getContext(), CourseView.class);
        Toast.makeText(getContext(), "Zapisano edycję kursu", Toast.LENGTH_SHORT).show();
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

    private int getClassRoomId(String className) {
        int classRoomId = 0;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select id from class_room where archival = 0 and number = '" + className + "'";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    classRoomId = resultSet.getInt(1);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return classRoomId;
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