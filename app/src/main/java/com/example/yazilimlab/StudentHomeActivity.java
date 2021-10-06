package com.example.yazilimlab;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yazilimlab.StudentHomeFragment.MakeApplicationFragment;
import com.example.yazilimlab.StudentHomeFragment.MyApplicationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private MyApplicationFragment myApplicationFragment;
    private MakeApplicationFragment makeApplicationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.student_home_activity_bottomNavigationView);
        makeApplicationFragment = new MakeApplicationFragment();
        myApplicationFragment = new MyApplicationFragment();
        setFragment(makeApplicationFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.studentHome_bottomBar_menu_makeApplication:
                        setFragment(makeApplicationFragment);
                        return true;
                    case R.id.studentHome_bottomBar_menu_MyApplication:
                        setFragment(myApplicationFragment);
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
        transaction.replace(R.id.student_home_activity_frameLayout, fragment);
        transaction.commit();
    }
    //https://www.youtube.com/watch?v=VUHMdkKUXxk&list=PL20Zn-5nPIPHvLPq5xJTTImOd0qeNd9rW&index=92

}