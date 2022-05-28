package com.example.curseschool.CourseView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.Course;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserKind;
import com.example.curseschool.UserUtils.UserUtils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;


public class CourseFormFragment extends Fragment {

    private String MyPREFERENCES = "userData";
    private String connectionResult = "";
    private View view;
    private int courseId;
    private TextView courseFormTeacher;
    private TextView courseFormLanguage;
    private TextView courseFormAdvancement;
    private TextView courseFormMaxStudents;
    private TextView courseFormStartDate;
    private TextView courseFormEndDate;
    private TextView coursePaymentDateTo;
    private TextView coursePaymentValue;
    private TextView courseEnroll;
    private Button signForCourse;
    private User currentUser;
    private int currentUserId;
    private ExtendedFloatingActionButton fabOptions, fabEdit, fabArchive;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isFabOpen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_form, container, false);
        courseId = getActivity().getIntent().getIntExtra("courseId", 0);
        currentUser = getCurrentUserId();
        initTextViews();
        setTextViewsValues();
        setListenerForSign();
        handleFloatingButton();
        return view;
    }

    private void initTextViews() {
        courseFormTeacher = view.findViewById(R.id.courseFormTeacher);
        courseFormLanguage = view.findViewById(R.id.courseFormLanguage);
        courseFormAdvancement = view.findViewById(R.id.courseFormAdvancement);
        courseFormMaxStudents = view.findViewById(R.id.courseFormMaxStudents);
        courseFormStartDate = view.findViewById(R.id.courseFormStartDate);
        courseFormEndDate = view.findViewById(R.id.courseFormEndDate);
        coursePaymentDateTo = view.findViewById(R.id.coursePaymentDateTo);
        coursePaymentValue = view.findViewById(R.id.coursePaymentValue);
        courseEnroll = view.findViewById(R.id.courseEnroll);
        signForCourse = view.findViewById(R.id.signForCourse);
    }

    private void setTextViewsValues() {
        Course course = getCourse();
        setTeacher(course.getTeacherId());
        setLanguage(course.getLanguageId());
        setAdvancement(course.getCourseAdvancementId());
        String maxStudents = String.valueOf(course.getMaxStudents());
        String students = course.getStudentsList().size() + "/" + maxStudents;
        courseFormMaxStudents.setText(students);
        String startDate = course.getStartDate().toString();
        courseFormStartDate.setText(startDate);
        String endDate = course.getEndDate().toString();
        courseFormEndDate.setText(endDate);
        String paymentDate = course.getPaymentDate().toString();
        coursePaymentDateTo.setText(paymentDate);
        String payment = String.valueOf(course.getPayment());
        coursePaymentValue.setText(payment);
        String signDate = course.getSignDate().toString();
        courseEnroll.setText(signDate);
    }

    private void setTeacher(int teacherId) {
        String teacherName = getTeacherName(teacherId);
        courseFormTeacher.setText(teacherName);
    }

    private void setLanguage(int languageId) {
        String language = getLanguageName(languageId);
        courseFormLanguage.setText(language);
    }

    private void setAdvancement(int advancementId) {
        String advancement = getCourseAdvancementName(advancementId);
        courseFormAdvancement.setText(advancement);
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
                    Date paymentDate = resultSet.getDate(9);
                    Float payment = resultSet.getFloat(10);
                    Date creationDate = resultSet.getDate(11);
                    boolean archival = resultSet.getBoolean(12);
                    Date signDate = resultSet.getDate(13);
                    ArrayList<Integer> studentsList = getStudentsList();
                    ArrayList<CourseDate> courseDatesList = getCourseDatesList();
                    course = new Course(id, courseName, teacherId, languageId, advancementId, maxStudents, startDate, endDate, paymentDate, payment, creationDate, archival, signDate, studentsList, courseDatesList);
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
                    int classRoomId = resultSet.getInt(6);
                    CourseDate courseDate = new CourseDate(dateId, courseId, date, courseTimeStart, courseTimeEnd, classRoomId);
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

    private String getTeacherName(int teacherId) {
        String teacherName = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select forename, surname from users where id = " + teacherId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String forename = resultSet.getString(1);
                    String surname = resultSet.getString(2);
                    teacherName = forename + " " + surname;
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return teacherName;
    }

    private String getLanguageName(int languageId) {
        String languageName = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select name from course_languages where id = " + languageId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    languageName = name;
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return languageName;
    }

    private String getCourseAdvancementName(int advancementId) {
        String advancementName = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select name from course_advancement where id = " + advancementId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    advancementName = name;
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return advancementName;
    }

    private void setListenerForSign() {
        if(currentUser.getType().equals(UserKind.student.toString())){
            signForCourse.setVisibility(View.VISIBLE);
        } else {
            signForCourse.setVisibility(View.GONE);
        }
        signForCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == signForCourse.getId()) {
                    Course course = getCourse();
                    boolean canSign = checkSignDate(course.getSignDate());
                    boolean alreadySigned = checkIfStudentAlreadySigned(course);
                    if (canSign == false) {
                        Toast.makeText(getContext(), "Zapisy zostały już zamknięte", Toast.LENGTH_LONG).show();
                    } else if (alreadySigned == true) {
                        Toast.makeText(getContext(), "Jesteś już zapisany na ten kurs", Toast.LENGTH_LONG).show();
                    } else {
                        signUserOnCourse();
                        Toast.makeText(getContext(), "Udało się zapisać na kurs", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean checkIfStudentAlreadySigned(Course course) {
        boolean alreadySigned = false;
        ArrayList<Integer> students = course.getStudentsList();
        for (Integer studentId : students) {
            if (studentId == currentUserId)
                alreadySigned = true;
        }
        return alreadySigned;
    }

    private User getCurrentUserId() {
        SharedPreferences sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        User user = UserUtils.getUserById(id);
        currentUserId = id;
        return user;
    }

    private void signUserOnCourse() {
        int id = findMaxId();
        String query = "INSERT INTO course_students (id, student_id, course_id) "
                + " VALUES(?,?,?)";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, currentUserId);
                preparedStatement.setInt(3, courseId);
                preparedStatement.execute();
                connect.close();
            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    private int findMaxId() {
        Integer id = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select MAX(id) from course_students";
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

    private void handleSignButtonVisibility() {
        Course course = getCourse();
        boolean isAlreadySigned = checkIfStudentAlreadySigned(course);
        if (isAlreadySigned == true) {
            signForCourse.setText("Zapisano");
            signForCourse.setEnabled(false);
        } else {
            signForCourse.setText("Zapisz się");
            signForCourse.setEnabled(true);
        }

    }

    private boolean checkSignDate(Date signDate) {
        boolean canSign = true;
        Date currentDate = new Date();
        if (signDate.before(currentDate)) {
            canSign = false;
        }

        return canSign;
    }

    private void handleFloatingButton() {
        initFloatingButton();
        setFabListeners();
    }

    private void initFloatingButton() {
        fabOptions = (ExtendedFloatingActionButton) view.findViewById(R.id.fab_options);
        fabEdit = (ExtendedFloatingActionButton) view.findViewById(R.id.fab_edit);
        fabArchive = (ExtendedFloatingActionButton) view.findViewById(R.id.fab_archive);
        
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        if(currentUser.getType().equals(UserKind.admin.toString())){
            fabOptions.setVisibility(View.VISIBLE);
        } else {
            fabOptions.setVisibility(View.GONE);
        }
    }

    private void setFabListeners(){
        fabOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditCourseForm.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });

        fabArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                archiveCourse();
            }
        });
    }

    private void animateFab(){
        if(isFabOpen){
            fabOptions.startAnimation(rotateForward);
            fabEdit.startAnimation(fabClose);
            fabArchive.startAnimation(fabClose);
            fabEdit.setClickable(false);
            fabArchive.setClickable(false);
            isFabOpen = false;
        } else{
            fabOptions.startAnimation(rotateBackward);
            fabEdit.startAnimation(fabOpen);
            fabArchive.startAnimation(fabOpen);
            fabEdit.setClickable(true);
            fabArchive.setClickable(true);
            isFabOpen = true;
        }
    }

    private void archiveCourse(){
        Course course = getCourse();
        Date currentDate = new Date();
        if(currentDate.before(course.getStartDate()) || currentDate.after(course.getEndDate())){
            archive();
            Toast.makeText(getContext(), "Usunięto kurs", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), CourseView.class);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Nie można usunąć aktualnie trwającego kursu", Toast.LENGTH_LONG).show();
        }


    }

    private void archive(){
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "UPDATE courses SET archival = 1 WHERE id = " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                connect.close();
            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }
}