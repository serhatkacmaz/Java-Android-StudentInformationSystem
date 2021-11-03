package com.example.yazilimlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yazilimlab.AdminHomeFragment.AdminCapFragment;
import com.example.yazilimlab.AdminHomeFragment.AdminDgsFragment;
import com.example.yazilimlab.AdminHomeFragment.AdminIntibakFragment;
import com.example.yazilimlab.AdminHomeFragment.AdminYatayGecisFragment;
import com.example.yazilimlab.AdminHomeFragment.AdminYazOkuluFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.collection.RBTreeSortedMap;

public class AdminHomeActivity extends AppCompatActivity {

    // Menu
    private DrawerLayout admin_home_drawerLayout;
    private NavigationView admin_home_navigationView;
    private Toolbar admin_home_toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    // fragment page
    private AdminCapFragment adminCapFragment;
    private AdminDgsFragment adminDgsFragment;
    private AdminIntibakFragment adminIntibakFragment;
    private AdminYazOkuluFragment adminYazOkuluFragment;
    private AdminYatayGecisFragment adminYatayGecisFragment;

    private void init() {
        // Menu
        admin_home_drawerLayout = (DrawerLayout) findViewById(R.id.admin_home_drawerLayout);
        admin_home_navigationView = (NavigationView) findViewById(R.id.admin_home_navigationView);
        admin_home_toolbar = (Toolbar) findViewById(R.id.admin_home_toolbar);

        //setSupportActionBar(admin_home_toolbar);

        // Menu ekleme
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, admin_home_drawerLayout, admin_home_toolbar, R.string.navView_open, R.string.navView_close);
        admin_home_drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // fragment init
        adminCapFragment = new AdminCapFragment();
        adminDgsFragment = new AdminDgsFragment();
        adminIntibakFragment = new AdminIntibakFragment();
        adminYazOkuluFragment = new AdminYazOkuluFragment();
        adminYatayGecisFragment = new AdminYatayGecisFragment();

        admin_home_toolbar.setTitle("Çap Başvuruları");
        setFragment(adminCapFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        init();

        // menu selected item
        admin_home_navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.admin_nav_menu_YazOkulu:
                        admin_home_toolbar.setTitle("Yaz Okulu Başvuruları");
                        setFragment(adminYazOkuluFragment);
                        admin_home_drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.admin_nav_menu_YatayGeciş:
                        admin_home_toolbar.setTitle("Yatay Geçiş Başvuruları");
                        setFragment(adminYatayGecisFragment);
                        admin_home_drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.admin_nav_menu_DGS:
                        admin_home_toolbar.setTitle("Dgs Başvuruları");
                        setFragment(adminDgsFragment);
                        admin_home_drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.admin_nav_menu_CAP:
                        admin_home_toolbar.setTitle("Çap Başvuruları");
                        setFragment(adminCapFragment);
                        admin_home_drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.admin_nav_menu_DersIntibaki:
                        admin_home_toolbar.setTitle("İntibak Başvuruları");
                        setFragment(adminIntibakFragment);
                        admin_home_drawerLayout.closeDrawer(GravityCompat.START);
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
        transaction.replace(R.id.admin_home_frameLayout, fragment);
        transaction.commit();
    }
    //https://www.youtube.com/watch?v=VUHMdkKUXxk&list=PL20Zn-5nPIPHvLPq5xJTTImOd0qeNd9rW&index=92

    @Override
    public void onBackPressed() {
        if (admin_home_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            admin_home_drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder checkAlertDialog = new AlertDialog.Builder(AdminHomeActivity.this);
            checkAlertDialog.setTitle("Kou Başvuru");
            checkAlertDialog.setMessage("Uygulamadan çıkış yapmak istiyor musunuz?");
            checkAlertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AdminHomeActivity.super.onBackPressed();
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
}