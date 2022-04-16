package com.example.curseschool.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.Grade;
import com.example.curseschool.StudentGradesView.GradesStudentView;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GradesStudentAdapter extends RecyclerView.Adapter<GradesStudentAdapter.GradeStudentHolder> {

    private Context context;
    private ArrayList<Grade> grades;
    private GradesStudentView gradesStudentView;

    public GradesStudentAdapter(Context context, ArrayList<Grade> gradeArrayList, GradesStudentView studentView) {
        this.context = context;
        this.grades = gradeArrayList;
        this.gradesStudentView = studentView;
    }

    @NonNull
    @Override
    public GradesStudentAdapter.GradeStudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grade_layout,
                parent, false);

        return new GradesStudentAdapter.GradeStudentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesStudentAdapter.GradeStudentHolder holder, int position) {
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

    class GradeStudentHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView grade;

        public GradeStudentHolder(@NonNull View itemView) {
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