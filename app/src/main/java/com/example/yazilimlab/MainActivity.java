package com.example.yazilimlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.example.yazilimlab.LoginFragment.AdminLoginFragment;
import com.example.yazilimlab.LoginFragment.StudentLoginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private AdminLoginFragment adminLoginFragment;
    private StudentLoginFragment studentLoginFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.login_bottomNavigationView);
        adminLoginFragment = new AdminLoginFragment();
        studentLoginFragment = new StudentLoginFragment();
        setFragment(studentLoginFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_bottomBar_loginMenu_studentLogin:
                        setFragment(studentLoginFragment);
                        return true;
                    case R.id.item_bottomBar_loginMenu_adminLogin:
                        setFragment(adminLoginFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    //https://www.youtube.com/watch?v=VUHMdkKUXxk&list=PL20Zn-5nPIPHvLPq5xJTTImOd0qeNd9rW&index=92
    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.login_frameLayout, fragment);
        transaction.commit();
    }
    //https://www.youtube.com/watch?v=VUHMdkKUXxk&list=PL20Zn-5nPIPHvLPq5xJTTImOd0qeNd9rW&index=92


}