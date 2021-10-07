package com.example.yazilimlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.yazilimlab.AdminHomeFragment.AcceptedApplicationFragment;
import com.example.yazilimlab.AdminHomeFragment.IncomingApplicationFragment;
import com.example.yazilimlab.AdminHomeFragment.RejectedApplicationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private IncomingApplicationFragment incomingApplicationFragment;
    private AcceptedApplicationFragment acceptedApplicationFragment;
    private RejectedApplicationFragment rejectedApplicationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.admin_home_activity_bottomNavigationView);
        incomingApplicationFragment = new IncomingApplicationFragment();
        acceptedApplicationFragment = new AcceptedApplicationFragment();
        rejectedApplicationFragment = new RejectedApplicationFragment();
        setFragment(incomingApplicationFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adminHome_bottomBar_menu_incomingApplication:
                        setFragment(incomingApplicationFragment);
                        return true;
                    case R.id.adminHome_bottomBar_menu_acceptedApplication:
                        setFragment(acceptedApplicationFragment);
                        return true;
                    case R.id.adminHome_bottomBar_menu_rejectedApplication:
                        setFragment(rejectedApplicationFragment);
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
        transaction.replace(R.id.admin_home_activity_frameLayout, fragment);
        transaction.commit();
    }
    //https://www.youtube.com/watch?v=VUHMdkKUXxk&list=PL20Zn-5nPIPHvLPq5xJTTImOd0qeNd9rW&index=92
}