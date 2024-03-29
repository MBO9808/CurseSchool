package com.example.curseschool.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curseschool.CourseView.CourseFormView;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.Course;
import com.example.curseschool.CourseView.CourseView;
import com.example.curseschool.NewObjectsHandlers.NewGradeNameHandler;
import com.example.curseschool.R;
import com.example.curseschool.StudentGradesView.GradesStudentView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.CourseHolder> {

    private Context context;
    private ArrayList<Course> courses;
    private CourseView courseView;

    public CourseViewAdapter(Context context, ArrayList<Course> courses, CourseView courseView) {
        this.context = context;
        this.courses = courses;
        this.courseView = courseView;
    }

    @NonNull
    @Override
    public CourseViewAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_card_layout,
                parent, false);

        return new CourseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewAdapter.CourseHolder holder, int position) {
        Course course = courses.get(position);
        holder.setDetails(course);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseFormView.class);
                intent.putExtra("courseId", course.getId());
                intent.putExtra("courseName", course.getCourseName());
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

    public void setCourses(ArrayList<Course> courseArrayList) {
        this.courses = courseArrayList;
        notifyDataSetChanged();
    }

    public void deleteCourse(int id) {
        Course course = courses.get(id);
        deleteItem(course.getId());
        courses.remove(id);
        notifyItemRemoved(id);
    }

    private void deleteItem(int id) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "UPDATE courses SET archival = 1 WHERE id = " + id;
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

    class CourseHolder extends RecyclerView.ViewHolder {
        private TextView courseName;
        private TextView courseTeacher;
        private TextView courseLanguage;
        private TextView courseAdvancement;
        private TextView courseMaxStudents;
        private TextView courseStartDate;
        private TextView courseEndDate;
        private TextView coursePaymentDate;
        private TextView coursePayment;
        private TextView courseSignDate;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            this.courseName = itemView.findViewById(R.id.courseName);
            this.courseTeacher = itemView.findViewById(R.id.courseTeacher);
            this.courseLanguage = itemView.findViewById(R.id.courseLanguage);
            this.courseAdvancement = itemView.findViewById(R.id.courseAdvancement);
            this.courseMaxStudents = itemView.findViewById(R.id.courseMaxStudents);
            this.courseStartDate = itemView.findViewById(R.id.courseStartDate);
            this.courseEndDate = itemView.findViewById(R.id.courseEndDate);
            this.coursePaymentDate = itemView.findViewById(R.id.coursePaymentDate);
            this.coursePayment = itemView.findViewById(R.id.coursePayment);
            this.courseSignDate = itemView.findViewById(R.id.courseSignDate);
        }

        public void setDetails(Course course) {
            String name = course.getCourseName();
            String teacher = getTeacherName(course.getTeacherId());
            String language = getLanguageName(course.getLanguageId());
            String advancement = getCourseAdvancementName(course.getCourseAdvancementId());
            String maxStudents = String.valueOf(course.getMaxStudents());
            String students = course.getStudentsList().size() + "/" + maxStudents;
            String startDate = course.getStartDate().toString();
            String endDate = course.getEndDate().toString();
            String paymentDate = course.getPaymentDate().toString();
            String payment = String.valueOf(course.getPayment());
            String signDate = course.getSignDate().toString();
            this.courseName.setText(name);
            this.courseTeacher.setText(HtmlCompat.fromHtml("<b>Prowadzący: </b>" + teacher, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseLanguage.setText(HtmlCompat.fromHtml("<b>Język: </b>" + language, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseAdvancement.setText(HtmlCompat.fromHtml("<b>Poziom zaawansowania: </b>" + advancement, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseMaxStudents.setText(HtmlCompat.fromHtml("<b>Liczba studentów: </b>" + students, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseStartDate.setText(HtmlCompat.fromHtml("<b>Data rozpoczęcia kursu: </b>" + startDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseEndDate.setText(HtmlCompat.fromHtml("<b>Data zakończenia kursu: </b>" + endDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.coursePaymentDate.setText(HtmlCompat.fromHtml("<b>Termin płatności: </b>" + paymentDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.coursePayment.setText(HtmlCompat.fromHtml("<b>Koszt: </b>" + payment, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.courseSignDate.setText(HtmlCompat.fromHtml("<b>Zapisy do dnia: </b>" + signDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
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