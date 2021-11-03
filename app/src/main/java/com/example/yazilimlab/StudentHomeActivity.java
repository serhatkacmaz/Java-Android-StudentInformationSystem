package com.example.yazilimlab;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yazilimlab.Catogery.CapActivity;
import com.example.yazilimlab.StudentHomeFragment.MakeApplicationFragment;
import com.example.yazilimlab.StudentHomeFragment.MyApplicationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.UploadService;

public class StudentHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private MyApplicationFragment myApplicationFragment;
    private MakeApplicationFragment makeApplicationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        createNotificationChannel();
        Logger.setLogLevel(Logger.LogLevel.DEBUG);
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
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

    //https://github.com/gotev/android-upload-service/wiki/Monitoring-upload-status
    public static String notificationChannel = "MyNotificationChannel";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    notificationChannel,
                    "My Channel", +
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
    //https://github.com/gotev/android-upload-service/wiki/Monitoring-upload-status

    //https://www.youtube.com/watch?v=VUHMdkKUXxk&list=PL20Zn-5nPIPHvLPq5xJTTImOd0qeNd9rW&index=92
    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.student_home_activity_frameLayout, fragment);
        transaction.commit();
    }
    //https://www.youtube.com/watch?v=VUHMdkKUXxk&list=PL20Zn-5nPIPHvLPq5xJTTImOd0qeNd9rW&index=92


    @Override
    public void onBackPressed() {
        AlertDialog.Builder checkAlertDialog = new AlertDialog.Builder(StudentHomeActivity.this);
        checkAlertDialog.setTitle("Kou Başvuru");
        checkAlertDialog.setMessage("Uygulamadan çıkış yapmak istiyor musunuz?");
        checkAlertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StudentHomeActivity.super.onBackPressed();
            }
        });
        checkAlertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("Hayır Bastın");
            }
        });
        checkAlertDialog.create().show();
    }
}