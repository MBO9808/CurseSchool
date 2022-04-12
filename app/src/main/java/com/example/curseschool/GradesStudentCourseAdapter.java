package com.example.curseschool;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GradesStudentCourseAdapter extends RecyclerView.Adapter<GradesStudentCourseAdapter.CoursesStudentGradeHolder> {

    private Context context;
    private ArrayList<Course> courses;
    private GradesStudentCourseView gradesStudentCourseView;

    public GradesStudentCourseAdapter(Context context, ArrayList<Course> courses, GradesStudentCourseView studentCourseView) {
        this.context = context;
        this.courses = courses;
        this.gradesStudentCourseView = studentCourseView;
    }

    @NonNull
    @Override
    public GradesStudentCourseAdapter.CoursesStudentGradeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grade_student_course_view,
                parent, false);

        return new GradesStudentCourseAdapter.CoursesStudentGradeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesStudentCourseAdapter.CoursesStudentGradeHolder holder, int position) {
        Course course = courses.get(position);
        holder.setDetails(course);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GradesStudentView.class);
                intent.putExtra("courseId", course.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public Context getContext() {
        return context;
    }

    class CoursesStudentGradeHolder extends RecyclerView.ViewHolder {
        private TextView courseStudentName;
        private TextView courseStudentTeacher;
        private TextView courseStudentLanguage;
        private TextView courseStudentAdvancement;
        private TextView courseStudentStartDate;
        private TextView courseStudentEndDate;

        public CoursesStudentGradeHolder(@NonNull View itemView) {
            super(itemView);
            this.courseStudentName = itemView.findViewById(R.id.courseGradeStudentName);
            this.courseStudentTeacher = itemView.findViewById(R.id.courseGradeStudentTeacher);
            this.courseStudentLanguage = itemView.findViewById(R.id.courseGradeStudentLanguage);
            this.courseStudentAdvancement = itemView.findViewById(R.id.courseGradeStudentAdvancement);
            this.courseStudentStartDate = itemView.findViewById(R.id.courseGradeStudentStartDate);
            this.courseStudentEndDate = itemView.findViewById(R.id.courseGradeStudentEndDate);
        }

        public void setDetails(Course course) {
            String name = course.getCourseName();
            String teacher = getTeacherName(course.getTeacherId());
            String language = getLanguageName(course.getLanguageId());
            String advancement = getCourseAdvancementName(course.getCourseAdvancementId());
            String startDate = course.getStartDate().toString();
            String endDate = course.getEndDate().toString();
            this.courseStudentName.setText(name);
            this.courseStudentTeacher.setText(HtmlCompat.fromHtml("<b>Prowadzący: </b>" + teacher, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseStudentLanguage.setText(HtmlCompat.fromHtml("<b>Język: </b>" + language, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseStudentAdvancement.setText(HtmlCompat.fromHtml("<b>Poziom zaawansowania: </b>" + advancement, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseStudentStartDate.setText(HtmlCompat.fromHtml("<b>Data rozpoczęcia kursu: </b>" + startDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseStudentEndDate.setText(HtmlCompat.fromHtml("<b>Data zakończenia kursu: </b>" + endDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
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
    }
}