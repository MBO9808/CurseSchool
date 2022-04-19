package com.example.curseschool.MainView;

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

import com.example.curseschool.Dictionaries.ClassRoomDictionary;
import com.example.curseschool.Dictionaries.CourseAdvancementDictionary;
import com.example.curseschool.Dictionaries.CourseLanguagesDictionary;
import com.example.curseschool.Dictionaries.GradeNameDictionary;
import com.example.curseschool.LoginActivity.LoginActivity;
import com.example.curseschool.R;
import com.example.curseschool.UserSettingsActivities.UserChangeEmail;
import com.example.curseschool.UserSettingsActivities.UserChangePassword;
import com.example.curseschool.UserSettingsActivities.UserChangePersonalData;
import com.example.curseschool.UserUtils.UserKind;
import com.example.curseschool.Dictionaries.UsersSettings;

public class thirdFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View view;
    private String MyPREFERENCES = "userData";
    private Toolbar toolbar;
    private Button settingsUsers;
    private Button settingsCourseLanguages;
    private Button settingsClassRoom;
    private Button settingsAboutApp;
    private Button settingsCourseAdvancement;
    private Button settingGradeType;
    private Button settingsChangeUserEmail;
    private Button settingsChangeUserPersonalData;
    private Button settingsChangeUserPassword;


    public thirdFragment() {
        // Required empty public constructor
    }

    public static thirdFragment newInstance(String param1, String param2) {
        thirdFragment fragment = new thirdFragment();
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
        view = inflater.inflate(R.layout.fragment_third, container, false);
        toolbar = view.findViewById(R.id.mainToolBar);
        toolbar.setTitle("Ustawienia");
        settingsUsers = view.findViewById(R.id.settingsUsers);
        settingsCourseLanguages = view.findViewById(R.id.settingsCourseLanguage);
        settingsClassRoom = view.findViewById(R.id.settingsClassRoom);
        settingsAboutApp = view.findViewById(R.id.settingsAboutApp);
        settingsCourseAdvancement = view.findViewById(R.id.settingsCourseAdvancement);
        settingGradeType = view.findViewById(R.id.settingsGradeType);
        settingsChangeUserEmail = view.findViewById(R.id.settingsChangeUserEmail);
        settingsChangeUserPersonalData = view.findViewById(R.id.settingsChangeUserPersonalData);
        settingsChangeUserPassword = view.findViewById(R.id.settingsChangeUserPassword);
        handleButtonsVisibility();
        setSettingsListeners();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return view;
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

    private void handleButtonsVisibility() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userType = sharedpreferences.getString("type", null);
        if (userType.equals(UserKind.admin.toString())) {
            settingsCourseLanguages.setVisibility(View.VISIBLE);
            settingsClassRoom.setVisibility(View.VISIBLE);
            settingsUsers.setVisibility(View.VISIBLE);
            settingsCourseAdvancement.setVisibility(View.VISIBLE);
            settingGradeType.setVisibility(View.VISIBLE);
        } else {
            settingsCourseLanguages.setVisibility(View.GONE);
            settingsClassRoom.setVisibility(View.GONE);
            settingsUsers.setVisibility(View.GONE);
            settingsCourseAdvancement.setVisibility(View.GONE);
            settingGradeType.setVisibility(View.GONE);
        }

    }

    private void setSettingsListeners() {
        setSettingsUsersListener();
        setSettingsCourseLanguagesListener();
        setSettingsClassRoomListener();
        setSettingsCourseAdvancementListener();
        setSettingsGradeTypeListener();
        setSettingsChangeUserEmailListener();
        setSettingsChangeUserPersonalDataListener();
        setSettingsChangeUserPasswordListener();
    }

    private void setSettingsUsersListener() {
        settingsUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == settingsUsers.getId()) {
                    startActivity(new Intent(getActivity(), UsersSettings.class));
                }
            }
        });
    }

    private void setSettingsCourseLanguagesListener() {
        settingsCourseLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == settingsCourseLanguages.getId()) {
                    startActivity(new Intent(getActivity(), CourseLanguagesDictionary.class));
                }
            }
        });
    }

    private void setSettingsClassRoomListener() {
        settingsClassRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == settingsClassRoom.getId()) {
                    startActivity(new Intent(getActivity(), ClassRoomDictionary.class));
                }
            }
        });
    }

    private void setSettingsCourseAdvancementListener() {
        settingsCourseAdvancement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == settingsCourseAdvancement.getId()) {
                    startActivity(new Intent(getActivity(), CourseAdvancementDictionary.class));
                }
            }
        });
    }

    private void setSettingsChangeUserEmailListener() {
        settingsChangeUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == settingsChangeUserEmail.getId()) {
                    startActivity(new Intent(getActivity(), UserChangeEmail.class));
                }
            }
        });
    }

    private void setSettingsGradeTypeListener() {
        settingGradeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == settingGradeType.getId()) {
                    startActivity(new Intent(getActivity(), GradeNameDictionary.class));
                }
            }
        });
    }

    private void setSettingsChangeUserPersonalDataListener(){
        settingsChangeUserPersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == settingsChangeUserPersonalData.getId()) {
                    startActivity(new Intent(getActivity(), UserChangePersonalData.class));
                }
            }
        });
    }

    private void setSettingsChangeUserPasswordListener(){
        settingsChangeUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == settingsChangeUserPassword.getId()) {
                    startActivity(new Intent(getActivity(), UserChangePassword.class));
                }
            }
        });
    }
}