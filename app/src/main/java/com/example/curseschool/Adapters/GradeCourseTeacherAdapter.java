package com.example.curseschool.Adapters;

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

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.Course;
import com.example.curseschool.TeacherGradesView.GradeCourseStudentsListView;
import com.example.curseschool.TeacherGradesView.GradeCourseTeacherView;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GradeCourseTeacherAdapter extends RecyclerView.Adapter<GradeCourseTeacherAdapter.CourseGradeHolder> {

    private Context context;
    private ArrayList<Course> courses;
    private GradeCourseTeacherView gradeCourseTeacherView;

    public GradeCourseTeacherAdapter(Context context, ArrayList<Course> courses, GradeCourseTeacherView teacherView) {
        this.context = context;
        this.courses = courses;
        this.gradeCourseTeacherView = teacherView;
    }

    @NonNull
    @Override
    public GradeCourseTeacherAdapter.CourseGradeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_grade_teacher_view,
                parent, false);

        return new GradeCourseTeacherAdapter.CourseGradeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeCourseTeacherAdapter.CourseGradeHolder holder, int position) {
        Course course = courses.get(position);
        holder.setDetails(course);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GradeCourseStudentsListView.class);
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

    class CourseGradeHolder extends RecyclerView.ViewHolder {
        private TextView courseName;
        private TextView courseTeacher;
        private TextView courseLanguage;
        private TextView courseAdvancement;
        private TextView courseStartDate;
        private TextView courseEndDate;

        public CourseGradeHolder(@NonNull View itemView) {
            super(itemView);
            this.courseName = itemView.findViewById(R.id.courseGradeTeacherName);
            this.courseTeacher = itemView.findViewById(R.id.courseGradeTeacherTeacher);
            this.courseLanguage = itemView.findViewById(R.id.courseGradeTeacherLanguage);
            this.courseAdvancement = itemView.findViewById(R.id.courseGradeTeacherAdvancement);
            this.courseStartDate = itemView.findViewById(R.id.courseGradeTeacherStartDate);
            this.courseEndDate = itemView.findViewById(R.id.courseGradeTeacherEndDate);
        }

        public void setDetails(Course course) {
            String name = course.getCourseName();
            String teacher = getTeacherName(course.getTeacherId());
            String language = getLanguageName(course.getLanguageId());
            String advancement = getCourseAdvancementName(course.getCourseAdvancementId());
            String startDate = course.getStartDate().toString();
            String endDate = course.getEndDate().toString();
            this.courseName.setText(name);
            this.courseTeacher.setText(HtmlCompat.fromHtml("<b>Prowadzący: </b>" + teacher, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseLanguage.setText(HtmlCompat.fromHtml("<b>Język: </b>" + language, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseAdvancement.setText(HtmlCompat.fromHtml("<b>Poziom zaawansowania: </b>" + advancement, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseStartDate.setText(HtmlCompat.fromHtml("<b>Data rozpoczęcia kursu: </b>" + startDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseEndDate.setText(HtmlCompat.fromHtml("<b>Data zakończenia kursu: </b>" + endDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
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
