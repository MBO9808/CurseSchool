package com.example.curseschool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View view;
    private String MyPREFERENCES = "userData";
    private Toolbar toolbar;
    private Button courseOverview;
    private Button teacherGradeView;
    private Button studentGradeView;
    private Button calendar;

    public mainFragment() {
        // Required empty public constructor
    }

    public static mainFragment newInstance(String param1, String param2) {
        mainFragment fragment = new mainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        toolbar = view.findViewById(R.id.mainToolBar);
        toolbar.setTitle("Strona główna");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        courseOverview = view.findViewById(R.id.courseOverview);
        teacherGradeView = view.findViewById(R.id.teachersGradeView);
        studentGradeView = view.findViewById(R.id.studentGradeView);
        calendar = view.findViewById(R.id.calendar);
        setCourseOverviewListener();
        setTeacherGradeViewListener();
        setStudentGradeViewListener();
        setCalendarViewListener();
        return view;
    }

    private void setCourseOverviewListener() {
        courseOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == courseOverview.getId()) {
                    startActivity(new Intent(getActivity(), CourseView.class));
                }
            }
        });
    }

    private void setTeacherGradeViewListener() {
        teacherGradeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == teacherGradeView.getId()) {
                    startActivity(new Intent(getActivity(), GradeCourseTeacherView.class));
                }
            }
        });
    }

    private void setStudentGradeViewListener() {
        studentGradeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == studentGradeView.getId()) {
                    startActivity(new Intent(getActivity(), GradesStudentCourseView.class));
                }
            }
        });
    }

    private void setCalendarViewListener() {
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == calendar.getId()) {
                    startActivity(new Intent(getActivity(), CalendarView.class));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.logoutAppBar:
                logout();
                return true;

        }
        return false;
    }

    private void logout() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}