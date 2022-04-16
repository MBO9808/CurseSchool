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
import android.widget.TextView;

import com.example.curseschool.LoginActivity.LoginActivity;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserUtils;
import com.example.curseschool.UserUtils.singleToneClass;

public class secondFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String MyPREFERENCES = "userData";

    private String mParam1;
    private String mParam2;
    private  View view;
    private Toolbar toolbar;

    public secondFragment() {
    }

    public static secondFragment newInstance(String param1, String param2) {
        secondFragment fragment = new secondFragment();
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
        view = inflater.inflate(R.layout.fragment_second, container, false);
        toolbar = view.findViewById(R.id.mainToolBar);
        toolbar.setTitle("Profil");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setUserProfileData(view);
        return view;
    }

    private void setUserProfileData(View view) {
        String email = singleToneClass.getInstance().getData();
        User user = UserUtils.getUserFromEmail(email);
        TextView userName = view.findViewById(R.id.userProfileName);
        TextView userEmail = view.findViewById(R.id.userProfileEmail);
        TextView userPhone = view.findViewById(R.id.userProfilePhone);
        TextView userCity = view.findViewById(R.id.userProfileCity);
        TextView userStreet = view.findViewById(R.id.userProfileStreet);
        String name = user.getFirstName() + " " + user.getLastName();
        userName.setText(name);
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhoneNumber());
        String city = user.getCity() + ", " + user.getPostalCode();
        userCity.setText(city);
        userStreet.setText(user.getStreet());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.logoutAppBar:
                logout();
                return true;

        }
        return false;
    }

    private void logout(){
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}