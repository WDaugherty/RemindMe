package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.example.myapplication.ui.daily.DailyFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DailyActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    boolean fabOpen = false;
    NavController navController = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fab1 = findViewById(R.id.fab2);
        FloatingActionButton fab2 = findViewById(R.id.fab3);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_daily, R.id.nav_weekly, R.id.nav_monthly)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        fab1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), CreateGoalActivity.class);
                //startActivity(intent);
                navController.navigate(R.id.nav_create_goal);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_create_task);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabOpen == false) {
                    fabOpen = true;
                    FloatingActionButton fab1 = findViewById(R.id.fab2);
                    FloatingActionButton fab2 = findViewById(R.id.fab3);
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    fab.setImageResource(R.drawable.ic_baseline_close_24);
                } else {
                    fabOpen = false;
                    FloatingActionButton fab1 = findViewById(R.id.fab2);
                    FloatingActionButton fab2 = findViewById(R.id.fab3);
                    fab1.setVisibility(View.GONE);
                    fab2.setVisibility(View.GONE);
                    fab.setImageResource(R.drawable.ic_baseline_add_24);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_goal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}