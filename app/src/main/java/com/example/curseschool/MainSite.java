package com.example.curseschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainSite extends AppCompatActivity {

    private BottomNavigationView navigationBarView;
    private FragmentContainerView navigationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_site);
        navigationBarView = findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainSite, new mainFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.mainFragment:
                fragment = new mainFragment();
                break;

                case R.id.secondFragment:
                    fragment = new secondFragment();
                    break;

                case R.id.thirdFragment:
                    fragment = new thirdFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.mainSite, fragment).commit();
            return false;
        }
    };
}