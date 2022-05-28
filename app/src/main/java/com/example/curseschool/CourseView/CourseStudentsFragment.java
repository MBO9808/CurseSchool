package com.example.curseschool.CourseView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.curseschool.Adapters.CourseFormStudentsAdapter;
import com.example.curseschool.Adapters.GradeCourseStudentsListAdapter;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.Student;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.TeacherGradesView.GradeCourseStudentsListView;
import com.example.curseschool.UserUtils.UserUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseStudentsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Student> studentArrayList;
    private CourseFormStudentsAdapter courseFormStudentsAdapter;
    private int courseId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_students, container, false);
        courseId = getActivity().getIntent().getIntExtra("courseId", 0);
        initDictionaryView(view);
        return view;
    }

    private void initDictionaryView(View view) {
        recyclerView = view.findViewById(R.id.courseStudentListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        studentArrayList = new ArrayList<>();
        handleCoursesList();
        courseFormStudentsAdapter = new CourseFormStudentsAdapter(getContext(), studentArrayList, CourseStudentsFragment.this);
        recyclerView.setAdapter(courseFormStudentsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    private void handleCoursesList() {
        studentArrayList = getStudentsList();
    }

    private ArrayList<Student> getStudentsList() {
        studentArrayList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select st.student_id, us.forename, us.surname, us.email, us.phone_number from course_students st " +
                        "join users us on us.id = st.student_id where st.course_id =  " + courseId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int studentId = resultSet.getInt(1);
                    String forename = resultSet.getString(2);
                    String surname = resultSet.getString(3);
                    String email = resultSet.getString(4);
                    String phoneNumber = resultSet.getString(5);
                    String name = forename + " " + surname;
                    Student student = new Student(studentId, courseId, name, email, phoneNumber);
                    studentArrayList.add(student);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return studentArrayList;
    }
}