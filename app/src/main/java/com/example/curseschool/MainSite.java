package com.example.curseschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainSite extends AppCompatActivity {

    private BottomNavigationView navigationBarView;
    private FragmentContainerView navigationFragment;
    private Toolbar toolbar;
    private String MyPREFERENCES = "userData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_site);
        toolbar = findViewById(R.id.mainToolBar);
        navigationBarView = findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainSite, new mainFragment()).commit();
        setSupportActionBar(toolbar);
        int navigationId = getIntent().getIntExtra("Navigation", 1);
        setNavigationSelected(navigationId);
    }

    private void setNavigationSelected(int navigationId) {
        if (navigationId == 2) {
            navigationBarView.setSelectedItemId(R.id.secondFragment);
        } else if (navigationId == 3) {
            navigationBarView.setSelectedItemId(R.id.thirdFragment);
        } else {
            navigationBarView.setSelectedItemId(R.id.mainFragment);
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
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
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(MainSite.this, LoginActivity.class));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.mainFragment:
                    fragment = new mainFragment();
                    item.setChecked(true);
                    break;

                case R.id.secondFragment:
                    fragment = new secondFragment();
                    item.setChecked(true);
                    break;

                case R.id.thirdFragment:
                    fragment = new thirdFragment();
                    item.setChecked(true);
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.mainSite, fragment).commit();
            return false;
        }
    };
}