package com.example.curseschool;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class secondFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
        View view = inflater.inflate(R.layout.fragment_second, container, false);
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
}