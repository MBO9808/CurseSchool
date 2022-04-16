package com.example.curseschool.CourseView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curseschool.Adapters.CourseFormDatesAdapter;
import com.example.curseschool.Adapters.CourseFormStudentsAdapter;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.Objects.Student;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;


public class CourseDatesFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<CourseDate> courseDateArrayList;
    private CourseFormDatesAdapter courseFormDatesAdapter;
    private int courseId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_dates, container, false);
        courseId = getActivity().getIntent().getIntExtra("courseId", 0);
        initDictionaryView(view);
        return view;
    }

    private void initDictionaryView(View view) {
        recyclerView = view.findViewById(R.id.courseDatesListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseDateArrayList = new ArrayList<>();
        handleCoursesList();
        courseFormDatesAdapter = new CourseFormDatesAdapter(getContext(), courseDateArrayList, CourseDatesFragment.this);
        recyclerView.setAdapter(courseFormDatesAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    private void handleCoursesList() {
        courseDateArrayList = getCourseDatesList();
    }

    private ArrayList<CourseDate> getCourseDatesList() {
        courseDateArrayList.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from course_dates where course_id = " + courseId + " order by course_date asc";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int dateId = resultSet.getInt(1);
                    int courseId = resultSet.getInt(2);
                    Date date = resultSet.getDate(3);
                    Time courseTimeStart = resultSet.getTime(4);
                    Time courseTimeEnd = resultSet.getTime(5);
                    CourseDate courseDate = new CourseDate(dateId, courseId, date, courseTimeStart, courseTimeEnd);
                    courseDateArrayList.add(courseDate);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return courseDateArrayList;
    }
}