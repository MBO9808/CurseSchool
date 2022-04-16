package com.example.curseschool.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.Grade;
import com.example.curseschool.NewObjectsHandlers.NewGradeHandler;
import com.example.curseschool.R;
import com.example.curseschool.TeacherGradesView.StudentGradesTeacherView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentGradesTeacherAdapter extends RecyclerView.Adapter<StudentGradesTeacherAdapter.GradeHolder> {

    private Context context;
    private ArrayList<Grade> grades;
    private StudentGradesTeacherView studentGradesTeacherView;

    public StudentGradesTeacherAdapter(Context context, ArrayList<Grade> gradeArrayList, StudentGradesTeacherView gradesTeacherView) {
        this.context = context;
        this.grades = gradeArrayList;
        this.studentGradesTeacherView = gradesTeacherView;
    }

    @NonNull
    @Override
    public StudentGradesTeacherAdapter.GradeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grade_layout,
                parent, false);

        return new StudentGradesTeacherAdapter.GradeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentGradesTeacherAdapter.GradeHolder holder, int position) {
        Grade grade = grades.get(position);
        holder.setDetails(grade);
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    public Context getContext() {
        return context;
    }

    public void setGrades(ArrayList<Grade> gradeArrayList) {
        this.grades = gradeArrayList;
        notifyDataSetChanged();
    }

    public void deleteGrade(int id) {
        Grade grade = grades.get(id);
        deleteItem(grade.getId());
        grades.remove(id);
        notifyItemRemoved(id);
    }

    private void deleteItem(int id) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "DELETE FROM grades WHERE id = " + id;
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

    public void editGrade(int id) {
        Grade grade = grades.get(id);
        Bundle bundle = new Bundle();
        bundle.putInt("id", grade.getId());
        bundle.putFloat("gradeValue", grade.getGrade());
        bundle.putInt("gradeTypeId", grade.getGradeNameId());
        NewGradeHandler handler = new NewGradeHandler(grade.getStudentId(), grade.getCourseId());
        handler.setArguments(bundle);
        handler.show(studentGradesTeacherView.getSupportFragmentManager(), NewGradeHandler.TAG);

    }

    class GradeHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView grade;

        public GradeHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.studentGradeName);
            this.grade = itemView.findViewById(R.id.studentGrade);
        }

        public void setDetails(Grade grade) {
            int gradeNameId = grade.getGradeNameId();
            String gradeName = String.valueOf(getGradeName(gradeNameId));
            String studentGrade = String.valueOf(grade.getGrade());
            this.name.setText(gradeName);
            this.grade.setText(studentGrade);
        }

        private String getGradeName(int gradeNameId) {
            String gradeName = "";
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                Connection connect = connectionHelper.getConnection();
                if (connect != null) {
                    String query = "Select name from grade_type where id = " + gradeNameId;
                    Statement statement = connect.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        String name = resultSet.getString(1);
                        gradeName = name;
                    }
                    connect.close();

                } else {
                    String connectionResult = "Check Connection";
                }
            } catch (Exception ex) {
                Log.e("Error :", ex.getMessage());
            }
            return gradeName;
        }
    }
}