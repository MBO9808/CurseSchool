package com.example.curseschool.CourseView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.curseschool.Adapters.CourseFormAdapter;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserKind;
import com.example.curseschool.UserUtils.UserUtils;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class CourseFormView extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabItem courseTab;
    private TabItem datesTab;
    private TabItem studentTab;
    private String MyPREFERENCES = "userData";
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form_view);
        toolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        String title = getIntent().getStringExtra("courseName");
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseFormView.this, CourseView.class);
                startActivity(intent);
            }
        });

        tabLayout = findViewById(R.id.courseFormLayout);
        viewPager = findViewById(R.id.courseFormViewPager);
        courseTab = findViewById(R.id.courseTab);
        datesTab = findViewById(R.id.datesTab);
        studentTab = findViewById(R.id.studentTab);
        tabLayout.setupWithViewPager(viewPager);
        currentUserId = getCurrentUserId();
        CourseFormAdapter courseFormAdapter = new CourseFormAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        courseFormAdapter.addFragment(new CourseFormFragment(), "Kurs");
        courseFormAdapter.addFragment(new CourseDatesFragment(), "Terminy");
        User currentUser = UserUtils.getUserById(currentUserId);
        if (!currentUser.getType().equals(UserKind.student)) {
            tabLayout.getTabAt(2).view.setVisibility(View.VISIBLE);
            courseFormAdapter.addFragment(new CourseStudentsFragment(), "Zapisani studenci");
        } else {
            tabLayout.getTabAt(2).view.setVisibility(View.GONE);
        }
        viewPager.setAdapter(courseFormAdapter);
    }

    private int getCurrentUserId() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id", 0);
        return id;
    }
}