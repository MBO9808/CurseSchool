package com.example.curseschool.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curseschool.CourseView.CourseFormView;
import com.example.curseschool.CourseView.CourseStudentsFragment;
import com.example.curseschool.CourseView.CourseViewStudentDataView;
import com.example.curseschool.Objects.Student;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserKind;
import com.example.curseschool.UserUtils.UserUtils;

import java.util.ArrayList;

public class CourseFormStudentsAdapter extends RecyclerView.Adapter<CourseFormStudentsAdapter.StudentListHolder> {

    private Context context;
    private ArrayList<Student> students;
    private CourseStudentsFragment courseStudentsFragment;
    private String MyPREFERENCES = "userData";
    private User currentUser;

    public CourseFormStudentsAdapter(Context context, ArrayList<Student> studentArrayList, CourseStudentsFragment studentsFragment) {
        this.context = context;
        this.students = studentArrayList;
        this.courseStudentsFragment = studentsFragment;
    }

    @NonNull
    @Override
    public CourseFormStudentsAdapter.StudentListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_student_card,
                parent, false);

        return new CourseFormStudentsAdapter.StudentListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseFormStudentsAdapter.StudentListHolder holder, int position) {
        Student student = students.get(position);
        holder.setDetails(student);
        currentUser = getCurrentUser();
        if(currentUser.getType().equals(UserKind.admin.toString())) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CourseViewStudentDataView.class);
                    intent.putExtra("courseId", student.getCourseId());
                    intent.putExtra("studentId", student.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private User getCurrentUser() {
        SharedPreferences sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        User user = UserUtils.getUserById(id);
        return user;
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public Context getContext() {
        return context;
    }

    class StudentListHolder extends RecyclerView.ViewHolder {
        private TextView studentName;
        private TextView studentEmail;
        private TextView studentPhoneNumber;

        public StudentListHolder(@NonNull View itemView) {
            super(itemView);
            this.studentName = itemView.findViewById(R.id.courseFormStudentName);
            this.studentEmail = itemView.findViewById(R.id.courseFormStudentEmail);
            this.studentPhoneNumber = itemView.findViewById(R.id.courseFormStudentPhoneNumber);
        }

        public void setDetails(Student student) {
            String name = student.getName();
            String email = student.getEmail();
            String phoneNumber = student.getPhoneNumber();
            this.studentName.setText(name);
            this.studentEmail.setText(email);
            this.studentPhoneNumber.setText(phoneNumber);
        }
    }
}