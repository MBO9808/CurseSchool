package com.example.curseschool.CourseView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.Course;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;


public class CourseFormFragment extends Fragment {

    private View view;
    private int courseId;
    private TextView courseFormTeacher;
    private TextView courseFormLanguage;
    private TextView courseFormAdvancement;
    private TextView courseFormMaxStudents;
    private TextView courseFormStartDate;
    private TextView courseFormEndDate;
    private TextView courseClassRoom;
    private TextView coursePaymentDateTo;
    private TextView coursePaymentValue;
    private TextView courseEnroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_form, container, false);
        courseId = getActivity().getIntent().getIntExtra("courseId", 0);
        initTextViews();
        setTextViewsValues();
        return view;
    }

    private void initTextViews() {
        courseFormTeacher = view.findViewById(R.id.courseFormTeacher);
        courseFormLanguage = view.findViewById(R.id.courseFormLanguage);
        courseFormAdvancement = view.findViewById(R.id.courseFormAdvancement);
        courseFormMaxStudents = view.findViewById(R.id.courseFormMaxStudents);
        courseFormStartDate = view.findViewById(R.id.courseFormStartDate);
        courseFormEndDate = view.findViewById(R.id.courseFormEndDate);
        courseClassRoom = view.findViewById(R.id.courseClassRoom);
        coursePaymentDateTo = view.findViewById(R.id.coursePaymentDateTo);
        coursePaymentValue = view.findViewById(R.id.coursePaymentValue);
        courseEnroll = view.findViewById(R.id.courseEnroll);
    }

    private void setTextViewsValues(){
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
        setClassRoom(course.getClassRoomId());
        String paymentDate = course.getPaymentDate().toString();
        coursePaymentDateTo.setText(paymentDate);
        String payment = String.valueOf(course.getPayment());
        coursePaymentValue.setText(payment);
        String signDate = course.getSignDate().toString();
        courseEnroll.setText(signDate);
    }

    private void setTeacher(int teacherId){
        String teacherName = getTeacherName(teacherId);
        courseFormTeacher.setText(teacherName);
    }

    private void setLanguage(int languageId){
        String language = getLanguageName(languageId);
        courseFormLanguage.setText(language);
    }

    private void setAdvancement(int advancementId) {
        String advancement = getCourseAdvancementName(advancementId);
        courseFormAdvancement.setText(advancement);
    }

    private void setClassRoom(int classRoomId){
        String classRoom = getCourseClassRoom(classRoomId);
        courseClassRoom.setText(classRoom);
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

    private String getCourseClassRoom(int classRoomId) {
        String classRoom = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select number from class_room where id = " + classRoomId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int room = resultSet.getInt(1);
                    classRoom = String.valueOf(room);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return classRoom;
    }
}