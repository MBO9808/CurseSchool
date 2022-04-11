package com.example.curseschool;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GradeCourseStudentsListAdapter extends RecyclerView.Adapter<GradeCourseStudentsListAdapter.CourseGradeTeacherStudentListHolder> {

    private Context context;
    private ArrayList<Student> students;
    private GradeCourseStudentsListView gradeCourseStudentsListView;

    public GradeCourseStudentsListAdapter(Context context, ArrayList<Student> studentArrayList, GradeCourseStudentsListView studentsListView) {
        this.context = context;
        this.students = studentArrayList;
        this.gradeCourseStudentsListView = studentsListView;
    }

    @NonNull
    @Override
    public GradeCourseStudentsListAdapter.CourseGradeTeacherStudentListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_grade_student_card,
                parent, false);

        return new GradeCourseStudentsListAdapter.CourseGradeTeacherStudentListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeCourseStudentsListAdapter.CourseGradeTeacherStudentListHolder holder, int position) {
        Student student = students.get(position);
        holder.setDetails(student);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentGradesTeacherView.class);
                intent.putExtra("studentId", student.getId());
                intent.putExtra("courseId", student.getCourseId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public Context getContext() {
        return context;
    }

    class CourseGradeTeacherStudentListHolder extends RecyclerView.ViewHolder {
        private TextView studentName;
        private TextView studentEmail;
        private TextView studentPhoneNumber;

        public CourseGradeTeacherStudentListHolder(@NonNull View itemView) {
            super(itemView);
            this.studentName = itemView.findViewById(R.id.studentName);
            this.studentEmail = itemView.findViewById(R.id.studentEmail);
            this.studentPhoneNumber = itemView.findViewById(R.id.studentPhoneNumber);
        }

        public void setDetails(Student student) {
            String name = student.getName();
            String email = student.getEmail();
            String phoneNumber = student.getPhoneNumber();
            this.studentName.setText(name);
            this.studentEmail.setText(HtmlCompat.fromHtml("<b>Email: </b>" + email, HtmlCompat.FROM_HTML_MODE_LEGACY));
            this.studentPhoneNumber.setText(HtmlCompat.fromHtml("<b>Numer telefonu: </b>" + phoneNumber, HtmlCompat.FROM_HTML_MODE_LEGACY));
        }
    }
}