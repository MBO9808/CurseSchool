package com.example.curseschool.ScheduleView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.MainView.MainSite;
import com.example.curseschool.Objects.ClassRoom;
import com.example.curseschool.Objects.Course;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserKind;
import com.example.curseschool.UserUtils.UserUtils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarView extends AppCompatActivity {

    private Toolbar toolbar;
    private String MyPREFERENCES = "userData";
    private CompactCalendarView calendar;
    private TextView text;
    private int currentUserId;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        currentUserId = getCurrentUserId();
        toolbar = findViewById(R.id.mainToolBar);
        Date date = new Date();
        toolbar.setTitle("Plan zajęć - " + dateFormat.format(date));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarView.this, MainSite.class);
                startActivity(intent);
            }
        });
        calendar = findViewById(R.id.calendarView);
        text = findViewById(R.id.textView);
        calendar.setUseThreeLetterAbbreviation(true);
        List<Event> events = getEvents();
        calendar.addEvents(events);
        calendar.displayOtherMonthDays(true);
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendar.getEvents(dateClicked);
                String eventText = "";
                for (Event event : events) {
                    Object data = event.getData();
                    String dataStr = data.toString();
                    eventText += dataStr;
                }
                text.setText(eventText);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                toolbar.setTitle("Plan zajęć - " + dateFormat.format(firstDayOfNewMonth));
            }
        });

    }

    private List<Event> getEvents() {
        ArrayList<Course> userCurrentCourses = getCourses();
        List<Event> eventsList = new ArrayList<>();
        for (Course course : userCurrentCourses) {
            ArrayList<CourseDate> courseDates = course.getCourseDatesList();
            for (CourseDate date : courseDates) {
                int courseId = date.getCourseId();
                Course currentCourse = getCourse(courseId, userCurrentCourses);
                ClassRoom classRoom = getClassRoom(date.getClassRoomId());
                Date eventDate = date.getCourseDate();
                Time eventStartTime = date.getCourseTimeStart();
                Time eventEndTime = date.getCourseTimeEnd();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                String eventText = currentCourse.getCourseName() + "\n Sala: " + classRoom.getClassRoom() + " \n W godzinach: " + format.format(eventStartTime) + " - " + format.format(eventEndTime) + " \n\n";
                Event event = new Event(Color.RED, eventDate.getTime(), eventText);
                eventsList.add(event);
            }
        }

        return eventsList;
    }

    private ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        User currentUser = UserUtils.getUserById(currentUserId);
        if (currentUser.getType().equals(UserKind.student)) {
            courses = getStudentCourses();
        } else {
            courses = getTeacherCourses();
        }

        return courses;
    }

    private ClassRoom getClassRoom(int classRoomId) {
        ClassRoom classRoom = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from class_room where id = " + classRoomId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    int number = resultSet.getInt(2);
                    boolean archival = resultSet.getBoolean(3);
                    classRoom = new ClassRoom(id, number, archival);
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

    private Course getCourse(int courseId, ArrayList<Course> courseArrayList) {
        for (Course course : courseArrayList) {
            if (course.getId() == courseId)
                return course;
        }
        return null;
    }

    private ArrayList<Course> getTeacherCourses() {
        ArrayList<Course> userCourses = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from courses where archival = 0 and teacher_id = " + currentUserId;
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
                    ArrayList<Integer> studentsList = getStudentsList(id);
                    ArrayList<CourseDate> courseDatesList = getCourseDatesList(id);
                    Course course = new Course(id, courseName, teacherId, languageId, advancementId, maxStudents, startDate, endDate, paymentDate, payment, creationDate, archival, signDate, studentsList, courseDatesList);
                    userCourses.add(course);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return userCourses;
    }

    private ArrayList<Course> getStudentCourses() {
        ArrayList<Course> userCourses = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from courses c join course_students s on s.course_id = c.id where c.archival = 0 and s.student_id = " + currentUserId;
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
                    ArrayList<Integer> studentsList = getStudentsList(id);
                    ArrayList<CourseDate> courseDatesList = getCourseDatesList(id);
                    Course course = new Course(id, courseName, teacherId, languageId, advancementId, maxStudents, startDate, endDate, paymentDate, payment, creationDate, archival, signDate, studentsList, courseDatesList);
                    userCourses.add(course);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return userCourses;
    }

    private int getCurrentUserId() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        return id;
    }

    private ArrayList<Integer> getStudentsList(int courseId) {
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

    private ArrayList<CourseDate> getCourseDatesList(int id) {
        ArrayList<CourseDate> courseDates = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from course_dates where course_id = " + id;
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
}